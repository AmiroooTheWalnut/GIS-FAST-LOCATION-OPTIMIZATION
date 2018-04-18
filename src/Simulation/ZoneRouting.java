/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulation;

import GIS3D.LocationNode;
import GIS3D.PreProcessor;
import GUI.main_frame;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Amir72c
 */
public class ZoneRouting {
    
    main_frame myParent;
    public Routing routings[];
    
    public ZoneRouting(main_frame parent)
    {
        myParent=parent;
    }
    
    public void simulateZoneRouting(final FacilityLocation FL[],final LocationNode supplier,int numCPUs,final int trafficIndex)
    {
        myParent.allData.setParallelLayers(numCPUs,-1);
        routings=new Routing[FL.length];
        Thread threads[]=new Thread[numCPUs];
        final CPUUpperLowerBounds tasks[]=PreProcessor.spreadTasks(numCPUs, routings.length);
        for(int i=0;i<numCPUs;i++)
        {
            
            
            final int passed_i=i;
            threads[i]=new Thread(new Runnable() {

                @Override
                public void run() {
                    for(int j=tasks[passed_i].startIndex;j<tasks[passed_i].endIndex;j++)
                    {
                        routings[j]=new Routing(myParent,myParent.allData,trafficIndex,passed_i);
                        routings[j].findPath(FL[j].nodeLocation, supplier);
                        myParent.flowControl.resetParallel(passed_i);
                    }
                }
            });
            threads[i].start();
        }
        for(int i=0;i<numCPUs;i++)
        {
            try {
                threads[i].join();
            } catch (InterruptedException ex) {
                Logger.getLogger(ZoneRouting.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

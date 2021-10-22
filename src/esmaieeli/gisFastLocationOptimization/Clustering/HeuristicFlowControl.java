/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esmaieeli.gisFastLocationOptimization.Clustering;

import esmaieeli.gisFastLocationOptimization.GIS3D.LocationNode;
import esmaieeli.gisFastLocationOptimization.GIS3D.PreProcessor;
import esmaieeli.gisFastLocationOptimization.GIS3D.Way;
import esmaieeli.gisFastLocationOptimization.GUI.MainFrame;
import esmaieeli.gisFastLocationOptimization.GUI.MainFramePanel;
import esmaieeli.gisFastLocationOptimization.Simulation.CPUUpperLowerBounds;
import esmaieeli.gisFastLocationOptimization.Simulation.FlowControl;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Amir72c
 */
public class HeuristicFlowControl {

    public double minLava = Double.POSITIVE_INFINITY;
    public double maxLava = Double.NEGATIVE_INFINITY;
    MainFramePanel myParent;
    Heuristic heuristicParent;
    public int resetLayerIndex = -1;

    HeuristicFlowControl(MainFramePanel parent, Heuristic hParent) {
        myParent = parent;
        heuristicParent = hParent;
    }

    public void iterate(HeuristicFacilityLocation facility) {
//        System.out.println("Current queue lava buffer size: " + facility.queue_lavaBuffer.size());
//        System.out.println("Current lava buffer size: " + facility.lavaBuffer.size());
        if (facility.queue_lavaBuffer.size() < 10000) {
            for (int i = facility.queue_lavaBuffer.size() - 1; i > -1; i--) {
                facility.lavaBuffer.add((HeuristicLava) facility.queue_lavaBuffer.get(i));
                facility.queue_lavaBuffer.remove(i);
            }
        } else {
            for (int i = 10000 - 1; i > -1; i--) {
                facility.lavaBuffer.add((HeuristicLava) facility.queue_lavaBuffer.get(i));
                facility.queue_lavaBuffer.remove(i);
            }
        }

        for (int i = 0; i < facility.lavaBuffer.size(); i++) {
            ((HeuristicLava) facility.lavaBuffer.get(i)).flow();
        }
    }

    public int detectWayIndex(LocationNode node) {
        for (int i = 0; i < myParent.allData.all_Ways.length; i++) {
            for (int j = 0; j < myParent.allData.all_Ways[i].myNodes.length; j++) {
                if (node.id==myParent.allData.all_Ways[i].myNodes[j].id) {
                    return i;
                }
            }
        }
        return -1;
    }

    public int detectNodeIndex(Way way, LocationNode node) {
        int return_val = -1;
        for (int i = 0; i < way.myNodes.length; i++) {
            if (node.id==way.myNodes[i].id) {
                return_val = i;
            }
        }
        return return_val;
    }

    public int detectGlobalNodeIndex(LocationNode passed_node) {
        for (int i = 0; i < myParent.allData.all_Nodes.length; i++) {
            if (passed_node.id==myParent.allData.all_Nodes[i].id) {
                return i;
            }
        }
        return -1;
    }

    public void init(ArrayList<HeuristicFreeLocation> freeLocations, int trafficLayer) {
        resetParallel(0);
        for (int i = 0; i < freeLocations.size(); i++) {
            freeLocations.get(i).facilityLocation.makeLava(freeLocations.get(i).facilityLocation.nodeLocation, freeLocations.get(i).facilityLocation.way, freeLocations.get(i).facilityLocation.capacity, trafficLayer, 1);
        }
    }

    public boolean runOneIterateForAll(ArrayList<HeuristicFreeLocation> freeLocations) {
        boolean lava_exists = false;
//        System.out.println(freeLocations.size());
        for (int i = 0; i < freeLocations.size(); i++) {
//            System.out.println("Location queue Lava buffer: "+freeLocations.get(i).facilityLocation.queue_lavaBuffer.size());
            iterate(freeLocations.get(i).facilityLocation);
//            System.out.println("Location Lava buffer: "+freeLocations.get(i).facilityLocation.lavaBuffer.size());
            if (!freeLocations.get(i).facilityLocation.lavaBuffer.isEmpty()) {
                lava_exists = true;
            }
        }
        return lava_exists;
    }

    public void simulateSerial(int trafficLayer, int popDensity) {
        init(heuristicParent.heuristicFreeLocations, trafficLayer);
        Thread calc_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                boolean lava_exists;
                do {
                    lava_exists = runOneIterateForAll(heuristicParent.heuristicFreeLocations);
//                    System.out.println("BASE demand: "+facility.baseDemand);
                } while (lava_exists == true);
            }
        });
        calc_thread.start();
        try {
            calc_thread.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(FlowControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void simulateParallel(int trafficLayer, int popDensity, int numCPUs) {
        init(heuristicParent.heuristicFreeLocations, trafficLayer);

        Thread threads[] = new Thread[numCPUs];
        final CPUUpperLowerBounds[] tasks = PreProcessor.spreadTasks(numCPUs, heuristicParent.heuristicFreeLocations.size());
//        System.out.println("Init parallel simulation!");
        for (int i = 0; i < numCPUs; i++) {
            final int passedThreadOrder = i;
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    boolean lava_exists;
                    do {
                        lava_exists = false;
                        for (int i = tasks[passedThreadOrder].startIndex; i < tasks[passedThreadOrder].endIndex; i++) {
                            iterate(heuristicParent.heuristicFreeLocations.get(i).facilityLocation);
                            if (!heuristicParent.heuristicFreeLocations.get(i).facilityLocation.lavaBuffer.isEmpty()) {
                                lava_exists = true;
                            }
                        }
                    } while (lava_exists == true);
                }
            });
            threads[i].start();
        }
        for (int i = 0; i < numCPUs; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException ex) {
                Logger.getLogger(HeuristicFlowControl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void resetParallel(int lavaIndex) {
        if (resetLayerIndex < 0) {
            System.out.println("No reset layer defined!");
            for (int i = 0; i < myParent.allData.all_Nodes.length; i++) {
                myParent.allData.all_Nodes[i].lava_value_indexed[lavaIndex] = 0;
                myParent.allData.all_Nodes[i].isBurned = false;
                myParent.allData.all_Nodes[i].isChecked[lavaIndex] = false;
                myParent.allData.all_Nodes[i].isPassed[lavaIndex] = false;
                myParent.allData.all_Nodes[i].f_value[lavaIndex] = 0;
                myParent.allData.all_Nodes[i].g_value[lavaIndex] = 0;
                myParent.allData.all_Nodes[i].h_value[lavaIndex] = 0;
            }
        } else {
            //DENORMALIZE, BREAKS MODULARITY!!!
            double expectedDenormalized = 3;
            //DENORMALIZE, BREAKS MODULARITY!!!
            for (int i = 0; i < myParent.allData.all_Nodes.length; i++) {
                myParent.allData.all_Nodes[i].lava_value_indexed[lavaIndex] = ((double) myParent.allData.all_Nodes[i].layers.get(resetLayerIndex)) * expectedDenormalized;
                myParent.allData.all_Nodes[i].isBurned = true;
                myParent.allData.all_Nodes[i].isChecked[lavaIndex] = false;
                myParent.allData.all_Nodes[i].isPassed[lavaIndex] = false;
                myParent.allData.all_Nodes[i].f_value[lavaIndex] = 0;
                myParent.allData.all_Nodes[i].g_value[lavaIndex] = 0;
                myParent.allData.all_Nodes[i].h_value[lavaIndex] = 0;
            }
        }
    }
}

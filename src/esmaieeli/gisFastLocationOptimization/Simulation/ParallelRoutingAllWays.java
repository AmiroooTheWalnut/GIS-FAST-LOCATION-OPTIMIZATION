/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esmaieeli.gisFastLocationOptimization.Simulation;

import esmaieeli.gisFastLocationOptimization.GIS3D.AllData;
import esmaieeli.gisFastLocationOptimization.GIS3D.ParallelProcessor;

/**
 *
 * @author user
 */
public class ParallelRoutingAllWays extends ParallelProcessor {

    FlowControl myParent;
    AllData myAllData;
    int trafficLayerIndex;

    ParallelRoutingAllWays(FlowControl parent, AllData allData, int startIndex, int endIndex, int passed_trafficLayerIndex) {
        super(startIndex, endIndex);

        myParent = parent;
        myAllData = allData;
        trafficLayerIndex = passed_trafficLayerIndex;
        myThread = new Thread(new Runnable() {

            @Override
            public void run() {
                for (int i = startIndex; i < endIndex; i++) {
                    //System.out.println("myParent.allData.all_Ways i: " + i);
                    for (int j = 0; j < myAllData.all_Ways[i].myNodes.length; j++) {
                        if (myAllData.all_Ways[i].myNodes[j].burntBy != null) {
                            for (int k = 0; k < myAllData.all_Ways[i].myNodes[j].burntBy.length; k++) {
                                if (myAllData.all_Ways[i].myNodes[j].burntBy[k] != null) {
                                    Routing routingTest = new Routing(myAllData, trafficLayerIndex, k);
                                    routingTest.findPathToFacilityLocation(myAllData.all_Ways[i].myNodes[j], myAllData.all_Ways[i].myNodes[j].burntBy[k]);
                                    if (routingTest.path == null) {
                                        Routing routingFix = new Routing(myAllData, trafficLayerIndex, k);
                                        routingFix.setClosestDifferentFacilityLocation(myAllData.all_Ways[i].myNodes[j], myAllData.all_Ways[i].myNodes[j].burntBy[k]);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }
}

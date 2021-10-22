/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esmaieeli.gisFastLocationOptimization.Simulation;

import esmaieeli.gisFastLocationOptimization.Clustering.Zones;

/**
 *
 * @author Amir72c
 */
public class ThreadMultiLayerCompetingZoneBased{

    public Thread myThread;
    public CPUUpperLowerBounds spreadedTask;

    public ThreadMultiLayerCompetingZoneBased(final Zones zones[], CPUUpperLowerBounds st) {
        spreadedTask = st;
        myThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = spreadedTask.startIndex; i < spreadedTask.endIndex; i++) {
                    for (int j = 0; j < zones[i].locations.length; j++) {
//                        System.out.println("%%%%%%%%%%%%");
//                        System.out.println("Zone number " + i);
//                        System.out.println("Lavabuffer size " + zones[i].locations[j].lavaBuffer.size());
//                        System.out.println("%%%%%%%%%%%%");
                        for (int k = 0; k < zones[i].locations[j].lavaBuffer.size(); k++) {
                            ((Lava_parallel) zones[i].locations[j].lavaBuffer.get(k)).flowMultiLayerCompetingZoneBased();
                        }
                    }
                }
            }
        });
    }
}

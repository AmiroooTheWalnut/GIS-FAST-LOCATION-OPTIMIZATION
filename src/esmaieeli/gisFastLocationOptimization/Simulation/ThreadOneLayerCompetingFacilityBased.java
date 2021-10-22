/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esmaieeli.gisFastLocationOptimization.Simulation;

/**
 *
 * @author Amir72c
 */
public class ThreadOneLayerCompetingFacilityBased {

    public Thread myThread;
    public CPUUpperLowerBounds spreadedTask;

    public ThreadOneLayerCompetingFacilityBased(final FacilityLocation FLs[], CPUUpperLowerBounds st) {
        spreadedTask = st;
        myThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = spreadedTask.startIndex; i < spreadedTask.endIndex; i++) {
//                    System.out.println("%%%%%%%%%%%%");
//                    System.out.println("Faility number " + i);
//                    System.out.println("Lavabuffer size " + FLs[i].lavaBuffer.size());
//                    System.out.println("%%%%%%%%%%%%");
                    for (int j = 0; j < FLs[i].lavaBuffer.size(); j++) {
                        ((Lava_parallel) FLs[i].lavaBuffer.get(j)).flowOneLayerCompetingFacilityBased();
                    }
                    
                }
            }
        });
    }
}

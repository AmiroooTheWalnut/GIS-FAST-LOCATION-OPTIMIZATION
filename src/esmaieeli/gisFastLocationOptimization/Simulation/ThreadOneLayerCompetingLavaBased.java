/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esmaieeli.gisFastLocationOptimization.Simulation;

/**
 *
 * @author Amir72c
 */
public class ThreadOneLayerCompetingLavaBased {

    public Thread myThread;
    public CPUUpperLowerBounds spreadedTask;
    public FlowControl myFCParent;

    public ThreadOneLayerCompetingLavaBased(FlowControl FC,CPUUpperLowerBounds st) {
        myFCParent = FC;
        spreadedTask=st;
        myThread = new Thread(new Runnable() {
            @Override
            public void run() {
//                System.out.println("%%%%%%%%%%%%");
//                System.out.println("startIndex " + spreadedTask.startIndex);
//                System.out.println("endIndex " + spreadedTask.endIndex);
//                System.out.println("lavaBuffer size " + myFCParent.lavaBuffer.size());
//                System.out.println("%%%%%%%%%%%%");
                for (int i = spreadedTask.startIndex; i < spreadedTask.endIndex; i++) {
                    myFCParent.lavaBuffer.get(i).flowOneLayerCompetingLavaBased();
                }
            }
        });
    }

//    public void runThread(CPUUpperLowerBounds st) {
//        spreadedTask = st;
//        myThread.start();
//    }
}

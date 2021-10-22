/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esmaieeli.gisFastLocationOptimization.GIS3D;

/**
 *
 * @author Amir72c
 */
public class ParallelProcessor {
    
    public int myStartIndex;
    public int myEndIndex;
    public Thread myThread;
    
    public ParallelProcessor(int startIndex,int endIndex)
    {
        myStartIndex=startIndex;
        myEndIndex=endIndex;
    }
    
    
}

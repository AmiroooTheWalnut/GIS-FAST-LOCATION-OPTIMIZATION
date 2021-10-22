/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esmaieeli.gisFastLocationOptimization.Clustering;

import java.util.ArrayList;

/**
 *
 * @author Amir72c
 */
public class Cluster {
    public ArrayList instances;
    public int myOrder;
    public Cluster(int order)
    {
        myOrder=order;
        instances=new ArrayList();
    }
}

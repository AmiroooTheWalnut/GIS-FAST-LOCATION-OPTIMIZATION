/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esmaieeli.gisFastLocationOptimization.Simulation;

import esmaieeli.gisFastLocationOptimization.GIS3D.LocationNode;
import esmaieeli.gisFastLocationOptimization.GIS3D.Way;

/**
 *
 * @author Amir72c
 */
public class AdjacentNode {
    
    public LocationNode myNode;
    public Way myWay;
    
    public AdjacentNode(LocationNode passed_Node,Way passed_Way)
    {
        myNode=passed_Node;
        myWay=passed_Way;
    }
    
}

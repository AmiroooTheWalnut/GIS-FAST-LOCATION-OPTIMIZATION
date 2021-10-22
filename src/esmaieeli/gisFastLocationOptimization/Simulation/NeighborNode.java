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
public class NeighborNode {
    public LocationNode myNode;
    public Way myWay;
    public NeighborNode cameFrom;
}

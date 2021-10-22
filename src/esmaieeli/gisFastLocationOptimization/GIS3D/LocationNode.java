/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esmaieeli.gisFastLocationOptimization.GIS3D;

import esmaieeli.gisFastLocationOptimization.Simulation.FacilityLocation;
import com.jme3.math.Vector3f;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Amir72c
 */
public class LocationNode implements Serializable {
    
    static final long serialVersionUID = 3L;

    public long id;
    public double lat;
    public double lon;
    public boolean isBurned = false;
    
    public double g_value[];
    public double h_value[];
    public double f_value[];
    public boolean isChecked[];
    public boolean isPassed[];
    public double lava_value_indexed[];
    
    public Way myWays[];
    public ArrayList layers=new ArrayList();
    public Grid myGrid;
    public Vector3f renderingLocation;
    public int myOrder;
    
    public transient FacilityLocation burntBy[];
    public transient boolean isConnectedToFacility[];
    
    public LocationNode(long passed_id, double passed_lat, double passed_lon,int passedorder) {
        id = passed_id;
        lat = passed_lat;
        lon = passed_lon;
        myOrder=passedorder;
    }
}

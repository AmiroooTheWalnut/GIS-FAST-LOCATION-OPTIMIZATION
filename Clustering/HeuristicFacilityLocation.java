/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Clustering;

import GIS3D.LocationNode;
import GIS3D.Way;
import GUI.main_frame;
import Simulation.FacilityLocation;
import com.jme3.math.Vector3f;
import java.util.ArrayList;

/**
 *
 * @author Amir72c
 */
public class HeuristicFacilityLocation extends FacilityLocation{
//    public LocationNode nodeLocation;
//    Way way;
//    Vector3f renderingLocation;
//    ArrayList lavaBuffer = new ArrayList();
//    ArrayList queue_lavaBuffer = new ArrayList();
//    public double capacity;
//    boolean burnable=true;
    main_frame myMFParent;
    HeuristicFreeLocation freeLocationParent;
//    
//    double obj;
//    public boolean isDecoyable=true;
    
//    public Zones zoneParent;
    
//    public float landPrice;
//    public float landExistance;
//    public float baseDemand;
//    public float tourismGain;
//    public float transportationCost;
    
    public HeuristicFacilityLocation(main_frame passedMFparent, LocationNode passed_nodeLocation, Way passed_way, double passedcapacity) {
        super(passedMFparent,passed_nodeLocation,passed_way,passedcapacity);
        capacity = passedcapacity;
        nodeLocation = passed_nodeLocation;
        way = passed_way;
        renderingLocation = nodeLocation.renderingLocation;
        myMFParent = passedMFparent;
    }
    
    public void makeLava(LocationNode node, Way way, double passed_fuel,int trafficLayerIndex,int lavaIndex){
        HeuristicLava temp_lava=new HeuristicLava(this, node, way, passed_fuel, node.lat, node.lon,lavaIndex);
        temp_lava.trafficLayerIndex=trafficLayerIndex;
        queue_lavaBuffer.add(temp_lava);
    }

    public void remove_lava(HeuristicLava lava) {
        lavaBuffer.remove(lava);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulation;

import Clustering.Zones;
import GIS3D.LocationNode;
import GIS3D.Way;
import GUI.main_frame;
import com.jme3.math.Vector3f;
import java.util.ArrayList;

/**
 *
 * @author Amir72c
 */
public class FacilityLocation {

    public LocationNode nodeLocation;
    public Way way;
    public Vector3f renderingLocation;
    public double capacity;
    public ArrayList lavaBuffer = new ArrayList();
    public ArrayList queue_lavaBuffer = new ArrayList();
    public boolean burnable=true;
    public main_frame myParent;
    //FacilityLocation candidate_facility_location[] = new FacilityLocation[6];
    public double obj;
    public double demand=0;//MUST BE DEPRECIATED
    public boolean isDecoyable=false;
    
    public Zones zoneParent;
    
    public float landPrice;
    public float landExistance;
    public float baseDemand[]=new float[3];
    public float tourismGain;
    public float transportationCost;
    

    public FacilityLocation(main_frame passedparent, LocationNode passed_nodeLocation, Way passed_way, double passedcapacity) {
        capacity = passedcapacity;
        nodeLocation = passed_nodeLocation;
        way = passed_way;
        renderingLocation = nodeLocation.renderingLocation;
        myParent = passedparent;
    }
    
    public void makeLava(LocationNode node, Way way, double passed_fuel,int trafficLayerIndex,int lavaIndex){
        Lava_parallel temp_lava=new Lava_parallel(this, node, way, passed_fuel, node.lat, node.lon,lavaIndex);
        temp_lava.trafficLayerIndex=trafficLayerIndex;
        queue_lavaBuffer.add(temp_lava);
    }

    public void removeLava(Lava_parallel lava) {
        lavaBuffer.remove(lava);
    }
}

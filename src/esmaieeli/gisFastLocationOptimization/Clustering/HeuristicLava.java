/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esmaieeli.gisFastLocationOptimization.Clustering;

import esmaieeli.gisFastLocationOptimization.GIS3D.LocationNode;
import esmaieeli.gisFastLocationOptimization.GIS3D.Way;
import esmaieeli.gisFastLocationOptimization.Simulation.AdjacentNode;
import com.jme3.math.Vector3f;
import java.util.ArrayList;

/**
 *
 * @author Amir72c
 */
public class HeuristicLava {
    HeuristicFacilityLocation myParent;
    LocationNode currentNode;
    Way currentWay;
    double fuel;
    int currentIndex;
    ArrayList adjacent = new ArrayList();
    double last_lat;
    double last_lon;
    double tollOff=0.1;
    int trafficLayerIndex=-1;
    int lavaIndex;//ALWAYS 1, 0 IS FOR COMPETITORS
    double fuelLoss;
    HeuristicLava(HeuristicFacilityLocation parent, LocationNode passedNode, Way passedWay, double passedfuel, double passedLat, double passedLon,int index){
        myParent = parent;
        currentNode = passedNode;
        currentWay = passedWay;
        fuel = passedfuel;
        currentIndex = detectIndex(currentWay, currentNode);
        last_lat = passedLat;
        last_lon = passedLon;
        lavaIndex=index;//ALWAYS 1, 0 IS FOR COMPETITORS
    }
    
    public void flow()
    {
        if(trafficLayerIndex>-1)
        {
            fuel = fuel - 100 * calculate_distance(currentNode.lat, currentNode.lon, last_lat, last_lon) * currentWay.typeWeight*(myParent.myMFParent.getLayerValue(currentNode,trafficLayerIndex));
        }else{
            fuel = fuel - 100 * calculate_distance(currentNode.lat, currentNode.lon, last_lat, last_lon) * currentWay.typeWeight;
        }
        //System.out.println(fuel);
        boolean isDestroyed=false;
        if(currentNode.lava_value_indexed[0]>=fuel)
        {
            isDestroyed=true;
//          System.out.println("Lost to old competitors");
        }
        if(currentNode.lava_value_indexed[1]>=fuel)
        {
            isDestroyed=true;
//          System.out.println("Lost to ally lavas");
        }
        if(isDestroyed==true)
        {
            selfDestroy();
            return;
        }else{
//            System.out.println("current: "+currentNode.lava_value_indexed[0]);
//            System.out.println("fuel: "+fuel);
            currentNode.lava_value_indexed[lavaIndex] = fuel;
            colorBurn(currentIndex,currentWay);
        }
        
        if (fuel > 0 || myParent.isDecoyable==false) {
            detectAdjacent();
            //System.out.println(adjacent.size());
            if (adjacent.size() > 0) {
                last_lat = currentNode.lat;
                last_lon = currentNode.lon;
                this.currentNode = ((AdjacentNode) adjacent.get(0)).myNode;
                this.currentWay = ((AdjacentNode) adjacent.get(0)).myWay;
                currentIndex = detectIndex(currentWay, currentNode);
                for (int i = 1; i < adjacent.size(); i++) {
                    branch(((AdjacentNode) adjacent.get(i)).myNode, ((AdjacentNode) adjacent.get(i)).myWay, fuel, last_lat, last_lon,lavaIndex);
                }
                if ("residential".equals(currentWay.type)) {
                    myParent.baseDemand[0]=myParent.baseDemand[0]+new Float(fuel*myParent.myMFParent.getLayerValue(currentNode, myParent.freeLocationParent.myParent.populationDensityLayer));
                    myParent.baseDemand[1]=myParent.baseDemand[1]+new Float(fuel*myParent.myMFParent.getLayerValue(currentNode, myParent.freeLocationParent.myParent.luxuryLayer));
                    myParent.baseDemand[2]=myParent.baseDemand[2]+new Float(fuel*myParent.myMFParent.getLayerValue(currentNode, myParent.freeLocationParent.myParent.studentLayer));
                    myParent.tourismGain=myParent.tourismGain+new Float(fuel*myParent.myMFParent.getLayerValue(currentNode, myParent.freeLocationParent.myParent.tourismLayer));
                }
            } else {
                selfDestroy();
//                System.out.println("Lost to path blocked");
            }
        }else{
            selfDestroy();
//            System.out.println("Lost to no fuel");
        }
    }
    
//    public double[] exertForce(int destinationIndex)
//    {
//        double forces[]=new double[2];
//        if(destinationIndex==-1)
//        {
//            Vector3f parentLocation = myParent.renderingLocation;
//            Vector3f lavaLocation = currentNode.renderingLocation;
//            forces[0]=Math.signum(lavaLocation.x-parentLocation.x)*(1d/Math.pow(parentLocation.x-lavaLocation.x, 2))*fuel;
//            forces[1]=Math.signum(lavaLocation.y-parentLocation.y)*(1d/Math.pow(parentLocation.y-lavaLocation.y, 2))*fuel;
//        }else{
//            
//        }
//    }
    
    public double distance(Vector3f parentLocation,Vector3f lavaLocation)
    {
        return Math.sqrt(Math.pow(parentLocation.x-lavaLocation.x, 2)+Math.pow(parentLocation.y-lavaLocation.y, 2));
    }
    
    public void colorBurn(int index, Way way) {
        way.color[index * 3 + 0] = myParent.freeLocationParent.color.getRed();
        way.color[index * 3 + 1] = myParent.freeLocationParent.color.getGreen();
        way.color[index * 3 + 2] = myParent.freeLocationParent.color.getBlue();
    }
    
    public double calculate_distance(double lat1, double lon1, double lat2, double lon2) {
        double distance;
        distance = Math.sqrt(Math.pow(lat1 - lat2, 2) + Math.pow(lon1 - lon2, 2));
        return distance;
    }
    
    public int detectIndex(Way way, LocationNode node) {
        int return_val = -1;
        for (int i = 0; i < way.myNodes.length; i++) {
            if (node.id==way.myNodes[i].id) {
                return_val = i;
            }
        }
        if (return_val == -1) {
            return 0;
        } else {
            return return_val;
        }
    }
    
    public void detectAdjacent(){
        adjacent.clear();
        for (int i = 0; i < currentNode.myWays.length; i++) {
            if (currentNode.myWays[i].id!=currentWay.id) {
                int temp = detectIndex(currentNode.myWays[i], currentNode);
                if (temp != -1) {
                    try {
                        boolean isAdjacent = true;
                        for(int j=0;j<currentNode.lava_value_indexed.length;j++)
                        {
                            if (!(currentNode.myWays[i].myNodes[temp - 1].lava_value_indexed[j] < fuel - tollOff && currentNode.myWays[i].isOneWay==false)) {
                                isAdjacent=false;
                            }
                        }
                        if (isAdjacent) {
                            //color_burn(temp, currentNode.myWays[i], fuel);
                            adjacent.add(new AdjacentNode(currentNode.myWays[i].myNodes[temp - 1], currentNode.myWays[i]));
                        }
                    } catch (Exception e) {
                    }
                    try {
                        boolean isAdjacent = true;
                        for(int j=0;j<currentNode.lava_value_indexed.length;j++)
                        {
                            if (!(currentNode.myWays[i].myNodes[temp + 1].lava_value_indexed[lavaIndex] < fuel - tollOff)){
                                isAdjacent=false;
                            }
                        }
                        if (isAdjacent) {
                            //color_burn(temp, currentNode.myWays[i], fuel);
                            adjacent.add(new AdjacentNode(currentNode.myWays[i].myNodes[temp + 1], currentNode.myWays[i]));
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }
        try {
            boolean isAdjacent = true;
            for (int j = 0; j < currentNode.lava_value_indexed.length; j++) {
                if (!(currentWay.myNodes[currentIndex + 1].lava_value_indexed[lavaIndex] < fuel - tollOff)) {
                    isAdjacent=false;
                }
            }
            if (isAdjacent) {
                adjacent.add(new AdjacentNode(currentWay.myNodes[currentIndex + 1], currentWay));
            }
        } catch (Exception e) {
        }
        try {
            boolean isAdjacent = true;
            for (int j = 0; j < currentNode.lava_value_indexed.length; j++) {
                if (!(currentWay.myNodes[currentIndex - 1].lava_value_indexed[lavaIndex] < fuel - tollOff && currentWay.isOneWay==false)) {
                    isAdjacent=false;
                }
            }
            if (isAdjacent) {
                adjacent.add(new AdjacentNode(currentWay.myNodes[currentIndex - 1], currentWay));
            }
        } catch (Exception e) {
        }
    }
    
    public void branch(LocationNode node, Way way, double passed_fuel, double lastLat, double lastLon,int lavaIndex) {
        myParent.makeLava(node, way, passed_fuel,trafficLayerIndex,lavaIndex);
        //System.out.println("number of lava: "+myParent.lavaBuffer.size());
    }
    
    public void selfDestroy() {
        myParent.remove_lava(this);
    }
    
}

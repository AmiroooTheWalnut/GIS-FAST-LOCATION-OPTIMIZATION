/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esmaieeli.gisFastLocationOptimization.Simulation;

import esmaieeli.gisFastLocationOptimization.GIS3D.LocationNode;
import esmaieeli.gisFastLocationOptimization.GIS3D.Way;
import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author Amir72c
 */
public class Lava_parallel {

    public FacilityLocation myFLParent;
    public FlowControl myFCParent;
    public LocationNode currentNode;
    public Way currentWay;
    public double fuel;
    public int currentIndex;
    public ArrayList adjacent = new ArrayList();
    public double last_lat;
    public double last_lon;
    public double tollOff = 0.1;
    public int trafficLayerIndex = -1;
    public int lavaIndex;
    public double fuelLoss;
    public double distancePassed;

    public Lava_parallel(FacilityLocation FLParent, LocationNode passedNode, Way passedWay, double passedfuel, double passedLat, double passedLon, int index, double passed_tollOff) {
        myFLParent = FLParent;
        currentNode = passedNode;
        currentWay = passedWay;
        fuel = passedfuel;
        currentIndex = detect_index(currentWay, currentNode);
        last_lat = passedLat;
        last_lon = passedLon;
        lavaIndex = index;
        tollOff=passed_tollOff;
        //flow();
    }

    public Lava_parallel(FlowControl FCParent, LocationNode passedNode, Way passedWay, double passedfuel, double passedLat, double passedLon, int index, double passed_tollOff) {
        myFCParent = FCParent;
        currentNode = passedNode;
        currentWay = passedWay;
        fuel = passedfuel;
        currentIndex = detect_index(currentWay, currentNode);
        last_lat = passedLat;
        last_lon = passedLon;
        lavaIndex = index;
        tollOff=passed_tollOff;
        //flow();
    }

    public void flowOneLayerCompetingLavaBased() {
        distancePassed = calculate_distance(currentNode.lat, currentNode.lon, last_lat, last_lon);
        if (trafficLayerIndex > -1) {
            fuelLoss = 100 * calculate_distance(currentNode.lat, currentNode.lon, last_lat, last_lon) * currentWay.typeWeight * (myFCParent.myParent.getLayerValue(currentNode, trafficLayerIndex));
        } else {
            fuelLoss = 100 * calculate_distance(currentNode.lat, currentNode.lon, last_lat, last_lon) * currentWay.typeWeight;
        }
        if (fuelLoss < 0.001) {
            fuelLoss = 0.001;
        }
        fuel = fuel - fuelLoss;
        //System.out.println(fuel);
        if (currentNode.lava_value_indexed[lavaIndex] < fuel) {
            currentNode.lava_value_indexed[lavaIndex] = fuel;
        } else {
//            System.out.println("STRONGER LAVA TRACE");
            selfDestroyOnFlowControl();
            return;
        }
        currentNode.isBurned = true;
        if (myFCParent.isDecoyable) {
            color_burn(currentIndex, currentWay, fuel);
        } else {
            //System.out.println(myParent.myParent.flowControl.minLava);
            if (fuel < myFCParent.minLava) {
                myFCParent.minLava = fuel;
            }
        }
        if (fuel > 0 || myFCParent.isDecoyable == false) {
            detectAdjacentCompetingLavaBased();
//            System.out.println(adjacent.size());
            if (adjacent.size() > 0) {
                last_lat = currentNode.lat;
                last_lon = currentNode.lon;
                this.currentNode = ((AdjacentNode) adjacent.get(0)).myNode;
                this.currentWay = ((AdjacentNode) adjacent.get(0)).myWay;
                currentIndex = detect_index(currentWay, currentNode);
                for (int i = 1; i < adjacent.size(); i++) {
                    brachOnFlowControl(((AdjacentNode) adjacent.get(i)).myNode, myFCParent.myParent.allData.all_Ways[myFCParent.detect_way_index(((AdjacentNode) adjacent.get(i)).myNode)], fuel, last_lat, last_lon, lavaIndex);
                }
            } else {
//                System.out.println("NO ADJACENT");
                selfDestroyOnFlowControl();
            }
        } else {
//            System.out.println("NO FUEL AND DECOYABLE");
            selfDestroyOnFlowControl();
        }
    }

    public void flowOneLayerCompetingFacilityBased() {
        if (trafficLayerIndex > -1) {
            fuelLoss = 100 * calculate_distance(currentNode.lat, currentNode.lon, last_lat, last_lon) * currentWay.typeWeight * (myFLParent.myParent.getLayerValue(currentNode, trafficLayerIndex));
        } else {
            fuelLoss = 100 * calculate_distance(currentNode.lat, currentNode.lon, last_lat, last_lon) * currentWay.typeWeight;
        }
        if (fuelLoss < 0.001) {
            fuelLoss = 0.001;
        }
        fuel = fuel - fuelLoss;
        //System.out.println(fuel);
        if (currentNode.lava_value_indexed[lavaIndex] < fuel) {
            currentNode.lava_value_indexed[lavaIndex] = fuel;
        } else {
            selfDestroyOnFacility();
            return;
        }
        currentNode.isBurned = true;
        currentNode.burntBy[lavaIndex]=myFLParent;
        if (myFLParent.isDecoyable) {
            //currentIndex = detect_index(currentWay, currentNode);
            color_set_facility(currentIndex, currentWay,myFLParent.color);
//            color_burn(currentIndex, currentWay, fuel);
        } else {
            //System.out.println(myParent.myParent.flowControl.minLava);
            if (fuel < myFLParent.myParent.flowControl.minLava) {
                myFLParent.myParent.flowControl.minLava = fuel;
            }
        }
        if (fuel > 0 || myFLParent.isDecoyable == false) {
            detectAdjacentCompetingFacilityBased();
            //System.out.println(adjacent.size());
            if (adjacent.size() > 0) {
                last_lat = currentNode.lat;
                last_lon = currentNode.lon;
                this.currentNode = ((AdjacentNode) adjacent.get(0)).myNode;
                this.currentWay = ((AdjacentNode) adjacent.get(0)).myWay;
                currentIndex = detect_index(currentWay, currentNode);
                for (int i = 1; i < adjacent.size(); i++) {
                    branchOnFacility(((AdjacentNode) adjacent.get(i)).myNode, myFLParent.myParent.allData.all_Ways[myFLParent.myParent.flowControl.detect_way_index(((AdjacentNode) adjacent.get(i)).myNode)], fuel, last_lat, last_lon, lavaIndex);
                }
                if ("residential".equals(currentWay.type)) {
//                    System.out.println(myFLParent);
//                    System.out.println(myFLParent.baseDemand);
                    if(myFLParent.zoneParent.populationDensityLayerIndex>-1){
                        myFLParent.baseDemand[0] = myFLParent.baseDemand[0] + new Float(fuel * myFLParent.myParent.getLayerValue(currentNode, myFLParent.zoneParent.populationDensityLayerIndex) * distancePassed);
                        if(myFLParent.zoneParent.luxuryLayerIndex>-1){
                            myFLParent.baseDemand[1] = myFLParent.baseDemand[0] + new Float(fuel * myFLParent.myParent.getLayerValue(currentNode, myFLParent.zoneParent.populationDensityLayerIndex) * distancePassed * myFLParent.myParent.getLayerValue(currentNode, myFLParent.zoneParent.luxuryLayerIndex));
                        }
                        if(myFLParent.zoneParent.studentLayerIndex>-1){
                            myFLParent.baseDemand[2] = myFLParent.baseDemand[0] + new Float(fuel * myFLParent.myParent.getLayerValue(currentNode, myFLParent.zoneParent.populationDensityLayerIndex) * distancePassed * myFLParent.myParent.getLayerValue(currentNode, myFLParent.zoneParent.studentLayerIndex));
                        }
                        myFLParent.tourismGain = myFLParent.tourismGain + new Float(fuel * myFLParent.myParent.getLayerValue(currentNode, myFLParent.zoneParent.populationDensityLayerIndex));
                    }
                }
            } else {
                selfDestroyOnFacility();
            }
        } else {
            selfDestroyOnFacility();
        }
    }

    public void flowMultiLayerCompetingZoneBased() {
        if (trafficLayerIndex > -1) {
            fuelLoss = 100 * calculate_distance(currentNode.lat, currentNode.lon, last_lat, last_lon) * currentWay.typeWeight * (myFLParent.myParent.getLayerValue(currentNode, trafficLayerIndex));
        } else {
            fuelLoss = 100 * calculate_distance(currentNode.lat, currentNode.lon, last_lat, last_lon) * currentWay.typeWeight;
        }
        if (fuelLoss < 0.001) {
            fuelLoss = 0.001;
        }
        fuel = fuel - fuelLoss;
        //System.out.println(fuel);
        if (currentNode.lava_value_indexed[lavaIndex] < fuel) {
            currentNode.lava_value_indexed[lavaIndex] = fuel;
        } else {
            selfDestroyOnFacility();
            return;
        }
        currentNode.isBurned = true;
        currentNode.burntBy[lavaIndex]=myFLParent;
        if (myFLParent.isDecoyable) {
            color_burn(currentIndex, currentWay, fuel);
        } else {
            //System.out.println(myParent.myParent.flowControl.minLava);
            if (fuel < myFLParent.myParent.flowControl.minLava) {
                myFLParent.myParent.flowControl.minLava = fuel;
            }
        }
        if (fuel > 0 || myFLParent.isDecoyable == false) {
            detectAdjacentCompetingFacilityBased();
            //System.out.println(adjacent.size());
            if (adjacent.size() > 0) {
                last_lat = currentNode.lat;
                last_lon = currentNode.lon;
                this.currentNode = ((AdjacentNode) adjacent.get(0)).myNode;
                this.currentWay = ((AdjacentNode) adjacent.get(0)).myWay;
                currentIndex = detect_index(currentWay, currentNode);
                for (int i = 1; i < adjacent.size(); i++) {
                    branchOnFacility(((AdjacentNode) adjacent.get(i)).myNode, myFLParent.myParent.allData.all_Ways[myFLParent.myParent.flowControl.detect_way_index(((AdjacentNode) adjacent.get(i)).myNode)], fuel, last_lat, last_lon, lavaIndex);
                }
                if ("residential".equals(currentWay.type)) {
                    myFLParent.baseDemand[0] = myFLParent.baseDemand[0] + new Float(fuel * myFLParent.myParent.getLayerValue(currentNode, myFLParent.zoneParent.populationDensityLayerIndex));
                    myFLParent.baseDemand[1] = myFLParent.baseDemand[0] + new Float(fuel * myFLParent.myParent.getLayerValue(currentNode, myFLParent.zoneParent.populationDensityLayerIndex) * myFLParent.myParent.getLayerValue(currentNode, myFLParent.zoneParent.luxuryLayerIndex));
                    myFLParent.baseDemand[2] = myFLParent.baseDemand[0] + new Float(fuel * myFLParent.myParent.getLayerValue(currentNode, myFLParent.zoneParent.populationDensityLayerIndex) * myFLParent.myParent.getLayerValue(currentNode, myFLParent.zoneParent.studentLayerIndex));
                    myFLParent.tourismGain = myFLParent.tourismGain + new Float(fuel * myFLParent.myParent.getLayerValue(currentNode, myFLParent.zoneParent.populationDensityLayerIndex));
                }
            } else {
                selfDestroyOnFacility();
            }
        } else {
            selfDestroyOnFacility();
        }
    }

    public void flowMultiLayerNonCompeting() {
        if (trafficLayerIndex > -1) {
            fuelLoss = 100 * calculate_distance(currentNode.lat, currentNode.lon, last_lat, last_lon) * currentWay.typeWeight * (myFLParent.myParent.getLayerValue(currentNode, trafficLayerIndex));
        } else {
            fuelLoss = 100 * calculate_distance(currentNode.lat, currentNode.lon, last_lat, last_lon) * currentWay.typeWeight;
        }
        if (fuelLoss < 0.001) {
            fuelLoss = 0.001;
        }
        fuel = fuel - fuelLoss;
        //System.out.println(fuel);
        if (currentNode.lava_value_indexed[lavaIndex] < fuel) {
            currentNode.lava_value_indexed[lavaIndex] = fuel;
        } else {
            selfDestroyOnFacility();
            return;
        }
        currentNode.isBurned = true;
        currentNode.burntBy[lavaIndex]=myFLParent;
        if (myFLParent.isDecoyable) {
            color_burn(currentIndex, currentWay, fuel);
        } else {
            //System.out.println(myParent.myParent.flowControl.minLava);
            if (fuel < myFLParent.myParent.flowControl.minLava) {
                myFLParent.myParent.flowControl.minLava = fuel;
            }
        }
        if (fuel > 0 || myFLParent.isDecoyable == false) {
            detectAdjacentNonCompeting();
            //System.out.println(adjacent.size());
            if (adjacent.size() > 0) {
                last_lat = currentNode.lat;
                last_lon = currentNode.lon;
                this.currentNode = ((AdjacentNode) adjacent.get(0)).myNode;
                this.currentWay = ((AdjacentNode) adjacent.get(0)).myWay;
                currentIndex = detect_index(currentWay, currentNode);
                for (int i = 1; i < adjacent.size(); i++) {
                    branchOnFacility(((AdjacentNode) adjacent.get(i)).myNode, myFLParent.myParent.allData.all_Ways[myFLParent.myParent.flowControl.detect_way_index(((AdjacentNode) adjacent.get(i)).myNode)], fuel, last_lat, last_lon, lavaIndex);
                }
                if ("residential".equals(currentWay.type)) {
                    myFLParent.baseDemand[0] = myFLParent.baseDemand[0] + new Float(fuel * myFLParent.myParent.getLayerValue(currentNode, myFLParent.zoneParent.populationDensityLayerIndex));
                    myFLParent.baseDemand[1] = myFLParent.baseDemand[0] + new Float(fuel * myFLParent.myParent.getLayerValue(currentNode, myFLParent.zoneParent.populationDensityLayerIndex) * myFLParent.myParent.getLayerValue(currentNode, myFLParent.zoneParent.luxuryLayerIndex));
                    myFLParent.baseDemand[2] = myFLParent.baseDemand[0] + new Float(fuel * myFLParent.myParent.getLayerValue(currentNode, myFLParent.zoneParent.populationDensityLayerIndex) * myFLParent.myParent.getLayerValue(currentNode, myFLParent.zoneParent.studentLayerIndex));
                    myFLParent.tourismGain = myFLParent.tourismGain + new Float(fuel * myFLParent.myParent.getLayerValue(currentNode, myFLParent.zoneParent.populationDensityLayerIndex));
                }
            } else {
                selfDestroyOnFacility();
            }
        } else {
            selfDestroyOnFacility();
        }
    }

    public void flowParallelZoneLinked() {
        if (trafficLayerIndex > -1) {
            fuel = fuel - 100 * calculate_distance(currentNode.lat, currentNode.lon, last_lat, last_lon) * currentWay.typeWeight * (myFLParent.myParent.getLayerValue(currentNode, trafficLayerIndex));
        } else {
            fuel = fuel - 100 * calculate_distance(currentNode.lat, currentNode.lon, last_lat, last_lon) * currentWay.typeWeight;
        }
        //System.out.println(fuel);
        if (currentNode.lava_value_indexed[lavaIndex] < fuel) {
            currentNode.lava_value_indexed[lavaIndex] = fuel;
        } else {
            selfDestroyOnFacility();
            return;
        }
        if (fuel > 0 || myFLParent.isDecoyable == false) {
            detect_adjacentParallelZoneLinked();
            //System.out.println(adjacent.size());
            if (adjacent.size() > 0) {
                last_lat = currentNode.lat;
                last_lon = currentNode.lon;
                this.currentNode = ((AdjacentNode) adjacent.get(0)).myNode;
                this.currentWay = ((AdjacentNode) adjacent.get(0)).myWay;
                currentIndex = detect_index(currentWay, currentNode);
                for (int i = 1; i < adjacent.size(); i++) {
                    branchOnFacility(((AdjacentNode) adjacent.get(i)).myNode, ((AdjacentNode) adjacent.get(i)).myWay, fuel, last_lat, last_lon, lavaIndex);
                }
                if ("residential".equals(currentWay.type)) {
                    myFLParent.baseDemand[0] = myFLParent.baseDemand[0] + new Float(fuel * myFLParent.myParent.getLayerValue(currentNode, myFLParent.zoneParent.populationDensityLayerIndex));
                    myFLParent.baseDemand[1] = myFLParent.baseDemand[0] + new Float(fuel * myFLParent.myParent.getLayerValue(currentNode, myFLParent.zoneParent.populationDensityLayerIndex) * myFLParent.myParent.getLayerValue(currentNode, myFLParent.zoneParent.luxuryLayerIndex));
                    myFLParent.baseDemand[2] = myFLParent.baseDemand[0] + new Float(fuel * myFLParent.myParent.getLayerValue(currentNode, myFLParent.zoneParent.populationDensityLayerIndex) * myFLParent.myParent.getLayerValue(currentNode, myFLParent.zoneParent.studentLayerIndex));
                    myFLParent.tourismGain = myFLParent.tourismGain + new Float(fuel * myFLParent.myParent.getLayerValue(currentNode, myFLParent.zoneParent.populationDensityLayerIndex));
                }
            } else {
                selfDestroyOnFacility();
            }
        } else {
            selfDestroyOnFacility();
        }
    }

    public double calculate_distance(double lat1, double lon1, double lat2, double lon2) {
        double distance;
        distance = Math.sqrt(Math.pow(lat1 - lat2, 2) + Math.pow(lon1 - lon2, 2));
        return distance;
    }

    public void color_burn(int index, Way way, double fuel) {
        if (myFLParent != null) {
            if (myFLParent.burnable) {
                way.color[index * 3 + 0] = (float) fuel / (float) myFLParent.capacity;
                way.color[index * 3 + 1] = 0f;
                way.color[index * 3 + 2] = (float) (myFLParent.capacity / (float) 10) / (float) fuel;
            }
        }
    }
    
    public void color_set_facility(int index,Way way,Color color){
        if (myFLParent != null) {
            if (myFLParent.burnable) {
                way.color[index * 3 + 0] = color.getRed()/255f;
                way.color[index * 3 + 1] = color.getGreen()/255f;
                way.color[index * 3 + 2] = color.getBlue()/255f;
            }
        }
    }

    public int detect_index(Way way, LocationNode node) {
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

    public void detect_adjacentParallelZoneLinked() {
        adjacent.clear();
        for (int i = 0; i < currentNode.myWays.length; i++) {
            if (currentNode.myWays[i].id!=currentWay.id) {
                int temp = detect_index(currentNode.myWays[i], currentNode);
                if (temp != -1) {
                    try {
                        if (currentNode.myWays[i].myNodes[temp - 1].lava_value_indexed[lavaIndex] < fuel - tollOff && currentNode.myWays[i].isOneWay == false) {
                            //color_burn(temp, currentNode.myWays[i], fuel);
                            adjacent.add(new AdjacentNode(currentNode.myWays[i].myNodes[temp - 1], currentNode.myWays[i]));
                        }
                    } catch (Exception e) {
                    }
                    try {
                        if (currentNode.myWays[i].myNodes[temp + 1].lava_value_indexed[lavaIndex] < fuel - tollOff) {
                            //color_burn(temp, currentNode.myWays[i], fuel);
                            adjacent.add(new AdjacentNode(currentNode.myWays[i].myNodes[temp + 1], currentNode.myWays[i]));
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }
        try {
            if (currentWay.myNodes[currentIndex + 1].lava_value_indexed[lavaIndex] < fuel - tollOff) {
                adjacent.add(new AdjacentNode(currentWay.myNodes[currentIndex + 1], currentWay));
            }
        } catch (Exception e) {
        }
        try {
            if (currentWay.myNodes[currentIndex - 1].lava_value_indexed[lavaIndex] < fuel - tollOff && currentWay.isOneWay == false) {
                adjacent.add(new AdjacentNode(currentWay.myNodes[currentIndex - 1], currentWay));
            }
        } catch (Exception e) {
        }
    }

    public void detectAdjacentCompetingFacilityBased() {
        adjacent.clear();
        for (int i = 0; i < currentNode.myWays.length; i++) {
            if (currentNode.myWays[i].id!=currentWay.id) {
                int temp = detect_index(currentNode.myWays[i], currentNode);
                if (temp != -1) {
                    try {
                        if ((currentNode.myWays[i].myNodes[temp - 1].lava_value_indexed[lavaIndex] < fuel - tollOff || (currentNode.myWays[i].myNodes[temp - 1].isBurned == false && myFLParent.isDecoyable == false)) && currentNode.myWays[i].isOneWay == false) {
                            //color_burn(temp, currentNode.myWays[i], fuel);
                            adjacent.add(new AdjacentNode(currentNode.myWays[i].myNodes[temp - 1], currentNode.myWays[i]));
                        }
                    } catch (Exception e) {
                    }
                    try {
                        if (currentNode.myWays[i].myNodes[temp + 1].lava_value_indexed[lavaIndex] < fuel - tollOff || (currentNode.myWays[i].myNodes[temp + 1].isBurned == false && myFLParent.isDecoyable == false)) {
                            //color_burn(temp, currentNode.myWays[i], fuel);
                            adjacent.add(new AdjacentNode(currentNode.myWays[i].myNodes[temp + 1], currentNode.myWays[i]));
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }
        try {
            if (currentWay.myNodes[currentIndex + 1].lava_value_indexed[lavaIndex] < fuel - tollOff || (currentWay.myNodes[currentIndex + 1].isBurned == false && myFLParent.isDecoyable == false)) {
                adjacent.add(new AdjacentNode(currentWay.myNodes[currentIndex + 1], currentWay));
            }
        } catch (Exception e) {
        }
        try {
            if ((currentWay.myNodes[currentIndex - 1].lava_value_indexed[lavaIndex] < fuel - tollOff || (currentWay.myNodes[currentIndex - 1].isBurned == false && myFLParent.isDecoyable == false)) && currentWay.isOneWay == false) {
                adjacent.add(new AdjacentNode(currentWay.myNodes[currentIndex - 1], currentWay));
            }
        } catch (Exception e) {
        }
    }

    public void detectAdjacentCompetingLavaBased() {
        adjacent.clear();
        for (int i = 0; i < currentNode.myWays.length; i++) {
            if (currentNode.myWays[i].id!=currentWay.id) {
                int temp = detect_index(currentNode.myWays[i], currentNode);
                if (temp != -1) {
                    try {
                        if ((currentNode.myWays[i].myNodes[temp - 1].lava_value_indexed[lavaIndex] < fuel - tollOff || (currentNode.myWays[i].myNodes[temp - 1].isBurned == false && myFCParent.isDecoyable == false)) && currentNode.myWays[i].isOneWay == false) {
                            //color_burn(temp, currentNode.myWays[i], fuel);
                            adjacent.add(new AdjacentNode(currentNode.myWays[i].myNodes[temp - 1], currentNode.myWays[i]));
                        }
                    } catch (Exception e) {
//                        System.out.println("OTHER WAY, ADJACENT -1 ERROR");
//                        System.out.println(e.toString());
                    }
                    try {
                        if (currentNode.myWays[i].myNodes[temp + 1].lava_value_indexed[lavaIndex] < fuel - tollOff || (currentNode.myWays[i].myNodes[temp + 1].isBurned == false && myFCParent.isDecoyable == false)) {
                            //color_burn(temp, currentNode.myWays[i], fuel);
                            adjacent.add(new AdjacentNode(currentNode.myWays[i].myNodes[temp + 1], currentNode.myWays[i]));
                        }
                    } catch (Exception e) {
//                        System.out.println("OTHER WAY, ADJACENT +1 ERROR");
//                        System.out.println(e.toString());
                    }
                }
            }
        }
        try {
            if (currentWay.myNodes[currentIndex + 1].lava_value_indexed[lavaIndex] < fuel - tollOff || (currentWay.myNodes[currentIndex + 1].isBurned == false && myFCParent.isDecoyable == false)) {
                adjacent.add(new AdjacentNode(currentWay.myNodes[currentIndex + 1], currentWay));
            }
        } catch (Exception e) {
//            System.out.println("CURRENT WAY, ADJACENT -1 ERROR");
//            System.out.println(e.toString());
        }
        try {
            if ((currentWay.myNodes[currentIndex - 1].lava_value_indexed[lavaIndex] < fuel - tollOff || (currentWay.myNodes[currentIndex - 1].isBurned == false && myFCParent.isDecoyable == false)) && currentWay.isOneWay == false) {
                adjacent.add(new AdjacentNode(currentWay.myNodes[currentIndex - 1], currentWay));
            }
        } catch (Exception e) {
//            System.out.println("CURRENT WAY, ADJACENT +1 ERROR");
//            System.out.println(e.toString());
        }
    }

    public void detectAdjacentNonCompeting() {
        adjacent.clear();
        for (int i = 0; i < currentNode.myWays.length; i++) {
            if (currentNode.myWays[i].id!=currentWay.id) {
                int temp = detect_index(currentNode.myWays[i], currentNode);
                if (temp != -1) {
                    try {
                        if (currentNode.myWays[i].isOneWay == false) {
                            //color_burn(temp, currentNode.myWays[i], fuel);
                            adjacent.add(new AdjacentNode(currentNode.myWays[i].myNodes[temp - 1], currentNode.myWays[i]));
                        }
                    } catch (Exception e) {
                    }
                    try {
//                        if (currentNode.myWays[i].myNodes[temp + 1].lava_value_indexed[lavaIndex] < fuel - tollOff || (currentNode.myWays[i].myNodes[temp + 1].isBurned==false && myParent.isDecoyable==false)) {
                        //color_burn(temp, currentNode.myWays[i], fuel);
                        adjacent.add(new AdjacentNode(currentNode.myWays[i].myNodes[temp + 1], currentNode.myWays[i]));
//                        }
                    } catch (Exception e) {
                    }
                }
            }
        }
        try {
//            if (currentWay.myNodes[currentIndex + 1].lava_value_indexed[lavaIndex] < fuel - tollOff || (currentWay.myNodes[currentIndex + 1].isBurned==false && myParent.isDecoyable==false)) {
            adjacent.add(new AdjacentNode(currentWay.myNodes[currentIndex + 1], currentWay));
//            }
        } catch (Exception e) {
        }
        try {
            if (currentWay.isOneWay == false) {
                adjacent.add(new AdjacentNode(currentWay.myNodes[currentIndex - 1], currentWay));
            }
        } catch (Exception e) {
        }
    }

    public void brachOnFlowControl(LocationNode node, Way way, double passed_fuel, double lastLat, double lastLon, int lavaIndex) {
        myFCParent.makeLava(node, way, passed_fuel, trafficLayerIndex, lavaIndex);
//        System.out.println(fuel);
//        System.out.println("BRANCHED!!!");
    }

    public void branchOnFacility(LocationNode node, Way way, double passed_fuel, double lastLat, double lastLon, int lavaIndex) {
        myFLParent.makeLava(node, way, passed_fuel, trafficLayerIndex, lavaIndex);
        //System.out.println("number of lava: "+myParent.lavaBuffer.size());
    }

    public void selfDestroyOnFlowControl() {
        myFCParent.lavaRemoveQueue(this);
    }

    public void selfDestroyOnFacility() {
        myFLParent.removeLava(this);
    }
}

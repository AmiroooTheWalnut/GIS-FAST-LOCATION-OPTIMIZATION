/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esmaieeli.gisFastLocationOptimization.Simulation;

import esmaieeli.gisFastLocationOptimization.GIS3D.AllData;
import esmaieeli.gisFastLocationOptimization.GIS3D.LayerDefinition;
import esmaieeli.gisFastLocationOptimization.GIS3D.LocationNode;
import esmaieeli.gisFastLocationOptimization.GIS3D.Way;
import esmaieeli.gisFastLocationOptimization.GUI.MainFrame;
import esmaieeli.gisFastLocationOptimization.GUI.MainFramePanel;
import java.util.ArrayList;

/**
 *
 * @author Amir72c
 */
public class Routing {

    AllData myAllData;
    ArrayList neighborNodeList = new ArrayList();
    ArrayList candidateNodeList = new ArrayList();
    public ArrayList path = new ArrayList();
    public double pathDistance;
    NeighborNode startLocation = new NeighborNode();
    NeighborNode endLocation = new NeighborNode();
    NeighborNode currentLocation = new NeighborNode();
    //MainFramePanel myParent;
    int trafficLayer;
    int lavaIndex;

    public Routing(AllData allData, int traffic, int Passed_lavaIndex) {
        //myParent = parent;
        myAllData = allData;
        trafficLayer = traffic;
        lavaIndex = Passed_lavaIndex;
    }

    private void detectNewNeighbour() {
        neighborNodeList.clear();
        for (int i = 0; i < currentLocation.myNode.myWays.length; i++) {
            Way tempWay = currentLocation.myNode.myWays[i];
            int index = detectNodeIndex(currentLocation.myNode.id, tempWay);
            try {
                NeighborNode neighborNode = new NeighborNode();
                neighborNode.myNode = tempWay.myNodes[index - 1];
                neighborNode.myWay = tempWay;
                if (neighborNode.myNode.isChecked[lavaIndex] == false) {
                    candidateNodeList.add(neighborNode);
                }
                neighborNodeList.add(neighborNode);
            } catch (Exception ex) {
            }
            try {
                NeighborNode neighborNode = new NeighborNode();
                neighborNode.myNode = tempWay.myNodes[index + 1];
                neighborNode.myWay = tempWay;
                if (neighborNode.myNode.isChecked[lavaIndex] == false) {
                    candidateNodeList.add(neighborNode);
                }
                neighborNodeList.add(neighborNode);
            } catch (Exception ex) {
            }
        }

    }

    private void calculateNeigbors() {
        for (int i = 0; i < neighborNodeList.size(); i++) {
            if (((NeighborNode) neighborNodeList.get(i)).myNode == null) {
                System.out.println("NEIGHBOR'S NODE IS NULL!");
            }
            if (((NeighborNode) neighborNodeList.get(i)).myNode.isChecked[lavaIndex] == false) {
                ((NeighborNode) neighborNodeList.get(i)).myNode.g_value[lavaIndex] = currentLocation.myNode.g_value[lavaIndex] + distance(currentLocation.myNode, ((NeighborNode) neighborNodeList.get(i)).myNode);
                ((NeighborNode) neighborNodeList.get(i)).cameFrom = currentLocation;
            } else {
                double temp = currentLocation.myNode.g_value[lavaIndex] + distance(currentLocation.myNode, ((NeighborNode) neighborNodeList.get(i)).myNode);
                if (temp < ((NeighborNode) neighborNodeList.get(i)).myNode.g_value[lavaIndex]) {
                    ((NeighborNode) neighborNodeList.get(i)).myNode.g_value[lavaIndex] = temp;
                    ((NeighborNode) neighborNodeList.get(i)).cameFrom = currentLocation;
                }
            }
            ((NeighborNode) neighborNodeList.get(i)).myNode.h_value[lavaIndex] = distance(((NeighborNode) neighborNodeList.get(i)).myNode, endLocation.myNode);
            ((NeighborNode) neighborNodeList.get(i)).myNode.f_value[lavaIndex] = ((NeighborNode) neighborNodeList.get(i)).myNode.g_value[lavaIndex] + ((NeighborNode) neighborNodeList.get(i)).myNode.h_value[lavaIndex];
            ((NeighborNode) neighborNodeList.get(i)).myNode.isChecked[lavaIndex] = true;
        }
    }

    private void detectNewNeighbourOfFacility(FacilityLocation fl) {
        neighborNodeList.clear();
        for (int i = 0; i < currentLocation.myNode.myWays.length; i++) {
            Way tempWay = currentLocation.myNode.myWays[i];
            int index = detectNodeIndex(currentLocation.myNode.id, tempWay);
            try {
                NeighborNode neighborNode = new NeighborNode();
                neighborNode.myNode = tempWay.myNodes[index - 1];
                neighborNode.myWay = tempWay;
                if (neighborNode.myNode.burntBy[lavaIndex] == fl) {
                    if (neighborNode.myNode.isChecked[lavaIndex] == false) {
                        candidateNodeList.add(neighborNode);
                    }
                    neighborNodeList.add(neighborNode);
                }
            } catch (Exception ex) {
            }
            try {
                NeighborNode neighborNode = new NeighborNode();
                neighborNode.myNode = tempWay.myNodes[index + 1];
                neighborNode.myWay = tempWay;
                if (neighborNode.myNode.burntBy[lavaIndex] == fl) {
                    if (neighborNode.myNode.isChecked[lavaIndex] == false) {
                        candidateNodeList.add(neighborNode);
                    }
                    neighborNodeList.add(neighborNode);
                }
            } catch (Exception ex) {
            }
        }

    }

    private double distance(LocationNode start, LocationNode end) {
        double dist = Math.sqrt(Math.pow(start.lat - end.lat, 2) + Math.pow(start.lon - end.lon, 2));
        short[] startIndexes = (short[]) start.layers.get(0);
        short[] endIndexes = (short[]) end.layers.get(0);
        double WV = 0;
        for (int i = 0; i < startIndexes.length; i++) {
            WV = ((LayerDefinition) myAllData.all_Layers.get(0)).values[startIndexes[i] - 1];
        }
        for (int i = 0; i < endIndexes.length; i++) {
            WV = ((LayerDefinition) myAllData.all_Layers.get(0)).values[endIndexes[i] - 1];
        }
        WV = WV / (double) (startIndexes.length + endIndexes.length);
        if (trafficLayer > -1) {
            double totalTraffic = 0;
            if (start.layers.get(trafficLayer) instanceof short[]) {
                short startIndex = ((short[]) start.layers.get(trafficLayer))[0];
                short endIndex = ((short[]) end.layers.get(trafficLayer))[0];
                double startTraffic = ((LayerDefinition) myAllData.all_Layers.get(0)).values[startIndex - 1];
                double endTraffic = ((LayerDefinition) myAllData.all_Layers.get(0)).values[endIndex - 1];
                totalTraffic = (startTraffic + endTraffic) / (double) 2.0;
            } else if (start.layers.get(trafficLayer) instanceof Double) {
                double startTraffic = (double) start.layers.get(trafficLayer);
                double endTraffic = (double) end.layers.get(trafficLayer);
                totalTraffic = (startTraffic + endTraffic) / (double) 2.0;
            }
            if (totalTraffic == 0) {
                System.out.println("No traffic layer found in this the Nodes: " + "Start=" + start.id + "End=" + end.id);
            } else {
                WV = WV * totalTraffic;
            }
        }
        dist = dist / WV;
        return dist;
    }

    public int detectNodeIndex(long nodeID, Way way) {
        for (int i = 0; i < way.myNodes.length; i++) {
            if (nodeID == way.myNodes[i].id) {
                return i;
            }
        }
        return -1;
    }

    private void refresh() {
        for (int i = 0; i < myAllData.all_Nodes.length; i++) {
            myAllData.all_Nodes[i].g_value[lavaIndex] = 0;
            myAllData.all_Nodes[i].isChecked[lavaIndex] = false;
            myAllData.all_Nodes[i].isPassed[lavaIndex] = false;
            myAllData.all_Nodes[i].f_value[lavaIndex] = 0;
            myAllData.all_Nodes[i].h_value[lavaIndex] = 0;
        }
        path.clear();
    }

    public void findPath(LocationNode startNode, LocationNode endNode) {
        refresh();
        startLocation.myNode = startNode;
        startLocation.myWay = startNode.myWays[0];
//        System.out.println(endLocation);
//        System.out.println(endNode);
//        System.out.println(endNode.myWays[0]);
        endLocation.myNode = endNode;
        endLocation.myWay = endNode.myWays[0];
        currentLocation = startLocation;
        currentLocation.myNode.isChecked[lavaIndex] = true;
        while (currentLocation.myNode.id != endLocation.myNode.id) {
            detectNewNeighbour();
            calculateNeigbors();
            double minValue = Double.POSITIVE_INFINITY;
            int minIndex = -1;
//            System.out.println("candidate number: "+candidateNodeList.size());
//            System.out.println("neighbor number: "+neighborNodeList.size());
//            System.out.println("is checked?: "+((NeighborNode) neighborNodeList.get(0)).myNode.isChecked);
            for (int i = 0; i < candidateNodeList.size(); i++) {
                if (((NeighborNode) candidateNodeList.get(i)).myNode == null) {
                    System.out.println("NEIGHBOR'S NODE IS NULL!");
                }

                if (((NeighborNode) candidateNodeList.get(i)).myNode.f_value[lavaIndex] < minValue) {
                    minValue = ((NeighborNode) candidateNodeList.get(i)).myNode.f_value[lavaIndex];
                    minIndex = i;
                }
//                System.out.println("here");
            }
            //System.out.println("minIndex: "+minIndex);
            if (candidateNodeList.isEmpty() == true) {
                pathDistance = Double.POSITIVE_INFINITY;
                path = null;
                return;
            } else {
                currentLocation = ((NeighborNode) candidateNodeList.get(minIndex));
                candidateNodeList.remove(minIndex);
            }

        }
        pathDistance = currentLocation.myNode.g_value[lavaIndex];
        while (currentLocation.myNode.id != startLocation.myNode.id) {
            path.add(currentLocation);
            currentLocation = currentLocation.cameFrom;
        }
    }

    public void findPathToFacilityLocation(LocationNode startNode, FacilityLocation parent) {
        refresh();
        startLocation.myNode = startNode;
        startLocation.myWay = startNode.myWays[0];
//        System.out.println(endLocation);
//        System.out.println(endNode);
//        System.out.println(endNode.myWays[0]);
        endLocation.myNode = parent.nodeLocation;
        endLocation.myWay = parent.nodeLocation.myWays[0];
        currentLocation = startLocation;
        currentLocation.myNode.isChecked[lavaIndex] = true;
        while (currentLocation.myNode.id != endLocation.myNode.id) {
            if(currentLocation.myNode.isConnectedToFacility[lavaIndex]==true){
                //System.out.println("BROKE");
                break;
            }
            detectNewNeighbourOfFacility(parent);
            calculateNeigbors();
            double minValue = Double.POSITIVE_INFINITY;
            int minIndex = -1;
//            System.out.println("candidate number: "+candidateNodeList.size());
//            System.out.println("neighbor number: "+neighborNodeList.size());
//            System.out.println("is checked?: "+((NeighborNode) neighborNodeList.get(0)).myNode.isChecked);
            for (int i = 0; i < candidateNodeList.size(); i++) {
                if (((NeighborNode) candidateNodeList.get(i)).myNode == null) {
                    System.out.println("NEIGHBOR'S NODE IS NULL!");
                }

                if (((NeighborNode) candidateNodeList.get(i)).myNode.f_value[lavaIndex] < minValue) {
                    minValue = ((NeighborNode) candidateNodeList.get(i)).myNode.f_value[lavaIndex];
                    minIndex = i;
                }
//                System.out.println("here");
            }
            //System.out.println("minIndex: "+minIndex);
            if (candidateNodeList.isEmpty() == true) {
                pathDistance = Double.POSITIVE_INFINITY;
                path = null;
                return;
            } else {
                currentLocation = ((NeighborNode) candidateNodeList.get(minIndex));
                candidateNodeList.remove(minIndex);
            }

        }
        pathDistance = currentLocation.myNode.g_value[lavaIndex];
        while (currentLocation.myNode.id != startLocation.myNode.id) {
            currentLocation.myNode.isConnectedToFacility[lavaIndex]=true;
            path.add(currentLocation);
            currentLocation = currentLocation.cameFrom;
        }
    }
    
    public void setClosestDifferentFacilityLocation(LocationNode startNode, FacilityLocation parent) {
        refresh();
        startLocation.myNode = startNode;
        startLocation.myWay = startNode.myWays[0];
//        System.out.println(endLocation);
//        System.out.println(endNode);
//        System.out.println(endNode.myWays[0]);
        endLocation.myNode = parent.nodeLocation;
        endLocation.myWay = parent.nodeLocation.myWays[0];
        currentLocation = startLocation;
        currentLocation.myNode.isChecked[lavaIndex] = true;
        while (currentLocation.myNode.burntBy[lavaIndex] == parent) {
            detectNewNeighbour();
            calculateNeigbors();
            double minValue = Double.POSITIVE_INFINITY;
            int minIndex = -1;
//            System.out.println("candidate number: "+candidateNodeList.size());
//            System.out.println("neighbor number: "+neighborNodeList.size());
//            System.out.println("is checked?: "+((NeighborNode) neighborNodeList.get(0)).myNode.isChecked);
            for (int i = 0; i < candidateNodeList.size(); i++) {
                if (((NeighborNode) candidateNodeList.get(i)).myNode == null) {
                    System.out.println("NEIGHBOR'S NODE IS NULL!");
                }

                if (((NeighborNode) candidateNodeList.get(i)).myNode.g_value[lavaIndex] < minValue) {
                    minValue = ((NeighborNode) candidateNodeList.get(i)).myNode.g_value[lavaIndex];
                    minIndex = i;
                }
//                System.out.println("here");
            }
            //System.out.println("minIndex: "+minIndex);
            if (candidateNodeList.isEmpty() == true) {
                pathDistance = Double.POSITIVE_INFINITY;
                path = null;
                return;
            } else {
                currentLocation = ((NeighborNode) candidateNodeList.get(minIndex));
                candidateNodeList.remove(minIndex);
            }

        }
        startNode.burntBy[lavaIndex]=currentLocation.myNode.burntBy[lavaIndex];
        //pathDistance = currentLocation.myNode.g_value[lavaIndex];
        //while (currentLocation.myNode.id != startLocation.myNode.id) {
        //    path.add(currentLocation);
        //    currentLocation = currentLocation.cameFrom;
        //}
    }

}

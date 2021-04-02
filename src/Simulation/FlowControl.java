/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulation;

import Clustering.Zones;
import GIS3D.LocationNode;
import GIS3D.PreProcessor;
import GIS3D.Way;
import GUI.main_frame;
import java.util.ArrayList;

/**
 *
 * @author Amir72c
 */
public class FlowControl {
    //LAVA BASED RELATED VARIABLES

    public ArrayList<Lava_parallel> lavaBuffer = new ArrayList();
    public ArrayList<Lava_parallel> queue_lavaBuffer = new ArrayList();
    public ArrayList<Lava_parallel> queue_lavaRemove = new ArrayList();
    public boolean isDecoyable = true;
    public ThreadOneLayerCompetingLavaBased oneLayerCompetingLavaBasedThreads[];
    public ThreadOneLayerCompetingFacilityBased oneLayerCompetingFacilityBasedThreads[];
    public ThreadMultiLayerCompetingZoneBased multiLayerCompetingZoneBasedThreads[];
    //LAVA BASED RELATED VARIABLES
    public double minLava = Double.POSITIVE_INFINITY;
    public double maxLava = Double.NEGATIVE_INFINITY;
    public main_frame myParent;
    public int resetLayerIndex = -1;
    public int blockStreetLayerIndex = -1;
    public int numActiveLava = 1000;

    public FlowControl(main_frame parent) {
        myParent = parent;
    }

    public void iterateOneLayerCompetingLavaBased(int numCPUs) {
//        System.out.println("Current queue lava buffer size: " + queue_lavaBuffer.size());
        if (queue_lavaBuffer.size() < numActiveLava) {
            for (int i = queue_lavaBuffer.size() - 1; i > -1; i--) {
                lavaBuffer.add(queue_lavaBuffer.get(i));
                queue_lavaBuffer.remove(i);
            }
        } else {
            for (int i = numActiveLava - 1; i > -1; i--) {
                lavaBuffer.add(queue_lavaBuffer.get(i));
                queue_lavaBuffer.remove(i);
            }
        }
        removeLava();
//        queue_lavaBuffer.clear();
//        System.out.println("Max int value: " + Integer.MAX_VALUE);
//        System.out.println("Current lava buffer size: " + lavaBuffer.size());
        CPUUpperLowerBounds spreadedTasks[] = PreProcessor.spreadTasks(numCPUs, lavaBuffer.size());
        for (int i = 0; i < numCPUs; i++) {
            oneLayerCompetingLavaBasedThreads[i] = new ThreadOneLayerCompetingLavaBased(this, spreadedTasks[i]);
        }
        for (int i = 0; i < oneLayerCompetingLavaBasedThreads.length; i++) {
            oneLayerCompetingLavaBasedThreads[i].myThread.start();
        }
        for (int i = 0; i < oneLayerCompetingLavaBasedThreads.length; i++) {
            try {
                oneLayerCompetingLavaBasedThreads[i].myThread.join();
//                System.out.println("thread " + i + "finished!");
            } catch (InterruptedException ie) {
                System.out.println(ie.toString());
            }
        }
    }

    public void iterateOneLayerCompetingFacilityBased(FacilityLocation facilities[], int numCPUs) {
        for (int j = 0; j < facilities.length; j++) {
            if (facilities[j].queue_lavaBuffer.size() < numActiveLava) {
                for (int i = facilities[j].queue_lavaBuffer.size() - 1; i > -1; i--) {
                    facilities[j].lavaBuffer.add((Lava_parallel) facilities[j].queue_lavaBuffer.get(i));
                    facilities[j].queue_lavaBuffer.remove(i);
                }
            } else {
                for (int i = numActiveLava - 1; i > -1; i--) {
                    facilities[j].lavaBuffer.add((Lava_parallel) facilities[j].queue_lavaBuffer.get(i));
                    facilities[j].queue_lavaBuffer.remove(i);
                }
            }
        }
        CPUUpperLowerBounds spreadedTasks[] = PreProcessor.spreadTasks(numCPUs, facilities.length);
        for (int i = 0; i < numCPUs; i++) {
            oneLayerCompetingFacilityBasedThreads[i] = new ThreadOneLayerCompetingFacilityBased(facilities, spreadedTasks[i]);
        }
        for (int i = 0; i < oneLayerCompetingFacilityBasedThreads.length; i++) {
            oneLayerCompetingFacilityBasedThreads[i].myThread.start();
        }
        for (int i = 0; i < oneLayerCompetingFacilityBasedThreads.length; i++) {
            try {
                oneLayerCompetingFacilityBasedThreads[i].myThread.join();
//                System.out.println("thread " + i + "finished!");
            } catch (InterruptedException ie) {
                System.out.println(ie.toString());
            }
        }
    }

    public void iterateMultiLayerCompetingZoneBased(Zones zones[], int numCPUs) {
        for (int k = 0; k < zones.length; k++) {
            for (int j = 0; j < zones[k].locations.length; j++) {
                if (zones[k].locations[j].queue_lavaBuffer.size() < numActiveLava) {
                    for (int i = zones[k].locations[j].queue_lavaBuffer.size() - 1; i > -1; i--) {
                        zones[k].locations[j].lavaBuffer.add((Lava_parallel) zones[k].locations[j].queue_lavaBuffer.get(i));
                        zones[k].locations[j].queue_lavaBuffer.remove(i);
                    }
                } else {
                    for (int i = numActiveLava - 1; i > -1; i--) {
                        zones[k].locations[j].lavaBuffer.add((Lava_parallel) zones[k].locations[j].queue_lavaBuffer.get(i));
                        zones[k].locations[j].queue_lavaBuffer.remove(i);
                    }
                }
            }
        }
        CPUUpperLowerBounds spreadedTasks[] = PreProcessor.spreadTasks(numCPUs, zones.length);
        for (int i = 0; i < numCPUs; i++) {
            multiLayerCompetingZoneBasedThreads[i] = new ThreadMultiLayerCompetingZoneBased(zones, spreadedTasks[i]);
        }
        for (int i = 0; i < multiLayerCompetingZoneBasedThreads.length; i++) {

            multiLayerCompetingZoneBasedThreads[i].myThread.start();
        }
        for (int i = 0; i < multiLayerCompetingZoneBasedThreads.length; i++) {
            try {
                multiLayerCompetingZoneBasedThreads[i].myThread.join();
//                System.out.println("thread " + i + "finished!");
            } catch (InterruptedException ie) {
                System.out.println(ie.toString());
            }
        }
    }

    public void iterateMultiLayerNonCompetingFacilityBased(Zones zones[], int numCPUs) {
        for (int k = 0; k < zones.length; k++) {
            if (zones[k].locations[0].queue_lavaBuffer.size() < numActiveLava) {
                for (int i = zones[k].locations[0].queue_lavaBuffer.size() - 1; i > -1; i--) {
                    zones[k].locations[0].lavaBuffer.add((Lava_parallel) zones[k].locations[0].queue_lavaBuffer.get(i));
                    zones[k].locations[0].queue_lavaBuffer.remove(i);
                }
            } else {
                for (int i = numActiveLava - 1; i > -1; i--) {
                    zones[k].locations[0].lavaBuffer.add((Lava_parallel) zones[k].locations[0].queue_lavaBuffer.get(i));
                    zones[k].locations[0].queue_lavaBuffer.remove(i);
                }
            }
        }
        CPUUpperLowerBounds spreadedTasks[] = PreProcessor.spreadTasks(numCPUs, zones.length);
        for (int i = 0; i < numCPUs; i++) {
            multiLayerCompetingZoneBasedThreads[i] = new ThreadMultiLayerCompetingZoneBased(zones, spreadedTasks[i]);
        }
        for (int i = 0; i < multiLayerCompetingZoneBasedThreads.length; i++) {
            multiLayerCompetingZoneBasedThreads[i].myThread.start();
        }
        for (int i = 0; i < multiLayerCompetingZoneBasedThreads.length; i++) {
            try {
                multiLayerCompetingZoneBasedThreads[i].myThread.join();
//                System.out.println("thread " + i + "finished!");
            } catch (InterruptedException ie) {
                System.out.println(ie.toString());
            }
        }
    }

    public int detect_way_index(LocationNode node) {
        for (int i = 0; i < myParent.allData.all_Ways.length; i++) {
            for (int j = 0; j < myParent.allData.all_Ways[i].myNodes.length; j++) {
                if (node.id == myParent.allData.all_Ways[i].myNodes[j].id) {
                    return i;
                }
            }
        }
        return -1;
    }

    public int detect_node_index(Way way, LocationNode node) {
        int return_val = -1;
        for (int i = 0; i < way.myNodes.length; i++) {
            if (node.id == way.myNodes[i].id) {
                return_val = i;
            }
        }
        return return_val;
    }

    public int detect_global_node_index(LocationNode passed_node) {
        for (int i = 0; i < myParent.allData.all_Nodes.length; i++) {
            if (passed_node.id == myParent.allData.all_Nodes[i].id) {
                return i;
            }
        }
        return -1;
    }

    public void resetParallel(int lavaIndex) {
        if (resetLayerIndex < 0) {
            System.out.println("No reset layer defined!");
            for (int i = 0; i < myParent.allData.all_Nodes.length; i++) {
                myParent.allData.all_Nodes[i].lava_value_indexed[lavaIndex] = 0;
                myParent.allData.all_Nodes[i].isBurned = false;
                myParent.allData.all_Nodes[i].isChecked[lavaIndex] = false;
                myParent.allData.all_Nodes[i].isPassed[lavaIndex] = false;
                myParent.allData.all_Nodes[i].f_value[lavaIndex] = 0;
                myParent.allData.all_Nodes[i].g_value[lavaIndex] = 0;
                myParent.allData.all_Nodes[i].h_value[lavaIndex] = 0;
            }
        } else {
            //DENORMALIZE, BREAKS MODULARITY!!!
            double expectedDenormalized = 2;
            //DENORMALIZE, BREAKS MODULARITY!!!
            for (int i = 0; i < myParent.allData.all_Nodes.length; i++) {
                myParent.allData.all_Nodes[i].lava_value_indexed[lavaIndex] = ((double) myParent.allData.all_Nodes[i].layers.get(resetLayerIndex)) * expectedDenormalized;
                myParent.allData.all_Nodes[i].isBurned = true;
                myParent.allData.all_Nodes[i].isChecked[lavaIndex] = false;
                myParent.allData.all_Nodes[i].isPassed[lavaIndex] = false;
                myParent.allData.all_Nodes[i].f_value[lavaIndex] = 0;
                myParent.allData.all_Nodes[i].g_value[lavaIndex] = 0;
                myParent.allData.all_Nodes[i].h_value[lavaIndex] = 0;
            }
        }
    }

    public void resetDeepOneLayer() {
        for (int i = 0; i < myParent.allData.all_Ways.length; i++) {
//            myParent.allData.setParallelLayers(1, -1);
            for (int j = 0; j < myParent.allData.all_Ways[i].myNodes.length; j++) {
                myParent.allData.all_Ways[i].myNodes[j].lava_value_indexed[0] = Double.NEGATIVE_INFINITY;
                myParent.allData.all_Ways[i].myNodes[j].isBurned = false;
            }
        }
        myParent.preProcessor.setWaysColorLayerBased(myParent.allData, 0);
    }

    public void simulateOneLayerCompetingLavaBased(FacilityLocation facilityLocations[], int trafficLayerIndex, int numCPUs, int passed_ResetLayer, boolean isDebug) {
//        System.out.println(((LayerDefinition)myParent.allData.all_Layers.get(passed_ResetLayer)).layerName);
        resetLayerIndex = passed_ResetLayer;
        lavaBuffer.clear();
        queue_lavaBuffer.clear();
        myParent.allData.setParallelLayers(1, passed_ResetLayer);
        for (int i = 0; i < facilityLocations.length; i++) {
            Lava_parallel temp_lava = new Lava_parallel(this, facilityLocations[i].nodeLocation, facilityLocations[i].way, facilityLocations[i].capacity, facilityLocations[i].nodeLocation.lat, facilityLocations[i].nodeLocation.lon, 0);
            temp_lava.trafficLayerIndex = trafficLayerIndex;
            queue_lavaBuffer.add(temp_lava);
        }
        oneLayerCompetingLavaBasedThreads = new ThreadOneLayerCompetingLavaBased[numCPUs];
        if (isDebug == false) {
            boolean lava_exists;
            do {
                lava_exists = false;
                iterateOneLayerCompetingLavaBased(numCPUs);
                if (!lavaBuffer.isEmpty()) {
                    lava_exists = true;
                }
                //System.out.println(lava_exists);
            } while (lava_exists == true);
        }
    }

    public void simulateOneLayerCompetingFacilityBased(FacilityLocation facilityLocations[], int trafficLayerIndex, int numCPUs, int passed_ResetLayer, boolean isDebug) {
        resetLayerIndex = passed_ResetLayer;
        myParent.allData.setParallelLayers(1, passed_ResetLayer);
        int landPriceLayerIndex = myParent.findLayer("landprice");
        int landExistanceLayerIndex = myParent.findLayer("freespace");
        int baseDemandLayerIndex = myParent.findLayer("base");
        int tourismGainLayerIndex = myParent.findLayer("tourismlayer");
        int populationDensityLayerIndex = myParent.findLayer("population_density");
        int studentLayerIndex = myParent.findLayer("studentLayer");
        int luxuryLayerIndex = myParent.findLayer("luxuryLayer");
        Zones temporaryZone = new Zones(landPriceLayerIndex, landExistanceLayerIndex, baseDemandLayerIndex, tourismGainLayerIndex, populationDensityLayerIndex, studentLayerIndex, luxuryLayerIndex);
        for (int i = 0; i < facilityLocations.length; i++) {
            facilityLocations[i].zoneParent = temporaryZone;
            facilityLocations[i].makeLava(facilityLocations[i].nodeLocation, facilityLocations[i].way, facilityLocations[i].capacity, trafficLayerIndex, 0);
        }
        oneLayerCompetingFacilityBasedThreads = new ThreadOneLayerCompetingFacilityBased[numCPUs];
        if (isDebug == false) {
            boolean lava_exists;
            do {
                lava_exists = false;
                iterateOneLayerCompetingFacilityBased(facilityLocations, numCPUs);
                for (int i = 0; i < facilityLocations.length; i++) {
                    if (!facilityLocations[i].lavaBuffer.isEmpty()) {
                        lava_exists = true;
                    }
                }
                //System.out.println(lava_exists);
            } while (lava_exists == true);
            for (int i = 0; i < myParent.allData.all_Nodes[0].burntBy.length; i++) {
                myParent.preProcessor.setWaysColorBurntByFacility(myParent.allData, i);
            }
        }
    }

    public void simulateMultiLayerCompetingZoneBased(Zones zones[], int trafficLayerIndex, int numCPUs, int passed_ResetLayer, boolean isDebug) {
        resetLayerIndex = passed_ResetLayer;
        System.out.println("RESET LAYER: " + passed_ResetLayer);
        myParent.allData.setParallelLayers(zones.length, passed_ResetLayer);
        for (int i = 0; i < zones.length; i++) {
            for (int j = 0; j < zones[i].locations.length; j++) {
                zones[i].locations[j].makeLava(zones[i].locations[j].nodeLocation, zones[i].locations[j].way, zones[i].locations[j].capacity, trafficLayerIndex, i);
            }
        }
        multiLayerCompetingZoneBasedThreads = new ThreadMultiLayerCompetingZoneBased[numCPUs];
        if (isDebug == false) {
            boolean lava_exists;
            do {
                lava_exists = false;
                iterateMultiLayerCompetingZoneBased(zones, numCPUs);
                for (int i = 0; i < zones.length; i++) {
                    for (int j = 0; j < zones[i].locations.length; j++) {
                        if (!zones[i].locations[j].lavaBuffer.isEmpty()) {
                            lava_exists = true;
                        }
                    }
                }
                //System.out.println(lava_exists);
            } while (lava_exists == true);
            for (int i = 0; i < myParent.allData.all_Nodes[0].burntBy.length; i++) {
                myParent.preProcessor.setWaysColorBurntByFacility(myParent.allData, i);
            }
        }
    }

    public void simulateOneLayerNonCompetingSerial(FacilityLocation facilityLocations[], int trafficLayerIndex, boolean isDebug) {
        resetLayerIndex = -1;
        myParent.allData.setParallelLayers(1, -1);
        int landPriceLayerIndex = myParent.findLayer("landprice");
        int landExistanceLayerIndex = myParent.findLayer("freespace");
        int baseDemandLayerIndex = myParent.findLayer("base");
        int tourismGainLayerIndex = myParent.findLayer("tourismlayer");
        int populationDensityLayerIndex = myParent.findLayer("population_density");
        int studentLayerIndex = myParent.findLayer("studentLayer");
        int luxuryLayerIndex = myParent.findLayer("luxuryLayer");
        Zones temporaryZone = new Zones(landPriceLayerIndex, landExistanceLayerIndex, baseDemandLayerIndex, tourismGainLayerIndex, populationDensityLayerIndex, studentLayerIndex, luxuryLayerIndex);
        for (int i = 0; i < 1; i++) {
            facilityLocations[i].zoneParent = temporaryZone;
            facilityLocations[i].makeLava(facilityLocations[i].nodeLocation, facilityLocations[i].way, facilityLocations[i].capacity, trafficLayerIndex, 0);
        }
        oneLayerCompetingFacilityBasedThreads = new ThreadOneLayerCompetingFacilityBased[1];
        for (int i = 0; i < facilityLocations.length; i++) {
            myParent.allData.setParallelLayers(1, -1);
            facilityLocations[i].makeLava(facilityLocations[i].nodeLocation, facilityLocations[i].way, facilityLocations[i].capacity, trafficLayerIndex, 0);
            FacilityLocation passedFacilityLocation[] = new FacilityLocation[1];
            passedFacilityLocation[0] = facilityLocations[i];
            if (isDebug == false) {
                boolean lava_exists;
                do {
                    lava_exists = false;
                    iterateOneLayerCompetingFacilityBased(passedFacilityLocation, 1);
                    if (!passedFacilityLocation[0].lavaBuffer.isEmpty()) {
                        lava_exists = true;
                    }
                    //System.out.println(lava_exists);
                } while (lava_exists == true);
                for (int j = 0; j < myParent.allData.all_Nodes[0].burntBy.length; j++) {
                    myParent.preProcessor.setWaysColorBurntByFacility(myParent.allData, j);
                }
            }
        }
    }

    public void simulateMultiLayerNonCompetingFacilityBased(Zones zones[], int trafficLayerIndex, int numCPUs, boolean isDebug) {
        resetLayerIndex = -1;
        myParent.allData.setParallelLayers(zones.length, -1);
        for (int i = 0; i < zones.length; i++) {
//            for (int j = 0; j < zones[i].locations.length; j++) {
            zones[i].locations[0].makeLava(zones[i].locations[0].nodeLocation, zones[i].locations[0].way, zones[i].locations[0].capacity, trafficLayerIndex, i);
//            }
        }
        multiLayerCompetingZoneBasedThreads = new ThreadMultiLayerCompetingZoneBased[numCPUs];
        if (isDebug == false) {
            boolean lava_exists;
            do {
                lava_exists = false;
                iterateMultiLayerNonCompetingFacilityBased(zones, numCPUs);
                for (int i = 0; i < zones.length; i++) {
                    if (!zones[i].locations[0].lavaBuffer.isEmpty()) {
                        lava_exists = true;
                    }
                }
                //System.out.println(lava_exists);
            } while (lava_exists == true);
            for(int i=0;i<myParent.allData.all_Nodes[0].burntBy.length;i++){
                myParent.preProcessor.setWaysColorBurntByFacility(myParent.allData, i);
            }
        }
    }

    public void makeLava(LocationNode node, Way way, double passed_fuel, int trafficLayerIndex, int lavaIndex) {
        Lava_parallel temp_lava = new Lava_parallel(this, node, way, passed_fuel, node.lat, node.lon, lavaIndex);
        temp_lava.trafficLayerIndex = trafficLayerIndex;
        queue_lavaBuffer.add(temp_lava);
    }

    public void lavaRemoveQueue(Lava_parallel lava) {
        queue_lavaRemove.add(lava);
    }

    public void removeLava() {
        for (int i = 0; i < queue_lavaRemove.size(); i++) {
            lavaBuffer.remove(queue_lavaRemove.get(i));
//            System.out.println("LAVA REMOVED!!!");
        }
        queue_lavaRemove.clear();
    }
}

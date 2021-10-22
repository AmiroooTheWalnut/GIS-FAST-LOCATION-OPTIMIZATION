/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esmaieeli.gisFastLocationOptimization.Clustering;

import esmaieeli.gisFastLocationOptimization.GIS3D.Grid;
import esmaieeli.gisFastLocationOptimization.GIS3D.LayerDefinition;
import esmaieeli.gisFastLocationOptimization.GIS3D.LocationNode;
import esmaieeli.gisFastLocationOptimization.GIS3D.NumericLayer;
import esmaieeli.gisFastLocationOptimization.GUI.HeuristicController;
import esmaieeli.gisFastLocationOptimization.GUI.MainFrame;
import esmaieeli.gisFastLocationOptimization.GUI.MainFramePanel;
import esmaieeli.gisFastLocationOptimization.MathematicalModel.BenchmarkModel;
import esmaieeli.gisFastLocationOptimization.Simulation.FacilityLocation;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Amir72c
 */
public class Heuristic {

    public ArrayList<HeuristicFreeLocation> heuristicFreeLocations = new ArrayList();
    MainFramePanel MFParent;
    HeuristicController myParent;
    public HeuristicFlowControl heuristicFlowControl;
    public int trafficLayer;
    public int allMixedLayer;
    public int populationDensityLayer;
    public int tourismLayer;
    public int studentLayer;
    public int luxuryLayer;
    private int numCPUs;
    public double minCompetitorLava;
    LocationNode candidateLocation;
    boolean isPruneStarted = false;
    public boolean isAlgorithmFinished = false;
    public BenchmarkModel benchmarkModel;
    public double competitorsThreshold;
    public double demandThreshold = 20000;
    public int numCandidates = 20;
    
    public double publicObjValue;

    public Heuristic(MainFramePanel mfp, HeuristicController hcp, int tl, int aml, int pdl, int toul, int sl, int ll, int nC) {
        MFParent = mfp;
        myParent = hcp;
        trafficLayer = tl;
        allMixedLayer = aml;
        populationDensityLayer = pdl;
        tourismLayer = toul;
        studentLayer = sl;
        luxuryLayer = ll;
        numCPUs = nC;
        heuristicFlowControl = new HeuristicFlowControl(MFParent, this);
        heuristicFlowControl.resetLayerIndex = allMixedLayer;
        initIndexedLava();
    }

    public void initIndexedLava() {
        MFParent.allData.setParallelLayers(2, -1);
//        for (int i = 0; i < MFParent.allData.all_Nodes.length; i++) {
//            MFParent.allData.all_Nodes[i].lava_value_indexed = new double[2];
//        }
    }

    public void buildClusterer(boolean isChunked, double ct,double demandThreshold,int numCandidates) {
        competitorsThreshold=ct;
        long start = System.currentTimeMillis();
        initIndexedLava();
        iterate(false, isChunked, competitorsThreshold,demandThreshold,numCandidates);//TEMPORARY
        isPruneStarted = false;//TEMPORARY
        while (isAlgorithmFinished == false) {
            iterate(false, isChunked, competitorsThreshold,demandThreshold,numCandidates);
//            heuristicFlowControl.simulateSerial(trafficLayer,populationDensityLayer);
        }
        FacilityLocation fl[] = new FacilityLocation[heuristicFreeLocations.size()];
        for (int i = 0; i < heuristicFreeLocations.size(); i++) {
            fl[i] = heuristicFreeLocations.get(i).facilityLocation;
        }
        long end = System.currentTimeMillis();
        System.out.println("Elapsed time: " + (end - start) + " miliSeconds.");
        System.out.println("Starting mathematical solver.");
        benchmarkModel = new BenchmarkModel(MFParent);
        double objValue = benchmarkModel.runExternalFacilities(fl, numCPUs).mySolution.z;
        publicObjValue=objValue;
        System.out.println("Obj value: " + objValue);
    }

    public void iterate(boolean isIterative, boolean isChunked, double competitorsThreshold,double demandThreshold,int numCandidates) {
        boolean isDemandThresholdBased;
        if(demandThreshold>-1)
        {
            isDemandThresholdBased=true;
        }else{
            isDemandThresholdBased=false;
        }
        if (isPruneStarted == false) {
            Object output[];
            if (isChunked == true) {
                output = chunkedDetectMinimumAllMixed(allMixedLayer);
            } else {
                output = bulkDetectMinimumAllMixed(allMixedLayer);
            }
            candidateLocation = (LocationNode) output[0];
            minCompetitorLava = (double) output[1];
//            System.out.println("Min competitor: " + minCompetitorLava);
            heuristicFreeLocations.add(new HeuristicFreeLocation(this, new HeuristicFacilityLocation(MFParent, candidateLocation, candidateLocation.myWays[0], 2.5), allMixedLayer));

            calculateColors();

            if (isIterative == false) {
//                heuristicFlowControl.simulateSerial(heuristicFreeLocations, trafficLayer,populationDensityLayer);
                heuristicFlowControl.simulateParallel(trafficLayer, populationDensityLayer, numCPUs);
            }
            if (minCompetitorLava > competitorsThreshold) {
                isPruneStarted = true;
                myParent.parentApp.isRefreshing = true;
                System.out.println("First phase size: " + heuristicFreeLocations.size());
            }
        } else {
            if (isDemandThresholdBased == true) {
                Collections.sort(heuristicFreeLocations, Collections.reverseOrder());
                for (int i = heuristicFreeLocations.size()-1; i > 0; i--) {
                    if (heuristicFreeLocations.get(i).facilityLocation.baseDemand[0] < demandThreshold) {
                        heuristicFreeLocations.remove(i);
                    } else {
                        break;
                    }
                }
            } else {
                Collections.sort(heuristicFreeLocations);
                int numRemoval = heuristicFreeLocations.size() - numCandidates;
                for (int i = numRemoval - 1; i > 0; i--) {
                    heuristicFreeLocations.remove(i);
                }
            }
//            for (int i = 0; i < heuristicFreeLocations.size(); i++) {
//                System.out.println("Demand size: " + heuristicFreeLocations.get(i).facilityLocation.baseDemand[0]);
//            }

            isAlgorithmFinished = true;
//            for (int i = 0; i < heuristicFreeLocations.size(); i++) {
//                if (heuristicFreeLocations.get(i).facilityLocation.baseDemand[0] >= 20000) {
//                    isAlgorithmFinished = true;
//                    System.out.println("Prunned size: " + heuristicFreeLocations.size());
//                    return;
//                }
//                if (heuristicFreeLocations.get(i).facilityLocation.baseDemand[0] < 20000) {
//                    System.out.println("Demand size: " + heuristicFreeLocations.get(i).facilityLocation.baseDemand[0]);
//                    heuristicFreeLocations.remove(i);
//                    System.out.println("Current tree size: " + heuristicFreeLocations.size());
//
//                }
//            }
            System.out.println("Prunned size: " + heuristicFreeLocations.size());
            System.out.println("Demand size: " + heuristicFreeLocations.get(0).facilityLocation.baseDemand[0]);
            heuristicFlowControl.simulateParallel(trafficLayer, populationDensityLayer, numCPUs);
        }
    }

    public void findGridMinimum(Grid grid, int layerIndex) {
        grid.minAllMixedValue = Double.POSITIVE_INFINITY;
        for (int i = 0; i < grid.myNodes.length; i++) {
            if (grid.myNodes[i] != null) {
                if (grid.myNodes[i].myWays[0].type != null) {
                    if (grid.myNodes[i].myWays[0].type.equals("motorway")) {
                        continue;
                    }
                    double tempValue = MFParent.getLayerValue(grid.myNodes[i], layerIndex);
                    if (grid.myNodes[i].lava_value_indexed[1] > tempValue) {
                        tempValue = grid.myNodes[i].lava_value_indexed[1];
                    }
                    if (tempValue < grid.minAllMixedValue && tempValue > 0) {
                        grid.minAllMixedValue = tempValue;
                        grid.minAllMixedNode = grid.myNodes[i];
                    }
                }
            }
        }
        grid.isMinAllMixedDetermined = true;
    }

    public Object[] chunkedDetectMinimumAllMixed(int allMixedLayer) {
        Object output[] = new Object[2];
        for (int i = 0; i < MFParent.allData.grid.length; i++) {
            for (int j = 0; j < MFParent.allData.grid[i].length; j++) {
//                System.out.println(MFParent.allData.grid[i][j].myNodes.length);
                if (MFParent.allData.grid[i][j].isMinAllMixedDetermined == false) {
                    findGridMinimum(MFParent.allData.grid[i][j], allMixedLayer);
                } else {
                    if (MFParent.allData.grid[i][j].minAllMixedNode != null)//if the grid has nodes
                    {
//                        System.out.println(MFParent.allData.grid[i][j].minAllMixedNode.lava_value_indexed[1]);
//                        System.out.println(MFParent.allData.grid[i][j].minAllMixedValue);
                        if (MFParent.allData.grid[i][j].minAllMixedNode.lava_value_indexed[1] > MFParent.allData.grid[i][j].minAllMixedValue) {
                            findGridMinimum(MFParent.allData.grid[i][j], allMixedLayer);
//                            System.out.println("Chunk refresh!");
                        }
                    }
                }
            }
        }
        double minAllMixed = Double.POSITIVE_INFINITY;
        LocationNode minNode = null;
        for (int i = 0; i < MFParent.allData.grid.length; i++) {
            for (int j = 0; j < MFParent.allData.grid[i].length; j++) {
                if (MFParent.allData.grid[i][j].minAllMixedValue < minAllMixed) {
                    minAllMixed = MFParent.allData.grid[i][j].minAllMixedValue;
                    minNode = MFParent.allData.grid[i][j].minAllMixedNode;
                }
            }
        }
        output[0] = minNode;
        output[1] = minAllMixed;
        return output;
    }

    public Object[] bulkDetectMinimumAllMixed(int allMixedLayer) {
        Object output[] = new Object[2];
        LocationNode minNode = null;
        double minAllMixed = Double.POSITIVE_INFINITY;
        for (int i = 0; i < MFParent.allData.all_Nodes.length; i++) {
            if (MFParent.allData.all_Nodes[i].myWays[0].type != null) {
                if (MFParent.allData.all_Nodes[i].myWays[0].type.equals("motorway")) {
                    continue;
                }
                double tempValue = Double.POSITIVE_INFINITY;
                if (((LayerDefinition) MFParent.allData.all_Layers.get(allMixedLayer)) instanceof NumericLayer) {
                    tempValue = (double) (MFParent.allData.all_Nodes[i].layers.get(allMixedLayer));
                } else {
                    tempValue = ((LayerDefinition) MFParent.allData.all_Layers.get(allMixedLayer)).values[((short[]) (MFParent.allData.all_Nodes[i].layers.get(allMixedLayer)))[0]];
                }
                for (int j = 1; j < MFParent.allData.all_Nodes[i].lava_value_indexed.length; j++) {
                    if (MFParent.allData.all_Nodes[i].lava_value_indexed[j] > tempValue) {
                        tempValue = MFParent.allData.all_Nodes[i].lava_value_indexed[j];
                    }
                }
                if (tempValue == Double.POSITIVE_INFINITY) {
                    System.out.println("Sever error! Can't get layer value of a Node!");
                }
                if (tempValue < minAllMixed && tempValue > 0) {
                    minAllMixed = tempValue;
                    minNode = MFParent.allData.all_Nodes[i];
                }
            } else {
//                System.out.println("Severe error! a Way has no type!");//UNKNOWN BUG
//                System.out.println("Severe error! a Node has no way attached to!");//OSM BUG
            }
        }
        output[0] = minNode;
        output[1] = minAllMixed;
        return output;
    }

    public void calculateColors() {
        for (int i = 0; i < heuristicFreeLocations.size(); i++) {
            heuristicFreeLocations.get(i).color = new Color(Color.HSBtoRGB((float) i / (float) heuristicFreeLocations.size() - 1, 1, 1));
        }
    }
}

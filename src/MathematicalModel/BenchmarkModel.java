/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MathematicalModel;

import Clustering.Zones;
import GIS3D.LayerDefinition;
import GIS3D.LocationNode;
import GIS3D.NumericLayer;
import GUI.main_frame;
import Simulation.FacilityLocation;
import Simulation.ZoneRouting;
import com.gams.api.GAMSDatabase;
import com.gams.api.GAMSJob;
import com.gams.api.GAMSVariableRecord;
import com.gams.api.GAMSWorkspace;
import com.gams.api.GAMSWorkspaceInfo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;

/**
 *
 * @author Amir72c
 */
public class BenchmarkModel {

    private File workingDirectory;
    main_frame myParent;
    public JTextArea myConsole = new JTextArea();
//    Routing routings[];

    public BenchmarkModel(main_frame parent) {
        myParent = parent;
    }

    public MathematicalSolution makeModel(Zones myZones, int numCPUs) {
        workingDirectory = new File(System.getProperty("user.dir"), "Warehouse");
        File[] files = workingDirectory.listFiles();
        File modelFile = null;
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().contains("model")) {
                modelFile = files[i];
                break;
            }
        }
        if (modelFile == null) {
            System.out.println("Model not found!");
        } else {
            if (myZones == null) {
                workingDirectory.mkdir();

                // create a workspace
                GAMSWorkspaceInfo wsInfo = new GAMSWorkspaceInfo();

                wsInfo.setWorkingDirectory(workingDirectory.getAbsolutePath());

                GAMSWorkspace ws = new GAMSWorkspace(wsInfo);
//                GAMSDatabase a = ws.addDatabase();
                GAMSJob job = ws.addJobFromFile("model.gms");
                job.run();   // job.run(opt);

                System.out.println("Objective value: " + job.OutDB().getVariable("z").getFirstRecord().getLevel());

                MathematicalSolution output = generateSolution(job.OutDB());

                myConsole.append(output.generateStringReport());

                System.out.println(output.generateStringReport());

                return output;

            } else {
                workingDirectory.mkdir();

                // create a workspace
                GAMSWorkspaceInfo wsInfo = new GAMSWorkspaceInfo();

                wsInfo.setWorkingDirectory(workingDirectory.getAbsolutePath());

                GAMSWorkspace ws = new GAMSWorkspace(wsInfo);
                String generatedModel = new String();
                //SET
                int i = myZones.locations.length;
                generatedModel = generatedModel + "options threads=" + numCPUs + ";" + "\n";
                generatedModel = generatedModel + "set" + "\n";
                generatedModel = generatedModel + "i /1*" + i + "/" + "\n";
                generatedModel = generatedModel + "m /1*12/" + "\n";
                generatedModel = generatedModel + "j /small,medium,large/" + "\n";
                generatedModel = generatedModel + "k /main,lux,student/;" + "\n";
                //SET
                //PAR
                generatedModel = generatedModel + "parameter" + "\n";
                generatedModel = generatedModel + "LP(i)" + "\n";
                generatedModel = generatedModel + "/";
                for (int iter = 1; iter <= i; iter++) {
                    generatedModel = generatedModel + iter + " " + myZones.landPrice[iter - 1];
                    if (iter < i) {
                        generatedModel = generatedModel + ",";
                    }
                }
                generatedModel = generatedModel + "/" + "\n";

                generatedModel = generatedModel + "LE(i)" + "\n";
                generatedModel = generatedModel + "/";
                for (int iter = 1; iter <= i; iter++) {
                    generatedModel = generatedModel + iter + " " + myZones.landExistance[iter - 1];
                    if (iter < i) {
                        generatedModel = generatedModel + ",";
                    }
                }
                generatedModel = generatedModel + "/" + "\n";

                generatedModel = generatedModel + "BD(i,k,j)" + "\n";
                generatedModel = generatedModel + "/";
                for (int iteri = 1; iteri <= i; iteri++) {
                    for (int iterk = 1; iterk <= 3; iterk++) {
                        for (int iterj = 1; iterj <= 3; iterj++) {
                            if (iterk == 1) {
                                generatedModel = generatedModel + iteri + ".main";
                            } else if (iterk == 2) {
                                generatedModel = generatedModel + iteri + ".lux";
                            } else if (iterk == 3) {
                                generatedModel = generatedModel + iteri + ".student";
                            }
                            if (iterj == 1) {
                                generatedModel = generatedModel + ".small" + " " + myZones.baseDemand[iteri - 1][iterk - 1][iterj - 1];
                            } else if (iterj == 2) {
                                generatedModel = generatedModel + ".medium" + " " + myZones.baseDemand[iteri - 1][iterk - 1][iterj - 1];
                            } else if (iterj == 3) {
                                generatedModel = generatedModel + ".large" + " " + myZones.baseDemand[iteri - 1][iterk - 1][iterj - 1];
                            }
                            if (iteri < i || iterk < 3 || iterj < 3) {
                                generatedModel = generatedModel + ",";
                            }
                        }

                    }
                }
                generatedModel = generatedModel + "/" + "\n";

                generatedModel = generatedModel + "TC(i)" + "\n";
                generatedModel = generatedModel + "/";
                for (int iter = 1; iter <= i; iter++) {
//                    System.out.println(myZones.locations.length);
//                    System.out.println(iter);
//                    System.out.println(i);
//                    System.out.println(myZones.locations[iter - 1]);
                    generatedModel = generatedModel + iter + " " + myZones.locations[iter - 1].transportationCost;
                    if (iter < i) {
                        generatedModel = generatedModel + ",";
                    }
                }
                generatedModel = generatedModel + "/" + "\n";

                generatedModel = generatedModel + "TOUG(i,j)" + "\n";
                generatedModel = generatedModel + "/";
                for (int iteri = 1; iteri <= i; iteri++) {
                    for (int iterj = 1; iterj <= 3; iterj++) {
                        if (iterj == 1) {
                            generatedModel = generatedModel + iteri + ".small" + " " + myZones.tourismGain[iteri - 1][iterj - 1];
                        } else if (iterj == 2) {
                            generatedModel = generatedModel + iteri + ".medium" + " " + myZones.tourismGain[iteri - 1][iterj - 1];
                        } else if (iterj == 3) {
                            generatedModel = generatedModel + iteri + ".large" + " " + myZones.tourismGain[iteri - 1][iterj - 1];
                        }
                        if (iteri < i || iterj < 3) {
                            generatedModel = generatedModel + ",";
                        }
                    }
                }
                generatedModel = generatedModel + "/";

                generatedModel = generatedModel + ";" + "\n";
                //PAR
                try {
                    BufferedReader modelBuffer;
                    modelBuffer = new BufferedReader(new InputStreamReader(new FileInputStream(modelFile)));
                    String sCurrentLine;
                    boolean isStaticModelStarted = false;
                    while ((sCurrentLine = modelBuffer.readLine()) != null) {
                        if (isStaticModelStarted == true) {
                            generatedModel = generatedModel + sCurrentLine + "\n";
                        }
                        if (sCurrentLine.contains("*Static*")) {
                            isStaticModelStarted = true;
                        }
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(BenchmarkModel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(BenchmarkModel.class.getName()).log(Level.SEVERE, null, ex);
                }
//                System.out.println(generatedModel);

                GAMSJob job = ws.addJobFromString(generatedModel);
                job.run();

                System.out.println("Objective value: " + job.OutDB().getVariable("z").getFirstRecord().getLevel());

                MathematicalSolution output = generateSolution(job.OutDB());

                myConsole.append(output.generateStringReport());

                return output;
            }
        }
        return null;
    }

    public void clean_GAMS_dir() {
        File[] gdx_files = workingDirectory.listFiles();
        System.gc();
        for (int i = 0; i < gdx_files.length; i++) {
            //System.out.println(gdx_files[i].getName());
            if (gdx_files[i].getName().contains(".gdx")) {
                //System.out.println("deleting!");
                gdx_files[i].delete();
            }
        }
    }

    public Zones[] runExternalZones(Zones zones[], int numCPUs) {
        int competitorsLayerIndex = myParent.findLayer("competitors");
        myParent.flowControl.resetLayerIndex = competitorsLayerIndex;
        int trafficLayerIndex = myParent.findLayer("traffic");
        //REMOVE AFTER INDEX IS DETERMINED
        LocationNode supplier = null;
        for (int k = 0; k < myParent.allData.all_Nodes.length; k++) {
            if (myParent.allData.all_Nodes[k].id==430873946) {
                System.out.println("Supplier location: Node number " + k + " ID: " + "430873946");
                supplier = myParent.allData.all_Nodes[k];
                break;
            }
        }
        //REMOVE AFTER INDEX IS DETERMINED
        for (int i = 0; i < zones.length; i++) {
//            System.out.println(zones[i]);
//            System.out.println(zones[i].locations);
            zones[i].baseDemand = new float[zones[i].locations.length][3][3];
            zones[i].landExistance = new float[zones[i].locations.length];
            zones[i].landPrice = new float[zones[i].locations.length];
            zones[i].tourismGain = new float[zones[i].locations.length][3];
            zones[i].transportationCost = new float[zones[i].locations.length];
            for (int j = 0; j < zones[i].locations.length; j++) {
                zones[i].locations[j].zoneParent = zones[i];
                zones[i].locations[j].isDecoyable = true;
                zones[i].locations[j].landPrice = new Float(myParent.getLayerValue(zones[i].locations[j].nodeLocation, zones[0].landPriceLayerIndex));
                zones[i].locations[j].landExistance = new Float(myParent.getLayerValue(zones[i].locations[j].nodeLocation, zones[0].landExistanceLayerIndex));
            }
        }

        //2
        for (int i = 0; i < zones.length; i++) {
            for (int j = 0; j < zones[i].locations.length; j++) {
                zones[i].locations[j].capacity = 2;
            }
        }

        myParent.flowControl.simulateMultiLayerCompetingZoneBased(zones, trafficLayerIndex, numCPUs, competitorsLayerIndex, false);

        ZoneRouting ZR[] = new ZoneRouting[zones.length];
        
        for (int i = 0; i < zones.length; i++) {
            ZR[i]=new ZoneRouting(myParent);
            ZR[i].simulateZoneRouting(zones[i].locations, supplier, numCPUs, trafficLayerIndex);
            for (int j = 0; j < zones[i].locations.length; j++) {
                zones[i].baseDemand[j][0] = zones[i].locations[j].baseDemand;
                zones[i].landExistance[j] = zones[i].locations[j].landExistance;
                zones[i].landPrice[j] = zones[i].locations[j].landPrice;
                zones[i].tourismGain[j][0] = zones[i].locations[j].tourismGain;
                zones[i].transportationCost[j] = new Float(ZR[i].routings[j].pathDistance);
            }
        }
        //2

        //2.5
        for (int i = 0; i < zones.length; i++) {
            for (int j = 0; j < zones[i].locations.length; j++) {
                zones[i].locations[j].capacity = 2.5;
            }
        }

        myParent.flowControl.simulateMultiLayerCompetingZoneBased(zones, trafficLayerIndex, numCPUs, competitorsLayerIndex, false);

        for (int i = 0; i < zones.length; i++) {
//            ZoneRouting ZR = new ZoneRouting(myParent);
//            ZR.simulateZoneRouting(zones[i].locations, supplier, numCPUs, trafficLayerIndex);
            for (int j = 0; j < zones[i].locations.length; j++) {
                zones[i].baseDemand[j][1] = zones[i].locations[j].baseDemand;
                zones[i].landExistance[j] = zones[i].locations[j].landExistance;
                zones[i].landPrice[j] = zones[i].locations[j].landPrice;
                zones[i].tourismGain[j][1] = zones[i].locations[j].tourismGain;
                zones[i].transportationCost[j] = new Float(ZR[i].routings[j].pathDistance);
            }
        }
        //2.5

        //3
        for (int i = 0; i < zones.length; i++) {
            for (int j = 0; j < zones[i].locations.length; j++) {
                zones[i].locations[j].capacity = 3;
            }
        }

        myParent.flowControl.simulateMultiLayerCompetingZoneBased(zones, trafficLayerIndex, numCPUs, competitorsLayerIndex, false);

        for (int i = 0; i < zones.length; i++) {
//            ZoneRouting ZR = new ZoneRouting(myParent);
//            ZR.simulateZoneRouting(zones[i].locations, supplier, numCPUs, trafficLayerIndex);
            for (int j = 0; j < zones[i].locations.length; j++) {
                zones[i].baseDemand[j][2] = zones[i].locations[j].baseDemand;
                zones[i].landExistance[j] = zones[i].locations[j].landExistance;
                zones[i].landPrice[j] = zones[i].locations[j].landPrice;
                zones[i].tourismGain[j][2] = zones[i].locations[j].tourismGain;
                zones[i].transportationCost[j] = new Float(ZR[i].routings[j].pathDistance);
            }
        }
        //3


        for (int i = 0; i < zones.length; i++) {
            zones[i].mySolution = makeModel(zones[i], numCPUs);
        }

        return zones;
    }

    public Zones runExternalFacilities(FacilityLocation facilityLocations[], int numCPUs) {
        int landPriceLayerIndex = myParent.findLayer("landprice");
        int landExistanceLayerIndex = myParent.findLayer("freespace");
        int baseDemandLayerIndex = myParent.findLayer("base");
        int tourismGainLayerIndex = myParent.findLayer("tourismlayer");
        int populationDensityLayerIndex = myParent.findLayer("population_density");
        int studentLayerIndex = myParent.findLayer("studentLayer");
        int luxuryLayerIndex = myParent.findLayer("luxuryLayer");
        Zones generatedZone = new Zones(landPriceLayerIndex, landExistanceLayerIndex, baseDemandLayerIndex, tourismGainLayerIndex, populationDensityLayerIndex, studentLayerIndex, luxuryLayerIndex);
        int allMixedLayerIndex = myParent.findLayer("allmixed");
        int competitorsLayerIndex = myParent.findLayer("competitors");
        myParent.flowControl.resetLayerIndex = competitorsLayerIndex;
        int trafficLayerIndex = myParent.findLayer("traffic");
//        for (int i = 0; i < myParent.allData.all_Nodes.length; i++) {
//            myParent.allData.all_Nodes[i].lava_value_indexed = new double[numCPUs];
//        }
        myParent.allData.setParallelLayers(numCPUs, competitorsLayerIndex);
        //REMOVE AFTER INDEX IS DETERMINED
        LocationNode supplier = null;
        for (int k = 0; k < myParent.allData.all_Nodes.length; k++) {
            if (myParent.allData.all_Nodes[k].id==430873946) {
                System.out.println("Supplier location: Node number " + k + " ID: " + "430873946");
                supplier = myParent.allData.all_Nodes[k];
                break;
            }
        }
        //REMOVE AFTER INDEX IS DETERMINED

        int numFacilities = facilityLocations.length;
//        System.out.println(numFacilities);
        generatedZone.locations = new FacilityLocation[numFacilities];
        generatedZone.baseDemand = new float[numFacilities][3][3];
        generatedZone.landExistance = new float[numFacilities];
        generatedZone.landPrice = new float[numFacilities];
        generatedZone.tourismGain = new float[numFacilities][3];
        generatedZone.transportationCost = new float[numFacilities];

        //2
        myParent.allData.setParallelLayers(numCPUs, competitorsLayerIndex);

        for (int i = 0; i < numFacilities; i++) {
            generatedZone.locations[i] = new FacilityLocation(myParent, facilityLocations[i].nodeLocation, facilityLocations[i].nodeLocation.myWays[0], 2);
            generatedZone.locations[i].zoneParent = generatedZone;
            generatedZone.locations[i].isDecoyable = true;
            generatedZone.locations[i].landPrice = new Float(myParent.getLayerValue(generatedZone.locations[i].nodeLocation, landPriceLayerIndex));
            generatedZone.locations[i].landExistance = new Float(myParent.getLayerValue(generatedZone.locations[i].nodeLocation, landExistanceLayerIndex));
        }

//        myParent.flowControl.resetParallel(0);

        myParent.flowControl.simulateOneLayerCompetingFacilityBased(generatedZone.locations, trafficLayerIndex, numCPUs, competitorsLayerIndex, false);

        for (int i = 0; i < numFacilities; i++) {
            generatedZone.baseDemand[i][0] = generatedZone.locations[i].baseDemand;
            generatedZone.landExistance[i] = generatedZone.locations[i].landExistance;
            generatedZone.landPrice[i] = generatedZone.locations[i].landPrice;
            generatedZone.tourismGain[i][0] = generatedZone.locations[i].tourismGain;
//            generatedZone.transportationCost[i] = generatedZone.locations[i].transportationCost;
        }
        //2

        //2.5
        myParent.allData.setParallelLayers(numCPUs, competitorsLayerIndex);

        for (int i = 0; i < numFacilities; i++) {
            generatedZone.locations[i] = new FacilityLocation(myParent, facilityLocations[i].nodeLocation, facilityLocations[i].nodeLocation.myWays[0], 2.5);
            generatedZone.locations[i].zoneParent = generatedZone;
            generatedZone.locations[i].isDecoyable = true;
            generatedZone.locations[i].landPrice = new Float(myParent.getLayerValue(generatedZone.locations[i].nodeLocation, landPriceLayerIndex));
            generatedZone.locations[i].landExistance = new Float(myParent.getLayerValue(generatedZone.locations[i].nodeLocation, landExistanceLayerIndex));
        }

//        myParent.flowControl.resetParallel(0);

        myParent.flowControl.simulateOneLayerCompetingFacilityBased(generatedZone.locations, trafficLayerIndex, numCPUs, competitorsLayerIndex, false);

        for (int i = 0; i < numFacilities; i++) {
            generatedZone.baseDemand[i][1] = generatedZone.locations[i].baseDemand;
            generatedZone.landExistance[i] = generatedZone.locations[i].landExistance;
            generatedZone.landPrice[i] = generatedZone.locations[i].landPrice;
            generatedZone.tourismGain[i][1] = generatedZone.locations[i].tourismGain;
//            generatedZone.transportationCost[i] = generatedZone.locations[i].transportationCost;
        }
        //2.5

        //3
        myParent.allData.setParallelLayers(numCPUs, competitorsLayerIndex);

        for (int i = 0; i < numFacilities; i++) {
            generatedZone.locations[i] = new FacilityLocation(myParent, facilityLocations[i].nodeLocation, facilityLocations[i].nodeLocation.myWays[0], 3);
            generatedZone.locations[i].zoneParent = generatedZone;
            generatedZone.locations[i].isDecoyable = true;
            generatedZone.locations[i].landPrice = new Float(myParent.getLayerValue(generatedZone.locations[i].nodeLocation, landPriceLayerIndex));
            generatedZone.locations[i].landExistance = new Float(myParent.getLayerValue(generatedZone.locations[i].nodeLocation, landExistanceLayerIndex));
        }

//        myParent.flowControl.resetParallel(0);

        myParent.flowControl.simulateOneLayerCompetingFacilityBased(generatedZone.locations, trafficLayerIndex, numCPUs, competitorsLayerIndex, false);

        for (int i = 0; i < numFacilities; i++) {
            generatedZone.baseDemand[i][2] = generatedZone.locations[i].baseDemand;
            generatedZone.landExistance[i] = generatedZone.locations[i].landExistance;
            generatedZone.landPrice[i] = generatedZone.locations[i].landPrice;
            generatedZone.tourismGain[i][2] = generatedZone.locations[i].tourismGain;
//            generatedZone.transportationCost[i] = generatedZone.locations[i].transportationCost;
        }
        //3


        myParent.allData.setParallelLayers(numCPUs, competitorsLayerIndex);

        ZoneRouting ZR = new ZoneRouting(myParent);
        ZR.simulateZoneRouting(facilityLocations, supplier, numCPUs, trafficLayerIndex);

        for (int i = 0; i < numFacilities; i++) {
            generatedZone.transportationCost[i] = new Float(ZR.routings[i].pathDistance);
        }
        generatedZone.mySolution = makeModel(generatedZone, numCPUs);
        return generatedZone;
    }

    public Zones runBasedOnlayerIndex(int selection, int numCPUs) {
        int landPriceLayerIndex = myParent.findLayer("landprice");
        int landExistanceLayerIndex = myParent.findLayer("freespace");
        int baseDemandLayerIndex = myParent.findLayer("base");
        int tourismGainLayerIndex = myParent.findLayer("tourismlayer");
        int populationDensityLayerIndex = myParent.findLayer("population_density");
        int studentLayerIndex = myParent.findLayer("studentLayer");
        int luxuryLayerIndex = myParent.findLayer("luxuryLayer");
        Zones generatedZone = new Zones(landPriceLayerIndex, landExistanceLayerIndex, baseDemandLayerIndex, tourismGainLayerIndex, populationDensityLayerIndex, studentLayerIndex, luxuryLayerIndex);
        if ((myParent.allData.all_Layers.get(selection) instanceof NumericLayer)) {
            System.out.println("Layer named " + ((LayerDefinition) (myParent.allData.all_Layers.get(selection))).layerName + " is not categorial layer, skipping layer!");
            return null;
        }

        int allMixedLayerIndex = myParent.findLayer("allmixed");
        int competitorsLayerIndex = myParent.findLayer("competitors");
        myParent.flowControl.resetLayerIndex = competitorsLayerIndex;
        int trafficLayerIndex = myParent.findLayer("traffic");

        myParent.allData.setParallelLayers(1, competitorsLayerIndex);
        //REMOVE AFTER INDEX IS DETERMINED
        LocationNode supplier = null;
        for (int k = 0; k < myParent.allData.all_Nodes.length; k++) {
//            System.out.println(myParent.allData.all_Nodes[k].id);
            if (myParent.allData.all_Nodes[k].id==430873946) {
                System.out.println("Supplier location: Node number " + k + " ID: " + "430873946");
                supplier = myParent.allData.all_Nodes[k];
                break;
            }
        }
        //REMOVE AFTER INDEX IS DETERMINED
        int numFacilities = ((LayerDefinition) myParent.allData.all_Layers.get(selection)).categories.length;
        double[] minValuesValues = new double[numFacilities];
        int[] minValuesIndexs = new int[numFacilities];
        for (int j = 0; j < numFacilities; j++) {
            minValuesValues[j] = Double.POSITIVE_INFINITY;
        }
        for (int j = 0; j < myParent.allData.all_Nodes.length; j++) {
            if (!(((LayerDefinition) myParent.allData.all_Layers.get(baseDemandLayerIndex)).categories[((short[]) myParent.allData.all_Nodes[j].layers.get(baseDemandLayerIndex))[0] - 1].toLowerCase().contains("unknown") || ((LayerDefinition) myParent.allData.all_Layers.get(baseDemandLayerIndex)).categories[((short[]) myParent.allData.all_Nodes[j].layers.get(baseDemandLayerIndex))[0] - 1].toLowerCase().contains("foot"))) {
//                    System.out.println();
                if (((double) myParent.allData.all_Nodes[j].layers.get(allMixedLayerIndex)) > 0) {
                    int category = ((short[]) myParent.allData.all_Nodes[j].layers.get(selection))[0];
                    if (myParent.getLayerValue(myParent.allData.all_Nodes[j], allMixedLayerIndex) < minValuesValues[category - 1]) {
                        minValuesValues[category - 1] = myParent.getLayerValue(myParent.allData.all_Nodes[j], allMixedLayerIndex);
                        minValuesIndexs[category - 1] = j;
                    }
                }
            }
        }
        generatedZone.locations = new FacilityLocation[numFacilities];
        generatedZone.baseDemand = new float[numFacilities][3][3];
        generatedZone.landExistance = new float[numFacilities];
        generatedZone.landPrice = new float[numFacilities];
        generatedZone.tourismGain = new float[numFacilities][3];
        generatedZone.transportationCost = new float[numFacilities];

        //2
        for (int j = 0; j < numFacilities; j++) {
//            System.out.println(minValuesIndexs[j]);
            myParent.allData.setParallelLayers(numCPUs, competitorsLayerIndex);
            generatedZone.locations[j] = new FacilityLocation(myParent, myParent.allData.all_Nodes[minValuesIndexs[j]], myParent.allData.all_Nodes[minValuesIndexs[j]].myWays[0], 2);
            generatedZone.locations[j].zoneParent = generatedZone;
            generatedZone.locations[j].isDecoyable = true;
            generatedZone.locations[j].landPrice = new Float(myParent.getLayerValue(generatedZone.locations[j].nodeLocation, landPriceLayerIndex));
            generatedZone.locations[j].landExistance = new Float(myParent.getLayerValue(generatedZone.locations[j].nodeLocation, landExistanceLayerIndex));
//            myParent.flowControl.resetParallel(0);
        }
        myParent.flowControl.simulateOneLayerCompetingFacilityBased(generatedZone.locations, trafficLayerIndex, numCPUs, competitorsLayerIndex, false);
        for (int j = 0; j < numFacilities; j++) {
            generatedZone.baseDemand[j][0] = generatedZone.locations[j].baseDemand;
            generatedZone.landExistance[j] = generatedZone.locations[j].landExistance;
            generatedZone.landPrice[j] = generatedZone.locations[j].landPrice;
            generatedZone.tourismGain[j][0] = generatedZone.locations[j].tourismGain;
//            generatedZone.transportationCost[j] = generatedZone.locations[j].transportationCost;
        }
        //2

        //2.5
        for (int j = 0; j < numFacilities; j++) {
//            System.out.println(minValuesIndexs[j]);
            myParent.allData.setParallelLayers(numCPUs, competitorsLayerIndex);
            generatedZone.locations[j] = new FacilityLocation(myParent, myParent.allData.all_Nodes[minValuesIndexs[j]], myParent.allData.all_Nodes[minValuesIndexs[j]].myWays[0], 2.5);
            generatedZone.locations[j].zoneParent = generatedZone;
            generatedZone.locations[j].isDecoyable = true;
            generatedZone.locations[j].landPrice = new Float(myParent.getLayerValue(generatedZone.locations[j].nodeLocation, landPriceLayerIndex));
            generatedZone.locations[j].landExistance = new Float(myParent.getLayerValue(generatedZone.locations[j].nodeLocation, landExistanceLayerIndex));
//            myParent.flowControl.resetParallel(0);
        }
        myParent.flowControl.simulateOneLayerCompetingFacilityBased(generatedZone.locations, trafficLayerIndex, numCPUs, competitorsLayerIndex, false);
        for (int j = 0; j < numFacilities; j++) {
            generatedZone.baseDemand[j][1] = generatedZone.locations[j].baseDemand;
            generatedZone.landExistance[j] = generatedZone.locations[j].landExistance;
            generatedZone.landPrice[j] = generatedZone.locations[j].landPrice;
            generatedZone.tourismGain[j][1] = generatedZone.locations[j].tourismGain;
//            generatedZone.transportationCost[j] = generatedZone.locations[j].transportationCost;
        }
        //2.5

        //3
        for (int j = 0; j < numFacilities; j++) {
//            System.out.println(minValuesIndexs[j]);
            myParent.allData.setParallelLayers(numCPUs, competitorsLayerIndex);
            generatedZone.locations[j] = new FacilityLocation(myParent, myParent.allData.all_Nodes[minValuesIndexs[j]], myParent.allData.all_Nodes[minValuesIndexs[j]].myWays[0], 3);
            generatedZone.locations[j].zoneParent = generatedZone;
            generatedZone.locations[j].isDecoyable = true;
            generatedZone.locations[j].landPrice = new Float(myParent.getLayerValue(generatedZone.locations[j].nodeLocation, landPriceLayerIndex));
            generatedZone.locations[j].landExistance = new Float(myParent.getLayerValue(generatedZone.locations[j].nodeLocation, landExistanceLayerIndex));
//            myParent.flowControl.resetParallel(0);
        }
        myParent.flowControl.simulateOneLayerCompetingFacilityBased(generatedZone.locations, trafficLayerIndex, numCPUs, competitorsLayerIndex, false);
        for (int j = 0; j < numFacilities; j++) {
            generatedZone.baseDemand[j][2] = generatedZone.locations[j].baseDemand;
            generatedZone.landExistance[j] = generatedZone.locations[j].landExistance;
            generatedZone.landPrice[j] = generatedZone.locations[j].landPrice;
            generatedZone.tourismGain[j][2] = generatedZone.locations[j].tourismGain;
//            generatedZone.transportationCost[j] = generatedZone.locations[j].transportationCost;
        }
        //3

        myParent.allData.setParallelLayers(numCPUs, competitorsLayerIndex);

        ZoneRouting ZR = new ZoneRouting(myParent);
        ZR.simulateZoneRouting(generatedZone.locations, supplier, numCPUs, trafficLayerIndex);

        for (int i = 0; i < numFacilities; i++) {
            generatedZone.locations[i].transportationCost = new Float(ZR.routings[i].pathDistance);
        }
        generatedZone.mySolution = makeModel(generatedZone, numCPUs);
        return generatedZone;
    }

    private MathematicalSolution generateSolution(GAMSDatabase GDB) {
        int i = GDB.getSet("i").getNumberOfRecords();
        int m = GDB.getSet("m").getNumberOfRecords();
        int j = GDB.getSet("j").getNumberOfRecords();
        int k = GDB.getSet("k").getNumberOfRecords();
        MathematicalSolution output = new MathematicalSolution(i, m, j, k);

        int setI = 0;
        int setM = 0;
        int setJ = 0;
        int setK = 0;

        int numRecords = GDB.getVariable("CH").getNumberOfRecords();
        Iterator<GAMSVariableRecord> iterator = GDB.getVariable("CH").iterator();
        for (int iter = 0; iter < numRecords; iter++) {
            if (iterator.hasNext()) {
                output.CH[iter] = iterator.next().getLevel();
            }
        }

        numRecords = GDB.getVariable("IO").getNumberOfRecords();
        iterator = GDB.getVariable("IO").iterator();
        setI = 0;
        setM = 0;
        setJ = 0;
        setK = 0;
        for (int iter = 0; iter < numRecords; iter++) {
            if (iterator.hasNext()) {
                output.IO[setI][setK][setM] = iterator.next().getLevel();
            }
            setM = setM + 1;
            if (setM > m - 1) {
                setK = setK + 1;
                setM = 0;
            }
            if (setK > k - 1) {
                setI = setI + 1;
                setK = 0;
            }
        }

        numRecords = GDB.getVariable("CAP").getNumberOfRecords();
        iterator = GDB.getVariable("CAP").iterator();
        setI = 0;
        setM = 0;
        setJ = 0;
        setK = 0;
        for (int iter = 0; iter < numRecords; iter++) {
            if (iterator.hasNext()) {
                output.CAP[setI][setJ] = iterator.next().getLevel();
            }
            setJ = setJ + 1;
            if (setJ > j - 1) {
                setI = setI + 1;
                setJ = 0;
            }
        }

        numRecords = GDB.getVariable("TOU").getNumberOfRecords();
        iterator = GDB.getVariable("TOU").iterator();
        setI = 0;
        setM = 0;
        setJ = 0;
        setK = 0;
        for (int iter = 0; iter < numRecords; iter++) {
            if (iterator.hasNext()) {
                output.TOU[setI][setJ] = iterator.next().getLevel();
            }
            setJ = setJ + 1;
            if (setJ > j - 1) {
                setI = setI + 1;
                setJ = 0;
            }
        }

        numRecords = GDB.getVariable("Q").getNumberOfRecords();
        iterator = GDB.getVariable("Q").iterator();
        setI = 0;
        setM = 0;
        setJ = 0;
        setK = 0;
        for (int iter = 0; iter < numRecords; iter++) {
            if (iterator.hasNext()) {
                output.Q[setI][setK][setM] = iterator.next().getLevel();
            }
            setM = setM + 1;
            if (setM > m - 1) {
                setK = setK + 1;
                setM = 0;
            }
            if (setK > k - 1) {
                setI = setI + 1;
                setK = 0;
            }
        }

        numRecords = GDB.getVariable("SA").getNumberOfRecords();
        iterator = GDB.getVariable("SA").iterator();
        setI = 0;
        setM = 0;
        setJ = 0;
        setK = 0;
        for (int iter = 0; iter < numRecords; iter++) {
            if (iterator.hasNext()) {
                output.SA[setI][setK][setM] = iterator.next().getLevel();
            }
            setM = setM + 1;
            if (setM > m - 1) {
                setK = setK + 1;
                setM = 0;
            }
            if (setK > k - 1) {
                setI = setI + 1;
                setK = 0;
            }
        }

        numRecords = GDB.getVariable("IN").getNumberOfRecords();
        iterator = GDB.getVariable("IN").iterator();
        setI = 0;
        setM = 0;
        setJ = 0;
        setK = 0;
        for (int iter = 0; iter < numRecords; iter++) {
            if (iterator.hasNext()) {
                output.IN[setI][setK][setM] = iterator.next().getLevel();
            }
            setM = setM + 1;
            if (setM > m - 1) {
                setK = setK + 1;
                setM = 0;
            }
            if (setK > k - 1) {
                setI = setI + 1;
                setK = 0;
            }
        }

        numRecords = GDB.getVariable("SH").getNumberOfRecords();
        iterator = GDB.getVariable("SH").iterator();
        setI = 0;
        setM = 0;
        setJ = 0;
        setK = 0;
        for (int iter = 0; iter < numRecords; iter++) {
            if (iterator.hasNext()) {
                output.SH[setI][setK][setM] = iterator.next().getLevel();
            }
            setM = setM + 1;
            if (setM > m - 1) {
                setK = setK + 1;
                setM = 0;
            }
            if (setK > k - 1) {
                setI = setI + 1;
                setK = 0;
            }
        }

        output.z = GDB.getVariable("z").getFirstRecord().getLevel();

        clean_GAMS_dir();
        return output;
    }
    static String main_model2 =
            "set\n"
            + "i /1*2/\n"
            + "j /1*2/\n"
            + ";\n"
            + "Free variable z;\n"
            + "positive variable x(i,j);\n"
            + "equation obj,const1,const2;\n"
            + "obj.. z=e=sum((i,j),x(i,j)*a*b);\n"
            + "const1.. sum((i,j),x(i,j))=g=5;  \n"
            + "const2.. sum((i,j),x(i,j)+b+a)=g=3\n"
            + "model mymodel /all/;\n"
            + "Solve mymodel using lp minimizing z;\n"
            + "display z.l,x.l;\n"
            + "file fy /result.txt/;\n"
            + "put fy z.l;";
    static String main_model =
            "set"
            + "i /1*3/"
            + "m /1*12/"
            + "j /small,medium,large/"
            + "k /main,lux,student/"
            + ";"
            + "binary variables"
            + "CH(i)"
            + "IO(m)"
            + "CAP(i,j)"
            + ";"
            + "positive variables"
            + "Q(i,k,m)"
            + "TOU(i)"
            + "SA(i,k,m)"
            + "IN(i,k,m)"
            + "SH(i,k,m)"
            + ";"
            + "parameter"
            + "LP(i)"
            + "/1 100,2 200,3 300/"
            + "LE(i)"
            + "/1 0.5,2 0.6,3 0.8/"
            + "BD(i,k)"
            + "/1.main 100,1.lux 110,1.student 120,2.main 90,2.lux 80,2.student 85,3.main 105,3.lux 120,3.student 95/"
            + "H(k)"
            + "/main 20,lux 15,student 25/"
            + "OC /200/"
            + "SHC(k)"
            + "/main 2,lux 5,student 3/"
            + "MD(k,m)"
            + "/main.1 50,main.2 60,main.3 65,main.4 70,main.5 45,main.6 55,main.7 80,main.8 85,main.9 90,main.10 65,main.11 70,main.12 100"
            + "lux.1 50,lux.2 60,lux.3 65,lux.4 70,lux.5 45,lux.6 55,lux.7 80,lux.8 85,lux.9 90,lux.10 65,lux.11 70,lux.12 100"
            + "student.1 50,student.2 60,student.3 65,student.4 70,student.5 45,student.6 55,student.7 80,student.8 85,student.9 90,student.10 65,student.11 70,student.12 100/"
            + "SI(k)"
            + "/main 20,lux 80,student 30/"
            + "CC(j)"
            + "/small 100,medium 120,large 140/"
            + ";"
            + "free variable z;"
            + "equation obj,TOU_L_CH,CAP_L_CH,CAP_E_1,IN_BALANCE,SELLING,ISORDER,CAPACITY,initIN;"
            + "obj.. z=e=sum(i,(LP(i)+LE(i))*(TOU(i)+sum(j,CAP(i,j)*CC(j))))+sum((i,k,m),SA(i,k,m)*SI(k))-sum(m,IO(m)*OC)-sum((i,k,m),IN(i,k,m)*H(k))-sum((i,k,m),SH(i,k,m)*SHC(k));"
            + "TOU_L_CH(i).. TOU(i)=l=CH(i);"
            + "CAP_L_CH(i).. sum(j,CAP(i,j))=l=CH(i);"
            + "CAP_E_1(i).. sum(j,CAP(i,j))=e=1;"
            + "IN_BALANCE(i,k,m).. -SA(i,k,m)+Q(i,k,m)+IN(i-1,k,m)=e=IN(i,k,m);"
            + "initIN(i,k,m).. IN(i,k,m)=l=10000000$(ord(m) ne 1);"
            + "SELLING(i,k,m).. SA(i,k,m)+SH(i,k,m)=e=(BD(i,k)+MD(k,m))*TOU(i);"
            + "ISORDER(m).. sum((i,k),Q(i,k,m))=l=IO(m)*10000000;"
            + "CAPACITY(i,m).. sum(k,IN(i,k,m))=l=sum(j,CAP(i,j));"
            + "model Project /ALL/;"
            //            + "options threads=6;"
            + "solve Project using mip minimizing z;"
            + "display z.l,CH.l,IN.l,Q.l;";
}

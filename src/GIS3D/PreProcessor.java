/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GIS3D;

import Simulation.CPUUpperLowerBounds;
import GUI.main_frame;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.jme3.math.Vector3f;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 *
 * @author Amir72c
 */
public class PreProcessor {

    main_frame myParent;
    ReportResults localReport;

    public PreProcessor(main_frame parent) {
        myParent = parent;
    }

    public AllData preprocess(String file_path, int h_grid_num, int v_grid_num, int cores) {
        try {
            //REPORTING
            String detailedResults = "Prepcoressing OSM data." + System.lineSeparator();
            detailedResults = detailedResults + "Source file: " + file_path+ System.lineSeparator();
            detailedResults = detailedResults + "Number of horizontal simple grid: " + h_grid_num+ System.lineSeparator();
            detailedResults = detailedResults + "Number of vertical simple grid: " + v_grid_num+ System.lineSeparator();
            Calendar currentDate = Calendar.getInstance();
            Date date = currentDate.getTime();
            double startRAM = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024.0;
            long startTime = System.nanoTime();
            //REPORTING

            AllData allData = new AllData();
            String sCurrentLine;
            File fXmlFile = new File(file_path);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            NodeList all_nodes = doc.getElementsByTagName("node");
            //System.out.println(all_nodes.getLength());
            allData.all_Nodes = new LocationNode[all_nodes.getLength()];
            NodeList all_ways = doc.getElementsByTagName("way");
            allData.all_Ways = new Way[all_ways.getLength()];
            System.out.println("hand made read started");
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file_path)));

            int m = 0;
            int k = 0;
            ArrayList nodes_tempo = new ArrayList();
            int line_count = 0;
            int thousand_count = 0;
            boolean isInsideWay = false;

            while ((sCurrentLine = br.readLine()) != null) {
                //System.out.println(sCurrentLine);
                if (line_count > 10000) {
                    thousand_count = thousand_count + 1;
                    System.out.println(thousand_count);
                    line_count = 0;
                }

                if (sCurrentLine.contains("<node")) {
                    int start = sCurrentLine.indexOf("id=\"");
                    String temp = sCurrentLine.substring(start + 4);
                    int end = temp.indexOf("\"");
                    String id = temp.substring(0, end);
                    //System.out.println(id);
                    start = sCurrentLine.indexOf("lat=\"");
                    temp = sCurrentLine.substring(start + 5);
                    end = temp.indexOf("\"");
                    String lat_str = temp.substring(0, end);
                    double lat = Double.parseDouble(lat_str);
                    //System.out.println(lat);
                    start = sCurrentLine.indexOf("lon=\"");
                    temp = sCurrentLine.substring(start + 5);
                    end = temp.indexOf("\"");
                    String lon_str = temp.substring(0, end);
                    double lon = Double.parseDouble(lon_str);
                    //System.out.println(lon);
                    allData.all_Nodes[m] = new LocationNode(id, lat, lon, m);
                    m = m + 1;
                }

                if (sCurrentLine.contains("<way")) {
                    int start = sCurrentLine.indexOf("id=\"");
                    String temp = sCurrentLine.substring(start + 4);
                    int end = temp.indexOf("\"");
                    String id = temp.substring(0, end);
                    //System.out.println(id);
                    allData.all_Ways[k] = new Way(id);
                    isInsideWay = true;
                }
                if (isInsideWay) {
                    if (sCurrentLine.contains("<tag k=\"highway\"")) {
                        int start = sCurrentLine.indexOf("v=\"");
                        String temp = sCurrentLine.substring(start + 3);
                        int end = temp.indexOf("\"");
                        String type_str = temp.substring(0, end);
                        allData.all_Ways[k].type = type_str;
                        allData.all_Ways[k].setTypeWeight();
                        //System.out.println(type_str);
                    }
                    if (sCurrentLine.contains("<tag k=\"oneway\"")) {
                        int start = sCurrentLine.indexOf("v=\"");
                        String temp = sCurrentLine.substring(start + 3);
                        int end = temp.indexOf("\"");
                        String oneway_str = temp.substring(0, end);
                        boolean oneWay = false;
                        if ("yes".equals(oneway_str)) {
                            oneWay = true;
                        }
                        allData.all_Ways[k].isOneWay = oneWay;
                        //System.out.println(oneWay);
                    }
                    if (sCurrentLine.contains("<nd ref=\"")) {
                        int start = sCurrentLine.indexOf("ref=\"");
                        String temp = sCurrentLine.substring(start + 5);
                        int end = temp.indexOf("\"");
                        String temp_node = temp.substring(0, end);

                        //ArrayList ways_tempo=new ArrayList();
                        for (int i = 0; i < allData.all_Nodes.length; i++) {
                            //System.out.println(all_Nodes[i].id);
                            //System.out.println(all_Ways[k].id);
                            if (allData.all_Nodes[i].id.equals(temp_node)) {
                                nodes_tempo.add(allData.all_Nodes[i]);
                                //System.out.println("found");
                            }
                        }
                        Object[] nodes_tempo_array = nodes_tempo.toArray();
                        LocationNode[] temp_LocationNode = new LocationNode[nodes_tempo_array.length];
                        for (int i = 0; i < nodes_tempo_array.length; i++) {
                            temp_LocationNode[i] = (LocationNode) nodes_tempo_array[i];
                            //System.out.println(temp_LocationNode[i].id);
                        }
                        allData.all_Ways[k].myNodes = temp_LocationNode;
                    }
                }
                if (sCurrentLine.contains("</way>")) {
                    nodes_tempo.clear();
                    isInsideWay = false;
                    k = k + 1;
                }
                line_count = line_count + 1;
            }
            br.close();
            System.out.println("preprocess");

            allData.myScale = new Scaling(allData.all_Nodes);
            allData.myScale.calculate();
            allData.grid = new Grid[h_grid_num][v_grid_num];
            for (int i = 0; i < h_grid_num; i++) {
                for (int j = 0; j < v_grid_num; j++) {
                    allData.grid[i][j] = new Grid(i, j, allData.myScale.min_x + i * ((allData.myScale.max_x - allData.myScale.min_x) / (double) allData.grid.length), allData.myScale.min_x + (i + 1) * ((allData.myScale.max_x - allData.myScale.min_x) / (double) allData.grid.length), allData.myScale.min_y + j * ((allData.myScale.max_y - allData.myScale.min_y) / (double) allData.grid[0].length), allData.myScale.min_y + (j + 1) * ((allData.myScale.max_y - allData.myScale.min_y) / (double) allData.grid[0].length),"Simple equal-width grid");
                }
            }

            int numProcessors = cores;
            if (numProcessors > Runtime.getRuntime().availableProcessors()) {
                numProcessors = Runtime.getRuntime().availableProcessors();
            }
            ParallelPreProcessor parallelFor[] = new ParallelPreProcessor[numProcessors];

            for (int i = 0; i < numProcessors - 1; i++) {
                parallelFor[i] = new ParallelPreProcessor(this, allData, (int) Math.floor(i * ((allData.all_Nodes.length) / numProcessors)), (int) Math.floor((i + 1) * ((allData.all_Nodes.length) / numProcessors)));
            }
            parallelFor[numProcessors - 1] = new ParallelPreProcessor(this, allData, (int) Math.floor((numProcessors - 1) * ((allData.all_Nodes.length) / numProcessors)), allData.all_Nodes.length);

            for (int i = 0; i < numProcessors; i++) {
                //parallelFor[i].myThread.start();
                parallelFor[i].myThread.start();
            }
            for (int i = 0; i < numProcessors; i++) {
                try {
                    parallelFor[i].myThread.join();
                    System.out.println("thread " + i + "finished!");
                } catch (InterruptedException ie) {
                    System.out.println(ie.toString());
                }
            }
            int numRefinedNodes = 0;
            for (int i = 0; i < numProcessors; i++) {
                numRefinedNodes = numRefinedNodes + parallelFor[i].myRefinedDataNumber;
            }

            System.out.println("refine data");
            LocationNode refinedNodes[] = new LocationNode[numRefinedNodes];
            int counter = 0;
            for (int i = 0; i < allData.all_Nodes.length; i++) {
                if (allData.all_Nodes[i].myWays.length > 0) {
                    refinedNodes[counter] = allData.all_Nodes[i];
                    counter = counter + 1;
                }
            }
            allData.all_Nodes = refinedNodes;
            setWaysColorLayerBased(allData, 0);
            System.out.println("make 3d");

            for (int i = 0; i < allData.grid.length; i++) {
                for (int j = 0; j < allData.grid[0].length; j++) {
                    allData.grid[i][j].myNodes = new LocationNode[allData.grid[i][j].temporaryNodes.size()];
                    for (int s = 0; s < allData.grid[i][j].temporaryNodes.size(); s++) {
                        allData.grid[i][j].myNodes[s] = (LocationNode) allData.grid[i][j].temporaryNodes.get(s);
                    }
                }
            }
            
            detailedResults = detailedResults + "Number of nodes: " + allData.all_Nodes.length+ System.lineSeparator();
            detailedResults = detailedResults + "Number of ways: " + allData.all_Ways.length+ System.lineSeparator();
            
            //REPORTING
            long endTime = System.nanoTime();
            double elapsed = ((endTime - startTime) / 1000000000);
            double endRAM = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024.0;
            double usedRAM = endRAM - startRAM;
            localReport = new ReportResults(date, "Preprocess", startTime, endTime, elapsed, cores, startRAM, endRAM, usedRAM, detailedResults);
            allData.results.add(localReport);
            //REPORTING

            //reserve_data();
            System.out.println("finished");
            return allData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int parallelInternalProcess(AllData allData, int i, int numRefinedNodes) {
        //ArrayList output = new ArrayList();

        boolean isAllwaysChecked = false;

        Way ways_tempo[] = new Way[1000];
        int counter = 0;
        for (int j = 0; j < allData.all_Ways.length; j++) {

            if (isAllwaysChecked == false) {
                if ("residential".equals(allData.all_Ways[j].type)) {
                    allData.totalDemand = allData.totalDemand + 1;
                }

                allData.all_Ways[j].lines = new Vector3f[allData.all_Ways[j].myNodes.length];
                for (int l = 0; l < allData.all_Ways[j].myNodes.length; l++) {
                    allData.all_Ways[j].lines[l] = new Vector3f((float) ((allData.all_Ways[j].myNodes[l].lon - allData.myScale.center_y) * allData.myScale.scale), (float) ((allData.all_Ways[j].myNodes[l].lat - allData.myScale.center_x) * allData.myScale.scale), 0f);
                    allData.all_Ways[j].myNodes[l].renderingLocation = allData.all_Ways[j].lines[l];
                }
            }

            for (int l = 0; l < allData.all_Ways[j].myNodes.length; l++) {
                if (allData.all_Ways[j].myNodes[l].id.equals(allData.all_Nodes[i].id)) {
                    ways_tempo[counter] = allData.all_Ways[j];
                    counter = counter + 1;
                }
            }
        }
        isAllwaysChecked = true;

        Way[] temp_Way = new Way[counter];
        System.arraycopy(ways_tempo, 0, temp_Way, 0, counter);

        allData.all_Nodes[i].myWays = temp_Way;

        if (allData.all_Nodes[i].myWays.length > 0) {
            if (allData.all_Nodes[i].myWays[0].type != null) {
                allData.all_Nodes[i].myGrid = detect_grid(allData, allData.all_Nodes[i].lat, allData.all_Nodes[i].lon);
                allData.all_Nodes[i].myGrid.temporaryNodes.add(allData.all_Nodes[i]);
            }
            numRefinedNodes = numRefinedNodes + 1;
        }

        short indexes[] = new short[allData.all_Nodes[i].myWays.length];
        for (int j = 0; j < allData.all_Nodes[i].myWays.length; j++) {
            if (allData.all_Nodes[i].myWays[j].type != null) {
                if (allData.all_Nodes[i].myWays[j].type.contains("motorway")) {
                    indexes[j] = 1;
                } else if (allData.all_Nodes[i].myWays[j].type.contains("trunk")) {
                    indexes[j] = 2;
                } else if (allData.all_Nodes[i].myWays[j].type.contains("primary")) {
                    indexes[j] = 3;
                } else if (allData.all_Nodes[i].myWays[j].type.contains("secondary")) {
                    indexes[j] = 4;
                } else if (allData.all_Nodes[i].myWays[j].type.contains("tertiary")) {
                    indexes[j] = 5;
                } else if (allData.all_Nodes[i].myWays[j].type.contains("unclassified")) {
                    indexes[j] = 6;
                } else if (allData.all_Nodes[i].myWays[j].type.contains("residential")) {
                    indexes[j] = 7;
                } else if (allData.all_Nodes[i].myWays[j].type.contains("footway") || allData.all_Nodes[i].myWays[j].type.contains("bridleway") || allData.all_Nodes[i].myWays[j].type.contains("steps") || allData.all_Nodes[i].myWays[j].type.contains("path")) {
                    indexes[j] = 9;
                } else {
                    indexes[j] = 10;
                }
            } else {
                indexes[j] = 10;
            }
        }

        allData.all_Nodes[i].layers.add(indexes);

        return numRefinedNodes;
    }

    public void setWaysColorLayerBased(AllData allData, int activeLayer) {
        for (int i = 0; i < allData.all_Ways.length; i++) {
            allData.all_Ways[i].color = new float[3 * allData.all_Ways[i].myNodes.length];

            for (int j = 0; j < allData.all_Ways[i].myNodes.length; j++) {
                float[] temp = getColor(allData, activeLayer, i, j);

                allData.all_Ways[i].color[3 * j + 0] = temp[0];
                allData.all_Ways[i].color[3 * j + 1] = temp[1];
                allData.all_Ways[i].color[3 * j + 2] = temp[2];
//                System.out.println("r "+temp[0]);
//                System.out.println("g "+temp[1]);
//                System.out.println("b "+temp[2]);
            }
        }
    }

    public void setWaysColorLavaLayerBased(AllData allData, int activeLavaLayer) {
        double maxLava = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < allData.all_Ways.length; i++) {
            for (int j = 0; j < allData.all_Ways[i].myNodes.length; j++) {
                if (maxLava < allData.all_Ways[i].myNodes[j].lava_value_indexed[activeLavaLayer]) {
                    maxLava = allData.all_Ways[i].myNodes[j].lava_value_indexed[activeLavaLayer];
                }
            }
        }
        for (int i = 0; i < allData.all_Ways.length; i++) {
            allData.all_Ways[i].color = new float[3 * allData.all_Ways[i].myNodes.length];

            for (int j = 0; j < allData.all_Ways[i].myNodes.length; j++) {
//                System.out.println();
                allData.all_Ways[i].color[3 * j + 0] = (float) (allData.all_Ways[i].myNodes[j].lava_value_indexed[activeLavaLayer] / maxLava);
                allData.all_Ways[i].color[3 * j + 1] = 0;
                allData.all_Ways[i].color[3 * j + 2] = 1 - (float) (allData.all_Ways[i].myNodes[j].lava_value_indexed[activeLavaLayer] / maxLava);
            }
        }
    }

    public void setWaysColorStatic(AllData allData, Color color) {
        for (int i = 0; i < allData.all_Ways.length; i++) {
            allData.all_Ways[i].color = new float[3 * allData.all_Ways[i].myNodes.length];

            for (int j = 0; j < allData.all_Ways[i].myNodes.length; j++) {

                allData.all_Ways[i].color[3 * j + 0] = color.getRed();
                allData.all_Ways[i].color[3 * j + 1] = color.getGreen();
                allData.all_Ways[i].color[3 * j + 2] = color.getBlue();
//                System.out.println("r "+temp[0]);
//                System.out.println("g "+temp[1]);
//                System.out.println("b "+temp[2]);
            }
        }
    }

    public float[] getColor(AllData allData, int activeLayer, int i, int j) {
//        System.out.println(allData.all_Ways[i].myNodes[j].layers.size());
//        System.out.println(allData.all_Nodes[allData.all_Ways[i].myNodes[j].myOrder].layers.size());
        float result[] = new float[3];
        if (((LayerDefinition) allData.all_Layers.get(activeLayer)).myType.equals("category") || ((LayerDefinition) allData.all_Layers.get(activeLayer)).myType.equals("base") || ((LayerDefinition) allData.all_Layers.get(activeLayer)).myType.equals("image")) {
            short[] temp = (short[]) allData.all_Ways[i].myNodes[j].layers.get(activeLayer);
            int mixedRed = 0;
            int mixedGreen = 0;
            int mixedBlue = 0;
            for (int k = 0; k < temp.length; k++) {
//            System.out.println(((LayerDefinition) allData.all_Layers.get(activeLayer)).colors[temp[k] - 1].getRed());
//            System.out.println(((LayerDefinition) allData.all_Layers.get(activeLayer)).colors[temp[k] - 1].getGreen());
//            System.out.println(((LayerDefinition) allData.all_Layers.get(activeLayer)).colors[temp[k] - 1].getBlue());
                mixedRed = mixedRed + ((LayerDefinition) allData.all_Layers.get(activeLayer)).colors[temp[k] - 1].getRed();
                mixedGreen = mixedGreen + ((LayerDefinition) allData.all_Layers.get(activeLayer)).colors[temp[k] - 1].getGreen();
                mixedBlue = mixedBlue + ((LayerDefinition) allData.all_Layers.get(activeLayer)).colors[temp[k] - 1].getBlue();
            }
//        System.out.println(temp.length);
//        System.out.println(mixedRed);
//        System.out.println(mixedGreen);
//        System.out.println(mixedBlue);
            //System.out.println("\n");
            result[0] = (float) mixedRed / (float) ((temp.length));
            result[1] = (float) mixedGreen / (float) ((temp.length));
            result[2] = (float) mixedBlue / (float) ((temp.length));
            if ((result[0] + result[1] + result[2]) < 255) {
                float additive = (float) (255 - (result[0] + result[1] + result[2])) / (float) 3;
                result[0] = result[0] + additive;
                result[1] = result[1] + additive;
                result[2] = result[2] + additive;
                if (result[0] > 255) {
                    result[0] = 255;
                }
                if (result[1] > 255) {
                    result[1] = 255;
                }
                if (result[2] > 255) {
                    result[2] = 255;
                }
            }
            result[0] = (float) result[0] / (float) (255);
            result[1] = (float) result[1] / (float) (255);
            result[2] = (float) result[2] / (float) (255);
            return result;
        } else if (((LayerDefinition) allData.all_Layers.get(activeLayer)).myType.equals("numeric")) {
            float temp = new Float((Double) allData.all_Ways[i].myNodes[j].layers.get(activeLayer));
            if (temp >= 0) {
                result[0] = temp;
                result[1] = 0;
                result[2] = 1 - temp;
            } else {
                result[0] = 0;
                result[1] = 1;
                result[2] = 0;
            }
            return result;
        }
        return result;
    }

    public void read_data(AllData allData, String file_path, String file_name, String data_type) {
        FileInputStream f_in;
        try {
//            AllData allData=new AllData();
            f_in = new FileInputStream(file_path + "\\" + file_name + "." + data_type);
            // Read object using ObjectInputStream
            ObjectInputStream obj_in;
            try {
                obj_in = new ObjectInputStream(f_in);
                try {
                    Object obj = obj_in.readObject();
                    if (obj instanceof Way[]) {
                        allData.all_Ways = (Way[]) obj;
                        // Do something with vector....
                    } else if (obj instanceof LocationNode[]) {
                        allData.all_Nodes = (LocationNode[]) obj;
                    } else if (obj instanceof Grid[][]) {
                        allData.grid = (Grid[][]) obj;
                    }
//                    return allData;
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(main_frame.class.getName()).log(Level.SEVERE, null, ex);
                }

            } catch (IOException ex) {
                Logger.getLogger(main_frame.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(main_frame.class.getName()).log(Level.SEVERE, null, ex);
        }
//        return null;
    }

    public AllData read_allData(AllData allData, String file_path, String file_name, String data_type) {
        FileInputStream f_in;
        try {

            //REPORTING
            String detailedResults = "Load serializable preprocessed database." + System.lineSeparator();
            detailedResults = detailedResults + "Source file: " + file_path+"|"+file_name;
            Calendar currentDate = Calendar.getInstance();
            Date date = currentDate.getTime();
            double startRAM = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024.0;
            long startTime = System.nanoTime();
            //REPORTING

            f_in = new FileInputStream(file_path + "\\" + file_name + "." + data_type);
            // Read object using ObjectInputStream
            ObjectInputStream obj_in;
            try {
                obj_in = new ObjectInputStream(f_in);
                try {
                    Object obj = obj_in.readObject();
                    if (obj instanceof AllData) {
                        allData = (AllData) obj;
                    }

                    //REPORTING
                    long endTime = System.nanoTime();
                    double elapsed = ((endTime - startTime) / 1000000000);
                    double endRAM = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024.0;
                    double usedRAM = endRAM - startRAM;
                    localReport = new ReportResults(date, "Load serializable", startTime, endTime, elapsed, 1, startRAM, endRAM, usedRAM, detailedResults);
//                    allData.results=new ArrayList();
                    allData.results.add(localReport);
                    //REPORTING

                    return allData;
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(main_frame.class.getName()).log(Level.SEVERE, null, ex);
                }

            } catch (IOException ex) {
                Logger.getLogger(main_frame.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(main_frame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public AllData read_allData_kryo(AllData allData, String file_path, String file_name, String data_type) {
        Kryo kryo = new Kryo();
        kryo.setInstantiatorStrategy(new Kryo.DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
        Input input;
        try {

            //REPORTING
            String detailedResults = "Load kryo preprocessed database." + System.lineSeparator();
            detailedResults = detailedResults + "Source file: " + file_path+"|"+file_name+ System.lineSeparator();
            Calendar currentDate = Calendar.getInstance();
            Date date = currentDate.getTime();
            double startRAM = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024.0;
            long startTime = System.nanoTime();
            //REPORTING

            input = new Input(new FileInputStream(file_path + "\\" + file_name + "." + "bin"));
            allData = kryo.readObject(input, AllData.class);
            input.close();
            detailedResults = detailedResults + "Number of nodes: " + allData.all_Nodes.length+ System.lineSeparator();
            detailedResults = detailedResults + "Number of ways: " + allData.all_Ways.length+ System.lineSeparator();
            detailedResults = detailedResults + "Number of layers: " + allData.all_Layers.size()+ System.lineSeparator();
            detailedResults = detailedResults + "Number of reports: " + allData.results.size()+ System.lineSeparator();

            //REPORTING
            long endTime = System.nanoTime();
            double elapsed = ((endTime - startTime) / 1000000000);
            double endRAM = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024.0;
            double usedRAM = endRAM - startRAM;
            localReport = new ReportResults(date, "Load kryo", startTime, endTime, elapsed, 1, startRAM, endRAM, usedRAM, detailedResults);
            allData.results.add(localReport);
            //REPORTING

            return allData;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PreProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Grid detect_grid(AllData allData, double lat, double lon) {
        int x_index = 0;
        int y_index = 0;
        for (int j = 0; j < allData.grid.length; j++) {
            if (lat < allData.grid[j][0].max_x_val && lat > allData.grid[j][0].min_x_val) {
                x_index = j;
            }
        }
        for (int j = 0; j < allData.grid[0].length; j++) {
            if (lon < allData.grid[0][j].max_y_val && lon > allData.grid[0][j].min_y_val) {
                y_index = j;
            }
        }
        return allData.grid[x_index][y_index];
    }

    public static CPUUpperLowerBounds[] spreadTasks(int numCPUs, int numTasks) {
        CPUUpperLowerBounds output[] = new CPUUpperLowerBounds[numCPUs];
        for (int i = 0; i < numCPUs; i++) {
            output[i] = new CPUUpperLowerBounds();
        }
        if (numCPUs < numTasks) {
            if (numCPUs > 2) {
                int step = (int) Math.floor(((numTasks) / numCPUs));
                for (int i = 0; i < numCPUs - 1; i++) {
                    output[i].startIndex = i * step;
                    output[i].endIndex = (i + 1) * step;
                }
//            System.out.println();
                output[numCPUs - 1].startIndex = output[numCPUs - 2].endIndex;
                output[numCPUs - 1].endIndex = numTasks;
            } else if (numCPUs == 2) {
                output[0].startIndex = 0;
                output[0].endIndex = (int) Math.floor(((numTasks) / numCPUs));
                output[numCPUs - 1].startIndex = output[numCPUs - 2].endIndex;
                output[numCPUs - 1].endIndex = numTasks;
            } else if (numCPUs == 1) {
                output[0].startIndex = 0;
                output[0].endIndex = numTasks;
            }
        } else {
            for (int i = 0; i < numTasks; i++) {
                output[i].startIndex = i;
                output[i].endIndex = i + 1;
            }
        }
        return output;
    }
}

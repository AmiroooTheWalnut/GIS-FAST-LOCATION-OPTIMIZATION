/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esmaieeli.gisFastLocationOptimization.Simulation;

import esmaieeli.gisFastLocationOptimization.GIS3D.AllData;
import esmaieeli.gisFastLocationOptimization.GIS3D.LayerDefinition;
import jankovicsandras.imagetracer.ImageTracer;
import static jankovicsandras.imagetracer.ImageTracer.checkoptions;
import static jankovicsandras.imagetracer.ImageTracer.imagedataToTracedata;
import static jankovicsandras.imagetracer.ImageTracer.loadImageData;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

/**
 *
 * @author user
 */
public class VectorToPolygon {

    public double scaleOffsetX;
    public double scaleOffsetY;
    public double scaleWidth;
    public double scaleHeight;

    public int imgWidth = 1000;
    public int imgHeight;

    public ArrayList<SimplePolygons> imageToPolygons(int[][] input, AllData allData, int layerIndex) {
        ArrayList<SimplePolygons> imgBoundaries = getBoundariesPoints(input, allData, layerIndex);
        ArrayList<SimplePolygons> output = convertToOriginalScale(imgBoundaries);
        return output;
    }

    public ArrayList<SimplePolygons> convertToOriginalScale(ArrayList<SimplePolygons> input) {
        for (int i = 0; i < input.size(); i++) {
            for (int j = 0; j < input.get(i).polygons.size(); j++) {
                for (int k = 0; k < input.get(i).polygons.get(j).points.size(); k++) {
                    double x = input.get(i).polygons.get(j).points.get(k).xIm;
                    input.get(i).polygons.get(j).points.get(k).xM = ((x / (imgWidth - 1) * scaleWidth) + scaleOffsetX);
                    input.get(i).polygons.get(j).points.get(k).yM = ((x / (imgHeight - 1) * scaleHeight) + scaleOffsetY);
                }
            }
        }
        return input;
    }

    public ArrayList<SimplePolygons> getBoundariesPoints(int[][] input, AllData allData, int layerIndex) {

        ArrayList<SimplePolygons> allBoundaries = new ArrayList();
        for (int i = 1; i < ((LayerDefinition) (allData.all_Layers.get(layerIndex))).categories.length; i++) {
            SimplePolygons boundaries = new SimplePolygons();
            //FOR DEBUGGING
//            saveIndexedImageAsPNG(getVDImage(input, i), allData, layerIndex, "temp");
            //FOR DEBUGGING

            BufferedImage image = indexedImageToBufferedImage(getVDImage(input, i), allData, layerIndex);

            try {
                ImageTracer.ImageData imgd = loadImageData(image);

                HashMap<String, Float> options = checkoptions(null);
                options.put("roundcoords", 3f);
                options.put("blurdelta", 1f);
                options.put("colorsampling", 0f);
                options.put("numberofcolors", 2f);
                options.put("ltres", 0.001f);
                options.put("qtres", 0.001f);
                options.put("mincolorratio", 0.0f);

                byte[][] palette = new byte[2][4];
                palette[0][0] = (byte) (-128);  // R
                palette[0][1] = (byte) (-128);  // G
                palette[0][2] = (byte) (-128);  // B
                palette[0][3] = (byte) 127;     // A

                palette[1][0] = (byte) (127);  // R
                palette[1][1] = (byte) (-128);  // G
                palette[1][2] = (byte) (-128);  // B
                palette[1][3] = (byte) 127;     // A

                ImageTracer.IndexedImage ii = imagedataToTracedata(imgd, options, palette);

                for (int j = 0; j < ii.layers.get(1).size(); j++) {
                    SimplePolygon polygon = new SimplePolygon();
                    for (int k = 0; k < ii.layers.get(1).get(j).size(); k++) {
                        if (ii.layers.get(1).get(j).get(k)[0] == 1) {
                            polygon.points.add(new SimplePoint(ii.layers.get(1).get(j).get(k)[3], ii.layers.get(1).get(j).get(k)[4]));
                        } else {
                            polygon.points.add(new SimplePoint(ii.layers.get(1).get(j).get(k)[5], ii.layers.get(1).get(j).get(k)[6]));
                        }
                    }
                    System.out.println("polygon " + j + " added");
                    boundaries.polygons.add(polygon);
                }

                System.out.println("!!!!!!!");
            } catch (Exception ex) {
                Logger.getLogger(VectorToPolygon.class.getName()).log(Level.SEVERE, null, ex);
            }

            allBoundaries.add(boundaries);
        }
        return allBoundaries;
    }

    public int[][] getVDImage(int[][] input, int vDIndex) {
        int[][] output = new int[input.length][input[0].length];
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[0].length; j++) {
                if (input[i][j] == vDIndex) {
                    output[i][j] = 1;
                }
            }
        }
        return output;
    }


    public int[][] layerToIndexedImage(AllData allData, int layerIndex, boolean isSaveResults) {
        setScaleFactors(allData);
        double ratio = scaleWidth / scaleHeight;

        imgHeight = (int) (imgWidth * (1 / ratio));
        int[][] output = new int[imgWidth][imgHeight];

        //COULD BE COMPLICATED! FIRST GOING FOR NODES
//        for(int i=0;i<allData.all_Ways.length;i++){
//            if(allData.all_Ways[i].)
//        }
        for (int i = 0; i < allData.all_Nodes.length; i++) {
            int[] imgP = vectorToImage(allData.all_Nodes[i].lon, allData.all_Nodes[i].lat, imgWidth, imgHeight);
            int cellIndex = ((int) (((short[]) (allData.all_Nodes[i].layers.get(layerIndex)))[0])) - 1;
            if (cellIndex != 0) {
                output[imgP[0]][imgP[1]] = cellIndex;
            }
        }
        output = filloutAsoluteTheImage(output, allData, layerIndex, 35, isSaveResults);
        if (isSaveResults == true) {
            saveIndexedImageAsPNG(output, allData, layerIndex, "finalABSFilled");
        }
        output = filloutRemainingHoles(output, allData, layerIndex, 4);
        if (isSaveResults == true) {
            saveIndexedImageAsPNG(output, allData, layerIndex, "finalFilled");
        }
        return output;
    }

    public int[][] filloutRemainingHoles(int[][] input, AllData allData, int layerIndex, int numIterations) {
        boolean isPixelLeft = true;
        int counter = 0;

        labelVoidPixels(input);

        //        while (isPixelLeft == true) {
        for (int iter = 0; iter < numIterations; iter++) {
            boolean isFilled[][] = new boolean[input.length][input[0].length];
            isPixelLeft = false;
            for (int i = 0; i < input.length; i++) {
                for (int j = 0; j < input[0].length; j++) {
                    int selfValue = input[i][j];
                    if (selfValue == -1 && isFilled[i][j] == false) {
//                    int N=-1;//values[0]
//                    int NE=-1;//values[1]
//                    int E=-1;//values[2]
//                    int SE=-1;//values[3]
//                    int S=-1;//values[4]
//                    int SW=-1;//values[5]
//                    int W=-1;//values[6]
//                    int NW=-1;//values[7]
                        int values[] = new int[8];
                        for (int k = 0; k < 8; k++) {
                            values[k] = -1;
                        }
                        if (i == 0) {
                            if (j == 0) {
                                values[2] = input[i + 1][j];//E
                                values[3] = input[i + 1][j + 1];//SE
                                values[4] = input[i][j + 1];//S
                            } else if (j == input[0].length - 1) {
                                values[0] = input[i][j - 1];//N
                                values[1] = input[i + 1][j - 1];//NE
                                values[2] = input[i + 1][j];//E
                            } else {
                                values[0] = input[i][j - 1];//N
                                values[1] = input[i + 1][j - 1];//NE
                                values[2] = input[i + 1][j];//E
                                values[3] = input[i + 1][j + 1];//SE
                                values[4] = input[i][j + 1];//S
                            }
                        } else if (i == input.length - 1) {
                            if (j == 0) {
                                values[4] = input[i][j + 1];//S
                                values[5] = input[i - 1][j + 1];//SW
                                values[6] = input[i - 1][j];//W
                            } else if (j == input[0].length - 1) {
                                values[6] = input[i - 1][j];//W
                                values[7] = input[i - 1][j - 1];//NW
                                values[0] = input[i][j - 1];//N
                            } else {
                                values[4] = input[i][j + 1];//S
                                values[5] = input[i - 1][j + 1];//SW
                                values[6] = input[i - 1][j];//W
                                values[7] = input[i - 1][j - 1];//NW
                                values[0] = input[i][j - 1];//N
                            }
                        } else {
                            if (j == 0) {
                                values[2] = input[i + 1][j];//E
                                values[3] = input[i + 1][j + 1];//SE
                                values[4] = input[i][j + 1];//S
                                values[5] = input[i - 1][j + 1];//SW
                                values[6] = input[i - 1][j];//W
                            } else if (j == input[0].length - 1) {
                                values[6] = input[i - 1][j];//W
                                values[7] = input[i - 1][j - 1];//NW
                                values[0] = input[i][j - 1];//N
                                values[1] = input[i + 1][j - 1];//NE
                                values[2] = input[i + 1][j];//E
                            } else {
                                values[0] = input[i][j - 1];//N
                                values[1] = input[i + 1][j - 1];//NE
                                values[2] = input[i + 1][j];//E
                                values[3] = input[i + 1][j + 1];//SE
                                values[4] = input[i][j + 1];//S
                                values[5] = input[i - 1][j + 1];//SW
                                values[6] = input[i - 1][j];//W
                                values[7] = input[i - 1][j - 1];//NW
                            }
                        }

                        List<Integer> intList = new ArrayList(values.length);
                        for (int k = 0; k < values.length; k++) {
                            if (values[k] > 0) {
                                intList.add(values[k]);
                            }
                        }

//                        Set<Integer> distinct = new HashSet(intList);
                        int maxFreq = 0;
                        int selectedIndex = 0;
                        for (int k = 0; k < intList.size(); k++) {
                            int freq = Collections.frequency(intList, intList.get(k));
                            if (freq > maxFreq) {
                                maxFreq = freq;
                                selectedIndex = intList.get(k);
                            }
                        }

                        if (selectedIndex > 0) {
                            isPixelLeft = true;
                            input[i][j] = selectedIndex;
                            if (values[0] >= 0) {
//                                input[i][j - 1] = selectedIndex;//N
                                isFilled[i][j - 1] = true;
                            }
                            if (values[1] >= 0) {
//                                input[i + 1][j - 1] = selectedIndex;//NE
                                isFilled[i + 1][j - 1] = true;
                            }
                            if (values[2] >= 0) {
//                                input[i + 1][j] = selectedIndex;//E
                                isFilled[i + 1][j] = true;
                            }
                            if (values[3] >= 0) {
//                                input[i + 1][j + 1] = selectedIndex;//SE
                                isFilled[i + 1][j + 1] = true;
                            }
                            if (values[4] >= 0) {
//                                input[i][j + 1] = selectedIndex;//S
                                isFilled[i][j + 1] = true;
                            }
                            if (values[5] >= 0) {
//                                input[i - 1][j + 1] = selectedIndex;//SW
                                isFilled[i - 1][j + 1] = true;
                            }
                            if (values[6] >= 0) {
//                                input[i - 1][j] = selectedIndex;//W
                                isFilled[i - 1][j] = true;
                            }
                            if (values[7] >= 0) {
//                                input[i - 1][j - 1] = selectedIndex;//NW
                                isFilled[i - 1][j - 1] = true;
                            }
                        }
                    }
                }
            }
        }

        return input;

    }

    public int[][] labelVoidPixels(int[][] input) {
        boolean[][] isPatched = new boolean[input.length][input[0].length];
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[0].length; j++) {
                if (input[i][j] == 0 && isPatched[i][j] == false) {
                    ArrayList<int[]> patch = getPatch(input, i, j);
                    if (patch.size() < 80) {
                        for (int k = 0; k < patch.size(); k++) {
                            input[patch.get(k)[0]][patch.get(k)[1]] = -1;
                        }
                    }
                    for (int k = 0; k < patch.size(); k++) {
                        isPatched[patch.get(k)[0]][patch.get(k)[1]] = true;
                    }
                }
            }
        }
        return input;
    }

    public ArrayList<int[]> getPatch(int[][] input, int sI, int sJ) {
        ArrayList<int[]> candidates = new ArrayList();
        ArrayList<int[]> points = new ArrayList();
        boolean[][] isChecked = new boolean[input.length][input[0].length];
        int[] init = new int[]{sI, sJ};
        points.add(init);
        candidates.add(init);

        isChecked[candidates.get(0)[0]][candidates.get(0)[1]] = true;

        while (candidates.size() > 0) {
            ArrayList<int[]> tempCandidates = new ArrayList();
            for (int i = 0; i < candidates.size(); i++) {
                ArrayList<int[]> neighbors = getNeighbors(input, candidates.get(i)[0], candidates.get(i)[1], isChecked);
                tempCandidates.addAll(neighbors);
            }
            candidates.clear();
            candidates.addAll(tempCandidates);
            points.addAll(candidates);
        }

        return points;
    }

    public ArrayList<int[]> getNeighbors(int[][] input, int tI, int tJ, boolean[][] isChecked) {
        ArrayList<int[]> output = new ArrayList();
        if (tI == 0) {
            if (tJ == 0) {
                if (isChecked[tI + 1][tJ] == false && input[tI + 1][tJ] == 0) {
                    int[] point = new int[]{tI + 1, tJ};
                    output.add(point);
                    isChecked[tI + 1][tJ] = true;
                }
                if (isChecked[tI + 1][tJ + 1] == false && input[tI + 1][tJ + 1] == 0) {
                    int[] point = new int[]{tI + 1, tJ + 1};
                    output.add(point);
                    isChecked[tI + 1][tJ + 1] = true;
                }
                if (isChecked[tI][tJ + 1] == false && input[tI][tJ + 1] == 0) {
                    int[] point = new int[]{tI, tJ + 1};
                    output.add(point);
                    isChecked[tI][tJ + 1] = true;
                }
//                values[2] = input[i + 1][j];//E
//                values[3] = input[i + 1][j + 1];//SE
//                values[4] = input[i][j + 1];//S
            } else if (tJ == input[0].length - 1) {
                if (isChecked[tI][tJ - 1] == false && input[tI][tJ - 1] == 0) {
                    int[] point = new int[]{tI, tJ - 1};
                    output.add(point);
                    isChecked[tI][tJ - 1] = true;
                }
                if (isChecked[tI + 1][tJ - 1] == false && input[tI + 1][tJ - 1] == 0) {
                    int[] point = new int[]{tI + 1, tJ - 1};
                    output.add(point);
                    isChecked[tI + 1][tJ - 1] = true;
                }
                if (isChecked[tI + 1][tJ] == false && input[tI + 1][tJ] == 0) {
                    int[] point = new int[]{tI + 1, tJ};
                    output.add(point);
                    isChecked[tI + 1][tJ] = true;
                }
//                values[0] = input[i][j - 1];//N
//                values[1] = input[i + 1][j - 1];//NE
//                values[2] = input[i + 1][j];//E
            } else {
                if (isChecked[tI][tJ - 1] == false && input[tI][tJ - 1] == 0) {
                    int[] point = new int[]{tI, tJ - 1};
                    output.add(point);
                    isChecked[tI][tJ - 1] = true;
                }
                if (isChecked[tI + 1][tJ - 1] == false && input[tI + 1][tJ - 1] == 0) {
                    int[] point = new int[]{tI + 1, tJ - 1};
                    output.add(point);
                    isChecked[tI + 1][tJ - 1] = true;
                }
                if (isChecked[tI + 1][tJ] == false && input[tI + 1][tJ] == 0) {
                    int[] point = new int[]{tI + 1, tJ};
                    output.add(point);
                    isChecked[tI + 1][tJ] = true;
                }
                if (isChecked[tI + 1][tJ + 1] == false && input[tI + 1][tJ + 1] == 0) {
                    int[] point = new int[]{tI + 1, tJ + 1};
                    output.add(point);
                    isChecked[tI + 1][tJ + 1] = true;
                }
                if (isChecked[tI][tJ + 1] == false && input[tI][tJ + 1] == 0) {
                    int[] point = new int[]{tI, tJ + 1};
                    output.add(point);
                    isChecked[tI][tJ + 1] = true;
                }
//                values[0] = input[i][j - 1];//N
//                values[1] = input[i + 1][j - 1];//NE
//                values[2] = input[i + 1][j];//E
//                values[3] = input[i + 1][j + 1];//SE
//                values[4] = input[i][j + 1];//S
            }
        } else if (tI == input.length - 1) {
            if (tJ == 0) {
                if (isChecked[tI][tJ + 1] == false && input[tI][tJ + 1] == 0) {
                    int[] point = new int[]{tI, tJ + 1};
                    output.add(point);
                    isChecked[tI][tJ + 1] = true;
                }
                if (isChecked[tI - 1][tJ + 1] == false && input[tI - 1][tJ + 1] == 0) {
                    int[] point = new int[]{tI - 1, tJ + 1};
                    output.add(point);
                    isChecked[tI - 1][tJ + 1] = true;
                }
                if (isChecked[tI - 1][tJ] == false && input[tI - 1][tJ] == 0) {
                    int[] point = new int[]{tI - 1, tJ};
                    output.add(point);
                    isChecked[tI - 1][tJ] = true;
                }
//                values[4] = input[i][j + 1];//S
//                values[5] = input[i - 1][j + 1];//SW
//                values[6] = input[i - 1][j];//W
            } else if (tJ == input[0].length - 1) {
                if (isChecked[tI - 1][tJ] == false && input[tI - 1][tJ] == 0) {
                    int[] point = new int[]{tI - 1, tJ};
                    output.add(point);
                    isChecked[tI - 1][tJ] = true;
                }
                if (isChecked[tI - 1][tJ - 1] == false && input[tI - 1][tJ - 1] == 0) {
                    int[] point = new int[]{tI - 1, tJ - 1};
                    output.add(point);
                    isChecked[tI - 1][tJ - 1] = true;
                }
                if (isChecked[tI][tJ - 1] == false && input[tI][tJ - 1] == 0) {
                    int[] point = new int[]{tI, tJ - 1};
                    output.add(point);
                    isChecked[tI][tJ - 1] = true;
                }
//                values[6] = input[i - 1][j];//W
//                values[7] = input[i - 1][j - 1];//NW
//                values[0] = input[i][j - 1];//N
            } else {
                if (isChecked[tI][tJ + 1] == false && input[tI][tJ + 1] == 0) {
                    int[] point = new int[]{tI, tJ + 1};
                    output.add(point);
                    isChecked[tI][tJ + 1] = true;
                }
                if (isChecked[tI - 1][tJ + 1] == false && input[tI - 1][tJ + 1] == 0) {
                    int[] point = new int[]{tI - 1, tJ + 1};
                    output.add(point);
                    isChecked[tI - 1][tJ + 1] = true;
                }
                if (isChecked[tI - 1][tJ] == false && input[tI - 1][tJ] == 0) {
                    int[] point = new int[]{tI - 1, tJ};
                    output.add(point);
                    isChecked[tI - 1][tJ] = true;
                }
                if (isChecked[tI - 1][tJ - 1] == false && input[tI - 1][tJ - 1] == 0) {
                    int[] point = new int[]{tI - 1, tJ - 1};
                    output.add(point);
                    isChecked[tI - 1][tJ - 1] = true;
                }
                if (isChecked[tI][tJ - 1] == false && input[tI][tJ - 1] == 0) {
                    int[] point = new int[]{tI, tJ - 1};
                    output.add(point);
                    isChecked[tI][tJ - 1] = true;
                }
//                values[4] = input[i][j + 1];//S
//                values[5] = input[i - 1][j + 1];//SW
//                values[6] = input[i - 1][j];//W
//                values[7] = input[i - 1][j - 1];//NW
//                values[0] = input[i][j - 1];//N
            }
        } else {
            if (tJ == 0) {
                if (isChecked[tI + 1][tJ] == false && input[tI + 1][tJ] == 0) {
                    int[] point = new int[]{tI + 1, tJ};
                    output.add(point);
                    isChecked[tI + 1][tJ] = true;
                }
                if (isChecked[tI + 1][tJ + 1] == false && input[tI + 1][tJ + 1] == 0) {
                    int[] point = new int[]{tI + 1, tJ + 1};
                    output.add(point);
                    isChecked[tI + 1][tJ + 1] = true;
                }
                if (isChecked[tI][tJ + 1] == false && input[tI][tJ + 1] == 0) {
                    int[] point = new int[]{tI, tJ + 1};
                    output.add(point);
                    isChecked[tI][tJ + 1] = true;
                }
                if (isChecked[tI - 1][tJ + 1] == false && input[tI - 1][tJ + 1] == 0) {
                    int[] point = new int[]{tI - 1, tJ + 1};
                    output.add(point);
                    isChecked[tI - 1][tJ + 1] = true;
                }
                if (isChecked[tI - 1][tJ] == false && input[tI - 1][tJ] == 0) {
                    int[] point = new int[]{tI - 1, tJ};
                    output.add(point);
                    isChecked[tI - 1][tJ] = true;
                }
//                values[2] = input[i + 1][j];//E
//                values[3] = input[i + 1][j + 1];//SE
//                values[4] = input[i][j + 1];//S
//                values[5] = input[i - 1][j + 1];//SW
//                values[6] = input[i - 1][j];//W
            } else if (tJ == input[0].length - 1) {
                if (isChecked[tI - 1][tJ] == false && input[tI - 1][tJ] == 0) {
                    int[] point = new int[]{tI - 1, tJ};
                    output.add(point);
                    isChecked[tI - 1][tJ] = true;
                }
                if (isChecked[tI - 1][tJ - 1] == false && input[tI - 1][tJ - 1] == 0) {
                    int[] point = new int[]{tI - 1, tJ - 1};
                    output.add(point);
                    isChecked[tI - 1][tJ - 1] = true;
                }
                if (isChecked[tI][tJ - 1] == false && input[tI][tJ - 1] == 0) {
                    int[] point = new int[]{tI, tJ - 1};
                    output.add(point);
                    isChecked[tI][tJ - 1] = true;
                }
                if (isChecked[tI + 1][tJ - 1] == false && input[tI + 1][tJ - 1] == 0) {
                    int[] point = new int[]{tI + 1, tJ - 1};
                    output.add(point);
                    isChecked[tI + 1][tJ - 1] = true;
                }
                if (isChecked[tI + 1][tJ] == false && input[tI + 1][tJ] == 0) {
                    int[] point = new int[]{tI + 1, tJ};
                    output.add(point);
                    isChecked[tI + 1][tJ] = true;
                }
//                values[6] = input[i - 1][j];//W
//                values[7] = input[i - 1][j - 1];//NW
//                values[0] = input[i][j - 1];//N
//                values[1] = input[i + 1][j - 1];//NE
//                values[2] = input[i + 1][j];//E
            } else {
                if (isChecked[tI][tJ - 1] == false && input[tI][tJ - 1] == 0) {
                    int[] point = new int[]{tI, tJ - 1};
                    output.add(point);
                    isChecked[tI][tJ - 1] = true;
                }
                if (isChecked[tI + 1][tJ - 1] == false && input[tI + 1][tJ - 1] == 0) {
                    int[] point = new int[]{tI + 1, tJ - 1};
                    output.add(point);
                    isChecked[tI + 1][tJ - 1] = true;
                }
                if (isChecked[tI + 1][tJ] == false && input[tI + 1][tJ] == 0) {
                    int[] point = new int[]{tI + 1, tJ};
                    output.add(point);
                    isChecked[tI + 1][tJ] = true;
                }
                if (isChecked[tI + 1][tJ + 1] == false && input[tI + 1][tJ + 1] == 0) {
                    int[] point = new int[]{tI + 1, tJ + 1};
                    output.add(point);
                    isChecked[tI + 1][tJ + 1] = true;
                }
                if (isChecked[tI][tJ + 1] == false && input[tI][tJ + 1] == 0) {
                    int[] point = new int[]{tI, tJ + 1};
                    output.add(point);
                    isChecked[tI][tJ + 1] = true;
                }
                if (isChecked[tI - 1][tJ + 1] == false && input[tI - 1][tJ + 1] == 0) {
                    int[] point = new int[]{tI - 1, tJ + 1};
                    output.add(point);
                    isChecked[tI - 1][tJ + 1] = true;
                }
                if (isChecked[tI - 1][tJ] == false && input[tI - 1][tJ] == 0) {
                    int[] point = new int[]{tI - 1, tJ};
                    output.add(point);
                    isChecked[tI - 1][tJ] = true;
                }
                if (isChecked[tI - 1][tJ - 1] == false && input[tI - 1][tJ - 1] == 0) {
                    int[] point = new int[]{tI - 1, tJ - 1};
                    output.add(point);
                    isChecked[tI - 1][tJ - 1] = true;
                }
//                values[0] = input[i][j - 1];//N
//                values[1] = input[i + 1][j - 1];//NE
//                values[2] = input[i + 1][j];//E
//                values[3] = input[i + 1][j + 1];//SE
//                values[4] = input[i][j + 1];//S
//                values[5] = input[i - 1][j + 1];//SW
//                values[6] = input[i - 1][j];//W
//                values[7] = input[i - 1][j - 1];//NW
            }
        }
        return output;
    }

    public int[][] filloutAsoluteTheImage(int[][] input, AllData allData, int layerIndex, int numIterations, boolean isMakeGIF) {

        boolean isPixelLeft = true;
        int counter = 0;

        ImageOutputStream output = null;
        GifSequenceWriter writer = null;

        if (isMakeGIF == true) {
            try {
                output = new FileImageOutputStream(new File("ABSFillAnimation.gif"));
                BufferedImage first = indexedImageToBufferedImage(input, allData, layerIndex);
                writer = new GifSequenceWriter(output, first.getType(), 250, true);
            } catch (IOException ex) {
                Logger.getLogger(VectorToPolygon.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

//        while (isPixelLeft == true) {
        for (int iter = 0; iter < numIterations; iter++) {
            boolean isFilled[][] = new boolean[input.length][input[0].length];
            isPixelLeft = false;
            for (int i = 0; i < input.length; i++) {
                for (int j = 0; j < input[0].length; j++) {
                    int selfValue = input[i][j];
                    if (selfValue > 0 && isFilled[i][j] == false) {
//                    int N=-1;//values[0]
//                    int NE=-1;//values[1]
//                    int E=-1;//values[2]
//                    int SE=-1;//values[3]
//                    int S=-1;//values[4]
//                    int SW=-1;//values[5]
//                    int W=-1;//values[6]
//                    int NW=-1;//values[7]
                        int values[] = new int[8];
                        for (int k = 0; k < 8; k++) {
                            values[k] = -1;
                        }
                        if (i == 0) {
                            if (j == 0) {
                                values[2] = input[i + 1][j];//E
                                values[3] = input[i + 1][j + 1];//SE
                                values[4] = input[i][j + 1];//S
                            } else if (j == input[0].length - 1) {
                                values[0] = input[i][j - 1];//N
                                values[1] = input[i + 1][j - 1];//NE
                                values[2] = input[i + 1][j];//E
                            } else {
                                values[0] = input[i][j - 1];//N
                                values[1] = input[i + 1][j - 1];//NE
                                values[2] = input[i + 1][j];//E
                                values[3] = input[i + 1][j + 1];//SE
                                values[4] = input[i][j + 1];//S
                            }
                        } else if (i == input.length - 1) {
                            if (j == 0) {
                                values[4] = input[i][j + 1];//S
                                values[5] = input[i - 1][j + 1];//SW
                                values[6] = input[i - 1][j];//W
                            } else if (j == input[0].length - 1) {
                                values[6] = input[i - 1][j];//W
                                values[7] = input[i - 1][j - 1];//NW
                                values[0] = input[i][j - 1];//N
                            } else {
                                values[4] = input[i][j + 1];//S
                                values[5] = input[i - 1][j + 1];//SW
                                values[6] = input[i - 1][j];//W
                                values[7] = input[i - 1][j - 1];//NW
                                values[0] = input[i][j - 1];//N
                            }
                        } else {
                            if (j == 0) {
                                values[2] = input[i + 1][j];//E
                                values[3] = input[i + 1][j + 1];//SE
                                values[4] = input[i][j + 1];//S
                                values[5] = input[i - 1][j + 1];//SW
                                values[6] = input[i - 1][j];//W
                            } else if (j == input[0].length - 1) {
                                values[6] = input[i - 1][j];//W
                                values[7] = input[i - 1][j - 1];//NW
                                values[0] = input[i][j - 1];//N
                                values[1] = input[i + 1][j - 1];//NE
                                values[2] = input[i + 1][j];//E
                            } else {
                                values[0] = input[i][j - 1];//N
                                values[1] = input[i + 1][j - 1];//NE
                                values[2] = input[i + 1][j];//E
                                values[3] = input[i + 1][j + 1];//SE
                                values[4] = input[i][j + 1];//S
                                values[5] = input[i - 1][j + 1];//SW
                                values[6] = input[i - 1][j];//W
                                values[7] = input[i - 1][j - 1];//NW
                            }
                        }

                        boolean canFillNeighbors = true;
                        for (int k = 0; k < values.length; k++) {
                            if (values[k] > 0) {
                                if (values[k] != selfValue) {
                                    canFillNeighbors = false;
                                    break;
                                }
                            }
                        }
                        if (canFillNeighbors == true) {
                            isPixelLeft = true;
                            if (values[0] >= 0) {
                                input[i][j - 1] = selfValue;//N
                                isFilled[i][j - 1] = true;
                            }
                            if (values[1] >= 0) {
                                input[i + 1][j - 1] = selfValue;//NE
                                isFilled[i + 1][j - 1] = true;
                            }
                            if (values[2] >= 0) {
                                input[i + 1][j] = selfValue;//E
                                isFilled[i + 1][j] = true;
                            }
                            if (values[3] >= 0) {
                                input[i + 1][j + 1] = selfValue;//SE
                                isFilled[i + 1][j + 1] = true;
                            }
                            if (values[4] >= 0) {
                                input[i][j + 1] = selfValue;//S
                                isFilled[i][j + 1] = true;
                            }
                            if (values[5] >= 0) {
                                input[i - 1][j + 1] = selfValue;//SW
                                isFilled[i - 1][j + 1] = true;
                            }
                            if (values[6] >= 0) {
                                input[i - 1][j] = selfValue;//W
                                isFilled[i - 1][j] = true;
                            }
                            if (values[7] >= 0) {
                                input[i - 1][j - 1] = selfValue;//NW
                                isFilled[i - 1][j - 1] = true;
                            }
                        }
                    }
                }
            }
//            saveIndexedImageAsPNG(input, allData, layerIndex, "ABSFill_step" + counter);
            if (writer != null) {
                try {
                    writer.writeToSequence(indexedImageToBufferedImage(input, allData, layerIndex));
                } catch (IOException ex) {
                    Logger.getLogger(VectorToPolygon.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            counter = counter + 1;
            System.out.println((float) counter / (float) numIterations + "%");
        }

        if (writer != null && output != null) {
            try {
                writer.close();
                output.close();
            } catch (IOException ex) {
                Logger.getLogger(VectorToPolygon.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return input;
    }

    public BufferedImage indexedImageToBufferedImage(int[][] input, AllData allData, int layerIndex) {
        int width = input.length;
        int height = input[0].length;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int numIndices = ((LayerDefinition) (allData.all_Layers.get(layerIndex))).categories.length;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                int index = input[x][y];

                if (index == 0) {
                    Color color = new Color(0, 0, 0);
                    image.setRGB(x, y, color.getRGB());
                } else {
                    Color color = new Color(Color.HSBtoRGB((float) index / (float) numIndices - 1, 1, 1));
                    image.setRGB(x, y, color.getRGB());
                }

            }
        }
        return image;
    }

    public void saveIndexedImageAsPNG(int[][] input, AllData allData, int layerIndex, String name) {
        BufferedImage image = indexedImageToBufferedImage(input, allData, layerIndex);

        File outputFile = new File(name + ".png");
        try {
            ImageIO.write(image, "png", outputFile);
        } catch (IOException ex) {
            Logger.getLogger(VectorToPolygon.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void saveIndexedImageAsJPG(int[][] input, AllData allData, int layerIndex, String name) {
        BufferedImage image = indexedImageToBufferedImage(input, allData, layerIndex);

        File outputFile = new File(name + ".jpg");
        try {
            ImageIO.write(image, "jpg", outputFile);
        } catch (IOException ex) {
            Logger.getLogger(VectorToPolygon.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int[] vectorToImage(double x, double y, int imgWidth, int imgHeight) {
        int[] output = new int[2];
        output[0] = (int) Math.floor(((x - scaleOffsetX) / scaleWidth) * (imgWidth - 1));
        output[1] = (int) Math.floor(((y - scaleOffsetY) / scaleHeight) * (imgHeight - 1));
        return output;
    }

    public void setScaleFactors(AllData allData) {
        scaleOffsetX = allData.myScale.min_y;
        scaleOffsetY = allData.myScale.min_x;
        scaleWidth = allData.myScale.max_y - allData.myScale.min_y;
        scaleHeight = allData.myScale.max_x - allData.myScale.min_x;
        System.out.println(allData.myScale.max_x);
        System.out.println(allData.myScale.min_x);
        System.out.println(allData.myScale.max_y);
        System.out.println(allData.myScale.min_y);
    }

    public ArrayList<Double[]> layerToPolygon() {
        ArrayList<Double[]> output = new ArrayList();

        return output;
    }

}

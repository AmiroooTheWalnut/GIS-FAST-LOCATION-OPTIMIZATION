/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GIS3D;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Amir72c
 */
public class AllData implements Serializable {

    static final long serialVersionUID = 3L;
    public int currentNumLavaLayers = 1;
    public Way all_Ways[];
    public LocationNode all_Nodes[];
    public Grid grid[][];
    public double totalDemand = 0;
    public ArrayList all_Layers;
    public Scaling myScale;
    
    public ArrayList<ReportResults> results = new ArrayList();

    public AllData() {
        all_Layers = new ArrayList();
        all_Layers.add(new LayerDefinition("base", "Base"));
    }

    public void setParallelLayers(int numLayers, int resetLayerIndex) {
        currentNumLavaLayers = numLayers;
        if (resetLayerIndex == -1) {
            for (int i = 0; i < all_Nodes.length; i++) {
                all_Nodes[i].lava_value_indexed = new double[numLayers];
                all_Nodes[i].f_value = new double[numLayers];
                all_Nodes[i].g_value = new double[numLayers];
                all_Nodes[i].h_value = new double[numLayers];
                all_Nodes[i].isChecked = new boolean[numLayers];
                all_Nodes[i].isPassed = new boolean[numLayers];
            }
        } else if(resetLayerIndex == -2){
            for (int i = 0; i < all_Nodes.length; i++) {
                all_Nodes[i].lava_value_indexed = new double[numLayers];
                all_Nodes[i].f_value = new double[numLayers];
                all_Nodes[i].g_value = new double[numLayers];
                all_Nodes[i].h_value = new double[numLayers];
                all_Nodes[i].isChecked = new boolean[numLayers];
                all_Nodes[i].isPassed = new boolean[numLayers];
                all_Nodes[i].isBurned = false;
                for(int j=0;j<numLayers;j++)
                {
                    all_Nodes[i].lava_value_indexed[j]=Double.NEGATIVE_INFINITY;
                }
            }
        }else {
            //DENORMALIZE, BREAKS MODULARITY!!!
            double expectedDenormalized = 3;
            //DENORMALIZE, BREAKS MODULARITY!!!
            for (int i = 0; i < all_Nodes.length; i++) {
                all_Nodes[i].lava_value_indexed = new double[numLayers];
                for (int j = 0; j < numLayers; j++) {
                    all_Nodes[i].lava_value_indexed[j] = ((double) all_Nodes[i].layers.get(resetLayerIndex)) * expectedDenormalized;
                }
                all_Nodes[i].f_value = new double[numLayers];
                all_Nodes[i].g_value = new double[numLayers];
                all_Nodes[i].h_value = new double[numLayers];
                all_Nodes[i].isChecked = new boolean[numLayers];
                all_Nodes[i].isPassed = new boolean[numLayers];
            }

        }

    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esmaieeli.gisFastLocationOptimization.GIS3D;

import esmaieeli.gisFastLocationOptimization.Simulation.FacilityLocation;
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
                all_Nodes[i].burntBy= new FacilityLocation[numLayers];
                all_Nodes[i].lava_value_indexed = new double[numLayers];
                all_Nodes[i].f_value = new double[numLayers];
                all_Nodes[i].g_value = new double[numLayers];
                all_Nodes[i].h_value = new double[numLayers];
                all_Nodes[i].isChecked = new boolean[numLayers];
                all_Nodes[i].isPassed = new boolean[numLayers];
                all_Nodes[i].isConnectedToFacility = new boolean[numLayers];
                all_Nodes[i].isBurned = false;
            }
        } else if(resetLayerIndex == -2){
            for (int i = 0; i < all_Nodes.length; i++) {
                all_Nodes[i].burntBy= new FacilityLocation[numLayers];
                all_Nodes[i].lava_value_indexed = new double[numLayers];
                all_Nodes[i].f_value = new double[numLayers];
                all_Nodes[i].g_value = new double[numLayers];
                all_Nodes[i].h_value = new double[numLayers];
                all_Nodes[i].isChecked = new boolean[numLayers];
                all_Nodes[i].isPassed = new boolean[numLayers];
                all_Nodes[i].isConnectedToFacility = new boolean[numLayers];
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
                all_Nodes[i].burntBy= new FacilityLocation[numLayers];
                all_Nodes[i].lava_value_indexed = new double[numLayers];
                for (int j = 0; j < numLayers; j++) {
                    if(all_Nodes[i].layers.get(resetLayerIndex) instanceof NumericLayer)
                    {
                        all_Nodes[i].lava_value_indexed[j] = ((double) all_Nodes[i].layers.get(resetLayerIndex)) * expectedDenormalized;
                    }else{
                        all_Nodes[i].lava_value_indexed[j] = (((short[]) all_Nodes[i].layers.get(resetLayerIndex))[0]) * expectedDenormalized;
                    }
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

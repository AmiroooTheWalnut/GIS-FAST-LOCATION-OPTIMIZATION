/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esmaieeli.gisFastLocationOptimization.Clustering;

import esmaieeli.gisFastLocationOptimization.MathematicalModel.MathematicalSolution;
import esmaieeli.gisFastLocationOptimization.Simulation.FacilityLocation;

/**
 *
 * @author Amir72c
 */
public class Zones {
    public FacilityLocation locations[];
    public int landPriceLayerIndex;
    public int landExistanceLayerIndex;
    public int baseDemandLayerIndex;
    public int tourismGainLayerIndex;
    public int populationDensityLayerIndex;
    public int studentLayerIndex;
    public int luxuryLayerIndex;
    
    public float landPrice[];
    public float landExistance[];
    public float baseDemand[][][];//1-Location Index.2-Capacity.3-Type
    public float tourismGain[][];
    public float transportationCost[];
    
    public MathematicalSolution mySolution;
    
    public Zones(int landPriceLI,int landExistanceLI,int baseDemandLI,int tourismGainLI,int populationDensityLI,int studentLI,int luxLI)
    {
        landPriceLayerIndex=landPriceLI;
        landExistanceLayerIndex=landExistanceLI;
        baseDemandLayerIndex=baseDemandLI;
        tourismGainLayerIndex=tourismGainLI;
        populationDensityLayerIndex=populationDensityLI;
        studentLayerIndex=studentLI;
        luxuryLayerIndex=luxLI;
    }
    
    public void makeZones()
    {
        if(locations==null)
        {
            System.out.println("Locations has not been initialized yet!");
        }else{
            
        }
    }
    
}

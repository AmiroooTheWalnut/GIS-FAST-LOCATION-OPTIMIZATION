/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esmaieeli.gisFastLocationOptimization.ExhustiveSearch;

import esmaieeli.gisFastLocationOptimization.GIS3D.NumericLayer;
import java.awt.Color;

/**
 *
 * @author Amir72c
 */
public class ExhustiveDemand {
    public ExhustiveDemand()
    {
        
    }
    
    public void run()
    {
        NumericLayer outputLayer=new NumericLayer("exhustiveDemand");
        outputLayer.minColor=new Color(0,0,1);
        outputLayer.maxColor=new Color(1,0,0);
        
    }
}

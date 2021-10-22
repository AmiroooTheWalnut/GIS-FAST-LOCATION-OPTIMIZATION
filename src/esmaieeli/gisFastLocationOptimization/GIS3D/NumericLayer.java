/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esmaieeli.gisFastLocationOptimization.GIS3D;

import java.awt.Color;
import java.io.Serializable;

/**
 *
 * @author Amir72c
 */
public class NumericLayer extends LayerDefinition implements Serializable{
    
    static final long serialVersionUID = 3L;
    public LocationNode locationNodes[];
    public long locationIds[];
    public double capacities[];
    public Color maxColor;
    public Color minColor;
    public NumericLayer(String name)
    {
        super("numeric",name);
    }
    
}

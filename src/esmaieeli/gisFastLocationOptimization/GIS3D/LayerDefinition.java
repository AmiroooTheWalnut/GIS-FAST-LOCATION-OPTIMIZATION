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
public class LayerDefinition implements Serializable{
    
    static final long serialVersionUID = 3L;
    
    public String layerName;
    public String myType;//numeric or string category or base or image
    public String categories[];
    public double values[];
    public double maxValue;//for numeric layer
    public double minValue;//for numeric layer
    public Color colors[];
    public boolean isBenefit=true;
    
    public LayerDefinition(String type,String name)
    {
        myType=type;
        layerName=name;
        if("base".equals(myType))
        {
            categories=new String[10];
            categories[0]="motorway";
            categories[1]="trunk";
            categories[2]="primary";
            categories[3]="secondary";
            categories[4]="tertiary";
            categories[5]="unclassified";
            categories[6]="residential";
            categories[7]="service";
            categories[8]="foot";
            categories[9]="unknown";
            values=new double[10];
            values[0]=0.4;
            values[1]=0.8;
            values[2]=1;
            values[3]=1.2;
            values[4]=1.4;
            values[5]=1.6;
            values[6]=1.7;
            values[7]=2;
            values[8]=4;
            values[9]=100;
            colors=new Color[values.length];
            for(int i=0;i<values.length;i++)
            {
                colors[i]=new Color(Color.HSBtoRGB((float)i/(float)values.length-1, 1, 1));
                
//                System.out.println("red: "+colors[i].getRed());
//                System.out.println("green : "+colors[i].getGreen());
//                System.out.println("blue : "+colors[i].getBlue());
            }
            
        }
    }
}

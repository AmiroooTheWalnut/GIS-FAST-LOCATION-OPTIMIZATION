/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GIS3D;

import com.jme3.math.Vector3f;
import java.io.Serializable;

/**
 *
 * @author Amir72c
 */
public class Way implements Serializable {
    
    static final long serialVersionUID = 3L;

    public String id;
    public String type;
    public double typeWeight;
    public boolean isOneWay;
    public double Length;
    public LocationNode myNodes[];
    public Vector3f lines[];
    public float color[];

    Way(String passed_id) {
        id = passed_id;
    }

    public void setTypeWeight() {
        switch (type) {
            case "motorway":
                typeWeight = 0.4;
                break;
            case "trunk":
                typeWeight = 0.8;
                break;
            case "primary":
                typeWeight = 1;
                break;
            case "secondary":
                typeWeight = 1.2;
                break;
            case "tertiary":
                typeWeight = 1.4;
                break;
            case "unclassified":
                typeWeight = 1.6;
                break;
            case "residential":
                typeWeight = 1.7;
                break;
            case "service":
                typeWeight = 2;
                break;
        }
    }

    public void WayLength() {
        Length = Math.sqrt(Math.pow(myNodes[myNodes.length - 1].lat - myNodes[0].lat, 2) + Math.pow(myNodes[myNodes.length - 1].lon - myNodes[0].lon, 2));
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esmaieeli.gisFastLocationOptimization.GIS3D;

import esmaieeli.gisFastLocationOptimization.GUI.MainFrame;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Amir72c
 */
public class Grid implements Serializable {
    
    static final long serialVersionUID = 3L;

    public int x_order;
    public int y_order;
    public double min_x_val;
    public double max_x_val;
    public double min_y_val;
    public double max_y_val;
    public double minAllMixedValue;
    public LocationNode minAllMixedNode;
    public LocationNode myNodes[];
    public boolean isMinAllMixedDetermined=false;
    public String name;
    public ArrayList temporaryNodes = new ArrayList();

    public Grid(int passedx_order, int passedy_order, double passedmin_x_val, double passedmax_x_val, double passedmin_y_val, double passedmax_y_val,String passedname) {
        x_order = passedx_order;
        y_order = passedy_order;
        min_x_val = passedmin_x_val;
        max_x_val = passedmax_x_val;
        min_y_val = passedmin_y_val;
        max_y_val = passedmax_y_val;
        name=passedname;
        //System.out.println(myParent.scale);
        //System.out.println(myParent.center_x);
    }

    public LocationNode detect_nearest_Node(double lat, double lon) {
        int index = -1;
        double min_distance = Double.POSITIVE_INFINITY;
        //System.out.println(myNodes.length);
        for (int i = 0; i < myNodes.length; i++) {
//            System.out.println("------------");
//            System.out.println(lat);
//            System.out.println(lon);
//            System.out.println(myNodes[i].lat);
//            System.out.println(myNodes[i].lon);
//            System.out.println(myNodes[i].myWays.length);
//            System.out.println("------------");
            if (Math.sqrt(Math.pow(myNodes[i].lat - lat, 2) + Math.pow(myNodes[i].lon - lon, 2)) < min_distance && myNodes[i].myWays[0] != null) {
                min_distance = Math.sqrt(Math.pow(myNodes[i].lat - lat, 2) + Math.pow(myNodes[i].lon - lon, 2));
                index = i;
            }
        }
        try {
            return myNodes[index];
        } catch (Exception exp) {
            return null;
        }
    }
}

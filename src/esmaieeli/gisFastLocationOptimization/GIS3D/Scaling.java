/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esmaieeli.gisFastLocationOptimization.GIS3D;

import java.io.Serializable;

/**
 *
 * @author Amir72c
 */
public class Scaling implements Serializable{
    
    static final long serialVersionUID = 3L;

    public LocationNode all_Nodes[];
    double mean_x = 0;
    double mean_y = 0;
    double max_total;
    double min_total;
    public double max_x = Double.NEGATIVE_INFINITY;
    public double min_x = Double.POSITIVE_INFINITY;
    public double max_y = Double.NEGATIVE_INFINITY;
    public double min_y = Double.POSITIVE_INFINITY;
    public float center_x;
    public float center_y;
    public double scale;
    public float x_position;
    public float y_position;

    public Scaling(LocationNode passedNodes[]) {
        all_Nodes = passedNodes;
    }

    public void calculate() {
        
        for (int i = 0; i < all_Nodes.length; i++) {
            mean_x = mean_x + all_Nodes[i].lat;
            mean_y = mean_y + all_Nodes[i].lon;
            if (all_Nodes[i].lat < min_x) {
                min_x = all_Nodes[i].lat;
            }
            if (all_Nodes[i].lat >= max_x) {
                max_x = all_Nodes[i].lat;
            }
            if (all_Nodes[i].lon < min_y) {
                min_y = all_Nodes[i].lon;
            }
            if (all_Nodes[i].lon >= max_y) {
                max_y = all_Nodes[i].lon;
            }
        }
        max_total = Math.max(max_y, max_x);
        min_total = Math.min(min_y, min_x);
        center_x = (float) mean_x / (float) (all_Nodes.length);
        center_y = (float) mean_y / (float) (all_Nodes.length);
        scale = (10000 / (max_total - min_total));
    }
}

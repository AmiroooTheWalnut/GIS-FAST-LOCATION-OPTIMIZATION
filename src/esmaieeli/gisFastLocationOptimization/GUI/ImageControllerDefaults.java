/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esmaieeli.gisFastLocationOptimization.GUI;

/**
 *
 * @author Amir72c
 */
public class ImageControllerDefaults {
    public float xPosition;
    public float yPosition;
    public float width;
    public float height;
    ImageControllerDefaults(MainFramePanel parent)
    {
        xPosition=(float)((parent.allData.myScale.min_x-parent.allData.myScale.center_x)*parent.allData.myScale.scale);
        yPosition=(float)((parent.allData.myScale.min_y-parent.allData.myScale.center_y)*parent.allData.myScale.scale);
        width=(float)((parent.allData.myScale.max_y-parent.allData.myScale.min_y)*parent.allData.myScale.scale);
        height=(float)((parent.allData.myScale.max_x-parent.allData.myScale.min_x)*parent.allData.myScale.scale);
    }
}

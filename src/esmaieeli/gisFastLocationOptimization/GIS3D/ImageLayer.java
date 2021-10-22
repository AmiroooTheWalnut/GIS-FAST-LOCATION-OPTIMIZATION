/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esmaieeli.gisFastLocationOptimization.GIS3D;

import esmaieeli.gisFastLocationOptimization.GUI.ImageControllerDefaults;
import com.jme3.math.Vector2f;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Amir72c
 */
public class ImageLayer extends LayerDefinition implements Serializable{
    
    static final long serialVersionUID = 3L;
    
    public float my_width;
    public float my_height;
    public Vector2f my_position;
    
    
    public byte[] imageInByte;
    //Viewing parentApp;

    public ImageLayer(String name, ImageControllerDefaults imageControllerDefaults, BufferedImage image) {
        super("image",name);
//        System.out.println("here0");
        //parentApp=app;
        my_position=new Vector2f();
        my_position.x=imageControllerDefaults.xPosition;
        my_position.y=imageControllerDefaults.yPosition;
        my_width=imageControllerDefaults.width;
        my_height=imageControllerDefaults.height;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write( image, "jpg", baos );
            baos.flush();
            imageInByte = baos.toByteArray();
            baos.close();
        } catch (IOException ex) {
            Logger.getLogger(ImageLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
	
    }
}
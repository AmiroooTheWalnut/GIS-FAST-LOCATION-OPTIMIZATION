/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esmaieeli.gisFastLocationOptimization.GIS3D;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.util.DefaultInstantiatorStrategy;
import com.jme3.math.Vector3f;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.objenesis.strategy.StdInstantiatorStrategy;
/**
 *
 * @author Amir72c
 */

public class StoreProcessedData implements Serializable {

    static final long serialVersionUID = 3L;
    String file_path;
    ReportResults localReport;

    public void save_allData_serializable(String passed_file_path,AllData allData) {
        for(int i=0;i<allData.all_Nodes.length;i++)
        {
            allData.all_Nodes[i].lava_value_indexed=new double[1];
        }
        file_path=passed_file_path;
        FileOutputStream f_out;
        try {
            f_out = new FileOutputStream(file_path + ".data");
            ObjectOutputStream obj_out;
            try {
                obj_out = new ObjectOutputStream(f_out);
                obj_out.writeObject(allData);
                obj_out.close();
            } catch (IOException ex) {
                Logger.getLogger(StoreProcessedData.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(StoreProcessedData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void save_allData_kryo(String passed_file_path,AllData allData) {
        for(int i=0;i<allData.all_Nodes.length;i++)
        {
            allData.all_Nodes[i].lava_value_indexed=new double[1];
        }
        Kryo kryo = new Kryo();
//        kryo.setInstantiatorStrategy(new Kryo.DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
        kryo.setReferences(true);
        kryo.setInstantiatorStrategy(new DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
//        kryo.register(esmaieeli.gisFastLocationOptimization.GIS3D.AllData.class);
//        kryo.register(java.util.ArrayList.class);
//        kryo.register(esmaieeli.gisFastLocationOptimization.GIS3D.Grid.class);
//        kryo.register(esmaieeli.gisFastLocationOptimization.GIS3D.LocationNode.class);
//        kryo.register(esmaieeli.gisFastLocationOptimization.GIS3D.Way.class);
        kryo.setRegistrationRequired(false);
        Output output;
        try {
            output = new Output(new FileOutputStream(passed_file_path+".bin"));
            kryo.writeObject(output, allData);
            output.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(StoreProcessedData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void save_ways(Way all_Ways[]) {
        FileOutputStream f_out;
        try {
            f_out = new FileOutputStream(file_path + ".way");
            ObjectOutputStream obj_out;
            try {
                obj_out = new ObjectOutputStream(f_out);
                obj_out.writeObject(all_Ways);
            } catch (IOException ex) {
                Logger.getLogger(StoreProcessedData.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(StoreProcessedData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void save_nodes(LocationNode all_Nodes[]) {
        FileOutputStream f_out;
        try {
            f_out = new FileOutputStream(file_path + ".nod");
            ObjectOutputStream obj_out;
            try {
                obj_out = new ObjectOutputStream(f_out);
                obj_out.writeObject(all_Nodes);
            } catch (IOException ex) {
                Logger.getLogger(StoreProcessedData.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(StoreProcessedData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void save_grid(Grid grid[][]) {
        FileOutputStream f_out;
        try {
            f_out = new FileOutputStream(file_path + ".grid");
            ObjectOutputStream obj_out;
            try {
                obj_out = new ObjectOutputStream(f_out);
                obj_out.writeObject(grid);
            } catch (IOException ex) {
                Logger.getLogger(StoreProcessedData.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(StoreProcessedData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void save_vertices(Vector3f vertices[][]) {
        FileOutputStream f_out;
        try {
            f_out = new FileOutputStream(file_path + ".ver");
            ObjectOutputStream obj_out;
            try {
                obj_out = new ObjectOutputStream(f_out);
                obj_out.writeObject(vertices);
            } catch (IOException ex) {
                Logger.getLogger(StoreProcessedData.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(StoreProcessedData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void save_colors(float color[][]) {
        FileOutputStream f_out;
        try {
            f_out = new FileOutputStream(file_path + ".col");
            ObjectOutputStream obj_out;
            try {
                obj_out = new ObjectOutputStream(f_out);
                obj_out.writeObject(color);
            } catch (IOException ex) {
                Logger.getLogger(StoreProcessedData.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(StoreProcessedData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

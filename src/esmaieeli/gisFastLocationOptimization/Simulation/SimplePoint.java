/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esmaieeli.gisFastLocationOptimization.Simulation;

/**
 *
 * @author user
 */
public class SimplePoint {
    public double xIm;
    public double yIm;
    public double xM;
    public double yM;
    public boolean isEnter;
    
    public SimplePoint(double passed_xIm, double passed_yIm){
        xIm=passed_xIm;
        yIm=passed_yIm;
    }
    
}

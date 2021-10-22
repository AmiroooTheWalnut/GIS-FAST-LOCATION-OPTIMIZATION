/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esmaieeli.gisFastLocationOptimization.Clustering;

import com.jme3.math.Vector3f;
import java.awt.Color;

/**
 *
 * @author Amir72c
 */
public class HeuristicFreeLocation implements Comparable{
    public HeuristicFacilityLocation facilityLocation;
    Color color;
    Vector3f location;
    Heuristic myParent;
    float stepSize=0.1f;
    int allMixedLayer;
    public boolean isNew=true;
    
    HeuristicFreeLocation(Heuristic parent,HeuristicFacilityLocation fl,int aML)
    {
        myParent=parent;
        facilityLocation=fl;
        location=facilityLocation.nodeLocation.renderingLocation.clone();
        allMixedLayer=aML;
        facilityLocation.freeLocationParent=this;
    }
    
    @Override
    public int compareTo(Object o) {
        if(facilityLocation.baseDemand[0]>((HeuristicFreeLocation)o).facilityLocation.baseDemand[0])
        {
            return 1;
        }else if(facilityLocation.baseDemand[0]==((HeuristicFreeLocation)o).facilityLocation.baseDemand[0]){
            return 0;
        }else{
            return -1;
        }
    }
    
}

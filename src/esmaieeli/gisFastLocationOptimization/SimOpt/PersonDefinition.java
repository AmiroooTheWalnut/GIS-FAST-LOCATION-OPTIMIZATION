/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esmaieeli.gisFastLocationOptimization.SimOpt;

import esmaieeli.gisFastLocationOptimization.Clustering.Zones;
import esmaieeli.gisFastLocationOptimization.Simulation.FacilityLocation;
import java.util.ArrayList;

/**
 *
 * @author Amir72c
 */
public class PersonDefinition implements Comparable<PersonDefinition> {

    public double objValue;
    public ArrayList<FacilityLocation> decodedPerson = new ArrayList();
    double codedPerson[];
    public Zones myZone;
    
    PersonDefinition()
    {
        
    }
    
    PersonDefinition(PersonDefinition clonedPerson)
    {
        objValue=clonedPerson.objValue;
        decodedPerson=clonedPerson.decodedPerson;
        codedPerson=clonedPerson.codedPerson;
        myZone=clonedPerson.myZone;
    }

    @Override
    public int compareTo(PersonDefinition o) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        if (this.objValue < o.objValue) {
            return -1;
        }
        if (this.objValue == o.objValue) {
            return 0;
        }
        return 1;
    }
}

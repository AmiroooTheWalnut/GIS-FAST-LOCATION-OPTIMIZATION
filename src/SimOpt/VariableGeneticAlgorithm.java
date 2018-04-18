/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SimOpt;

import Clustering.Zones;
import GIS3D.LocationNode;
import GIS3D.NumericLayer;
import GUI.main_frame;
import MathematicalModel.BenchmarkModel;
import Simulation.FacilityLocation;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JTextArea;

/**
 *
 * @author Amir72c
 */
public class VariableGeneticAlgorithm {

    main_frame myParent;
    int numPop;
    int numGen;
    int numElite;
    int numMutate;
    int initLength;
    int numValidCandidates;
    double internalMutateRate = 0.001;
    PersonDefinition pop[];
    ArrayList<LocationNode> validNodes;
    PersonDefinition bestPerson = new PersonDefinition();
    public JTextArea console = new JTextArea();

    /*
     * Percents must be in (0,1) interval float.
     * 
     */
    public VariableGeneticAlgorithm(main_frame parent, int passed_numPop, int passed_numGen, float passed_mutatePercent, float passed_internalMutatePercent, float passed_elitPercent, float passed_initLengthPercentToMaxLength) {
        myParent = parent;
        numPop = passed_numPop;
        numGen = passed_numGen;
        numValidCandidates = myParent.allData.all_Nodes.length;
        validNodes = new ArrayList();
        for (int i = 0; i < myParent.allData.all_Nodes.length; i++) {
            validNodes.add(myParent.allData.all_Nodes[i]);
        }
        internalMutateRate = passed_internalMutatePercent;
        numMutate = (int) (passed_mutatePercent * numPop);
        numElite = (int) (passed_elitPercent * numPop);
        if (numElite < 2) {
            System.out.println("Number of elite members is less than 2!");
        }
        initLength = (int) (passed_initLengthPercentToMaxLength * numValidCandidates);
        pop = new PersonDefinition[numPop];
    }

    public VariableGeneticAlgorithm(main_frame parent, int passed_numPop, int passed_numGen, float passed_mutatePercent, float passed_internalMutatePercent, float passed_elitPercent, float passed_initLengthPercentToMaxLength, String layerName) {
        myParent = parent;
        numPop = passed_numPop;
        numGen = passed_numGen;
        int targetLayer = myParent.findLayer(layerName);
        if (targetLayer < 0) {
            System.out.println("Layer name not found! All nodes are considered!");
            numValidCandidates = myParent.allData.all_Nodes.length;
        }
        if (myParent.allData.all_Layers.get(targetLayer) instanceof NumericLayer) {
            validNodes = new ArrayList();
            for (int i = 0; i < myParent.allData.all_Nodes.length; i++) {
                if ((double) myParent.allData.all_Nodes[i].layers.get(targetLayer) > 0) {
                    validNodes.add(myParent.allData.all_Nodes[i]);
                }
            }
        } else {
            System.out.println("Can not evaluate valid nodes by categorial layer, numeric layer is required. Valid nodes are defined as non-negative nodes.");
        }

        numValidCandidates = validNodes.size();
        numMutate = (int) (passed_mutatePercent * numPop);
        internalMutateRate = passed_internalMutatePercent;
        numElite = (int) (passed_elitPercent * numPop);
        initLength = (int) (passed_initLengthPercentToMaxLength * numValidCandidates);

        pop = new PersonDefinition[numPop];

    }

    public PersonDefinition run(int numCPUs) {
        //TEMPORARY
        initLength=20;
        //TEMPORARY
        BenchmarkModel benchmarkModel = new BenchmarkModel(myParent);
        Zones zones[]=new Zones[numPop];
        int landPriceLayerIndex = myParent.findLayer("landprice");
        int landExistanceLayerIndex = myParent.findLayer("freespace");
        int baseDemandLayerIndex = myParent.findLayer("base");
        int tourismGainLayerIndex = myParent.findLayer("tourismlayer");
        int populationDensityLayerIndex = myParent.findLayer("population_density");
        int studentLayerIndex = myParent.findLayer("studentLayer");
        int luxuryLayerIndex = myParent.findLayer("luxuryLayer");
        for (int i = 0; i < pop.length; i++) {
            pop[i] = mutate();
            zones[i]=new Zones(landPriceLayerIndex, landExistanceLayerIndex, baseDemandLayerIndex, tourismGainLayerIndex, populationDensityLayerIndex, studentLayerIndex, luxuryLayerIndex);
        }
        
        for (int gen = 0; gen < numGen; gen++) {
            for (int i = 0; i < pop.length; i++) {
                System.out.println("SIZE: "+pop[i].decodedPerson.size());
                zones[i].locations=pop[i].decodedPerson.toArray(new FacilityLocation[pop[i].decodedPerson.size()]);

//                pop[i].objValue = benchmarkModel.runExternalFacilities(pop[i].decodedPerson.toArray(new FacilityLocation[pop[i].decodedPerson.size()]),numCPUs);
//                System.out.println("Obj num: "+i+" Evaluated!");
            }
            
            zones = benchmarkModel.runExternalZones(zones, numCPUs);
            System.out.println("Generation "+gen+" evaluated");
            
            for (int i = 0; i < pop.length; i++) {
                pop[i].objValue=zones[i].mySolution.z;
            }
            
            System.out.println("***");
            for(int i=0;i<pop.length;i++)
            {
                System.out.println(pop[i].objValue);
            }
            System.out.println("|||");
            
            Arrays.sort(pop);
            
            System.out.println("|||");
            for(int i=0;i<pop.length;i++)
            {
                System.out.println(pop[i].objValue);
            }
            System.out.println("***");
            System.out.println("BEST PERSON: "+bestPerson.objValue);
            System.out.println("POP[0]: "+pop[0].objValue);
            if(bestPerson.objValue>pop[0].objValue)
            {
                bestPerson=new PersonDefinition(pop[0]);
                System.out.println("Change best person: "+bestPerson.objValue);
            }
            PersonDefinition newPop[] = new PersonDefinition[numPop];
            int counter = 0;
            for (int j = 0; j < numElite; j++) {
                newPop[counter] = pop[j];
                counter = counter + 1;
            }
            for (int j = 0; j < numPop - numMutate - numElite; j++) {
                int fatherIndex = (int) Math.round(Math.random() * numElite);
                int motherIndex = -1;
                while (fatherIndex != motherIndex) {
                    motherIndex = (int) Math.round(Math.random() * numElite);
                }
                newPop[counter] = crossOver(pop[fatherIndex], pop[motherIndex]);
                counter = counter + 1;
            }
            for (int j = 0; j < numMutate; j++) {
                newPop[counter] = mutate();
                counter = counter + 1;
            }
            pop = newPop;

            console.append("Generation number: " + gen + System.lineSeparator());
            console.append("Best obj: " + bestPerson.objValue + System.lineSeparator());
            
        }
        
        benchmarkModel = new BenchmarkModel(myParent);
        Zones finalZones[]=new Zones[1];
        finalZones[0].locations=bestPerson.decodedPerson.toArray(new FacilityLocation[bestPerson.decodedPerson.size()]);
        finalZones[0] = new Zones(landPriceLayerIndex, landExistanceLayerIndex, baseDemandLayerIndex, tourismGainLayerIndex, populationDensityLayerIndex, studentLayerIndex, luxuryLayerIndex);
        benchmarkModel.runExternalZones(finalZones, numCPUs);
        console.append(benchmarkModel.myConsole.getText());
        
        return bestPerson;
        
    }

    public PersonDefinition crossOver(PersonDefinition father, PersonDefinition mother) {
        PersonDefinition output = new PersonDefinition();
        PersonDefinition worsePerson;
        PersonDefinition betterPerson;
        if (father.objValue > mother.objValue) {
            betterPerson = father;
            worsePerson = mother;
        } else {
            betterPerson = mother;
            worsePerson = father;
        }
        double alpha = (((betterPerson.objValue - worsePerson.objValue) / (bestPerson.objValue - worsePerson.objValue)) / 2) + 0.5;
        if (alpha > 0.9) {
            alpha = 0.9;
        }
        PersonDefinition shortPerson;
        PersonDefinition longPerson;
        if (father.codedPerson.length > mother.codedPerson.length) {
            longPerson = father;
            shortPerson = mother;
        } else {
            longPerson = mother;
            shortPerson = father;
        }
        int diffLength = longPerson.codedPerson.length - shortPerson.codedPerson.length;
        int maxLength = longPerson.codedPerson.length + diffLength;
        int minLength = shortPerson.codedPerson.length - diffLength;
        if (minLength < 1) {
            minLength = 1;
        }
        int outputLength = (int) Math.round(Math.random() * (maxLength - minLength) + minLength);
        output.codedPerson = new double[outputLength];
        for (int i = 0; i < output.codedPerson.length; i++) {
            if (Math.random() < internalMutateRate) {
                output.codedPerson[i] = Math.random();
            } else {
                if (Math.random() > alpha) {
                    if (i < betterPerson.codedPerson.length) {
                        output.codedPerson[i] = worsePerson.codedPerson[i];
                    } else {
                        output.codedPerson[i] = betterPerson.codedPerson[i];
                    }
                } else {
                    if (i < betterPerson.codedPerson.length) {
                        output.codedPerson[i] = betterPerson.codedPerson[i];
                    } else {
                        output.codedPerson[i] = worsePerson.codedPerson[i];
                    }
                }
            }
        }
        decode(output);
        return output;
    }

    public PersonDefinition mutate() {
        PersonDefinition output = new PersonDefinition();
        int length = (int) (Math.random() * initLength)+1;
        output.codedPerson = new double[length];
        for (int i = 0; i < length; i++) {
            output.codedPerson[i] = Math.random();
        }
        decode(output);
        return output;
    }

    public void decode(PersonDefinition pd) {
        double copy[] = pd.codedPerson.clone();
        Arrays.sort(copy);
        int lastIndex = (int) (copy[0] * numValidCandidates);
        if (lastIndex == 0) {
            lastIndex = 1;
        }
        pd.decodedPerson.add(new FacilityLocation(myParent, validNodes.get(lastIndex - 1), validNodes.get(lastIndex - 1).myWays[0], 2));
        for (int i = 1; i < copy.length; i++) {
            int newIndex = (int) (copy[i] * numValidCandidates);
            if (newIndex == 0) {
                newIndex = 1;
            }
            if (newIndex != lastIndex) {
                pd.decodedPerson.add(new FacilityLocation(myParent, validNodes.get(newIndex - 1), validNodes.get(newIndex - 1).myWays[0], 2));
                lastIndex = newIndex;
            }
        }
    }
}

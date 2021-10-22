/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esmaieeli.gisFastLocationOptimization.GIS3D;

/**
 *
 * @author Amir72c
 */
public class ParallelPreProcessorNodes extends ParallelProcessor{
    PreProcessor myParent;
    AllData myAllData;
    int myRefinedDataNumber=0;
    ParallelPreProcessorNodes(PreProcessor parent,AllData allData,int startIndex,int endIndex)
    {
        super(startIndex,endIndex);
        myParent=parent;
        myAllData=allData;
        myThread=new Thread(new Runnable() {

            @Override
            public void run() {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                int cumPercent=0;
                System.out.println(myStartIndex);
                System.out.println(myEndIndex);
                for(int i=myStartIndex;i<myEndIndex;i++)
                {
                    myRefinedDataNumber=myParent.parallelInternalProcessNodes(myAllData, i,myRefinedDataNumber);
                    if (((100 * cumPercent) / (myEndIndex-myStartIndex) > 10)) {
                        System.out.println((100 * (i-myStartIndex)) / (myEndIndex-myStartIndex));
                        cumPercent = 0;
                    } else {
                        cumPercent = cumPercent + 1;
                    }
                }
            }
        });
    }
}

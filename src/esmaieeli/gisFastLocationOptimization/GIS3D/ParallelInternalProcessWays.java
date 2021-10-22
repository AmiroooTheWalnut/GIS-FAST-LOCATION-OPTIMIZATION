/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esmaieeli.gisFastLocationOptimization.GIS3D;

/**
 *
 * @author user
 */
public class ParallelInternalProcessWays extends ParallelProcessor{
    PreProcessor myParent;
    AllData myAllData;
    ParallelInternalProcessWays(PreProcessor parent,AllData allData,int startIndex,int endIndex)
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
                    myParent.parallelInternalProcessWays(myAllData, i);
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

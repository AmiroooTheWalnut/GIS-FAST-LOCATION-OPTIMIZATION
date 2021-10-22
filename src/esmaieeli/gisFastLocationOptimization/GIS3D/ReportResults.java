/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esmaieeli.gisFastLocationOptimization.GIS3D;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Amir72c
 */
public class ReportResults implements Serializable {

    static final long serialVersionUID = 3L;
    
    Date date;
    String taskName;
    long startNanoTime;
    long endNanoTime;
    double elapsedTime;
    int numCPUs;
    double startEstimatedRAMUsage;
    double endEstimatedRAMUsage;
    double estimatedRAMUsage;
    String results;
    
    public String shortReportName;

    public ReportResults(Date d, String tn, long snt, long ent, double et, int nc, double seru, double eeru, double eru, String r) {
        date = d;
        taskName = tn;
        startNanoTime=snt;
        endNanoTime=ent;
        elapsedTime=et;
        numCPUs=nc;
        startEstimatedRAMUsage=seru;
        endEstimatedRAMUsage=eeru;
        estimatedRAMUsage=eru;
        results=r;
        shortReportName=taskName+date.toString();
    }

    public String generateResaults() {
        String output=new String();
        output=output+date.toString()+System.lineSeparator();
        output=output+taskName+System.lineSeparator();
        output=output+"Elapsed time(seconds): "+elapsedTime+System.lineSeparator();
        output=output+"Number of CPUs(threads): "+numCPUs+System.lineSeparator();
        output=output+"Estimated RAM usage(MB): "+estimatedRAMUsage/1024.0+System.lineSeparator();
        output=output+results+System.lineSeparator();
        return output;
    }
}

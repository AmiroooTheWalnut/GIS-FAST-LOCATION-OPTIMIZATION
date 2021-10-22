/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esmaieeli.gisFastLocationOptimization.MathematicalModel;

/**
 *
 * @author Amir72c
 */
public class MathematicalSolution {

    public double CH[];
    public double IO[][][];
    public double CAP[][];
    public double TOU[][];
    public double Q[][][];
    public double SA[][][];
    public double IN[][][];
    public double SH[][][];
    public double z;

    MathematicalSolution(int i, int m, int j, int k) {
        CH = new double[i];
        IO = new double[i][k][m];
        CAP = new double[i][j];
        TOU = new double[i][j];

        Q = new double[i][k][m];
        SA = new double[i][k][m];
        IN = new double[i][k][m];
        SH = new double[i][k][m];
    }

    public String generateStringReport() {
        String output = new String();
        output = output + "CH:" + System.lineSeparator();
        for (int i = 0; i < CH.length; i++) {
            output = output + (i+1) + "." + " ";
            output = output + CH[i] + "   ";
        }
        output = output + System.lineSeparator();

        output = output + "IO:" + System.lineSeparator();
        for (int i = 0; i < IO.length; i++) {
            for (int k = 0; k < IO[0].length; k++) {
                for (int m = 0; m < IO[0][0].length; m++) {
                    output = output + (i+1) + "." + (k+1) + "." + (m+1) + " ";
                    output = output + IO[i][k][m] + "   ";
                }
            }
        }
        output = output + System.lineSeparator();

        output = output + "CAP:" + System.lineSeparator();
        for (int i = 0; i < CAP.length; i++) {
            for (int j = 0; j < CAP[0].length; j++) {
                output = output + (i+1) + "." + (j+1) + " ";
                output = output + CAP[i][j] + "   ";
            }
        }
        output = output + System.lineSeparator();
        
        output = output + "TOU:" + System.lineSeparator();
        for (int i = 0; i < TOU.length; i++) {
            for (int j = 0; j < TOU[0].length; j++) {
                output = output + (i+1) + "." + (j+1) + " ";
                output = output + TOU[i][j] + "   ";
            }
        }
        output = output + System.lineSeparator();
        
        output = output + "Q:" + System.lineSeparator();
        for (int i = 0; i < Q.length; i++) {
            for (int k = 0; k < Q[0].length; k++) {
                for (int m = 0; m < Q[0][0].length; m++) {
                    output = output + (i+1) + "." + (k+1) + "." + (m+1) + " ";
                    output = output + Q[i][k][m] + "   ";
                }
            }
        }
        output = output + System.lineSeparator();
        
        output = output + "SA:" + System.lineSeparator();
        for (int i = 0; i < SA.length; i++) {
            for (int k = 0; k < SA[0].length; k++) {
                for (int m = 0; m < SA[0][0].length; m++) {
                    output = output + (i+1) + "." + (k+1) + "." + (m+1) + " ";
                    output = output + SA[i][k][m] + "   ";
                }
            }
        }
        output = output + System.lineSeparator();
        
        output = output + "IN:" + System.lineSeparator();
        for (int i = 0; i < IN.length; i++) {
            for (int k = 0; k < IN[0].length; k++) {
                for (int m = 0; m < IN[0][0].length; m++) {
                    output = output + (i+1) + "." + (k+1) + "." + (m+1) + " ";
                    output = output + IN[i][k][m] + "   ";
                }
            }
        }
        output = output + System.lineSeparator();
        
        output = output + "SH:" + System.lineSeparator();
        for (int i = 0; i < SH.length; i++) {
            for (int k = 0; k < SH[0].length; k++) {
                for (int m = 0; m < SH[0][0].length; m++) {
                    output = output + (i+1) + "." + (k+1) + "." + (m+1) + " ";
                    output = output + SH[i][k][m] + "   ";
                }
            }
        }
        output = output + System.lineSeparator();
        
        output = output + "z:" + System.lineSeparator();
        output = output + z;

        return output;
    }
}

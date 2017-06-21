/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg8hlavolam;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Matus Olejnik <matus.olejnik@gmail.com>
 */
public class LoadData {

    int startArr[][];
    int endArr[][];
    String fileName;
    InputStream fis;
    InputStreamReader isr;
    //BufferedReader raf;
    int linesCount = 0;
    String result;
    int rows;
    int columns;
    RandomAccessFile raf;
    File file;

    public LoadData(String fileName) {
        this.fileName = fileName;
        try {
            file = new File(fileName);
            raf = new RandomAccessFile(file, "r");
        }
        catch (FileNotFoundException ex) {
            Logger.getLogger(LoadData.class.getName()).log(Level.SEVERE, null, ex);
        }
        setLinesCount();
    }

    public void loadData(int start) {
        String line;
        char[] arr;
        rows = 0;
        columns = 0;
        boolean temp = false;

        try {
            for (int m = 0; m < start; m++) {
                raf.readLine();
            }
            //System.out.println("TEMP1");
            line = raf.readLine();
            arr = line.toCharArray();
            int i = 0;

            //get count of rows and columns
            for (i = 0; i < arr.length; i++) {
                if (arr[i] == '(') {
                    rows++;
                    //stlpce staci z prvej zatvorky
                    if (!temp) {
                        i++;
                        while (arr[i] != ')') {
                            if (arr[i] >= '0' && arr[i] <= '9') {
                                columns++;
                            }
                            i++;
                            //System.out.println("TEMP2");
                        }
                        temp = true;
                    }
                }
            }
//            for(int l = 0; l<arr.length;l++)
//                System.out.print(arr[l] + " ");
//            System.out.println("\nrows = " + rows +" columns = " + columns);

            startArr = new int[rows][columns];
            endArr = new int[rows][columns];

            int j = 0;
            int k = 0;
            for (i = 1; i < arr.length; i++) {
                if (arr[i] >= '0' && arr[i] <= '9') {
                    startArr[j][k] = Character.getNumericValue(arr[i]);
                    //System.out.println(arr[i]);
                    k++;
                }
                if (arr[i] == ')') {
                    j++;
                    k = 0;
                    i++;
                }
            }

            line = raf.readLine();
            arr = line.toCharArray();
            j = 0;
            k = 0;
            for (i = 1; i < arr.length; i++) {
                if (arr[i] >= '0' && arr[i] <= '9') {
                    endArr[j][k] = Character.getNumericValue(arr[i]);
                    k++;
                }
                if (arr[i] == ')') {
                    j++;
                    k = 0;
                    i++;
                }
            }
            //result = raf.readLine();
            raf.seek(0);
        }
        catch (IOException ex) {
            Logger.getLogger(LoadData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int setLinesCount() {
        try {
            raf.seek(0);
            while (raf.readLine() != null) {
                linesCount++;
            }
            raf.seek(0);
        }
        catch (IOException ex) {
            Logger.getLogger(LoadData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return linesCount;
    }

    public int getLinesCount() {
        return linesCount;
    }

    public int[][] getStartArr() {
        return startArr;
    }

    public int[][] getEndArr() {
        return endArr;
    }

    public String getResult() {
        return result;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

}

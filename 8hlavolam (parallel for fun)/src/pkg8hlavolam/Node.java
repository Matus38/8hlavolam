/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg8hlavolam;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

/**
 *
 * @author Matus Olejnik <matus.olejnik@gmail.com>
 */
public class Node {

    private int columns;
    private int rows;
    private int depth;

    private int[][] state;
    private String hash;
    private int spacePositionX;
    private int spacePositionY;
    Visited visited;
    Node previousNode;
    public String step = "";

    //x rows y columns
    public Node(int rows, int columns, Visited v) {
        this.columns = columns;
        this.rows = rows;
        state = new int[rows][columns];
        this.visited = v;
    }

    public void setState(int[][] data) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                state[i][j] = data[i][j];
                if (state[i][j] == 0) {
                    spacePositionX = i;
                    spacePositionY = j;
                }
            }
        }
    }

    public ArrayList<Node> expandNode(Hashtable table, Node prev) {
        ArrayList<Node> v = new ArrayList<Node>();

        Node n;

        //zistim ci sa moze posunut hore a dole
        if ((spacePositionX - 1 > -1) && (spacePositionX + 1 < rows)) {
            n = new Node(rows, columns, visited);
            n.setState(state);
            n.swap(spacePositionX, spacePositionY, spacePositionX - 1, spacePositionY);
            n.makeHash();
            if (!(table.containsKey((n.getHash())))) {
                n.setPrevious(prev);
                n.setDepth(this.depth + 1);
                n.step = ("U ");
                //n.setState(tmpA);
                v.add(n);
                table.put(n.getHash(), n);
                //System.out.println("1 " + n.getHash());
                //System.out.println("PREV\n " + n.getPrevious());
            }

            n = new Node(rows, columns, visited);
            n.setState(state);
            n.swap(spacePositionX, spacePositionY, spacePositionX + 1, spacePositionY);
            n.makeHash();
            if (!(table.containsKey((n.getHash())))) {
                n.setPrevious(prev);
                n.setDepth(this.depth + 1);
                n.step = ("D ");
                v.add(n);
                table.put(n.getHash(), n);
                //System.out.println("2 " + n.getHash());
                //System.out.println("PREV \n" + n.getPrevious());
            }
        }
        //zistim ci sa moze posunut dolava a doprava
        if ((spacePositionY - 1 > -1) && (spacePositionY + 1 < columns)) {

            n = new Node(rows, columns, visited);
            n.setState(state);
            n.swap(spacePositionX, spacePositionY, spacePositionX, spacePositionY - 1);
            n.makeHash();
            if (!(table.containsKey((n.getHash())))) {
                n.setPrevious(prev);
                n.setDepth(this.depth + 1);
                n.step = ("L ");
                v.add(n);
                table.put(n.getHash(), n);
                // System.out.println("3 " + n.getHash());
                // System.out.println("PREV\n " + n.getPrevious());
            }

            n = new Node(rows, columns, visited);
            n.setState(state);
            n.swap(spacePositionX, spacePositionY, spacePositionX, spacePositionY + 1);
            n.makeHash();
            if (!(table.containsKey((n.getHash())))) {
                n.setPrevious(prev);
                n.setDepth(this.depth + 1);
                n.step = ("R ");
                v.add(n);
                table.put(n.getHash(), n);
                //System.out.println("4 " + n.getHash());
                //System.out.println("PREV \n" + n.getPrevious());
            }
        }
        //zistim ci sa moze posunut len dole
        if (spacePositionX == 0) {

            n = new Node(rows, columns, visited);
            n.setState(state);
            n.swap(spacePositionX, spacePositionY, spacePositionX + 1, spacePositionY);
            n.makeHash();
            if (!(table.containsKey((n.getHash())))) {
                n.setPrevious(prev);
                n.setDepth(this.depth + 1);
                n.step = ("D ");
                v.add(n);
                table.put(n.getHash(), n);
                //System.out.println("5 " + n.getHash());
                //System.out.println("PREV\n " + n.getPrevious());
            }
        }
        //zsitim ci sa moze posunut len hore
        if (spacePositionX == rows - 1) {

            n = new Node(rows, columns, visited);
            n.setState(state);
            n.swap(spacePositionX, spacePositionY, spacePositionX - 1, spacePositionY);
            n.makeHash();
            if (!(table.containsKey((n.getHash())))) {
                n.setPrevious(prev);
                n.setDepth(this.depth + 1);
                n.step = ("U ");
                v.add(n);
                table.put(n.getHash(), n);
                //System.out.println("6 " + n.getHash());
                //System.out.println("PREV \n" + n.getPrevious());
            }
        }
        //zsitim ci sa moze posunut len doprava
        if (spacePositionY == 0) {

            n = new Node(rows, columns, visited);
            n.setState(state);
            n.swap(spacePositionX, spacePositionY, spacePositionX, spacePositionY + 1);
            n.makeHash();
            if (!(table.containsKey((n.getHash())))) {
                n.setPrevious(prev);
                n.setDepth(this.depth + 1);
                n.step = ("R ");
                v.add(n);
                table.put(n.getHash(), n);
                //System.out.println("7 " + n.getHash());
                //System.out.println("PREV\n " + n.getPrevious());
            }
        }
        //zsitim ci sa moze posunut len dolava
        if (spacePositionY == columns - 1) {

            n = new Node(rows, columns, visited);
            n.setState(state);
            n.swap(spacePositionX, spacePositionY, spacePositionX, spacePositionY - 1);
            n.makeHash();
            if (!(table.containsKey((n.getHash())))) {
                n.setPrevious(prev);
                n.setDepth(this.depth + 1);
                n.step = ("L ");
                v.add(n);
                table.put(n.getHash(), n);
                //System.out.println("8 " + n.getHash());
                // System.out.println("PREV \n" + n.getPrevious());
            }
        }
        return v;
    }
    
    public int getDepth(){
        return depth;
    }
    
    public void setDepth(int depth){
        this.depth = depth;
    }

    public String getHash() {
        return hash;
    }

    //vytvorenie hash kodu pre dany uzol
    public void makeHash() {
        hash = "";
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if(state[i][j] < 10){
                hash += String.valueOf(state[i][j]);
                }
                else{
                    switch (state[i][j]){
                        case 10: hash += "a";
                        break;
                        case 11: hash += "b";
                        break;
                        case 12: hash += "c";
                        break;
                        case 13: hash += "d";
                        break;
                        case 14: hash += "e";
                        break;
                        case 15: hash += "f";
                        break;
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                s += String.valueOf(state[i][j]);
            }
            s += "\n";
        }
        s += "\n";
        return s;
    }

    public void swap(int x, int y, int x2, int y2) {
        int tmp = state[x][y];
        state[x][y] = state[x2][y2];
        state[x2][y2] = tmp;
        spacePositionX = x2;
        spacePositionY = y2;
    }

    public int[][] getState() {
        return state;
    }

    public Node getPrevious() {
        return previousNode;
    }

    public void setPrevious(Node p) {
        this.previousNode = p;
    }

    public void setStep(String s) {
        this.step += s;
    }

    public String getStep() {
        return step;
    }

}

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

    private int[][] state;
    private int index;
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
                System.out.print(data[i][j]);
                state[i][j] = data[i][j];
                if (state[i][j] == 0) {
                    spacePositionX = i;
                    spacePositionY = j;
                }
            }
            System.out.println("");
        }
    }

    public ArrayList<Node> expandNode(Hashtable table, Node prev) {
        ArrayList<Node> v = new ArrayList<Node>();

        Node n;
        int tmpA[][] = new int[rows][columns];
        System.out.println("x = " + spacePositionX + " y = " + spacePositionY);

        if ((spacePositionX - 1 > -1) && (spacePositionX + 1 < rows)) {
            n = new Node(rows, columns, visited);
            n.setState(state);
            n.swap(spacePositionX, spacePositionY, spacePositionX - 1, spacePositionY);
            if (!(table.containsKey((n.getHash())))) {
                n.setPrevious(prev);
                n.step = ("U ");
                //n.setState(tmpA);
                v.add(n);
                table.put(n.getHash(), n);
                System.out.println("1 " + n.getHash());
                System.out.println("PREV\n " + n.getPrevious());
            }

            n = new Node(rows, columns , visited);
            n.setState(state);
            n.swap(spacePositionX, spacePositionY, spacePositionX + 1, spacePositionY);
            if (!(table.containsKey((n.getHash())))) {
                n.setPrevious(prev);
                n.step = ("D ");
                v.add(n);
                table.put(n.getHash(), n);
                System.out.println("2 " + n.getHash());
                System.out.println("PREV \n" + n.getPrevious());
            }
        }
        if ((spacePositionY - 1 > -1) && (spacePositionY + 1 < columns)) {

            n = new Node(rows, columns, visited);
            n.setState(state);
            n.swap(spacePositionX, spacePositionY, spacePositionX, spacePositionY - 1);
            if (!(table.containsKey((n.getHash())))) {
                n.setPrevious(prev);
                n.step = ("L ");
                v.add(n);
                table.put(n.getHash(), n);
                System.out.println("3 " + n.getHash());
                System.out.println("PREV\n " + n.getPrevious());
            }

            n = new Node(rows, columns, visited);
            n.setState(state);
            n.swap(spacePositionX, spacePositionY, spacePositionX, spacePositionY + 1);
            if (!(table.containsKey((n.getHash())))) {
                n.setPrevious(prev);
                n.step = ("R ");
                v.add(n);
                table.put(n.getHash(), n);
                System.out.println("4 " + n.getHash());
                System.out.println("PREV \n" + n.getPrevious());
            }
        }
        if (spacePositionX == 0) {

            n = new Node(rows, columns, visited);
            n.setState(state);
            n.swap(spacePositionX, spacePositionY, spacePositionX + 1, spacePositionY);
            if (!(table.containsKey((n.getHash())))) {
                n.setPrevious(prev);
                n.step = ("D ");
                v.add(n);
                table.put(n.getHash(), n);
                System.out.println("5 " + n.getHash());
                System.out.println("PREV\n " + n.getPrevious());
            }
        }
        if (spacePositionX == rows - 1) {

            n = new Node(rows, columns, visited);
            n.setState(state);
            n.swap(spacePositionX, spacePositionY, spacePositionX - 1, spacePositionY);
            if (!(table.containsKey((n.getHash())))) {
                n.setPrevious(prev);
                n.step = ("U ");
                v.add(n);
                table.put(n.getHash(), n);
                System.out.println("6 " + n.getHash());
                System.out.println("PREV \n" + n.getPrevious());
            }
        }
        if (spacePositionY == 0) {

            n = new Node(rows, columns, visited);
            n.setState(state);
            n.swap(spacePositionX, spacePositionY, spacePositionX, spacePositionY + 1);
            if (!(table.containsKey((n.getHash())))) {
                n.setPrevious(prev);
                n.step = ("R ");
                v.add(n);
                table.put(n.getHash(), n);
                System.out.println("7 " + n.getHash());
                System.out.println("PREV\n " + n.getPrevious());
            }
        }
        if (spacePositionY == columns - 1) {

            n = new Node(rows, columns, visited);
            n.setState(state);
            n.swap(spacePositionX, spacePositionY, spacePositionX, spacePositionY - 1);
            if (!(table.containsKey((n.getHash())))) {
                n.setPrevious(prev);
                n.step = ("L ");
                v.add(n);
                table.put(n.getHash(), n);
                System.out.println("8 " + n.getHash());
                System.out.println("PREV \n" + n.getPrevious());
            }
        }
        return v;
    }

    public String getHash() {
        String hash = "";
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                hash += String.valueOf(state[i][j]);
            }
        }
        return hash;
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

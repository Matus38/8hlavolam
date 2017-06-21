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
public class Search {

    private int depth = 1000;

    private Hashtable visitedNodesStart;
    private Hashtable visitedNodesEnd;
    private ArrayList<Node> startFront;
    private ArrayList<Node> endFront;
    private ArrayList<ArrayList<Node>> tempV;
    Node start, end;
    Visited visited;
    private boolean haveSolution = false;
    Node result;

    public Search(Node start, Node end, Visited table) {
        this.start = start;
        this.end = end;
        start.setPrevious(null);
        end.setPrevious(null);
        //visitedNodes = new Hashtable(100000);
        startFront = new ArrayList<Node>();
        endFront = new ArrayList<Node>();
        tempV = new ArrayList<ArrayList<Node>>();
        this.visited = table;
        visitedNodesStart = new Hashtable(150000);
        visitedNodesEnd = new Hashtable(150000);

    }

    public void startSearch() {
        if (start.getHash().equals(end.getHash())) {
            System.out.println("Start a ciel sa rovnaju, netreba hladat cestu");
            haveSolution = true;
        }
        //visitedNodes.put(start.getHash(), 1);
        //visitedNodes.put(end.getHash(), 1);
        visited.getTable().put(start.getHash(), 1);
        visited.getTable().put(end.getHash(), 1);
        visitedNodesStart.put(start.getHash(), 1);
        visitedNodesEnd.put(end.getHash(), 1);

        startFront.add(start);
        endFront.add(end);

        while (!haveSolution) {
            System.out.println("aa");
            tempV.clear();
            System.out.println(startFront.get(0));
            Node t = startFront.remove(0);
            tempV.add(t.expandNode(visitedNodesStart, t));
            for (int k = 0; k < tempV.get(0).size(); k++) {
                //Node no = (Node) tempV.get(k);
                if (visited.getTable().containsKey(tempV.get(0).get(k).getHash())) {
                    haveSolution = true;
                    System.out.println("V startovej vetve sa nasiel uz navstiveny uzol " + tempV.get(0).get(k).getHash());
                    System.out.println("Predchodca \n" + tempV.get(0).get(k).getPrevious());
                    //System.out.println("Step " + tempV.get(0).get(k).getStep());
                    result = tempV.get(0).get(k);
                    break;
                }
                else {
                    visited.getTable().put((tempV.get(0).get(k)).getHash(), tempV.get(0).get(k));
                    System.out.println("S " + (tempV.get(0).get(k)).getHash());
                }
                startFront.add(tempV.get(0).get(k));
            }
            //startFront.add(tempV.get(0));
            if (!haveSolution) {
                System.out.println("TTTT");
                tempV.clear();
                System.out.println(endFront.get(0));
                Node tt = endFront.remove(0);
                tempV.add(tt.expandNode(visitedNodesEnd, tt));
                for (int k = 0; k < tempV.get(0).size(); k++) {
                    if (visited.getTable().containsKey((tempV.get(0).get(k)).getHash())) {
                        haveSolution = true;
                        System.out.println("V koncovej vetve sa nasiel uz navstiveny uzol " + tempV.get(0).get(k).getHash());
                        System.out.println("Predchodca\n " + tempV.get(0).get(k).getPrevious());
                       // System.out.println("E" + tempV.get(0).get(k).getStep());
                        result = tempV.get(0).get(k);
                        break;
                    }
                    else {
                        visited.getTable().put((tempV.get(0).get(k)).getHash(), tempV.get(0).get(k));
                        System.out.println("K " + (tempV.get(0).get(k)).getHash());
                    }
                    endFront.add(tempV.get(0).get(k));
                }
            }
            // endFront.add(tempV);
        }

        if (haveSolution) {
            String path = "";
            Node act = (Node) visitedNodesStart.get(result.getHash());
            while(act.getPrevious() != null){
                path = act.getStep() + path;
                act = act.getPrevious();
            }
            act = (Node) visitedNodesEnd.get(result.getHash());
            while(act.getPrevious() != null){
                String s = "";
                switch (act.getStep()){
                    case "U ": s= "D ";
                    break;
                    case "D ": s= "U ";
                    break;
                    case "R ": s= "L ";
                    break;
                    case "L ": s= "R ";
                    break;
                }
                
                path =  path + s;
                act = act.getPrevious();
            }
            
            //path = path + act.getStep();
            
            System.out.println(path);
        }
    }

}

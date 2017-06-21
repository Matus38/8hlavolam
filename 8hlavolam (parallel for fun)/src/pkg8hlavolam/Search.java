/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg8hlavolam;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Matus Olejnik <matus.olejnik@gmail.com>
 */
public class Search {

    static final int MAX_DEPTH = 5000;

    /**
     * Semafor sluziaci na synchronizaciu pri zistovani spolocneho navstiveneho
     * uzla
     */
    private final Semaphore mutex = new Semaphore(1);

    private final Semaphore sem = new Semaphore(0);

    /**
     * Hash tabulka ktora uchovava navstivene uzly zo zaciatocneho smeru aby sa
     * negenerovali rovnake
     */
    private Hashtable visitedNodesStart;

    /**
     * Hash tabulka ktora uchovava navstivene uzly z koncoveho smeru aby sa
     * negenerovali rovnake
     */
    private Hashtable visitedNodesEnd;

    /**
     * Fronta naplnana systemom pre prehladavanie do sirky, pre hladanie zo
     * zaciatku
     */
    private ArrayList<Node> startFront;

    /**
     * Fronta naplnana systemom pre prehladavanie do sirky, pre hladanie od
     * konca
     */
    private ArrayList<Node> endFront;

    /**
     * Pomocne pole kam sa docastne ulozia rozvinute uzly
     */
    private ArrayList<ArrayList<Node>> tempV;

    /**
     * Zaciatocny a koncovy uzol
     */
    Node start, end;

    Node result;

    /**
     * Trieda s hash tabulkov pre vsetky navstivene uzly
     */
    Visited visited;

    private boolean haveSolution = false;

    boolean endHasMaxDepth = false;

    public Search(Node start, Node end, Visited table) {
        this.start = start;
        this.end = end;
        start.setPrevious(null);
        end.setPrevious(null);
        startFront = new ArrayList<Node>();
        endFront = new ArrayList<Node>();
        tempV = new ArrayList<ArrayList<Node>>();
        this.visited = table;
        visitedNodesStart = new Hashtable(300000);
        visitedNodesEnd = new Hashtable(300000);
        this.start.makeHash();
        this.start.setDepth(0);
        this.end.makeHash();
        this.end.setDepth(0);
    }

    public void startSearch() {
        EndThread endThread;

        //ak sa zaciatocny a konecny uzol rovnaju netreba hladat cestu
        if (start.getHash().equals(end.getHash())) {
            System.out.println("Start a ciel sa rovnaju, netreba hladat cestu");
            haveSolution = true;
            return;
        }
        else {
            //do hash tabulky navstivenych uzlov pridame zaciatocny aj koncovy 
            visited.getTable().put(start.getHash(), 1);
            visited.getTable().put(end.getHash(), 1);

            //vlozenie uzlov do ich hashtabuliek aby sa nerozvijali stale tie iste uzly
            visitedNodesStart.put(start.getHash(), 1);
            visitedNodesEnd.put(end.getHash(), 1);

            //do zaciatocnej fronty pridame zaciatocny uzol a do koncovej konecny uzol
            startFront.add(start);
            endFront.add(end);

            //vytvorenie a spustenie druheho vlakna ktore bude hladat od konca
            endThread = new EndThread(visited, visitedNodesEnd, result, this, mutex, sem, endFront);
            endThread.start();

            while (!haveSolution && !endHasMaxDepth) {
                //vycistime pomocne pole kde sa pridaju rozvinute uzly
                tempV.clear();

                if (startFront.isEmpty()) {
                    try {
                        sem.acquire();
                        break;
                    }
                    catch (InterruptedException ex) {
                        Logger.getLogger(Search.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                //zobereme prvy uzol z fronty
                Node t = startFront.remove(0);
                //rozvinieme ho
                tempV.add(t.expandNode(visitedNodesStart, t));

                //ak je rozvinuty uzol uz v maximalnej povolenej vetve skoncime hladanie
                if (!tempV.get(0).isEmpty() && tempV.get(0).get(0).getDepth() >= MAX_DEPTH) {
                    //druhemu vlaknu mozme zaklamat ze sme cestu nasli aby sa ukoncilo
                    endThread.setHaveSolution();
                    //netreba dalsia premenna na zastavenie cyklu preto pouzijem tuto
                    endHasMaxDepth = true;
                    break;
                }

                //pre kazdy rozvinuty uzol zistime ci sme taky uz navstivili
                for (int k = 0; k < tempV.get(0).size(); k++) {
                    try {
                        //mutex pre synchronizaciu
                        mutex.acquire();
                        //ak uz dany uzol navstiveny bol mame riesenie
                        if (visited.getTable().containsKey(tempV.get(0).get(k).getHash()) && !endThread.getHaveSolution() && !endHasMaxDepth) {
                            haveSolution = true;
                            System.out.println("V startovej vetve sa nasiel uz navstiveny uzol\n" + tempV.get(0).get(k).toString());
                            //System.out.println("Predchodca \n" + tempV.get(0).get(k).getPrevious());
                            result = tempV.get(0).get(k);
                            mutex.release();
                            endThread.setHaveSolution();
                            break;
                        }
                        //inak ho pridame do navstivenych uzlov a pokracujeme v testovani
                        else {
                            //aby sa nedokoncoval cely for ak druha vetva najde riesenie
                            if (haveSolution || endHasMaxDepth) {
                                break;
                            }
                            visited.getTable().put((tempV.get(0).get(k)).getHash(), tempV.get(0).get(k));
                            mutex.release();
                        }
                        //do frontu pridame uzol ktory budeme neskor rozvijat
                        startFront.add(tempV.get(0).get(k));
                    }
                    catch (InterruptedException ex) {
                        Logger.getLogger(Search.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        //mame riesenie, pomocou udaju o predchadzajucom uzle zistime cestu a vypiseme ju
        if (haveSolution || endThread.getHaveSolution()) {
            String path = "";
            Node act;
            if (visitedNodesStart.get(result.getHash()) != visitedNodesStart.get(start.getHash())) {
                act = (Node) visitedNodesStart.get(result.getHash());
                if (act != null) {
                    while (act.getPrevious() != null) {
                        path = act.getStep() + path;
                        act = act.getPrevious();
                    }
                }
            }
            if (visitedNodesEnd.get(result.getHash()) != visitedNodesEnd.get(end.getHash())) {

                act = (Node) visitedNodesEnd.get(result.getHash());

                while (act.getPrevious() != null) {
                    String s = "";
                    switch (act.getStep()) {
                        case "U ":
                            s = "D ";
                            break;
                        case "D ":
                            s = "U ";
                            break;
                        case "R ":
                            s = "L ";
                            break;
                        case "L ":
                            s = "R ";
                            break;
                    }

                    path = path + s;
                    act = act.getPrevious();
                }
            }

            try {
                //aby sa pripadne dokoncil paralelny proces a vypisy neboli poprehadzovane
                Thread.sleep(100);
            }
            catch (InterruptedException ex) {
                Logger.getLogger(Search.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Zaciatocny stav\n" + start.toString());
            System.out.println("Konecny stav\n" + end.toString());
            System.out.println("Riesenie = " + path);
        }
        else {
            System.out.println("Hladanie sa dostalo do maximalnej povolenej hlbky preto bolo ukoncene");
        }
    }

    public void getStatePath() {
 String path = "";

        Node act;
        if (visitedNodesStart.get(result.getHash()) != visitedNodesStart.get(start.getHash())) {
            act = (Node) visitedNodesStart.get(result.getHash());
            if (act != null) {
                while (act.getPrevious() != null) {
                    path = act.toString() + path;
                    //System.out.println(act.toString());
                    act = act.getPrevious();
                }
            }
        }
        if (visitedNodesEnd.get(result.getHash()) != visitedNodesEnd.get(end.getHash())) {

            act = (Node) visitedNodesEnd.get(result.getHash());

            while (act.getPrevious() != null) {
                //System.out.println(act.toString());
                path = path + act.getPrevious().toString();
                act = act.getPrevious();
            }
        }
        path = start.toString() + path;
        System.out.print(path);
    }

    public void setEndHasMaxDepth(boolean b) {
        this.endHasMaxDepth = true;
    }

    public void setHaveSolution() {
        haveSolution = true;
    }

    public boolean getHaveSolution() {
        return haveSolution;
    }

    public void copyResult(Node copy) {
        this.result = copy;
    }
}

class EndThread implements Runnable {

    private ArrayList<ArrayList<Node>> tempV;
    private ArrayList<Node> endFront;
    private Hashtable visitedNodesEnd;
    boolean haveSolution = false;
    Visited visited;
    Node result;
    Search startThread;
    Semaphore mutex, sem;

    public EndThread(Visited visited, Hashtable visitedNodesEnd, Node result, Search startThread, Semaphore mutex, Semaphore sem, ArrayList<Node> endFront) {
        this.visited = visited;
        this.visitedNodesEnd = visitedNodesEnd;
        this.result = result;
        this.startThread = startThread;
        this.mutex = mutex;
        this.endFront = endFront;
        this.sem = sem;

        tempV = new ArrayList<ArrayList<Node>>();
        endFront = new ArrayList<Node>();
    }

    public void start() {
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {

        while (!haveSolution) {
            tempV.clear();
            //ak je fronta prazdna ukonci sa cyklus a caka sa ci prve vlakno nenajde navstiveny uzol
            if (endFront.isEmpty()) {
                break;
            }
            Node tt = endFront.remove(0);
            tempV.add(tt.expandNode(visitedNodesEnd, tt));

            //ak je vlakno v maximalnej hlbke ukonci sa cyklus ale bez najdeneho riesenia
            if (!tempV.get(0).isEmpty() && tempV.get(0).get(0).getDepth() >= Search.MAX_DEPTH) {
                //startovaciemu vlaknu povieme ze sme v maximalnej hlbke
                startThread.setEndHasMaxDepth(true);
                break;
            }

            //rovnako ako v prvej vetve
            for (int k = 0; k < tempV.get(0).size(); k++) {
                try {
                    mutex.acquire();
                    if (visited.getTable().containsKey((tempV.get(0).get(k)).getHash()) && !startThread.getHaveSolution()) {
                        haveSolution = true;
                        System.out.println("V koncovej vetve sa nasiel uz navstiveny uzol\n" + tempV.get(0).get(k).toString());
                        startThread.copyResult(tempV.get(0).get(k));
                        mutex.release();
                        sem.release();
                        startThread.setHaveSolution();
                        break;
                    }
                    else {
                        //aby sa nedokoncoval cely cyklus ak uz prva vetva nasla riasenie
                        if (haveSolution) {
                            break;
                        }
                        visited.getTable().put((tempV.get(0).get(k)).getHash(), tempV.get(0).get(k));
                        mutex.release();
                    }
                    endFront.add(tempV.get(0).get(k));
                }
                catch (InterruptedException ex) {
                    Logger.getLogger(EndThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        sem.release();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setHaveSolution() {
        haveSolution = true;
    }

    public boolean getHaveSolution() {
        return haveSolution;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg8hlavolam;

import java.util.Hashtable;

/**
 *
 * @author Matus Olejnik <matus.olejnik@gmail.com>
 */
public class Visited {

    private Hashtable visitedNodes;

    public Visited() {
        visitedNodes = new Hashtable(500000);
    }

    public Hashtable getTable() {
        return visitedNodes;
    }

}

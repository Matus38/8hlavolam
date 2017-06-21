/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg8hlavolam;

/**
 *
 * @author Matus Olejnik <matus.olejnik@gmail.com>
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int[][] s = new int[][]{
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8}
        };
        int[][] e = new int[][]{
            {1, 4, 0}, {3, 5, 2}, {6, 7, 8}
            //{3, 1, 2}, {7, 5, 8}, {4, 6, 0}
            //{8, 0, 6}, {5, 4, 7}, {2, 3, 1} //D D R R U U L D D R U U L D L D R R U L L U R R D L L D R U U
           // {3, 4, 5}, {0, 1, 2} //D R R U L D L U R R D L U L D R R U L D L 
        };
        
        Visited v = new Visited();

        Node start = new Node(3, 3, v);
        start.setState(s);
        Node end = new Node(3, 3, v);
        end.setState(e);

        Search search = new Search(start, end, v);
        search.startSearch();
    }

}

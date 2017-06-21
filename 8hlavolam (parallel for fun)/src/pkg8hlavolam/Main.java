/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg8hlavolam;

import java.util.Scanner;

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
            {4, 2, 3, 0}, {5, 10, 6, 1}, {8, 9, 11, 7}, {12, 13, 14, 15}
        };
       int[][] e = new int[][]{
            {1, 2, 3, 0}, {4, 5, 6, 7}, {8, 9, 10, 11}, {12, 13, 14, 15}
        };

        Scanner reader = new Scanner(System.in);
        //LoadData l = new LoadData("C:\\Users\\Matúš\\Documents\\NetBeansProjects\\8hlavolam\\input.txt");
        int startLine = 0;

        while (true) {
            Visited v = new Visited();
            //l.loadData(startLine);

            Node start = new Node(4, 4, v);
            start.setState(s);
            Node end = new Node(4, 4, v);
            end.setState(e);
            
//             Node start = new Node(l.getRows(), l.getColumns(), v);
//            start.setState(l.getStartArr());
//            Node end = new Node(l.getRows(), l.getColumns(), v);
//            end.setState(l.getEndArr());

            Search search = new Search(start, end, v);
            search.startSearch();
            System.out.println("Zobrazit vizualne riesenie? A = ano, Ina klavesa = nie");
            
            if(reader.nextLine().toUpperCase().equals("A")){
                search.getStatePath();
            }
            
            System.out.println("Chcete pokracovat? A = ano, Ina klavesa = koniec");

            if ("A".equals(reader.nextLine().toUpperCase())) {
                startLine += 3;
                //System.out.println("pocet = " + l.getLinesCount());
//                if (startLine >= l.getLinesCount()) {
//                    System.out.println("Nacitali ste vsetky udaje");
//                    break;
//                }
            }
            else {
                break;
            }
        }
    }

}

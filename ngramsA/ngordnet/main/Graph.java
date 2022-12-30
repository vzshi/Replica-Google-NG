package ngordnet.main;

import java.util.ArrayList;

public class Graph {
    private ArrayList<Integer>[] lst_ad;

    public Graph(int vertices) {
        lst_ad = new ArrayList[vertices];
        for (int i = 0; i < vertices; i++) {
            lst_ad[i] = new ArrayList<>();
        }
    }
    public void addEdge(int from, int to) {
        lst_ad[from].add(to);
    }
    public ArrayList<Integer> get(int value) {
        return lst_ad[value];
    }
}

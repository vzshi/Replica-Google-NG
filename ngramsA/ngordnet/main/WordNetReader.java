package ngordnet.main;

import edu.princeton.cs.algs4.In;

import java.util.*;

public class WordNetReader {
    private TreeMap<Integer, TreeSet<String>> strings;
    private Graph graph;
    private int numVer = 0;

    public WordNetReader(String synsets, String hyponyms) {
        In synset = new In(synsets);
        In hyponym = new In(hyponyms);

        strings = new TreeMap<>();

        while (synset.hasNextLine()) {
            String[] thisLine = synset.readLine().split(",");
            Integer lineInt = Integer.parseInt(thisLine[0]);
            strings.put(lineInt, new TreeSet<>());
            String[] mulWords = thisLine[1].split("\\s+");
            for (String word : mulWords) {
                if (!strings.get(lineInt).contains(word)) {
                    strings.get(lineInt).add(word);
                }
            }
        }

        graph = new Graph(strings.lastKey() + 1);

        while (hyponym.hasNextLine()) {
            String[] thisLine = hyponym.readLine().split(",");
            numVer = strings.size();
            for (int j = 1; j < thisLine.length; j++) {
                graph.addEdge(Integer.parseInt(thisLine[0]), Integer.parseInt(thisLine[j]));
            }
        }
    }

    public ArrayList<Integer> find(String str) {
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < numVer; i++) {
            if (strings.get(i).contains(str)) {
                indices.add(i);
            }
        }
        return indices;
    }

    public ArrayList<String> bstTraversal(String str) {
        ArrayList<String> hypos = new ArrayList<>();
        List<Integer> indices = find(str);
        LinkedList<Integer> q = new LinkedList<>(indices);
        for (int i : indices) {
            for (String n : strings.get(i)) {
                if (!hypos.contains(n)) {
                    hypos.add(n);
                }
            }
        }
        while (q.size() > 0) {
            int dequeue = q.pollFirst();
            for (int m : graph.get(dequeue)) {
                q.add(m);
                for (String s : strings.get(m)) {
                    if (!hypos.contains(s)) {
                        hypos.add(s);
                    }
                }
            }
        }
        return hypos;
    }
}

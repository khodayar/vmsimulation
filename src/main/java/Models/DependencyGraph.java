package Models;


import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Khodayar on 1/21/2018.
 */
public class DependencyGraph {
    private Map<VMSet, List<VMSet>> dependencyMap;

    public Map<VMSet, List<VMSet>> getDependencyMap() {
        return dependencyMap;
    }


    public DependencyGraph() {
        dependencyMap = new HashMap<>();
    }

    public void addDependent(VMSet dep, VMSet source) {
        if (dependencyMap.get(dep) == null) {
            List<VMSet> vmSetList = new ArrayList<VMSet>() {{
                add(source);
            }};
            dependencyMap.put(dep, vmSetList);
        } else {

            if (!dependencyMap.get(dep).contains(source)) dependencyMap.get(dep).add(source);
        }

    }


    public void printDependency() {
        for (Map.Entry<VMSet, List<VMSet>> entry : dependencyMap.entrySet()) {
            System.out.print("\n" + entry.getKey() + " --> ");
            System.out.print(entry.getValue());
        }
        System.out.println();
    }




    //return chain between any two this works fine -- update return chain based on this
    //return all the chain except dependant , if add set1, set1 and set1 is there , that's a cycle
    public List<VMSet> chainBetween(VMSet dependant, VMSet destination, List<VMSet> visited, List<VMSet> found) {
        visited.add(dependant);
        if (dependencyMap.get(dependant) != null && dependencyMap.get(dependant).contains(destination)) {
              found.add(destination);
        }
         else if (dependencyMap.get(dependant) != null) {
            dependencyMap.get(dependant).forEach(vmSet -> {
                if (!visited.contains(vmSet)) {
                    int checkSize = found.size();
                    chainBetween(vmSet, destination, visited, found);
                    //we found something ?
                    if (found.size() > checkSize) {
                        found.add(vmSet);
                    }

                }
            });
        }
        return found;
    }


    public List<VMSet> getPath(VMSet dependant, VMSet destination){

        List<VMSet> path =  chainBetween(dependant , destination , new ArrayList<>() ,  new ArrayList<>());

        Collections.reverse(path);
        path.add(0, dependant);
        return path;

    }


    //logic can be used for cycle checking
    public int getDependencyDept(VMSet vmSet , List<VMSet> visited, int weight ,  List<VMSet> longestPath ){
        visited.add(vmSet);
        //weight++;
        if (dependencyMap.get(vmSet)== null || dependencyMap.get(vmSet).isEmpty()){
            System.out.println("reaches one end");
        } else if (dependencyMap.get(vmSet).contains(visited.get(0))){
            weight += 1000;
            //System.out.println("weight:" + weight);
           // System.out.println("visiting" + visited.get(0));
            System.out.println("loop");
        }
        else {

            final int[] finalWeight = {weight};
            dependencyMap.get(vmSet).forEach(set ->{
                if (!visited.contains(set)) {
                    System.out.println("visiting" + set);
                    if (finalWeight[0] < getDependencyDept(set,  visited , finalWeight[0],longestPath)){
                        longestPath.add(set);
                    }
                    finalWeight[0] = getDependencyDept(set,  visited , finalWeight[0],longestPath);
                }

            });
            System.out.println("weight:" + weight + "  " +finalWeight[0]);

            if (finalWeight[0] > weight){
                weight = finalWeight[0];
            }
        }
        return weight;
    }



    //works only with a dependent and a source
    public List<VMSet> returnChain(VMSet dependant, VMSet current) {
        List<VMSet> chain = new ArrayList<>();
        if (dependencyMap.get(current) != null && dependencyMap.get(current).equals(dependant) && !dependencyMap.get(dependant).equals(current)) {
            System.out.println("backward");
            chain = chainBetween(current, dependant, new ArrayList<>(), new ArrayList<>());
        }
        //handling case of pair cycle
        else if (dependencyMap.get(current)!= null && dependencyMap.get(current).equals(dependant) && dependencyMap.get(dependant).equals(current)) {
            chain = chainBetween(dependant, current, new ArrayList<>(), new ArrayList<>());
        }
        else {
            chain = chainBetween(dependant, current, new ArrayList<>(), new ArrayList<>());
        }

        if (chain.size() > 0 && chain.get(chain.size() - 1).equals(current)) {
           // System.out.println("there is chain between " + dependant + "  and " + current);
        }

        //Collections.reverse(chain);
        return chain;
    }




    public boolean isCycle(VMSet dependant, VMSet current) {
        List<VMSet> chain1 = returnChain(dependant, current);
        List<VMSet> chain2 = returnChain(current,dependant);

        if (chain1.size() > 0 && chain1.contains(current) && chain1.contains(dependant) && dependencyMap.get(dependant).contains(current)) {
            return true;
        }
        return false;
    }


    //if any two pm are in a same cycle
    //vsn be be used only if dependent --> current (if current -> dependent without opposite returns true)
    //if we want to check p-->q are in a cycle we must check p,q,visited, found)
    public List<VMSet> chainBetweenold(VMSet dependant, VMSet destination, List<VMSet> visited, List<VMSet> found) {
        visited.add(destination);
        final boolean[] thereIS = {false};
        //final step , return back to dependent
        //stops when reaches the dependant
        if (dependencyMap.get(dependant) != null && dependencyMap.get(dependant).contains(destination)) {
            thereIS[0] = true;
            found.add(dependant);
        }

        if (thereIS[0]) {
            //  found.add(destination);

        } else if (dependencyMap.get(dependant) != null) {
            dependencyMap.get(dependant).forEach(vmSet -> {
                if (!visited.contains(vmSet)) {
                    int checkSize = found.size();
                    chainBetweenold(vmSet, destination, visited, found);
                    //we found something ?
                    if (found.size() > checkSize) {
                        found.add(vmSet);
                    }

                }
            });
        }
        return found;
    }


    public void draw(){
        Graph<String, String> g = new SparseMultigraph<String, String>();

        for (Map.Entry<VMSet, List<VMSet>> entry : dependencyMap.entrySet()) {
            g.addVertex(String.valueOf(entry.getKey()));
           // System.out.print("\n" + entry.getKey() + " --> ");
           // System.out.print(entry.getValue());
        }

//        g.addVertex("a2");
//        g.addVertex("a1");
//        g.addVertex("a3");
//        g.addEdge("a1" , "a2" , "this" , EdgeType.DIRECTED);
        Layout<Integer, String> layout = new CircleLayout(g);
        layout.setSize(new Dimension(800,800));
        BasicVisualizationServer<Integer,String> vv =
                new BasicVisualizationServer<Integer,String>(layout);
        vv.setPreferredSize(new Dimension(850,850));

        JFrame frame = new JFrame("Simple Graph View");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        frame.pack();
        frame.setVisible(true);
    }

}

package Models;


import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;

import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Khodayar on 1/21/2018.
 */
public class DependencyGraph {
    private Map<VMSet, List<VMSet>> dependencyMap;
    private List<ComplexDependency> cmplxDepend;

    public Map<VMSet, List<VMSet>> getDependencyMap() {
        return dependencyMap;
    }


    public DependencyGraph() {
        dependencyMap = new HashMap<>();
        cmplxDepend = new ArrayList<>();
    }

    public List<ComplexDependency> getCmplxDepend() {
        return cmplxDepend;
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

    public VMSet getKeyContaining (VM vm) {
        for (Map.Entry<VMSet, List<VMSet>> entry : dependencyMap.entrySet()) {
            if (entry.getKey().getVMList().contains(vm)) {
                return entry.getKey();
            }
        }
        return null;
    }



    public VMSet getEntryContaining (VM vm) {
        for (Map.Entry<VMSet, List<VMSet>> entry : dependencyMap.entrySet()) {
            if (entry.getValue().get(0).getVMList().contains(vm)) {
                //the entry is a singleton list
                return entry.getValue().get(0);
            }
        }
        return null;
    }

    //return chain between any two this works fine -- update return chain based on this
    //return all the chain except dependant , if add set1, set1 and set1 is there , that's a cycle
    public List<VMSet> chainBetween(VMSet dependant, VMSet destination, List<VMSet> visited, List<VMSet> found) {
        //adds already dependant


        //first here , we must get the correct left side of the dep-map
        visited.add(dependant);
        //if in current loop we reached destination from dependant
        if (dependencyMap.get(dependant) != null && dependencyMap.get(dependant).contains(destination)) {
              found.add(destination);
        }
         else if (dependencyMap.get(dependant) != null) {
            //get the list of VMset in the map, and for each of them checks if they are not visited , tries it
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


    //todo :
    public List<VMSet> chainBetweenOn(VM dependant, VM destination, List<VMSet> visited, List<VMSet> found) {


        VMSet dependantLeft = getKeyContaining(dependant);
        //first here , we must get the correct left side of the dep-map
        if (dependantLeft != null){ visited.add(dependantLeft);}
        //if in current loop we reached destination from dependant
        if (dependantLeft != null && dependencyMap.get(dependantLeft).get(0).getVMList().contains(destination)) {
            VMSet destinationRight = getEntryContaining(destination);
            found.add(destinationRight);
        }
        else if (dependantLeft != null) {
            //get the list of VMset in the map, and for each of them checks if they are not visited , tries it
            dependencyMap.get(dependantLeft).get(0).getVMList().forEach(vm -> {
                if (!visited.contains(getKeyContaining(vm))) {
                    int checkSize = found.size();
                    chainBetweenOn(vm, destination, visited, found);
                    //we found something ?
                    if (found.size() > checkSize) {
                        found.add(getEntryContaining(vm));
                        found.add(getKeyContaining(vm));
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


    public List<VMSet> getPathO(VM dependant, VM destination){

        List<VMSet> path =  chainBetweenOn(dependant , destination , new ArrayList<>() ,  new ArrayList<>());

        if (path.size() > 0) {
            Collections.reverse(path);

            //not sure
            path.add(0, getKeyContaining(dependant));
        }

        //todo here the path is correct, the way we check if there is cycle in the path is not correct
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

}

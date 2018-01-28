package Models;

import java.util.*;

/**
 * Created by Khodayar on 1/21/2018.
 */
public class DependencyGraph {
    private Map<VMSet, List<VMSet>> dependencyMap;


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

    //if any two pm are in a same cycle
    //vsn be be used only if dependent --> current (if current -> dependent without opposite returns true)
    //if we want to check p-->q are in a cycle we must check p,q,visited, found)
    public List<VMSet> chainBetween(VMSet dependant, VMSet destination, List<VMSet> visited, List<VMSet> found) {
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


    //return chain between any two this works fine -- update return chain based on this
    //return all the chain except dependant , if add set1, set1 and set1 is there , that's a cycle
    public List<VMSet> chainBetween2(VMSet dependant, VMSet destination, List<VMSet> visited, List<VMSet> found) {
        visited.add(dependant);
        final boolean[] thereIS = {false};
        if (dependencyMap.get(dependant) != null && dependencyMap.get(dependant).contains(destination)) {
            thereIS[0] = true;
            found.add(destination);
        }

        if (thereIS[0]) {
            //  found.add(destination);

        } else if (dependencyMap.get(dependant) != null) {
            dependencyMap.get(dependant).forEach(vmSet -> {
                if (!visited.contains(vmSet)) {
                    int checkSize = found.size();
                    chainBetween2(vmSet, destination, visited, found);
                    //we found something ?
                    if (found.size() > checkSize) {
                        found.add(vmSet);
                    }

                }
            });
        }
        return found;
    }



    //works only with a dependent and a source
    public List<VMSet> returnChain(VMSet dependant, VMSet current) {
        List<VMSet> chain = new ArrayList<>();
        if (dependencyMap.get(current) != null && dependencyMap.get(current).equals(dependant) && !dependencyMap.get(dependant).equals(current)) {
            System.out.println("backward");
            chain = chainBetween2(current, dependant, new ArrayList<>(), new ArrayList<>());
        }
        //handling case of pair cycle
        else if (dependencyMap.get(current)!= null && dependencyMap.get(current).equals(dependant) && dependencyMap.get(dependant).equals(current)) {
            chain = chainBetween2(dependant, current, new ArrayList<>(), new ArrayList<>());
        }
        else {
            chain = chainBetween2(dependant, current, new ArrayList<>(), new ArrayList<>());
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

}

package Models;

import java.util.*;

/**
 * Created by Khodayar on 1/21/2018.
 */
public class DependencyGraph {
    int numberOFPMs;
    private Map<VMSet , VMSet> dependencyMap;


    public DependencyGraph() {
        dependencyMap = new HashMap<>();
    }

    public void addDependent(VMSet dep , VMSet source){
            dependencyMap.put(dep , source);
    }



    public void printDependency(){
        for (Map.Entry<VMSet,VMSet> entry : dependencyMap.entrySet()) {
            System.out.print( "\n" + entry.getKey() + " --> ");
            System.out.print(entry.getValue());
        }
    }

    //if any two pm are in a same cycle
    //vsn be be used only if dependent --> current (if current -> dependent without opposite returns true)
    //if we want to check p-->q are in a cycle we must check p,q,visited, found)
    public List<VMSet> chainBetween(VMSet dependant , VMSet current , List<VMSet> visited , List<VMSet> found){
            visited.add(current);
            final boolean[] thereIS = {false};
            //final step , return back to dependent
            //stops when reaches the dependant
            if (dependencyMap.get(current) != null && dependencyMap.get(current).equals(dependant)){
                    thereIS[0] = true;
            }

            if (thereIS[0]) {
                found.add(current);

            } else {
                VMSet vmset = dependencyMap.get(current);
                    if (!visited.contains(vmset)) {
                        int checkSize = found.size();
                        chainBetween(dependant, vmset, visited, found);
                        if (found.size() > checkSize){
                            found.add(current);
                     }

                    }
            }
        return found;
    }


    //works only with a dependent and a source
    public List<VMSet> returnCycle(VMSet dependant , VMSet current){
        List<VMSet> chain = new ArrayList<>();
        if (dependencyMap.get(current).equals(dependant) && !dependencyMap.get(dependant).equals(current)){
            System.out.println("backward");
            chain = chainBetween(current, dependant, new ArrayList<>(), new ArrayList<>());
             chain.add(current);


        }
        //handling case of pair cycle
        else if (dependencyMap.get(current).equals(dependant) && dependencyMap.get(dependant).equals(current)){
            chain = chainBetween(dependant, current, new ArrayList<>(), new ArrayList<>());
            chain.add(dependant);
        }

        else {
             chain = chainBetween(dependant, current, new ArrayList<>(), new ArrayList<>());
        }

        if (chain.size() > 0 && chain.get(chain.size() - 1).equals(current)) {
            System.out.println("there is chain between " + current + "  and " + dependant);
            chain.add(0, dependant);
            if (dependencyMap.get(dependant).equals(current)) {
                System.out.println("there is cycle");
            }
        }

        Collections.reverse(chain);
        return chain;
    }


//    public void findPairWise(PM pm1, PM pm2 , List<PM> series){
//        if (dependencyMap.get(pm2).contains(pm1)){
//            series.add(pm2);
//            return;
//        }else {
//
//            dependencyMap.get(pm2).forEach(pm -> {
//                series.add(pm);
//                findPairWise(pm1 , pm , series);
//            });
//        }
//
//    }


}

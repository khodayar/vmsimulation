package Models;

import java.util.*;

/**
 * Created by Khodayar on 1/21/2018.
 */
public class DependencyGraph {
    int numberOFPMs;
    private Map<PM , List<PM>> dependencyMap;

    public DependencyGraph(List<PM> pms) {
        dependencyMap = new HashMap<>();
        pms.forEach(pm -> dependencyMap.put(pm , new ArrayList<>()));
    }

    public void addDependent(PM dependent , PM destination){
            dependencyMap.get(dependent).add(destination);
    }



    public void printDependency(){
        for (Map.Entry<PM,List<PM>> entry : dependencyMap.entrySet()) {
            System.out.print( "\n" + "Key = " + entry.getKey().getName());
            if (entry.getValue().size() > 0){
                System.out.print("  dependent to");
                entry.getValue().forEach(pm -> System.out.print(" " + pm.getName()));
            }
        }

    }

    //if any two pm are in a same cycle
    //vsn be be used only if dependent --> current (if current -> dependent without opposite returns true)
    //goes backward of dependencies - reverse order of dependencies
    //if we want to check p-->q are in a cycle we must check p,q,visited, found)
    public List<PM> chainBetween(PM dependant , PM current , List<PM> visited , List<PM> found){
            visited.add(current);
            final boolean[] thereIS = {false};
            //final step , return back to dependent
            dependencyMap.get(current).forEach(pm -> {
                if (pm.equals(dependant)) {
                    thereIS[0] = true;
                }
            });
            if (thereIS[0]) {
                found.add(current);

            } else {
                dependencyMap.get(current).forEach(pm -> {
                    if (!visited.contains(pm)) {
                        int checkSize = found.size();
                        chainBetween(dependant, pm, visited, found);
                        if (found.size() > checkSize){
                            found.add(current);
                        }

                    }
                });

            }
        return found;
    }

    public void returnCycle(PM dependant , PM current){
        if (dependencyMap.get(current).contains(dependant)){
            System.out.println("backward");
        }

        List<PM> chain = chainBetween(dependant , current , new ArrayList<>() , new ArrayList<>());

        if (chain.get(chain.size() - 1).equals(current)){
            System.out.println("there is chain from " + current.getName() + "  to " + dependant.getName());

            if (dependencyMap.get(dependant).contains(current)){
                System.out.println("there is cycle");
                chain.add(0 , dependant);
                System.out.println(chain);
            }
            else {
                System.out.println(chain);
            }
        }

    }


    public void findPairWise(PM pm1, PM pm2 , List<PM> series){
        if (dependencyMap.get(pm2).contains(pm1)){
            series.add(pm2);
            return;
        }else {

            dependencyMap.get(pm2).forEach(pm -> {
                series.add(pm);
                findPairWise(pm1 , pm , series);
            });
        }

    }



}

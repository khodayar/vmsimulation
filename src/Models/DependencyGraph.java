package Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    public boolean isLastChain(PM dependant , PM current ,  List<PM> visited){
        visited.add(current);
        final boolean[] thereIS = {false};
        dependencyMap.get(current).forEach(pm -> {
            if (pm.equals(dependant)){
                thereIS[0] =  true;

            }
        });
        if (thereIS[0]){
            return true;
        }
        dependencyMap.get(current).forEach(pm -> {
            if (!visited.contains(pm)){
                isLastChain(dependant , pm ,  visited);
            }
        });
        return false;

    }


    public void findPairWise(PM pm1, PM pm2 , List<PM> series){
        if (dependencyMap.get(pm2).contains(pm1)){
            series.add(pm2);
            return;
        }else {
            series.remove(pm2);
            dependencyMap.get(pm2).forEach(pm -> {
                series.add(pm);
                findPairWise(pm1 , pm , series);
            });
        }

    }



}

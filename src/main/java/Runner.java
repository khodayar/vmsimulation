import Models.*;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Runner {

    public static void main(String[] args) throws FileNotFoundException {



        Cloud current = new Cloud();
        SetUp.initialAllocation(current);
        current.showAssignments();

        /*
        current.addOptimalPlacement(4 , 6);
        current.assignRndNewLocations();
        */

        //end of setting up the network

        current.displayCloudInfo();
        current.showAssignments();


        DependencyGraph dependencyGraph;


        dependencyGraph = current.generateDependencyGraph(current.generateMigrations());

        //it will fill the list of migrations for the network
        current.generateMigrations();

//        //setting default migration weights
        current.setMigrationWeights(current.getMigrations());

        List<Migration> migrationsOFCurrent = current.getMigrations();

        System.out.println("migration with initial weights :");
        Collections.sort(current.getMigrations(), new Comparator<Migration>() {
            @Override
            public int compare(Migration o1, Migration o2) {
                return o2.getWeight() - o1.getWeight();
            }
        });
        System.out.println(current.getMigrations());




//
        dependencyGraph.printDependency();

        current.drawComplex(dependencyGraph);


        current.solveCycles();
//
//
//


        dependencyGraph = current.generateDependencyGraph(current.getMigrations());
        current.drawComplex(dependencyGraph);
//        //loop for cycles
//        current.setDependencyWeights(current.getMigrations());
//        System.out.println("migration with dependency weights :");
//        System.out.println(current.getMigrations());
//        //   System.out.println("Dependencies :");
//        dependencyGraph.printDependency();
//        //  System.out.println(dependencyGraph.getPath(set2 , set2));
//        //  System.out.println(dependencyGraph.getPath(set5 , set5));
//        //  System.out.println(current.getMigrations());
//        //   System.out.println(current.getAllOutGoingSets(current.generateMigrations()));
//        // System.out.println(dependencyGraph.getDependencyDept(set3 ,  new ArrayList<>(), 0,new ArrayList<>()));
//        // System.out.println(dependencyGraph.returnChain(set1 , set4));
//        //System.out.println(dependencyGraph.getPath(set1 , set5));
//
//        MigrationProcess migrationProcess = new MigrationProcess();
//        migrationProcess.setPipelineDegree(4);
//        migrationProcess.setLinkDegree(2);
//        migrationProcess.setCloud(current);
//        try {
//            migrationProcess.doMigration();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


    }
}

import Models.*;

import java.util.ArrayList;
import java.util.List;

public class Runner {

    public static void main(String[] args) {

        Network current = new Network();
        current.addOptimalPlacement(8 , 10);
        current.assignRndNewLocations();

        //end of setting up the network

        current.showAssignments();


        DependencyGraph dependencyGraph;


        dependencyGraph = current.generateDependencyGraph(current.generateMigrations());

        //it will fill the list of migrations for the network
        current.generateMigrations();

//        //setting default migration weights
        current.setMigrationWeights(current.getMigrations());

        List<Migration> migrationsOFCurrent = current.getMigrations();

        System.out.println(current.getMigrations());
//
        dependencyGraph.printDependency();
//
        current.solveCycles();
//
//
//        //
        dependencyGraph = current.generateDependencyGraph(current.getMigrations());
        //loop for cycles
        current.setDependencyWeights(current.getMigrations());
        //   System.out.println("Dependencies :");
        dependencyGraph.printDependency();
        //  System.out.println(dependencyGraph.getPath(set2 , set2));
        //  System.out.println(dependencyGraph.getPath(set5 , set5));
        //  System.out.println(current.getMigrations());
        //   System.out.println(current.getAllOutGoingSets(current.generateMigrations()));
        // System.out.println(dependencyGraph.getDependencyDept(set3 ,  new ArrayList<>(), 0,new ArrayList<>()));
        // System.out.println(dependencyGraph.returnChain(set1 , set4));
        //System.out.println(dependencyGraph.getPath(set1 , set5));

        MigrationProcess migrationProcess = new MigrationProcess();
        migrationProcess.setDegree(2);
        migrationProcess.setNetwork(current);
        try {
            migrationProcess.doMigration();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}

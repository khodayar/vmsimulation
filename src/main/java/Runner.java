import Models.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Runner {

    public static void main(String[] args) throws Exception {

        Cloud current = new Cloud();
        //we can use this function to read the set up and current and new placements from setup.txt
        SetUp.readSetUp(current);
        //  alternative way to set up , create an optimal new assignment and a random current
        //DataGenerator.setUpCloud(5 , 20, 1, 80 , current);

        //end of setting up the network

        current.displayCloudInfo();
        current.showAssignments(false);

        DependencyGraph dependencyGraph;
        dependencyGraph = current.generateOnoueDependencyGraph(current.generateMigrations());

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

        dependencyGraph.printDependency();
        //****  two option to draw the dependency graph, based on VMs and based on PMs

       current.draw(dependencyGraph);
      // current.drawComplexGraph(dependencyGraph);

       current.showCycles();

       current.solveCycles();

        //recreate dependency graph after solving the deadlocks
       dependencyGraph = current.generateOnoueDependencyGraph(current.getMigrations());

       current.draw(dependencyGraph);



        current.setDependencyWeights(current.getMigrations());
        System.out.println("migration with dependency weights :");
        System.out.println(current.getMigrations());
        dependencyGraph.printDependency();

        MigrationProcess migrationProcess = new MigrationProcess();
        migrationProcess.setPipelineDegree(2);
        migrationProcess.setLinkDegree(2);
        migrationProcess.setCloud(current);
        try {
            migrationProcess.doMigration();
        } catch (Exception e) {
            e.printStackTrace();
        }

        current.showAssignments(true);


    }
}

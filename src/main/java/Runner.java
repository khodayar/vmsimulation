import Models.*;

import java.util.Collections;
import java.util.Comparator;

public class Runner {

    public static void main(String[] args) throws Exception {

        Cloud current = new Cloud();

        //read Charles's version
       //CsvReader.readFile(current , "src/main/Feed/out_inst_100_CONS-20-80_50_80-85.csv");
        //we can use this function to read the set up and current and new placements from setup.txt

          SetUp.readSetUp(current);
        //  alternative way to set up , create an optimal new assignment and a random current
        //DataGenerator.setUpCloud(5 , 20, 1, 80 , current);

        //end of setting up the network

        current.displayCloudInfo();
        current.showAssignments(false);

        DependencyGraph dependencyGraph;

        System.out.println("Onoue dependency graph");
        dependencyGraph = current.generateOnoueDependencyGraph(current.generateMigrations());
        dependencyGraph.printDependency();

        current.showCyclesO(dependencyGraph);

 /*
        System.out.println("old dependency graph");
        dependencyGraph = current.generateDependencyGraph(current.generateMigrations());
        dependencyGraph.printDependency();

//        //setting default migration weights
        current.setMigrationTimes(current.getMigrations());


        System.out.println("Number of Migrations :" + current.getMigrations().size());

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

       current.showCycles(dependencyGraph);


*/
       // current.draw(dependencyGraph);

        current.setMigrationTimes(current.getMigrations());
        System.out.println(current.getMigrations());
       //current.setDependencyWeightsO(current.getMigrations());
        System.out.println(current.getMigrations());

        current.solveCycles();

       /*


        //recreate dependency graph after solving the deadlocks
     //  dependencyGraph = current.generateOnoueDependencyGraph(current.getMigrations());

       current.draw(dependencyGraph);



      //  current.setDependencyWeights(current.getMigrations());
      */

      //  System.out.println("migration with dependency weights :");
      //  System.out.println(current.getMigrations());


        //dependencyGraph.printDependency();

        MigrationProcess migrationProcess = new MigrationProcess();
        migrationProcess.setPipelineDegree(1000);
        migrationProcess.setLinkDegree(1000);
        migrationProcess.setCloud(current);
        try {
            migrationProcess.doMigration();
        } catch (Exception e) {
            e.printStackTrace();
        }

        current.showAssignments(true);



    }
}

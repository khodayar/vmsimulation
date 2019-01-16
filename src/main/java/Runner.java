import Models.*;

import java.io.File;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;

public class Runner {

    public static void main(String[] args) throws Exception {
//        List<String> files = new ArrayList<>();
//
//
//        readNestedFiles("D:\\google drive\\vm migration\\generator\\dataset_small-x" , files);
//            //more code
//
//            files.forEach(file -> {
//                try {
//                    CsvWriter.addReportToCsv(runTheFile(file));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            });


        String  folderPath = "D:\\google drive\\vm migration\\generator\\datasets_50_100";
        File folder = new File(folderPath);
        File[] listOfFolders = folder.listFiles();

        for (File directory : listOfFolders) {


            List<String> files = new ArrayList<>();

            File[] innerFolders = directory.listFiles();
            for (File innerfolder : innerFolders ) {

                if (innerfolder.isDirectory()) {
                    files.add(innerfolder.listFiles()[1].toString());
                }
            }

            final boolean[] firsLine = {true};
           files.forEach(file -> {
                try {
                    CsvWriter.addReportToCsv(runTheFile(file) , directory.getName() , firsLine[0]);
                    firsLine[0] = false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        }




    }

    private static void readFiles(String folderPath , List<String> files) {

        File folder = new File(folderPath);
        File[] listOfFiles = folder.listFiles();

        for (File directory : listOfFiles) {

             if (directory.isDirectory()){
                files.add(directory.listFiles()[1].toString());
            }
        }

    }


    private static void readNestedFiles(String folderPath , List<String> files) {

        File folder = new File(folderPath);
        File[] listOfFolders = folder.listFiles();

        for (File directory : listOfFolders) {

            File[] innerFolders = directory.listFiles();
            for (File innerfolder : innerFolders ) {

                if (innerfolder.isDirectory()) {
                    files.add(innerfolder.listFiles()[1].toString());
                }
            }
        }

    }


    public static Report runTheFile(String filePath){



        Cloud current = new Cloud();

        //read Charles's version
         CsvReader.readFile(current , filePath);
        System.out.println("Start of file" +  filePath);

        //out_inst_100_CONS-20-80_50_90-95.csv
        //out_inst_100_CONS-20-80_50_80-85.csv
        //we can use this function to read the set up and current and new placements from setup.txt

        //  SetUp.readSetUp(current);
        //  alternative way to set up , create an optimal new assignment and a random current
        //DataGenerator.setUpCloud(5 , 20, 1, 80 , current);

        //end of setting up the network

        // current.displayCloudInfo();
        // current.showAssignments(false);

        DependencyGraph dependencyGraph;

       // System.out.println("Onoue dependency graph");
        dependencyGraph = current.generateOnoueDependencyGraph(current.generateMigrations());
        //  dependencyGraph.printDependency();

        //  current.showCyclesO(dependencyGraph);

//        current.getVMsWithoutOutEdges(dependencyGraph).forEach(vm->{
//            System.out.println(vm);
//        });

        //current.draw(dependencyGraph);

//        System.out.println("old dependency graph");
//        dependencyGraph = current.generateDependencyGraph(current.generateMigrations());
//        dependencyGraph.printDependency();

//        //setting default migration weights
        current.setInitialMigrationTimes(current.getMigrations());
        current.setDependencyWeightsO(dependencyGraph);
        System.out.println(current.getMigrations());

/*

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
 /*
        current.setInitialMigrationTimes(current.getMigrations());
        System.out.println(current.getMigrations());
        current.setDependencyWeightsO(current.getMigrations());
        System.out.println(current.getMigrations());

      //  current.solveCycles();




        //recreate dependency graph after solving the deadlocks
     //  dependencyGraph = current.generateOnoueDependencyGraph(current.getMigrations());

       current.draw(dependencyGraph);



      //  current.setDependencyWeights(current.getMigrations());


      //  System.out.println("migration with dependency weights :");
      //  System.out.println(current.getMigrations());


        //dependencyGraph.printDependency();
*/


        //  System.out.println(current.getMigrations());
//        System.out.println("-------solving-----------");
//        current.solveCyclesOn(current.detectCyclesO(dependencyGraph) , dependencyGraph);
//
//        dependencyGraph = current.generateOnoueDependencyGraph(current.getMigrations());
//
//
//        System.out.println("------------independant migration-----");
//        current.getVMsWithoutOutEdges(dependencyGraph).forEach(vm->{
//            System.out.println(vm);
//        });
//
//        System.out.println("-------------------------------");
//        System.out.println(current.getMigrations());
//
//        System.out.println("-------------------------------");
        System.out.println("-------------------------------process-------------"+ filePath);
        MigrationProcess migrationProcess = new MigrationProcess();
        migrationProcess.setPipelineDegree(1000);
        migrationProcess.setLinkDegree(1000);
        migrationProcess.setCloud(current);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        String timestamp = sdf.format(new Date());
        current.getReport().setTimeStampMigStart(timestamp);

        try {
            migrationProcess.OnCcDoMigrations(dependencyGraph);
        } catch (Exception e) {
            e.printStackTrace();
        }

        current.showAssignments(false);


       return current.getReport();


    }

}

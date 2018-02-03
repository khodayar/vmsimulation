import Models.*;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        Network current = new Network();

        PM pm1 = new PM("pm1", 10, 10, 10);
        PM pm2 = new PM("pm2", 15, 15, 20);
        PM pm3 = new PM("pm3", 20, 20, 20);
        PM pm4 = new PM("pm4", 10, 10, 10);
        PM pm5 = new PM("pm5", 10, 10, 10);
        PM pm6 = new PM("pm6", 10, 10, 10);


        VM vm1 = new VM("vm1", 3, 4, 2);
        VM vm2 = new VM("vm2", 6, 2, 2);
        VM vm3 = new VM("vm3", 3, 7, 3);
        VM vm4 = new VM("vm4", 2, 4, 2);
        VM vm5 = new VM("vm5", 1, 2, 2);
        VM vm6 = new VM("vm6", 4, 2, 4);
        VM vm7 = new VM("vm7", 5, 4, 3);
        VM vm8 = new VM("vm8", 3, 6, 5);
        VM vm9 = new VM("vm9", 3, 2, 2);



        current.getPmList().add(pm1);
        current.getPmList().add(pm2);
        current.getPmList().add(pm3);
        current.getPmList().add(pm4);
        current.getPmList().add(pm5);
        current.getPmList().add(pm6);

        try {
            current.assignToCurrentLocation(vm1, pm1);
            current.assignToCurrentLocation(vm2, pm1);
            current.assignToCurrentLocation(vm3, pm2);
            current.assignToCurrentLocation(vm4, pm2);
            current.assignToCurrentLocation(vm5, pm3);
            current.assignToCurrentLocation(vm6, pm3);
            current.assignToCurrentLocation(vm7, pm3);
            current.assignToCurrentLocation(vm8, pm3);
            current.assignToCurrentLocation(vm8, pm3);
            current.assignToCurrentLocation(vm9, pm3);
        } catch (Exception e) {
            e.printStackTrace();
        }



        try {
        current.assignToNewLocation(vm1, pm2);
        current.assignToNewLocation(vm2, pm2);
        current.assignToNewLocation(vm3, pm3);
        current.assignToNewLocation(vm4, pm3);
        current.assignToNewLocation(vm5, pm1);
        current.assignToNewLocation(vm6, pm1);
        current.assignToNewLocation(vm7, pm5);
        current.assignToNewLocation(vm8, pm5);
        current.assignToNewLocation(vm9, pm6);

        } catch (Exception e) {
            e.printStackTrace();
        }

        current.showAssignments();


        DependencyGraph dependencyGraph;
        List<Migration> migrationList = current.getMigrations();

        // System.out.println(current.getOutgoingVmsFrom(migrationList , pm1));
        // System.out.println(current.getVMGoingFromTo(migrationList , pm2, pm1));

        //loop over pms , check if there is any locked migratuion add to set and stop current iteration
        //i think it has repated dependencie

        dependencyGraph = current.generateDependencyGraph(current.getMigrations());
        List<Migration> migrations = current.getMigrations();

        current.setMigrationWeights(migrations);

        System.out.println(migrations);

        current.setDependencyWeights(migrations);

        //System.out.println("Dependencies :");
        dependencyGraph.printDependency();

        System.out.println(migrations);

        VMSet set1 = new VMSet();
        VMSet set2 = new VMSet();
        VMSet set3 = new VMSet();
        VMSet set4 = new VMSet();
        VMSet set5 = new VMSet();

        set1.add(vm1);
        set1.add(vm2);

        set2.add(vm3);
        set2.add(vm4);

        set3.add(vm5);
        set3.add(vm6);

        set4.add(vm7);
        set4.add(vm8);

        set5.add(vm9);


     //   System.out.println(current.getAllOutGoingSets(current.getMigrations()));

        // System.out.println(dependencyGraph.getDependencyDept(set3 ,  new ArrayList<>(), 0,new ArrayList<>()));
        // System.out.println(dependencyGraph.returnChain(set1 , set4));


        //System.out.println(dependencyGraph.getPath(set1 , set5));

//        MigrationProcess migrationProcess = new MigrationProcess();
//        migrationProcess.setDegree(2);
//        migrationProcess.setNetwork(current);
//        try {
//            migrationProcess.doMigration();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//

        //System.out.println(dependencyGraph.isCycle(set1, set3));


    }
}

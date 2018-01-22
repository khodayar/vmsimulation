import Models.*;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        Network current = new Network();

        PM pm1 = new PM("pm1", 10,10,10);
        PM pm2 = new PM("pm2", 10,10,10);
        PM pm3 = new PM("pm3" ,10,10,10);
        PM pm4 = new PM("pm4", 10,10,10);
        PM pm5 = new PM("pm5", 10,10,10);
        PM pm6 = new PM("pm6" ,10,10,10);

        current.getPmList().add(pm1);
        current.getPmList().add(pm2);
        current.getPmList().add(pm3);
        current.getPmList().add(pm4);
        current.getPmList().add(pm5);
        current.getPmList().add(pm6);
//
//
//
//        VM vm1 = new VM("vm1", 3,4,2);
//        VM vm2 = new VM("vm2",6,2,2);
//        VM vm3 = new VM("vm3", 3,7,3);
//        VM vm4 = new VM("vm4", 2,4,2);
//        VM vm5 = new VM("vm5",1,2,2);
//        VM vm6 = new VM("vm6", 4,2,4);
//        VM vm7 = new VM("vm7", 5,4,3);
//        VM vm8 = new VM("vm8",3,6,5);
//        VM vm9 = new VM("vm9", 3,7,2);
//
//        current.assignToLocation(vm1 , pm1);
//        current.assignToLocation(vm2 , pm1);
//        current.assignToLocation(vm3 , pm3);
//        current.assignToLocation(vm4 , pm6);
//        current.assignToLocation(vm5 , pm5);
//        current.assignToLocation(vm6 , pm5);
//        current.assignToLocation(vm7 , pm2);
//        current.assignToLocation(vm8 , pm4);
//        current.assignToLocation(vm9 , pm6);
//
//        current.showAssignments();
//
//        Network newNetwork = new Network();
//
//        newNetwork.setPmList(current.getPmList());
//
//        newNetwork.assignToLocation(vm1 , pm2);
//        newNetwork.assignToLocation(vm2 , pm1);
//        newNetwork.assignToLocation(vm3 , pm3);
//        newNetwork.assignToLocation(vm4 , pm6);
//        newNetwork.assignToLocation(vm5 , pm3);
//        newNetwork.assignToLocation(vm6 , pm2);
//        newNetwork.assignToLocation(vm7 , pm4);
//        newNetwork.assignToLocation(vm8 , pm4);
//        newNetwork.assignToLocation(vm9 , pm1);
//
//
//        newNetwork.showAssignments();
//
//       List<Migration> migrations = current.getMigrations(current , newNetwork);
//       migrations.forEach(migration -> {
//           System.out.println(migration.getVm().getName() + " from  " + migration.getSource().getName() + "  to :" + migration.getDestination().getName());
//       });


        DependencyGraph dependencyGraph = new DependencyGraph(current.getPmList());
        dependencyGraph.addDependent(pm1 , pm2);
        dependencyGraph.addDependent(pm2 , pm3);
        dependencyGraph.addDependent(pm3 , pm4);
        dependencyGraph.addDependent(pm4 , pm5);
        dependencyGraph.addDependent(pm5 , pm1);
        dependencyGraph.addDependent(pm6 , pm1);



        dependencyGraph.returnCycle(pm4,pm5);


        dependencyGraph.printDependency();


    }
}

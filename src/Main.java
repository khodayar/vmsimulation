import Models.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        Network current = new Network();

        PM pm1 = new PM("pm1", 10, 10, 10);
        PM pm2 = new PM("pm2", 10, 10, 10);
        PM pm3 = new PM("pm3", 10, 10, 10);
        PM pm4 = new PM("pm4", 10, 10, 10);
        PM pm5 = new PM("pm5", 10, 10, 10);
        PM pm6 = new PM("pm6", 10, 10, 10);

        current.getPmList().add(pm1);
        current.getPmList().add(pm2);
        current.getPmList().add(pm3);
        current.getPmList().add(pm4);
        current.getPmList().add(pm5);
        current.getPmList().add(pm6);

        VM vm1 = new VM("vm1", 3, 4, 2);
        VM vm2 = new VM("vm2", 6, 2, 2);
        VM vm3 = new VM("vm3", 3, 7, 3);
        VM vm4 = new VM("vm4", 2, 4, 2);
        VM vm5 = new VM("vm5", 1, 2, 2);
        VM vm6 = new VM("vm6", 4, 2, 4);
        VM vm7 = new VM("vm7", 5, 4, 3);
        VM vm8 = new VM("vm8", 3, 6, 5);
        VM vm9 = new VM("vm9", 3, 7, 2);


        current.assignToLocation(vm1, pm1);
        current.assignToLocation(vm2, pm1);
        current.assignToLocation(vm3, pm2);
        current.assignToLocation(vm4, pm2);
        current.assignToLocation(vm5, pm3);
        current.assignToLocation(vm6, pm3);
        current.assignToLocation(vm7, pm3);
        current.assignToLocation(vm8, pm3);


       // current.showAssignments();

        Network newNetwork = new Network();
        newNetwork.setPmList(current.getPmList());


        newNetwork.assignToLocation(vm1, pm2);
        newNetwork.assignToLocation(vm2, pm2);
        newNetwork.assignToLocation(vm3, pm3);
        newNetwork.assignToLocation(vm4, pm3);
        newNetwork.assignToLocation(vm5, pm1);
        newNetwork.assignToLocation(vm6, pm1);
        newNetwork.assignToLocation(vm7, pm5);
        newNetwork.assignToLocation(vm8, pm5);

        System.out.println("Migrations :");

        System.out.println(current.getMigrations(current, newNetwork));


        DependencyGraph dependencyGraph;
        List<Migration> migrationList = current.getMigrations(current, newNetwork);

        // System.out.println(current.getOutgoingVmsFrom(migrationList , pm1));
        // System.out.println(current.getVMGoingFromTo(migrationList , pm2, pm1));

        //loop over pms , check if there is any locked migratuion add to set and stop current iteration
        //i think it has repated dependencie

        dependencyGraph = current.generateDependencyGraph(current, newNetwork);

        System.out.println("Dependencies :");
        dependencyGraph.printDependency();


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



        // System.out.println(dependencyGraph.getDependencyDept(set3 ,  new ArrayList<>(), 0,new ArrayList<>()));
        System.out.println(dependencyGraph.returnChain(set1 , set4));


        System.out.println(dependencyGraph.chainBetween2(set1 , set4, new ArrayList<>() ,  new ArrayList<>()));

      //  System.out.println(dependencyGraph.chainBetween2(set3 , set3, new ArrayList<>() ,  new ArrayList<>()));


        //System.out.println(dependencyGraph.isCycle(set1, set3));


    }
}

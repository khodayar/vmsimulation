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
        VM vm1 = new VM("vm1", 3,4,2);
        VM vm2 = new VM("vm2",6,2,2);
        VM vm3 = new VM("vm3", 3,7,3);
        VM vm4 = new VM("vm4", 2,4,2);
        VM vm5 = new VM("vm5",1,2,2);
        VM vm6 = new VM("vm6", 4,2,4);
        VM vm7 = new VM("vm7", 5,4,3);
        VM vm8 = new VM("vm8",3,6,5);
        VM vm9 = new VM("vm9", 3,7,2);


        VMSet set1 = new VMSet();
        VMSet set2 = new VMSet();
        VMSet set3 = new VMSet();
        VMSet set4 = new VMSet();
        VMSet set5 = new VMSet();
        set1.add(vm1);
        //set1.add(vm5);
        set2.add(vm2);
        //set2.add(vm6);
        set3.add(vm3);
        set4.add(vm4);
        set5.add(vm5);


        DependencyGraph dependencyGraph = new DependencyGraph();
        dependencyGraph.addDependent(set1 , set2);
        dependencyGraph.addDependent(set2 , set3);
        dependencyGraph.addDependent(set3 , set5);
        dependencyGraph.addDependent(set4 , set1);
        dependencyGraph.addDependent(set5 , set1);

        dependencyGraph.printDependency();

        System.out.println();

//
//        System.out.println(dependencyGraph.returnCycle(set1,set5));
//
//        System.out.println(dependencyGraph.returnCycle(set1,set4));
//
//        System.out.println(dependencyGraph.returnCycle(set3,set4));


        System.out.println(dependencyGraph.returnCycle(set1,set3));





    }
}

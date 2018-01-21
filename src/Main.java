import Models.*;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        Network current = new Network();

        PM pm1 = new PM("pm1", 10,10,10);
        PM pm2 = new PM("pm2", 10,10,10);
        PM pm3 = new PM("pm3" ,10,10,10);

        current.getPmList().add(pm1);
        current.getPmList().add(pm2);
        current.getPmList().add(pm3);


        VM vm1 = new VM("vm1", 3,4,2);
        VM vm2 = new VM("vm2",6,2,2);
        VM vm3 = new VM("vm3", 3,7,3);

        current.assignToLocation(vm1 , pm1);
        current.assignToLocation(vm2 , pm1);
        current.assignToLocation(vm3 , pm3);

        current.showAssignments();

        Network newNetwork = new Network();

        newNetwork.getPmList().add(pm1);
        newNetwork.getPmList().add(pm2);
        newNetwork.getPmList().add(pm3);

        newNetwork.assignToLocation(vm1 , pm2);
        newNetwork.assignToLocation(vm2 , pm1);
        newNetwork.assignToLocation(vm3 , pm3);


        newNetwork.showAssignments();

       List<Migration> migrations = current.getMigrations(current , newNetwork);
       migrations.forEach(migration -> {
           System.out.println(migration.getVm().getName() + " from  " + migration.getSource().getName() + "  to :" + migration.getDestination().getName());
       });



    }
}

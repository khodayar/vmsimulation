import Models.Network;
import Models.PM;
import Models.VM;

public class Main {

    public static void main(String[] args) {

        Network network = new Network();

        PM pm1 = new PM("pm1", 10,10,10);
        PM pm2 = new PM("pm2", 10,10,10);
        PM pm3 = new PM("pm3" ,10,10,10);

        network.getPmList().add(pm1);
        network.getPmList().add(pm2);
        network.getPmList().add(pm3);

        VM vm1 = new VM("vm1", 3,4,2);
        VM vm2 = new VM("vm2",8,2,2);
        VM vm3 = new VM("vm3", 3,7,10);

        pm1.assignVM(vm1);
        pm2.assignVM(vm2);
        pm3.assignVM(vm3);

        network.showAssignments();

    }
}

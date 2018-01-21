package Models;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by I857455 on 1/18/2018.
 */
public class Network {

    List<PM> pmList;
    List<Assignment> assignments;

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }

    public Network(List<PM> pmList) {
        this.pmList = pmList;
    }

    public List<PM> getPmList() {
        return pmList;
    }

    public void setPmList(List<PM> pmList) {
        this.pmList = pmList;
    }

    public Network(List<PM> pmList, List<Assignment> assignments) {
        this.pmList = pmList;
        this.assignments = assignments;
    }

    public Network() {
        this.pmList = new ArrayList<>();
        this.assignments = new ArrayList<>();
    }

    //change assignment of a vm to new location
    public void assignToLocation(VM vm, PM pm){
        if (hasFreeCapacityFor(pm,vm)) {
            Assignment newAssignment = new Assignment(pm, vm);
            Predicate<Assignment> assignmentPredicate = p -> p.getVm() == vm;
            assignments.removeIf(assignmentPredicate);
            assignments.add(newAssignment);
        }
        else {
            System.out.println("There is no free Capacity for assigning "+ vm.getName() + " to " + pm.getName());
        }
    }


    public List<VM> getVMs(){
        List<VM> vms= new ArrayList<>();
        assignments.forEach(assignment-> {
            vms.add(assignment.getVm());
        });
        return vms;
    }



    public List<VM> getVMForPM(PM pm){
        List<VM> vms= new ArrayList<>();
        assignments.forEach(assignment -> {
            if (assignment.getPm().equals(pm)){
                vms.add(assignment.getVm());
            }
        });
        return vms;
    }

    public void showAssignments() {
        System.out.println("-------------------------");
        pmList.forEach(pm ->  {
            System.out.print(pm.getName() + " [free memory:"+ freeMemory(pm)+  ", free CPU:" +  freeProcessor(pm) + ",free network:"+ freeNetwork(pm) +"]   assigned VMs: ");
            getVMForPM(pm).forEach(vm -> {
                System.out.print(vm.getName());
            });
            System.out.println();
        });
    }


    public PM getVMLocation(VM vm){
        final PM[] pm = new PM[1];
        this.getAssignments().forEach(assignment ->{
            if (assignment.getVm() == vm){
                pm[0] = assignment.getPm();
            }
        });
        return pm[0];
    }


    public List<Migration> getMigrations(Network currentN, Network newN){
        List<Migration> migrations = new ArrayList<>();
        currentN.getAssignments().forEach(assignment -> {
            if (!getVMLocation(assignment.getVm()).equals(newN.getVMLocation(assignment.getVm()))){
                migrations.add(new Migration(assignment.getPm() , newN.getVMLocation(assignment.getVm()) , assignment.getVm()));
            }

        });

        return migrations;

    }



    public int freeMemory(PM pm){
        final int[] freeMemory = {pm.getMemoryCapacity()};
        assignments.forEach(assignment -> {
            if (assignment.getPm().equals(pm)){
                freeMemory[0] -= assignment.getVm().getMemorySize();
            }
        });

        return freeMemory[0];

    }

    public int freeProcessor(PM pm){
        final int[] freeMemory = {pm.getProcessorCapacity()};
        assignments.forEach(assignment -> {
            if (assignment.getPm().equals(pm)){
                freeMemory[0] -= assignment.getVm().getProcessorSize();
            }
        });

        return freeMemory[0];

    }

    public int freeNetwork(PM pm){
        final int[] freeMemory = {pm.getNetworkCapacity()};
        assignments.forEach(assignment -> {
            if (assignment.getPm().equals(pm)){
                freeMemory[0] -= assignment.getVm().getNetworkSize();
            }
        });

        return freeMemory[0];

    }

    public boolean hasFreeCapacityFor(PM pm , VM vm){
        return freeMemory(pm) >= vm.getMemorySize() && freeNetwork(pm) >= vm.getNetworkSize()
                && freeProcessor(pm) >= vm.getProcessorSize();
    }

}

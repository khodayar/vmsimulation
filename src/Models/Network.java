package Models;

import java.util.*;
import java.util.function.Predicate;

/**
 * Created by I857455 on 1/18/2018.
 */
public class Network {

    List<PM> pmList;
    List<Assignment> currentAssignments;
    List<Assignment> newAssignments;

    public List<Assignment> getCurrentAssignments() {
        return currentAssignments;
    }

    public void setCurrentAssignments(List<Assignment> currentAssignments) {
        this.currentAssignments = currentAssignments;
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

    public List<Assignment> getNewAssignments() {
        return newAssignments;
    }

    public void setNewAssignments(List<Assignment> newAssignments) {
        this.newAssignments = newAssignments;
    }

    public Network(List<PM> pmList, List<Assignment> currentAssignments) {
        this.pmList = pmList;
        this.currentAssignments = currentAssignments;
    }

    public Network() {
        this.pmList = new ArrayList<>();
        this.currentAssignments = new ArrayList<>();
        this.newAssignments = new ArrayList<>();
}

    public Assignment findAssignment(List<Assignment> assignments, VM vm){
        Assignment result;
         result = assignments.stream().filter(obj -> obj.getVm().equals(vm)).findFirst()
                 .get();
    return result;
    }


    //change assignment of a vm to new location
    //no need to check here
     //if (hasFreeCapacityFor(pm, vm)) {
    public void assignToCurrentLocation(VM vm, PM pm) throws Exception {

        if (!hasFreeCapacityFor(currentAssignments , pm , vm)){
            throw new Exception("there is no free capacity for this assignment " +  vm.getName() +" to " + pm.getName());
        }
            Assignment newAssignment = new Assignment(pm, vm);
            Predicate<Assignment> assignmentPredicate = p -> p.getVm() == vm;
            currentAssignments.removeIf(assignmentPredicate);
            currentAssignments.add(newAssignment);
    }


    public void assignToNewLocation(VM vm, PM pm) throws Exception {

        if (!hasFreeCapacityFor(newAssignments , pm , vm)){
            throw new Exception("there is no free capacity for this assignment " +  vm.getName() +" to " + pm.getName());
        }
        Assignment newAssignment = new Assignment(pm, vm);
        Predicate<Assignment> assignmentPredicate = p -> p.getVm() == vm;
        newAssignments.removeIf(assignmentPredicate);
        newAssignments.add(newAssignment);

    }

    public void removeFromCurrent(VM vm , PM pm){
        final Assignment[] remove = new Assignment[1];
        currentAssignments.forEach(assignment -> {
            if (assignment.getVm().equals(vm)){
            remove[0] = assignment;
            }
        });

        currentAssignments.remove(remove[0]);
    }


    public List<VM> getVMs() {
        List<VM> vms = new ArrayList<>();
        currentAssignments.forEach(assignment -> {
            vms.add(assignment.getVm());
        });
        return vms;
    }


    public List<VM> getVMForPM(List<Assignment> assignments, PM pm) {
        List<VM> vms = new ArrayList<>();
        assignments.forEach(assignment -> {
            if (assignment.getPm().equals(pm)) {
                vms.add(assignment.getVm());
            }
        });
        return vms;
    }

    public void showAssignments() {
        System.out.println("-------------------------legacy");
        pmList.forEach(pm -> {
            System.out.print(pm.getName() + " [free memory:" + freeMemory(currentAssignments, pm) + ", free CPU:" + freeProcessor(currentAssignments, pm) + ",free network:" + freeNetwork(currentAssignments,pm) + "]   assigned VMs: ");
            getVMForPM(currentAssignments,pm).forEach(vm -> {
                System.out.print(vm.getName() + " ");
            });
            System.out.println();
        });
        System.out.println("-------------------------New");
        pmList.forEach(pm -> {
            System.out.print(pm.getName() + " [free memory:" + freeMemory(newAssignments,pm) + ", free CPU:" + freeProcessor(newAssignments,pm) + ",free network:" + freeNetwork(newAssignments,pm) + "]   assigned VMs: ");
            getVMForPM(newAssignments,pm).forEach(vm -> {
                System.out.print(vm.getName() + " ");
            });
            System.out.println();
        });
   }


    public PM getLegacyVMLocation(VM vm) {
        final PM[] pm = new PM[1];
        this.getCurrentAssignments().forEach(assignment -> {
            if (assignment.getVm() == vm) {
                pm[0] = assignment.getPm();
            }
        });
        return pm[0];
    }


    public List<Migration> getMigrations() {
        List<Migration> migrations = new ArrayList<>();
        currentAssignments.forEach(assignment -> {
            if (!getLegacyVMLocation(assignment.getVm()).equals(findAssignment(newAssignments ,assignment.getVm()).getPm())) {
                migrations.add(new Migration(assignment.getPm(), findAssignment(newAssignments ,assignment.getVm()).getPm(), assignment.getVm()));
            }
        });
        return migrations;
    }

    //outgoing set of pm , separated based on each destination pm
    public List<VMSet> getOutgoingVmsFrom(List<Migration> migrations, PM pm) {

        Map<PM, List<VM>> migrationsOfPM = new HashMap<>();
        migrations.forEach(migration -> {
            if (migration.getSource().equals(pm)) {
                if (migrationsOfPM.get(migration.getDestination()) == null) {
                    List<VM> newList = new ArrayList<>();
                    newList.add(migration.getVm());
                    migrationsOfPM.put(migration.getDestination(), newList);
                } else {
                    migrationsOfPM.get(migration.getDestination()).add(migration.getVm());
                }
            }
        });

        List<VMSet> vmSets = new ArrayList<>();
        for (Map.Entry<PM, List<VM>> entry : migrationsOfPM.entrySet()) {
            VMSet vmSet = new VMSet();
            vmSet.setVMList(entry.getValue());
            vmSets.add(vmSet);
        }

        return vmSets;

    }



    //get outgoing vmset from pm to pm
    public VMSet getVMGoingFromTo(List<Migration> migrations, PM source , PM destination){
        VMSet vmSet = new VMSet();
        migrations.forEach(migration -> {
            if (migration.getSource().equals(source) && migration.getDestination().equals(destination)){
                vmSet.add(migration.getVm());
            }
        });
        return vmSet;
    }



    //all the sets  , no use for now
    public List<VMSet> getAllOutGoingSets() {
        List<Migration> migrations = getMigrations();
        List<VMSet> vmSetList = new ArrayList<>();
        this.getPmList().forEach(pm -> {
            if (!getOutgoingVmsFrom(migrations, pm).isEmpty())
                getOutgoingVmsFrom(migrations, pm).forEach(vmsetlist -> {
                    vmSetList.add(vmsetlist);
                });
        });
        return vmSetList;
    }
//
//
//    public List<DependencyGraph> getDependencies(Network currentN, Network newN) {
//
//        List<DependencyGraph> dependencies = new ArrayList<>();
//        List<Migration> migrations = getMigrations(currentN, newN);
//        migrations.forEach(migration -> {
//            // VMSet vmSet =
//        });
//
//
//        return null;
//    }

    public int freeMemory(List<Assignment> assignments , PM pm) {
        final int[] freeMemory = {pm.getMemoryCapacity()};
        assignments.forEach(assignment -> {
            if (assignment.getPm().equals(pm)) {
                freeMemory[0] -= assignment.getVm().getMemorySize();
            }
        });

        return freeMemory[0];

    }

    public int freeProcessor(List<Assignment> assignments ,PM pm) {
        final int[] freeMemory = {pm.getProcessorCapacity()};
        assignments.forEach(assignment -> {
            if (assignment.getPm().equals(pm)) {
                freeMemory[0] -= assignment.getVm().getProcessorSize();
            }
        });

        return freeMemory[0];

    }

    public int freeNetwork(List<Assignment> assignments ,PM pm) {
        final int[] freeMemory = {pm.getNetworkCapacity()};
        assignments.forEach(assignment -> {
            if (assignment.getPm().equals(pm)) {
                freeMemory[0] -= assignment.getVm().getNetworkSize();
            }
        });

        return freeMemory[0];

    }

    public boolean hasFreeCapacityFor(List<Assignment> assignments, PM pm, VM vm) {
        return freeMemory(assignments,pm) >= vm.getMemorySize() && freeNetwork(assignments,pm) >= vm.getNetworkSize()
                && freeProcessor(assignments,pm) >= vm.getProcessorSize();
    }



    public List<Migration> getMigrationsFrom(List<Migration> migrations , PM source){
        List<Migration> migrationList = new ArrayList<>();
        migrations.forEach(m -> {
            if (m.getSource().equals(source)){
                migrationList.add(m);
            }
        });
     return migrationList;
    }


    //check of logic is not deteriorated
    public DependencyGraph generateDependencyGraph( List<Migration> migrationList) {
        DependencyGraph dependencyGraph = new DependencyGraph();
        pmList.forEach(sourcePm -> {
            getMigrationsFrom(migrationList , sourcePm).forEach(migration -> {
                if (!hasFreeCapacityFor(currentAssignments, migration.getDestination() , migration.getVm())) {
                    VMSet vmSet = getVMGoingFromTo(migrationList, sourcePm, migration.getDestination());
                    getOutgoingVmsFrom(migrationList, migration.getDestination()).forEach(destOutSet -> {
                       // System.out.println(vmSet + "->" + destOutSet);
                        dependencyGraph.addDependent(vmSet, destOutSet);
                    });
                }
            });

        });
        return dependencyGraph;
    }

    public void setMigrationWeights(List<Migration> migrations){
        migrations.forEach(migration -> {
            migration.setWeight(migration.getVm().getMemorySize());
        });
    }


    public void setDependencyWeights(List<Migration> migrations){
        DependencyGraph dependencyGraph = generateDependencyGraph(migrations);





    }


}

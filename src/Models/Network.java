package Models;

import java.util.*;
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
    //no need to check here
     //if (hasFreeCapacityFor(pm, vm)) {
    public void assignToLocation(VM vm, PM pm) {

            Assignment newAssignment = new Assignment(pm, vm);
            Predicate<Assignment> assignmentPredicate = p -> p.getVm() == vm;
            assignments.removeIf(assignmentPredicate);
            assignments.add(newAssignment);

    }


    public List<VM> getVMs() {
        List<VM> vms = new ArrayList<>();
        assignments.forEach(assignment -> {
            vms.add(assignment.getVm());
        });
        return vms;
    }


    public List<VM> getVMForPM(PM pm) {
        List<VM> vms = new ArrayList<>();
        assignments.forEach(assignment -> {
            if (assignment.getPm().equals(pm)) {
                vms.add(assignment.getVm());
            }
        });
        return vms;
    }

    public void showAssignments() {
        System.out.println("-------------------------");
        pmList.forEach(pm -> {
            System.out.print(pm.getName() + " [free memory:" + freeMemory(pm) + ", free CPU:" + freeProcessor(pm) + ",free network:" + freeNetwork(pm) + "]   assigned VMs: ");
            getVMForPM(pm).forEach(vm -> {
                System.out.print(vm.getName() + " ");
            });
            System.out.println();
        });
    }


    public PM getVMLocation(VM vm) {
        final PM[] pm = new PM[1];
        this.getAssignments().forEach(assignment -> {
            if (assignment.getVm() == vm) {
                pm[0] = assignment.getPm();
            }
        });
        return pm[0];
    }


    public List<Migration> getMigrations(Network currentN, Network newN) {
        List<Migration> migrations = new ArrayList<>();
        currentN.getAssignments().forEach(assignment -> {
            if (!getVMLocation(assignment.getVm()).equals(newN.getVMLocation(assignment.getVm()))) {
                migrations.add(new Migration(assignment.getPm(), newN.getVMLocation(assignment.getVm()), assignment.getVm()));
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
    public List<VMSet> getAllOutGoingSets(List<Migration> migrations) {
        List<VMSet> vmSetList = new ArrayList<>();
        this.getPmList().forEach(pm -> {
            if (!getOutgoingVmsFrom(migrations, pm).isEmpty())
                getOutgoingVmsFrom(migrations, pm).forEach(vmsetlist -> {
                    vmSetList.add(vmsetlist);
                });
        });
        return vmSetList;
    }


    public List<DependencyGraph> getDependencies(Network currentN, Network newN) {

        List<DependencyGraph> dependencies = new ArrayList<>();
        List<Migration> migrations = getMigrations(currentN, newN);
        migrations.forEach(migration -> {
            // VMSet vmSet =
        });


        return null;


    }

    public int freeMemory(PM pm) {
        final int[] freeMemory = {pm.getMemoryCapacity()};
        assignments.forEach(assignment -> {
            if (assignment.getPm().equals(pm)) {
                freeMemory[0] -= assignment.getVm().getMemorySize();
            }
        });

        return freeMemory[0];

    }

    public int freeProcessor(PM pm) {
        final int[] freeMemory = {pm.getProcessorCapacity()};
        assignments.forEach(assignment -> {
            if (assignment.getPm().equals(pm)) {
                freeMemory[0] -= assignment.getVm().getProcessorSize();
            }
        });

        return freeMemory[0];

    }

    public int freeNetwork(PM pm) {
        final int[] freeMemory = {pm.getNetworkCapacity()};
        assignments.forEach(assignment -> {
            if (assignment.getPm().equals(pm)) {
                freeMemory[0] -= assignment.getVm().getNetworkSize();
            }
        });

        return freeMemory[0];

    }

    public boolean hasFreeCapacityFor(PM pm, VM vm) {
        return freeMemory(pm) >= vm.getMemorySize() && freeNetwork(pm) >= vm.getNetworkSize()
                && freeProcessor(pm) >= vm.getProcessorSize();
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

    public DependencyGraph generateDependencyGraph(Network current, Network newNetwork) {
        DependencyGraph dependencyGraph = new DependencyGraph();
        List<Migration> migrationList = current.getMigrations(current , newNetwork);
        current.getPmList().forEach(sourcePm -> {
            current.getMigrationsFrom(migrationList , sourcePm).forEach(migration -> {
                if (!current.hasFreeCapacityFor(migration.getDestination() , migration.getVm())) {
                    VMSet vmSet = current.getVMGoingFromTo(migrationList, sourcePm, migration.getDestination());
                    current.getOutgoingVmsFrom(migrationList, migration.getDestination()).forEach(destOutSet -> {
                       // System.out.println(vmSet + "->" + destOutSet);
                        dependencyGraph.addDependent(vmSet, destOutSet);
                    });
                }
            });

        });
        return dependencyGraph;
    }





}

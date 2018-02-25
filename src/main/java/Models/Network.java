package main.java.Models;

import java.util.*;
import main.java.Models.*;
import java.util.function.Predicate;

/**
 * Created by I857455 on 1/18/2018.
 */
public class Network {

    List<PM> pmList;
    List<Assignment> currentAssignments;
    List<Assignment> newAssignments;
    List<Migration> migrations;
    List<Migration> nextPhaseMigrations;

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

    public List<Migration> getMigrations() {
        return migrations;
    }

    public void setMigrations(List<Migration> migrations) {
        this.migrations = migrations;
    }

    public List<Migration> getNextPhaseMigrations() {
        return nextPhaseMigrations;
    }

    public void setNextPhaseMigrations(List<Migration> nextPhaseMigrations) {
        this.nextPhaseMigrations = nextPhaseMigrations;
    }

    public Network(List<PM> pmList, List<Assignment> currentAssignments) {
        this.pmList = pmList;
        this.currentAssignments = currentAssignments;
    }

    public Network() {
        this.pmList = new ArrayList<>();
        this.currentAssignments = new ArrayList<>();
        this.newAssignments = new ArrayList<>();
        this.nextPhaseMigrations = new ArrayList<>();
    }

    public Assignment findAssignment(List<Assignment> assignments, VM vm) {
        Assignment result;
        result = assignments.stream().filter(obj -> obj.getVm().equals(vm)).findFirst()
                .get();
        return result;
    }


    //change assignment of a vm to new location
    //no need to check here
    //if (hasFreeCapacityFor(pm, vm)) {
    public void assignToCurrentLocation(VM vm, PM pm) throws Exception {

        if (!hasFreeCapacityFor(currentAssignments, pm, vm)) {
            throw new Exception("there is no free capacity for this assignment " + vm.getName() + " to " + pm.getName());
        }
        Assignment newAssignment = new Assignment(pm, vm);
        Predicate<Assignment> assignmentPredicate = p -> p.getVm() == vm;
        currentAssignments.removeIf(assignmentPredicate);
        currentAssignments.add(newAssignment);
    }


    public void assignToNewLocation(VM vm, PM pm) throws Exception {

        if (!hasFreeCapacityFor(newAssignments, pm, vm)) {
            throw new Exception("there is no free capacity for this assignment " + vm.getName() + " to " + pm.getName());
        }
        Assignment newAssignment = new Assignment(pm, vm);
        Predicate<Assignment> assignmentPredicate = p -> p.getVm() == vm;
        newAssignments.removeIf(assignmentPredicate);
        newAssignments.add(newAssignment);

    }

    public void removeFromCurrent(VM vm, PM pm) {
        final Assignment[] remove = new Assignment[1];
        currentAssignments.forEach(assignment -> {
            if (assignment.getVm().equals(vm)) {
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
            System.out.print(pm.getName() + " [free memory:" + freeMemory(currentAssignments, pm) + ", free CPU:" + freeProcessor(currentAssignments, pm) + ",free network:" + freeNetwork(currentAssignments, pm) + "]   assigned VMs: ");
            getVMForPM(currentAssignments, pm).forEach(vm -> {
                System.out.print(vm.getName() + " ");
            });
            System.out.println();
        });
        System.out.println("-------------------------New");
        pmList.forEach(pm -> {
            System.out.print(pm.getName() + " [free memory:" + freeMemory(newAssignments, pm) + ", free CPU:" + freeProcessor(newAssignments, pm) + ",free network:" + freeNetwork(newAssignments, pm) + "]   assigned VMs: ");
            getVMForPM(newAssignments, pm).forEach(vm -> {
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


    public List<Migration> generateMigrations() {
        List<Migration> migrations = new ArrayList<>();
        currentAssignments.forEach(assignment -> {
            if (!getLegacyVMLocation(assignment.getVm()).equals(findAssignment(newAssignments, assignment.getVm()).getPm())) {
                migrations.add(new Migration(assignment.getPm(), findAssignment(newAssignments, assignment.getVm()).getPm(), assignment.getVm()));
            }
        });
        this.migrations = migrations;
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
    public VMSet getVMGoingFromTo(List<Migration> migrations, PM source, PM destination) {
        VMSet vmSet = new VMSet();
        migrations.forEach(migration -> {
            if (migration.getSource().equals(source) && migration.getDestination().equals(destination)) {
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


    public int freeMemory(List<Assignment> assignments, PM pm) {
        final int[] freeMemory = {pm.getMemoryCapacity()};
        assignments.forEach(assignment -> {
            if (assignment.getPm().equals(pm)) {
                freeMemory[0] -= assignment.getVm().getMemorySize();
            }
        });

        return freeMemory[0];

    }

    public int freeProcessor(List<Assignment> assignments, PM pm) {
        final int[] freeMemory = {pm.getProcessorCapacity()};
        assignments.forEach(assignment -> {
            if (assignment.getPm().equals(pm)) {
                freeMemory[0] -= assignment.getVm().getProcessorSize();
            }
        });

        return freeMemory[0];

    }

    public int freeNetwork(List<Assignment> assignments, PM pm) {
        final int[] freeMemory = {pm.getNetworkCapacity()};
        assignments.forEach(assignment -> {
            if (assignment.getPm().equals(pm)) {
                freeMemory[0] -= assignment.getVm().getNetworkSize();
            }
        });

        return freeMemory[0];

    }

    public boolean hasFreeCapacityFor(List<Assignment> assignments, PM pm, VM vm) {
        return freeMemory(assignments, pm) >= vm.getMemorySize() && freeNetwork(assignments, pm) >= vm.getNetworkSize()
                && freeProcessor(assignments, pm) >= vm.getProcessorSize();
    }


    public boolean hasFreeCapacityFor(List<Assignment> assignments, PM pm, VMSet vmSet) {
        int neededMemory = 0, neededCpu = 0, neededNet = 0;

        for (int i = 0; i < vmSet.getVMList().size(); i++) {
            VM vm = vmSet.getVMList().get(i);
            neededMemory += vm.getMemorySize();
            neededCpu += vm.getProcessorSize();
            neededNet += vm.getNetworkSize();
        }

        return freeMemory(assignments, pm) >= neededMemory && freeNetwork(assignments, pm) >= neededNet
                && freeProcessor(assignments, pm) >= neededCpu;
    }


    public List<Migration> getMigrationsFrom(List<Migration> migrations, PM source) {
        List<Migration> migrationList = new ArrayList<>();
        migrations.forEach(m -> {
            if (m.getSource().equals(source)) {
                migrationList.add(m);
            }
        });
        return migrationList;
    }


    //check of logic is not deteriorated
    //done : it's each pm must be sets
    public DependencyGraph generateDependencyGraph(List<Migration> migrationList) {
        DependencyGraph dependencyGraph = new DependencyGraph();
        pmList.forEach(sourcePm -> {
            getOutgoingVmsFrom(migrationList, sourcePm).forEach(vmSet -> {
                PM destination = findMigrationOfVM(vmSet.getVMList().get(0), migrationList).getDestination();
                if (!hasFreeCapacityFor(currentAssignments, destination, vmSet)) {
                    getOutgoingVmsFrom(migrationList, destination).forEach(destOutSet -> {
                        // System.out.println(vmSet + "->" + destOutSet);
                        dependencyGraph.addDependent(vmSet, destOutSet);
                    });
                }
            });
        });
        return dependencyGraph;
    }

    public void setMigrationWeights(List<Migration> migrations) {
        migrations.forEach(migration -> {
            migration.setWeight(migration.getVm().getMemorySize());
        });
    }


    public void setDependencyWeights(List<Migration> migrations) {
        DependencyGraph dependencyGraph = generateDependencyGraph(migrations);
        Map<VMSet, List<VMSet>> map = dependencyGraph.getDependencyMap();

        List<VMSet> removedInEdge = new ArrayList<>();
        List<VMSet> allSets = getAllOutGoingSets(migrations);

        while (removedInEdge.size() < allSets.size()) {

            allSets.forEach(set -> {
                if (!removedInEdge.contains(set)) {
                    boolean hasInEdge = false;
                    for (Map.Entry<VMSet, List<VMSet>> entry : map.entrySet()) {
                        if (entry.getValue().contains(set) && !removedInEdge.contains(entry.getKey())) {
                            hasInEdge = true;
                        }
                    }

                    if (!hasInEdge) {
                        int setWeight = maxWeightOfSet(set, migrations);
                        if (map.get(set) != null) {
                            map.get(set).forEach(dependentSet -> {
                                dependentSet.getVMList().forEach(vm -> {
                                            findMigrationOfVM(vm, migrations).setWeight(findMigrationOfVM(vm, migrations).getWeight() + setWeight);

                                        }

                                );
                            });
                        }
                        removedInEdge.add(set);
                    }

                }
            });
        }


    }

    private int maxWeightOfSet(VMSet set, List<Migration> migrations) {
        final int[] weight = {0};
        set.getVMList().forEach(vm -> {
                    if (findMigrationOfVM(vm, migrations).getWeight() > weight[0]) {
                        weight[0] = findMigrationOfVM(vm, migrations).getWeight();

                    }
                }
        );

        return weight[0];
    }


    public Migration findMigrationOfVM(VM vm, List<Migration> migrations) {
        final Migration[] migration = new Migration[1];
        migrations.forEach(mig -> {
            if (mig.getVm().equals(vm)) migration[0] = mig;
        });

        return migration[0];

    }


    public void solveCycles() {
        getAllOutGoingSets(migrations).forEach(vmSet -> {
            DependencyGraph dGraph = generateDependencyGraph(migrations);
            if (Collections.frequency(dGraph.getPath(vmSet, vmSet), vmSet) > 1) {
                System.out.println("There is a cycle :");
                System.out.println(dGraph.getPath(vmSet, vmSet));
                VMSet bestCandidate = findTheMinWeightSet(dGraph.getPath(vmSet, vmSet));
                PM bestPm = findBestTempPM(dGraph.getPath(vmSet, vmSet), vmSet);
                updateMigration(bestCandidate, bestPm);
            }

        });
    }

    private void updateMigration(VMSet bestCandidate, PM bestPm) {
        List<VM> vmList = bestCandidate.getVMList();
        vmList.forEach(vm -> {
            Migration oldmig = findMigrationOfVM(vm, migrations);
            Migration newMig = new Migration(oldmig.getSource(), bestPm, vm);
            newMig.setWeight(oldmig.getWeight());
            System.out.println("new temp Migration has been added :" + newMig);
            nextPhaseMigrations.add(oldmig);
            migrations.remove(oldmig);
            migrations.add(newMig);
        });
    }


    private VMSet findTheMinWeightSet(List<VMSet> vmSetList) {
        final VMSet[] found = {vmSetList.get(0)};
        vmSetList.forEach(vmSet -> {
            if (maxWeightOfSet(vmSet, migrations) < maxWeightOfSet(found[0], migrations)) {
                found[0] = vmSet;
            }
        });
        return found[0];
    }


    private PM findBestTempPM(List<VMSet> cycleVMSetList, VMSet candidate) {
        final PM[] pm = new PM[1];
        List<PM> candidatePms = tempLocationPMs(cycleVMSetList);
        candidatePms.forEach(candidatePm -> {
            if (hasFreeCapacityForSet(newAssignments, candidatePm, candidate)) {
                pm[0] = candidatePm;
            }
        });
        if (pm[0].getName() == null) {
            System.out.println("there is no temp location for solving the cycle");
        }
        return pm[0];

    }


    private boolean hasFreeCapacityForSet(List<Assignment> assignments, PM pm, VMSet vmSet) {
        final boolean[] hasCp = {true};
        vmSet.getVMList().forEach(vm -> {
            if (!hasFreeCapacityFor(assignments, pm, vm)) {
                hasCp[0] = false;
            }
        });
        return hasCp[0];
    }


    private List<PM> tempLocationPMs(List<VMSet> cycleVMSetList) {
        List<PM> pms = new ArrayList<PM>(pmList);
        cycleVMSetList.forEach(vmSet -> {
            vmSet.getVMList().forEach(vm -> {
                migrations.forEach(migration -> {
                    if (migration.getVm().equals(vm)) {
                        pms.remove(migration.getDestination());
                    }
                });
            });
        });
        return pms;
    }

    //add initial creation of pms and vms and assigns as FFD
    public void addOptimalPlacement(int numberOfPMs, int numberOfVMs) {

        PM[] pms = new PM[numberOfPMs];

        for (int i = 0; i < numberOfPMs; i++) {
            pms[i] = new PM("pm" + i, 10, 10, 10);
            this.getPmList().add(pms[i]);
        }

        for (int j = 0; j < numberOfVMs; j++) {
            VM currentVm = createRndVM(j);
            for (int i = 0; i < numberOfPMs; i++) {
                if (hasFreeCapacityFor(currentAssignments, pms[i], currentVm)) {
                    try {
                        assignToCurrentLocation(currentVm, pms[i]);
                    } catch (Exception e) {
                        //handled in assignToCurrentLocation
                    }
                    break;
                }
            }
        }
    }


    public void assignRndNewLocations() {
        currentAssignments.forEach(assignment -> {
            VM vm = assignment.getVm();
            boolean assigned = false;
            while (!assigned) {
                PM pm = getPmList().get((int) (Math.random() * (pmList.size())));
                if (hasFreeCapacityFor(newAssignments, pm, vm)) {
                    try {
                        assignToNewLocation(vm , pm);
                    } catch (Exception e) {}

                    assigned = true;
                }

            }


        });
    }


    private VM createRndVM(int index) {
        int min = 3;
        int max = 6;
        return new VM("vm" + index, (int) (Math.random() * ((max - min) + 1)) + min,
                (int) (Math.random() * ((max - min) + 1)) + min, (int) (Math.random() * ((max - min) + 1)) + min);
    }
}

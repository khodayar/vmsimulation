package Models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by I857455 on 2/2/2018.
 */
public class MigrationProcess {



    private Cloud cloud;
    private int pipelineDegree;
    private int linkDegree;
    private List<Migration> onGoingMigrations;
    private int timeStamp;



    public Cloud getCloud() {
        return cloud;
    }

    public void setCloud(Cloud cloud) {
        this.cloud = cloud;
    }

    public int getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getPipelineDegree() {
        return pipelineDegree;
    }

    public void setPipelineDegree(int pipelineDegree) {
        this.pipelineDegree = pipelineDegree;
    }

    public List<Migration> getOnGoingMigrations() {
        return onGoingMigrations;
    }

    public void setOnGoingMigrations(List<Migration> onGoingMigrations) {
        this.onGoingMigrations = onGoingMigrations;
    }

    public int getLinkDegree() {
        return linkDegree;
    }

    public void setLinkDegree(int linkDegree) {
        this.linkDegree = linkDegree;
    }

    public MigrationProcess() {
        this.onGoingMigrations = new ArrayList<>();
        timeStamp=0;
    }

    private void startMigration(Migration m) throws Exception {
        cloud.assignToCurrentLocation(m.getVm() , m.getDestination(), true);
        onGoingMigrations.add(m);
        pipelineDegree--;
        System.out.println("Migration Started " + m + " at " + timeStamp);
        if (!m.getDestination().equals(m.getFinalDestination())){
            cloud.getReport().setNumberOFTempMig(cloud.getReport().getNumberOFTempMig() + 1);
        }

        cloud.getMigrations().remove(m);
        cloud.getNextPhaseMigrations().remove(m);

    }



   //finishes the minimum remaining time migrations in the on going list and returns the finished ones
    private List<Migration> finishNextMigration() {
        List<Migration> finished = new ArrayList<>();
        final int[] minRemainingTime = {!onGoingMigrations.isEmpty()? onGoingMigrations.get(0).getWeight() : 0};
        onGoingMigrations.forEach(currentMigration -> {
            if (currentMigration.getRemainingSize() < minRemainingTime[0]) {
                minRemainingTime[0] = currentMigration.getRemainingSize();
            }
        });

        timeStamp += minRemainingTime[0];
        cloud.getReport().setTimeSteps(timeStamp +1);
        onGoingMigrations.forEach(currentMigration -> {
            currentMigration.setRemainingSize(currentMigration.getRemainingSize() - minRemainingTime[0]);
            if (currentMigration.getRemainingSize() == 0) {

                pipelineDegree++;
                finished.add(currentMigration);
                //remove initial vm running on source
                cloud.removeFromCurrent(currentMigration.getVm() , currentMigration.getSource());
                if (timeStamp == 490){
                    System.out.println();
                }
                System.out.println("Migration Finished " + currentMigration + " at " + timeStamp);
                System.out.println("free pipelineDegree :" + pipelineDegree);
            }
        });

        onGoingMigrations.removeAll(finished);
     List<Migration> originalFinished = finished.stream().filter(finishedmigration -> finishedmigration.getDestination().equals(finishedmigration.getFinalDestination()))
             .collect(Collectors.toList());

        List<Migration> tempFinished = finished.stream().filter(finishedmigration -> !finishedmigration.getDestination().equals(finishedmigration.getFinalDestination()))
                .collect(Collectors.toList());
        List<Migration> feasibleFromNext = cloud.getFeasibleNextPhase(tempFinished);
        cloud.getMigrations().addAll(feasibleFromNext);
        cloud.getNextPhaseMigrations().removeAll(feasibleFromNext);

        cloud.report.setNumberOFFinishedMigs(cloud.report.getNumberOFFinishedMigs()+ originalFinished.size());
        cloud.getReport().setNumberOfOnGoingMigs(onGoingMigrations.size());
        return finished;
    }


    public void OnCcDoMigrations(DependencyGraph dg) throws Exception {

        cloud.getReport().setNumberOfInitialCycles(0);

        cloud.setInitialMigrationTimes(cloud.getMigrations());
        Set<List<VMSet>> cycles;
        List<Set<VM>> c = new ArrayList<>();  //connected components with no candidate
        List<VM> l = new ArrayList<>();
        List<Set<VM>> g = cloud.getConnectedComponents(dg);
        List<VM> t = new ArrayList<>();
        List<VM> x = new ArrayList<>();


        DependencyGraph finalDg = dg;
        List<Set<VM>> finalC = new ArrayList<>();
        g.forEach(setOfVms -> {
            List<VM> i = cloud.getVMsWithoutOutEdges(finalDg, setOfVms);
            if (!i.isEmpty()){
                l.addAll(i);
            } else {
                finalC.add(setOfVms);
            }
        });
        c.addAll(finalC);
        finalC.clear();

        do {
            if (!c.isEmpty()) {
                cycles = cloud.detectCyclesO(dg);
                cloud.solveCyclesOn(cycles, dg,g ); //here just one cycle per each c ?
                dg = cloud.generateOnoueDependencyGraph(cloud.getMigrations());
                cloud.setDependencyWeightsO(cloud.generateOnoueDependencyGraph(cloud.getMigrations()));
                g = cloud.getConnectedComponents(dg);
                //  d = cloud.generateOnoueDependencyGraph(cloud.getMigrations());
                t.addAll(cloud.getVMsWithoutOutEdges(dg));  //in next iteration will be added to l
                t.removeAll(l);
            }

            Collections.sort(l);
            for (int i=0; i<l.size();i++) {

                Migration m = cloud.findMigrationOfVM(l.get(i), cloud.getMigrations());
                if (m == null){
                    System.out.println();
                }
                if (cloud.hasFreeCapacityFor(cloud.getCurrentAssignments(), m.getDestination(), l.get(i)) &&
                        linksHaveCapacity(m)) {
                    try {
                        startMigration(m);
                        x.add(l.get(i));  //ongoing migrating VMs
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            l.removeAll(x);

            t.forEach(vm -> {
                if (!l.contains(vm)) {
                    l.add(vm);
                }
            });
            t.clear();

            if (x.isEmpty()){
                //stop sign
                //     System.out.println();
                //move temp migrations to a new temp server
                if (!cloud.shuffleTempMigrations()){
                    cloud.printReport();
                    if (l.isEmpty()){
                                  throw new Exception("infeasible migration(s)");
                    }
                    throw new Exception("blocked temp migration(s)");
                }

            }

//
//            if (x.isEmpty() && l.isEmpty()){
//                // cloud.solveEmptyDependencies(d);
//                cloud.printReport();
//                throw new Exception("infeasible migration(s)");
//                //if there is no temp location, it will continue
//            }


            //line 21 of Onoue
            List<Migration> finished = finishNextMigration();


            //  cloud.getMigrations().removeAll(finished);

            //todo it stocks in a situation where there is no feasible migration
            //but also no cycles !


            //infimnite loop of cycles
            //l is not empty but all the vm are in ccycles
            finished.forEach(finishedMigration -> {
                x.remove(finishedMigration.getVm());
            });
            dg = cloud.generateOnoueDependencyGraph(cloud.getMigrations());
            cloud.setDependencyWeightsO(cloud.generateOnoueDependencyGraph(cloud.getMigrations()));

            //  d = cloud.generateOnoueDependencyGraph(cloud.getMigrations());
           // Set<List<VMSet>> ct = cloud.ge(dg);
            //todo : error we have common vm s in connected components
            g = cloud.getConnectedComponents(dg);
            DependencyGraph finalDg2  = dg;
            l.clear();
            finalC.clear();
            g.forEach(setOfVms -> {
                List<VM> i = cloud.getVMsWithoutOutEdges(finalDg2, setOfVms);
                if (!i.isEmpty()){
                    l.addAll(i);
                } else {
                    finalC.add(setOfVms);
                }
            });

            c.clear();
            if (!finalC.isEmpty()) {
                c.addAll(finalC);

                //**** test, it will be replaced by following function
                //***  cloud.removeVMsInCycle(c , l);
                cloud.removeDependantVMs(dg , l);
            } else {

                //for new ones after the migrations has finished
//                cloud.getVMsWithoutOutEdges(dg).forEach(vm ->{
//                    if (l.indexOf(vm)<0) {l.add(vm);}
//                });
            }





        } while (!cloud.getMigrations().isEmpty() || !onGoingMigrations.isEmpty());

        cloud.printReport();



    }



    public void doMigrationsOnoue (DependencyGraph d) throws Exception {

        cloud.setInitialMigrationTimes(cloud.getMigrations());

        Set<List<VMSet>> c = cloud.detectCyclesO(d);
        cloud.getReport().setNumberOfInitialCycles(c.size());
        List<VM> l = new ArrayList<>();

        //line 4--8 of onoue
        l.addAll(cloud.getVMsWithoutOutEdges(d));

        List<VM> x = new ArrayList<>();
        List<VM> t = new ArrayList<>();

        do {
            //transformMDG
            if (!c.isEmpty()){
                cloud.solveCyclesOn(c, d);
                d = cloud.generateOnoueDependencyGraph(cloud.getMigrations());
                cloud.setDependencyWeightsO(cloud.generateOnoueDependencyGraph(cloud.getMigrations()));
              //  d = cloud.generateOnoueDependencyGraph(cloud.getMigrations());
                t.addAll(cloud.getVMsWithoutOutEdges(d));  //in next iteration will be added to l
                t.removeAll(l);   //only new woe resulted form solving cycle
            }


            Collections.sort(l);
             for (int i=0; i<l.size();i++) {

                Migration m = cloud.findMigrationOfVM(l.get(i), cloud.getMigrations());
                if (cloud.hasFreeCapacityFor(cloud.getCurrentAssignments(), m.getDestination(), l.get(i)) &&
                        linksHaveCapacity(m)) {
                    try {
                        startMigration(m);
                        x.add(l.get(i));  //ongoing migrating VMs
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            l.removeAll(x);

            l.addAll(t);
            t.clear();


            if (x.isEmpty()){
                //stop sign
           //     System.out.println();
                //move temp migrations to a new temp server
               if (!cloud.shuffleTempMigrations()){
                   cloud.printReport();
                   throw new Exception("blocked temp migration(s)");
               }

            }

            //if ((x.isEmpty() && l.isEmpty()) || (x.isEmpty() && c.isEmpty())){
            if (x.isEmpty() && l.isEmpty()){
           // cloud.solveEmptyDependencies(d);
            cloud.printReport();
                throw new Exception("infeasible migration(s)");
                //if there is no temp location, it will continue
            }


            //line 21 of Onoue
            List<Migration> finished = finishNextMigration();


          //  cloud.getMigrations().removeAll(finished);

            //todo it stocks in a situation where there is no feasible migration
            //but also no cycles !


            //infimnite loop of cycles
            //l is not empty but all the vm are in ccycles
            finished.forEach(finishedMigration -> {
                x.remove(finishedMigration.getVm());
            });
            d = cloud.generateOnoueDependencyGraph(cloud.getMigrations());
            cloud.setDependencyWeightsO(cloud.generateOnoueDependencyGraph(cloud.getMigrations()));
          //  d = cloud.generateOnoueDependencyGraph(cloud.getMigrations());
            Set<List<VMSet>> ct = cloud.detectCyclesO(d);
            if (ct.isEmpty() && x.isEmpty()){
                System.out.println();
            }
            if (!ct.isEmpty()) {
               c = ct;

                //**** test, it will be replaced by following function
                //***  cloud.removeVMsInCycle(c , l);
                cloud.removeDependantVMs(d , l);
            } else {

                //for new ones after the migrations has finished
                cloud.getVMsWithoutOutEdges(d).forEach(vm ->{
                    if (l.indexOf(vm)<0) {l.add(vm);}
                 });
            }
        } while (!cloud.getMigrations().isEmpty() || !onGoingMigrations.isEmpty());
        cloud.printReport();
   }

    private boolean linksHaveCapacity(Migration migration) {
        final int[] sourceTraffic = {0};
        final int[] destTraffic = {0};
        onGoingMigrations.forEach(onGoingMigration -> {
            if (onGoingMigration.getSource() == migration.getSource() || onGoingMigration.getDestination() == migration.getSource()){
                sourceTraffic[0]++;
            }
            if (onGoingMigration.getSource() == migration.getDestination() || onGoingMigration.getDestination() == migration.getDestination()){
                destTraffic[0]++;
            }

        });

        return sourceTraffic[0] < linkDegree && destTraffic[0] < linkDegree;
    }


}

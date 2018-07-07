package Models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    }

    private List<Migration> finishNextMigration() {
        List<Migration> finished = new ArrayList<>();
        final int[] minRemainingTime = {1000};
        onGoingMigrations.forEach(currentMigration -> {
            if (currentMigration.getRemainingSize() < minRemainingTime[0]) {
                minRemainingTime[0] = currentMigration.getRemainingSize();
            }
        });

        timeStamp += minRemainingTime[0];
        List<Migration> toBeRemoved = new ArrayList<>();
        onGoingMigrations.forEach(currentMigration -> {
            currentMigration.setRemainingSize(currentMigration.getRemainingSize() - minRemainingTime[0]);
            if (currentMigration.getRemainingSize() == 0) {
                toBeRemoved.add(currentMigration);
                pipelineDegree++;
                finished.add(currentMigration);
                //remove initial vm running on source
                cloud.removeFromCurrent(currentMigration.getVm() , currentMigration.getSource());
                System.out.println("Migration Finished " + currentMigration + " at " + timeStamp);
                System.out.println("free pipelineDegree :" + pipelineDegree);
            }
        });

        onGoingMigrations.removeAll(toBeRemoved);
        return finished;
    }


    public void doMigration() throws Exception {
        List<Migration> queue = cloud.getMigrations();

      //  cloud.setMigrationTimes(queue);
    //    Collections.sort(queue);

        queue.addAll(cloud.getNextPhaseMigrations());

        System.out.println("Migration Order :");
        System.out.println(queue);

        while (!queue.isEmpty()) {

            boolean allIsChecked = false;
            //we must handle a case where there is no feasible migration for a while to use all the pipelineDegree
            while (pipelineDegree > 0  && !allIsChecked) {

                for (int i = 0; i < queue.size(); i++) {
                    //second term checks if destination has capacity, otherwise checks next migration
                    if (!onGoingMigrations.contains(queue.get(i)) && cloud.hasFreeCapacityFor(cloud.getCurrentAssignments() , queue.get(i).getDestination() , queue.get(i).getVm())
                            && linksHaveCapacity(queue.get(i))) {
                        System.out.println("i" + i + "   free degree :" + pipelineDegree );
                        startMigration(queue.get(i));
                         break;
                    }
                if (i == queue.size()-1 ) allIsChecked = true;
                }
            }

            queue.removeAll(finishNextMigration());

        }

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

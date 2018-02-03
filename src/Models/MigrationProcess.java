package Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by I857455 on 2/2/2018.
 */
public class MigrationProcess {



    private Network network;
    private int degree;
    private List<Migration> onGoingMigrations;
    private int timeStamp;

    public Network getNetwork() {
        return network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public int getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public List<Migration> getOnGoingMigrations() {
        return onGoingMigrations;
    }

    public void setOnGoingMigrations(List<Migration> onGoingMigrations) {
        this.onGoingMigrations = onGoingMigrations;
    }

    public MigrationProcess() {
        this.onGoingMigrations = new ArrayList<>();
        timeStamp=0;
    }

    private void startMigration(Migration m) throws Exception {
        network.assignToCurrentLocation(m.getVm() , m.getDestination());
        onGoingMigrations.add(m);
        degree--;
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
                degree++;
                finished.add(currentMigration);
                network.removeFromCurrent(currentMigration.getVm() , currentMigration.getDestination());
                System.out.println("Migration Finished " + currentMigration + " at " + timeStamp);
                System.out.println("free degree :" + degree);
            }
        });

        onGoingMigrations.removeAll(toBeRemoved);
        return finished;
    }


    public void doMigration() throws Exception {
        List<Migration> queue = network.getMigrations();

        while (!queue.isEmpty()) {

            boolean allIsChecked = false;
            //we must handle a case where there is no feasible migration for a while to use all the degree
            while (degree > 0  && !allIsChecked) {

                for (int i = 0; i < queue.size(); i++) {
                    if (!onGoingMigrations.contains(queue.get(i))) { //todo && migration can be done empty space in the destination
                        startMigration(queue.get(i));
                         break;
                    }
                allIsChecked = true;
                }
            }

            queue.removeAll(finishNextMigration());

        }

    }

}

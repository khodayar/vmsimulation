package Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by I857455 on 2/2/2018.
 */
public class MigrationProcess {


    private List<Migration> migrations;
    private int degree;
    private List<Migration> currentMigrations;
    private int timeStamp;


    public List<Migration> getMigrations() {
        return migrations;
    }

    public void setMigrations(List<Migration> migrations) {
        this.migrations = migrations;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public List<Migration> getCurrentMigrations() {
        return currentMigrations;
    }

    public void setCurrentMigrations(List<Migration> currentMigrations) {
        this.currentMigrations = currentMigrations;
    }

    public MigrationProcess() {
        this.currentMigrations = new ArrayList<>();
        timeStamp=0;
    }

    private void startMigration(Migration m) {
        currentMigrations.add(m);
        degree--;
        System.out.println("Migration Started " + m + " at " + timeStamp);

    }

    private List<Migration> finishNextMigration() {
        List<Migration> finished = new ArrayList<>();
        final int[] minRemainingTime = {1000};
        currentMigrations.forEach(currentMigration -> {
            if (currentMigration.getRemainingSize() < minRemainingTime[0]) {
                minRemainingTime[0] = currentMigration.getRemainingSize();
            }
        });

        timeStamp += minRemainingTime[0];
        List<Migration> toBeRemoved = new ArrayList<>();
        currentMigrations.forEach(currentMigration -> {
            currentMigration.setRemainingSize(currentMigration.getRemainingSize() - minRemainingTime[0]);
            if (currentMigration.getRemainingSize() == 0) {
                toBeRemoved.add(currentMigration);
                degree++;
                finished.add(currentMigration);
                System.out.println("Migration Finished " + currentMigration + " at " + timeStamp);
            }
        });

        currentMigrations.removeAll(toBeRemoved);
        return finished;
    }


    public void doMigration() {
        List<Migration> queue = migrations;

        while (!queue.isEmpty()) {

            while (degree > 0 && queue.size() >1) {

                for (int i = 0; i < queue.size(); i++) {
                    if (!currentMigrations.contains(queue.get(i))) { //todo && migration can be done empty space in the destination
                        startMigration(queue.get(i));
                        break;
                    }

                }
            }

            queue.removeAll(finishNextMigration());

        }

    }

}

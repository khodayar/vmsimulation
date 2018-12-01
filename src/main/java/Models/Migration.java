package Models;

/**
 * Created by Khodayar on 1/20/2018.
 */
public class Migration implements Comparable<Migration>{
    private PM source;
    private PM destination;
    private VM vm;
    private int weight;
    private int remainingSize;
    private PM finalDestination;
    private int depLevel;


    public PM getFinalDestination() {
        return finalDestination;
    }

    public void setFinalDestination(PM finalDestination) {
        this.finalDestination = finalDestination;
    }

    public PM getSource() {
        return source;
    }

    public void setSource(PM source) {
        this.source = source;
    }

    public PM getDestination() {
        return destination;
    }

    public void setDestination(PM destination) {
        this.destination = destination;
    }

    public VM getVm() {
        return vm;
    }

    public void setVm(VM vm) {
        this.vm = vm;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
        this.getVm().setMigrationWeight(weight);
    }

    public int getRemainingSize() {
        return remainingSize;
    }

    public void setRemainingSize(int remainingSize) {
        this.remainingSize = remainingSize;
    }

    public int getDepLevel() {
        return depLevel;
    }

    public void setDepLevel(int depLevel) {
        this.depLevel = depLevel;
    }

    public Migration(PM source, PM destination, VM vm , PM finalDestination) {
        this.source = source;
        this.destination = destination;
        this.vm = vm;
        this.remainingSize = vm.getMemorySize();
        this.finalDestination = finalDestination;
        this.depLevel = 0;
    }

    @Override
    public String toString() {
        return "Migration{" +
                vm.getName() + " : " +
                source.getName() +
                " -> " + destination.getName() +
                " final:" + finalDestination.getName() +
                " weight :" + weight +
                " DP Level :" + depLevel +
               "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Migration migration = (Migration) o;

        if (!source.equals(migration.source)) return false;
        if (!destination.equals(migration.destination)) return false;
        return vm.equals(migration.vm);
    }

    @Override
    public int hashCode() {
        int result = source.hashCode();
        result = 31 * result + destination.hashCode();
        result = 31 * result + vm.hashCode();
        return result;
    }


    public boolean isTemp(){
        return !destination.getName().equals(finalDestination.getName());
    }


    @Override
    public int compareTo(Migration o) {
        if (weight > o.getWeight()) return -1;
        else if (weight < o.getWeight()) return 1;
        else return 0;
    }
}

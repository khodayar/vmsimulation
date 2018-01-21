package Models;

/**
 * Created by Khodayar on 1/20/2018.
 */
public class Migration {
    private PM source;
    private PM destination;
    private VM vm;

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

    public Migration(PM source, PM destination, VM vm) {
        this.source = source;
        this.destination = destination;
        this.vm = vm;
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
}

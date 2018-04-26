package Models;

/**
 * Created by I857455 on 4/16/2018.
 */
public class ComplexDependency {
    private VMSet vmSet;
    private PM source;
    private PM destination;

    public ComplexDependency() {
    }

    public ComplexDependency(VMSet vmSet, PM source, PM destination) {
        this.vmSet = vmSet;
        this.source = source;
        this.destination = destination;
    }

    public VMSet getVmSet() {
        return vmSet;
    }

    public void setVmSet(VMSet vmSet) {
        this.vmSet = vmSet;
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
}

package Models;

import com.sun.istack.internal.NotNull;
import java.util.List;
import java.util.Objects;

/**
 * Created by I857455 on 1/18/2018.
 */
public class VM {
    private String name;
    private int memorySize;
    private int processorSize;
    private int network;
    @NotNull
    private PM location;


    public int getMemorySize() {
        return memorySize;
    }

    public void setMemorySize(int memorySize) {
        this.memorySize = memorySize;
    }

    public int getProcessorSize() {
        return processorSize;
    }

    public void setProcessorSize(int processorSize) {
        this.processorSize = processorSize;
    }

    public PM getLocation() {
        return location;
    }

    public void setLocation(PM location) {
        this.location = location;
    }

    public int getNetwork() {
        return network;
    }

    public void setNetwork(int network) {
        this.network = network;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public VM(String name, int memorySize, int processorSize, int network) {
        this.name = name;
        this.memorySize = memorySize;
        this.processorSize = processorSize;
        this.network = network;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VM vm = (VM) o;
        return memorySize == vm.memorySize &&
                processorSize == vm.processorSize &&
                network == vm.network &&
                Objects.equals(name, vm.name) &&
                Objects.equals(location, vm.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, memorySize, processorSize, network, location);
    }


    @Override
    public String toString() {
        return "VM{" +
                "name='" + name + '\'' +
                '}';
    }

    public String getInformation() {
        return "VM{" +
                "name='" + name + '\'' +
                ", memorySize=" + memorySize +
                ", processorSize=" + processorSize +
                ", network=" + network +
                ", location=" + location +
                '}';
    }
}

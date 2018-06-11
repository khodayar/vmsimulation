package Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by I857455 on 1/18/2018.
 */
public class PM {
    private String name;
    private int memoryCapacity;
    private int networkCapacity;
    private int processorCapacity;

    public int getMemoryCapacity() {
        return memoryCapacity;
    }

    public void setMemoryCapacity(int memoryCapacity) {
        this.memoryCapacity = memoryCapacity;
    }

    public int getNetworkCapacity() {
        return networkCapacity;
    }

    public void setNetworkCapacity(int networkCapacity) {
        this.networkCapacity = networkCapacity;
    }

    public int getProcessorCapacity() {
        return processorCapacity;
    }

    public void setProcessorCapacity(int processorCapacity) {
        this.processorCapacity = processorCapacity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public PM(String name, int memoryCapacity, int networkCapacity, int processorCapacity) {
        this.name = name;
        this.memoryCapacity = memoryCapacity;
        this.networkCapacity = networkCapacity;
        this.processorCapacity = processorCapacity;

    }

    @Override
    public String toString() {
        return "PM{" +
                "name='" + name + '\'' +
                ", memoryCapacity=" + memoryCapacity +
                ", networkCapacity=" + networkCapacity +
                ", processorCapacity=" + processorCapacity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PM pm = (PM) o;

        return name.equals(pm.name);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + memoryCapacity;
        result = 31 * result + networkCapacity;
        result = 31 * result + processorCapacity;
        return result;
    }


    public PM copy(){
        return new PM(this.getName() + "-copy" , this.memoryCapacity , this.networkCapacity , this.processorCapacity);
    }
}

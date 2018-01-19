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
    List<VM> residedVMs;


    public List<VM> getResidedVMs() {
        return residedVMs;
    }

    public void setResidedVMs(List<VM> residedVMs) {
        this.residedVMs = residedVMs;
    }

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

    public int getFreeMemory(){
        final int[] freeMemory = {memoryCapacity};
        residedVMs.forEach(vm -> {
            freeMemory[0] -= vm.getMemorySize();
        });
        return freeMemory[0];
    }


    public int getFreeProcessor(){
        final int[] freeCPU = {processorCapacity};
        residedVMs.forEach(vm -> {
            freeCPU[0] -= vm.getProcessorSize();
        });
        return freeCPU[0];
    }

    public int getFreeNetwork(){
        final int[] freeNet = {networkCapacity};
        residedVMs.forEach(vm -> {
            freeNet[0] -= vm.getNetwork();
        });
        return freeNet[0];
    }

    public boolean hasFreeCapacityFor(VM vm){
        return vm.getMemorySize() < getFreeMemory() && vm.getProcessorSize() < getFreeProcessor()
                && vm.getNetwork() < getFreeNetwork();
    }


    //assignment has different mode, when you assign but not moved yet, during migration and start on new location
    public void assignVM(VM vm){
        //make the capacity smaller , before migration
        if (!this.hasFreeCapacityFor(vm)){
            System.out.println("There is no free capacity");
        }
        else {
            residedVMs.add(vm);
            vm.setLocation(this);
        }
    }

    //or turn off after the migration is finished
    public void removeVM(VM vm){

        if (residedVMs.indexOf(vm) < 0) {
           System.out.println("there is no such a VM located in this PM");
        } else {
            residedVMs.remove(vm);
            vm.setLocation(null);
        }
    }

    public boolean isVMHere(VM vm){
        return residedVMs.indexOf(vm) > -1;
    }


    public PM(String name, int memoryCapacity, int networkCapacity, int processorCapacity) {
        this.name = name;
        this.memoryCapacity = memoryCapacity;
        this.networkCapacity = networkCapacity;
        this.processorCapacity = processorCapacity;
        this.residedVMs = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "PM{" +
                "name='" + name + '\'' +
                ", memoryCapacity=" + memoryCapacity +
                ", networkCapacity=" + networkCapacity +
                ", processorCapacity=" + processorCapacity +
                ", residedVMs=" + residedVMs +
                '}';
    }
}

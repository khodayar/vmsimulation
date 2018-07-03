package Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Khodayar on 1/27/2018.
 */
public class VMSet {
    List<VM> VMList;

    public VMSet() {
        VMList = new ArrayList<>();
    }

    public void add(VM vm){
        VMList.add(vm);
    }
    public List<VM> getVMList() {
        return VMList;
    }

    public void setVMList(List<VM> VMList) {
        this.VMList = VMList;
    }

    @Override
    public String toString() {
        final String[] str = new String[1];
        str[0] = "{";
        if (VMList.size() > 0) {

            VMList.forEach(vm -> {
                str[0] = str[0] + vm.getName() + ",";
            });

            str[0] = str[0].substring(0, str[0].length() - 1);

        }
        str[0] += '}';
        return str[0];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VMSet vmSet = (VMSet) o;

        return VMList != null ? VMList.equals(vmSet.VMList) : vmSet.VMList == null;
    }

    @Override
    public int hashCode() {
        return VMList != null ? VMList.hashCode() : 0;
    }


    public int getWeightSum(){
        final int[] weight = {0};
        VMList.forEach(vm -> {
            weight[0] += vm.getMemorySize();
        });
        return weight[0];
    }
}

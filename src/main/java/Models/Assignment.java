package Models;

import Models.PM;

/**
 * Created by Khodayar on 1/20/2018.
 */
public class Assignment {
    private PM pm;
    private VM vm;

    public PM getPm() {
        return pm;
    }

    public void setPm(PM pm) {
        this.pm = pm;
    }

    public Assignment(PM pm, VM vm) {
        this.pm = pm;
        this.vm = vm;
    }

    public VM getVm() {
        return vm;
    }

    public void setVm(VM vm) {
        this.vm = vm;
    }
}

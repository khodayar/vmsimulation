package Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by I857455 on 1/18/2018.
 */
public class Network {

    List<PM> pmList;

    public Network() {
        this.pmList = new ArrayList<>();
    }

    public Network(List<PM> pmList) {
        this.pmList = pmList;
    }

    public List<PM> getPmList() {
        return pmList;
    }

    public void setPmList(List<PM> pmList) {
        this.pmList = pmList;
    }


    public void showAssignments() {
        System.out.println("-------------------------");
        pmList.forEach(pm -> {
            System.out.print("PM : " + pm.getName() + "  : {");
            System.out.print(pm.getResidedVMs());
            System.out.println("}");
        });

    }

}

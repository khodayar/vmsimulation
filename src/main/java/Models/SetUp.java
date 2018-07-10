package Models;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by I857455 on 4/16/2018.
 */
public class SetUp {

    private static List<VM> vmList = new ArrayList<>();


    public static void readSetUp(Cloud cloud) throws Exception {


        String cloudSetUp = null;
        try(BufferedReader br = new BufferedReader(new FileReader("src/main/Feed/setup.txt"))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
             cloudSetUp = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] tokens = cloudSetUp.split("\\r?\\n");
        String setupCase = "" ;
        for (int i = 0;i<tokens.length; i++){
            if (tokens[i].indexOf("definition") > -1){
                setupCase = tokens[i];
                continue;
            }

            if (setupCase.equals("pm definition")){
                String pmRead[] = tokens[i].split(" ");
                PM pm = new PM(pmRead[0] , Integer.valueOf(pmRead[1]),Integer.valueOf(pmRead[3]),Integer.valueOf(pmRead[2]));
                cloud.getPmList().add(pm);
            }
            else if (setupCase.equals("vm definition")){
                String VMRead[] = tokens[i].split(" ");
                VM vm = new VM(VMRead[0] , Integer.valueOf(VMRead[1]),Integer.valueOf(VMRead[2]),Integer.valueOf(VMRead[3]));
                vmList.add(vm);
            }
            else if (setupCase.equals("current assignment definition")){
                String assignment[] = tokens[i].split(" ");
                PM pm = cloud.getPmList().stream().filter(thispm -> thispm.getName().equals(assignment[0])).findFirst().orElse(null);
                    for (int v=1;v<assignment.length;v++){
                        int finalV = v;
                        VM vm = vmList.stream().filter(thisvm -> thisvm.getName().equals(assignment[finalV])).findFirst().orElse(null);
                        cloud.assignToCurrentLocation(vm, pm, false);

                    }
            }
            else if (setupCase.equals("new assignment definition")){
                String assignment[] = tokens[i].split(" ");
                PM pm = cloud.getPmList().stream().filter(thispm -> thispm.getName().equals(assignment[0])).findFirst().orElse(null);
                for (int v=1;v<assignment.length;v++){
                    int finalV = v;
                    VM vm = vmList.stream().filter(thisvm -> thisvm.getName().equals(assignment[finalV])).findFirst().orElse(null);
                    cloud.assignToNewLocation(vm , pm);
                }
            }

        }
        cloud.getReport().setNumberOFMig(cloud.generateMigrations().size());
    }



}

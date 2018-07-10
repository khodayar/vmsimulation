package Models;

import java.io.BufferedReader;
import java.io.FileReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Khodayar on 6/11/2018.
 */
public class CsvReader {


    public static void readFile(Cloud cloud, String filePath) {
        String csvFile = filePath;
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        int defaultCapacity = 1000;

        String stage = "";

        ArrayList<String> VMRead = new ArrayList<String>();

        try {

            br = new BufferedReader(new FileReader(csvFile));

            while ((line = br.readLine()) != null) {
                String[] thisline = line.split(cvsSplitBy);

                if (stage == "") {
                    VMRead.add(line);
                }

                if (thisline.length > 0 && thisline[0].equals("Server") && stage.equals("")) {
                    stage = "servers";
                    continue;
                }

                //reading PMs
                if (thisline.length > 0 && stage.equals("servers") && !thisline[0].equals("TREE DATA") && !thisline[0].equals("CLASSES:")) {
                    PM pm = new PM("PM-" + thisline[0], Integer.parseInt(thisline[4]), defaultCapacity,
                            Integer.parseInt(thisline[1]));
                    cloud.getPmList().add(pm);
                }
            }

            //now read VMs

            for (String s : VMRead) {
                String[] thisline = s.split(cvsSplitBy);

                if (thisline.length > 0 && thisline[0].equals("VM")) {
                    stage = "vms";
                    continue;
                }

                //reading PMs
                if (thisline.length > 0 && stage.equals("vms")) {
                    VM vm = new VM("VM-" + thisline[0], Integer.parseInt(thisline[5]), Integer.parseInt(thisline[4]),
                            1);
                    PM iPm = cloud.findPMByName("PM-" + thisline[1]);
                    cloud.assignToCurrentLocation(vm, iPm, false);
                    PM fPm = cloud.findPMByName("PM-" + thisline[2]);
                    cloud.assignToNewLocation(vm, fPm);
                }

                if (thisline.length == 0 && stage.equals("vms")) {
                    stage = "finished";
                }
            }

/*
            VM	I	F	N	CPU	Memory	Links
            1	1	1	8	2	8	    1
            0   1   2   3   4   5       6
*/

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        cloud.getReport().setFileName(filePath);
        cloud.getReport().setNumberOFPms(cloud.getPmList().size());
        cloud.getReport().setNumberOfVMs(cloud.getVMs().size());
        cloud.getReport().setNumberOFMig(cloud.generateMigrations().size());
    }


}

package Models;

import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by I857455 on 7/12/2018.
 */
public class CsvWriter {

    public static void addReportToCsv (Report report , String set, boolean firsLine) throws IOException {

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");

        //Instance,Total Time,SV,VM,VM To Move (Migrations),optimal,time, Finished Migrations, Temporary Migrations, Initial Cycles,Solved Cycles,Failed Attempts For Solving Cycles, CPU Time
        FileWriter pw = null;
        String resultFile = "results//report_" + set + ".csv";
        pw = new FileWriter (resultFile, true);

        StringBuilder sb = new StringBuilder();
        if (firsLine) sb.append("Instance,Total Time,SV,VM,VM To Move (Migrations),optimal,time, Finished Migrations, Temporary Migrations, Initial Cycles,Solved Cycles,Failed Attempts For Solving Cycles, CPU time\n");
        sb.append(report.getFileName());
        sb.append(',');
        sb.append("");
        sb.append(',');
        sb.append(report.getNumberOFPms());
        sb.append(',');
        sb.append(report.getNumberOfVMs());
        sb.append(',');
        sb.append(report.getNumberOFMig());
        sb.append(',');
        sb.append("");
        sb.append(',');
        sb.append(report.getTimeSteps());
        sb.append(',');
        sb.append(report.getNumberOFFinishedMigs());
        sb.append(',');
        sb.append(report.getNumberOFTempMig());
        sb.append(',');
        sb.append(report.getNumberOfInitialCycles());
        sb.append(',');
        sb.append(report.getNumberOfSolvedCycles());
        sb.append(',');
        sb.append(report.getNumberOfFailedAttempts());
        sb.append(',');
        long duration = 0;
        try {
             duration = sdf.parse(report.getTimeStampFinished()).getTime()  - sdf.parse(report.getTimeStampCalcStarted()).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sb.append(duration);
        sb.append(',');
//        sb.append(report.getMaxOngoingMigs());
//        sb.append(',');
//        sb.append(report.getInitialCapacityOfnetwork());
//        sb.append(',');
//        sb.append(report.getNextCapacityOfNetwork());
//        sb.append(',');
          sb.append('\n');

        pw.append(sb.toString());
        pw.close();
        System.out.println("Adding " + report.getFileName() + " report is done!");
    }










}

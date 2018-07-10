package Models;

/**
 * Created by I857455 on 7/10/2018.
 */
public class Report {

    int numberOFPms;
    int numberOfVMs;
    int numberOFMig;
    int numberOFFinishedMigs;
    int numberOfOnGoingMigs;
    int numberOfInitialCycles;
    int numberOfSolvedCycles;
    int numberOfCurrentCycles;
    int numberOfFailedAttemptsForSolcingCycles;
    int numberOFTempMig;
    int timeSteps;

    String timeStampCalcStarted;
    String timeStampMigStart;
    String timeStampFinished;
    String fileName;



    @Override
    public String toString() {
        return "Report{" +
                "  FileName='" + fileName + " \n" +
                ", number Of servers= " + numberOFPms + " \n" +
                ", number Of VMs= " + numberOfVMs + " \n" +
                ", number Of Migrations= " + numberOFMig + " \n" +
                ", number OF Finished Migrations= " + numberOFFinishedMigs + " \n" +
                ", number OF Temporary Migrations= " + numberOFTempMig + " \n" +
                ", number Of OnGoing Migrations= " + numberOfOnGoingMigs + " \n" +
                ", number Of Initial Cycles= " + numberOfInitialCycles + " \n" +
                ", number Of Solved Cycles= " + numberOfSolvedCycles + " \n" +
                ", number Of Failed Attempts For Solving Cycles= " + numberOfFailedAttemptsForSolcingCycles + " \n" +
                ", number Of Current Cycles= " + numberOfCurrentCycles + " \n" +
                ", number Of Time Steps= " + timeSteps + " \n" +
                ", timeStamp Calc Started= " + timeStampCalcStarted + " \n" +
                ", timeStamp Migrations Started= " + timeStampMigStart + " \n" +
                ", timeStamp Finished= " + timeStampFinished +
                '}' + " \n" ;
    }


    public int getTimeSteps() {
        return timeSteps;
    }

    public void setTimeSteps(int timeSteps) {
        this.timeSteps = timeSteps;
    }

    public int getNumberOFPms() {
        return numberOFPms;
    }

    public void setNumberOFPms(int numberOFPms) {
        this.numberOFPms = numberOFPms;
    }

    public int getNumberOfVMs() {
        return numberOfVMs;
    }

    public void setNumberOfVMs(int numberOfVMs) {
        this.numberOfVMs = numberOfVMs;
    }

    public int getNumberOFMig() {
        return numberOFMig;
    }

    public void setNumberOFMig(int numberOFMig) {
        this.numberOFMig = numberOFMig;
    }

    public int getNumberOFFinishedMigs() {
        return numberOFFinishedMigs;
    }

    public void setNumberOFFinishedMigs(int numberOFFinishedMigs) {
        this.numberOFFinishedMigs = numberOFFinishedMigs;
    }

    public int getNumberOfOnGoingMigs() {
        return numberOfOnGoingMigs;
    }

    public void setNumberOfOnGoingMigs(int numberOfOnGoingMigs) {
        this.numberOfOnGoingMigs = numberOfOnGoingMigs;
    }

    public int getNumberOfInitialCycles() {
        return numberOfInitialCycles;
    }

    public void setNumberOfInitialCycles(int numberOfInitialCycles) {
        this.numberOfInitialCycles = numberOfInitialCycles;
    }

    public int getNumberOfSolvedCycles() {
        return numberOfSolvedCycles;
    }

    public void setNumberOfSolvedCycles(int numberOfSolvedCycles) {
        this.numberOfSolvedCycles = numberOfSolvedCycles;
    }

    public int getNumberOfCurrentCycles() {
        return numberOfCurrentCycles;
    }

    public void setNumberOfCurrentCycles(int numberOfCurrentCycles) {
        this.numberOfCurrentCycles = numberOfCurrentCycles;
    }

    public String getTimeStampCalcStarted() {
        return timeStampCalcStarted;
    }

    public void setTimeStampCalcStarted(String timeStampCalcStarted) {
        this.timeStampCalcStarted = timeStampCalcStarted;
    }

    public String getTimeStampMigStart() {
        return timeStampMigStart;
    }

    public void setTimeStampMigStart(String timeStampMigStart) {
        this.timeStampMigStart = timeStampMigStart;
    }

    public String getTimeStampFinished() {
        return timeStampFinished;
    }

    public void setTimeStampFinished(String timeStampFinished) {
        this.timeStampFinished = timeStampFinished;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getNumberOfFailedAttempts() {
        return numberOfFailedAttemptsForSolcingCycles;
    }

    public void setNumberOfFailedAttempts(int numberOfFailedAttemptsForSolcingCycles) {
        this.numberOfFailedAttemptsForSolcingCycles = numberOfFailedAttemptsForSolcingCycles;
    }


    public int getNumberOfFailedAttemptsForSolcingCycles() {
        return numberOfFailedAttemptsForSolcingCycles;
    }

    public void setNumberOfFailedAttemptsForSolcingCycles(int numberOfFailedAttemptsForSolcingCycles) {
        this.numberOfFailedAttemptsForSolcingCycles = numberOfFailedAttemptsForSolcingCycles;
    }

    public int getNumberOFTempMig() {
        return numberOFTempMig;
    }

    public void setNumberOFTempMig(int numberOFTempMig) {
        this.numberOFTempMig = numberOFTempMig;
    }
}

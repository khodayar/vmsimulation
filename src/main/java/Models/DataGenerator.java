package Models;

/**
 * Created by I857455 on 4/27/2018.
 */
public class DataGenerator {

    private static final int PM_MEMORY_SIZE = 100;
    private static final int PM_CPU_SIZE = 100;
    private static final int PM_NET_SIZE = 100;


    public static void setUpCloud (int numberOfPms , int numberOfVMs , int numberOfFreePms, int optimalPMRate , Cloud cloud){

        //creating PMs
        PM[] pms = new PM[numberOfPms];
        for (int i = 0; i < numberOfPms; i++) {
            pms[i] = new PM("pm" + i, PM_MEMORY_SIZE, PM_NET_SIZE, PM_CPU_SIZE);
            cloud.getPmList().add(pms[i]);
        }

        int cloudExpectedFreeMemory = Math.toIntExact(Math.round(PM_MEMORY_SIZE * (1.0 - (double) optimalPMRate / 100)));


        int averageVmMemorySize = (numberOfPms - numberOfFreePms) * Math.round(optimalPMRate* PM_MEMORY_SIZE /(numberOfVMs * 100));

        for (int j = 0; j < numberOfVMs; j++) {
            VM currentVm = createRndVM(j , averageVmMemorySize);
            for (int i = 0; i < numberOfPms; i++) {
                if (cloud.hasFreeCapacityFor(cloud.getNewAssignments(), pms[i], currentVm) && (cloud.freeMemory(cloud.getNewAssignments(), pms[i]) > cloudExpectedFreeMemory + averageVmMemorySize/2 )) {
                    try {
                        cloud.assignToNewLocation(currentVm, pms[i]);
                    } catch (Exception e) {
                        //handled in assignToCurrentLocation
                    }
                    break;
                }
            }
        }

        cloud.getNewAssignments().forEach(assignment -> {
            VM vm = assignment.getVm();
            boolean assigned = false;
            while (!assigned) {
                PM pm = cloud.getPmList().get((int) (Math.random() * (cloud.getPmList().size())));
                if (cloud.hasFreeCapacityFor(cloud.getCurrentAssignments(), pm, vm)) {
                    try {
                        cloud.assignToCurrentLocation(vm, pm, false);
                    } catch (Exception e) {
                    }

                    assigned = true;
                }

            }

        });

    }


    public void assignRndNewLocations() {

    }


    private static VM createRndVM(int nameIndex, int memorySize) {
        int min = Math.toIntExact((long) (memorySize * 1.2));
        int max = Math.toIntExact((long) (memorySize * 0.8));
        return new VM("vm" + nameIndex, (int) (Math.random() * ((max - min) + 1)) + min,
                (int) (Math.random() * ((max - min) + 1)) + min, (int) (Math.random() * ((max - min) + 1)) + min);
    }


}

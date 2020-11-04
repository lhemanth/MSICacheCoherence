

import java.util.ArrayList;
import java.util.Scanner;


enum States{
    INVALID, SHARED, MODIFIED
}

class ProcessorMachine{
    CacheStore cacheStore;
    boolean isValid;

    public ProcessorMachine() {
        this.cacheStore = new CacheStore();
        this.isValid = false;
    }

    @Override
    public String toString() {
        return "ProcessorMachine{" +
                "cacheStore=" + cacheStore +
                ", isValid=" + isValid +
                '}';
    }

    public CacheStore getCacheStore() {
        return cacheStore;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }
}


class CacheStore{
    ArrayList<String> memoryStore = new ArrayList<>();
    States currentState;

    public CacheStore()
    {
        this.currentState = States.INVALID;
    }

    @Override
    public String toString() {
        return "CacheStore{" +
                "memoryLocation=" + memoryStore +
                ", currentState=" + currentState +
                '}';
    }

    public ArrayList<String> getMemoryStore() {
        return memoryStore;
    }

    public void setMemoryStore(ArrayList<String> memoryStore) {
        this.memoryStore = memoryStore;
    }

    public States getCurrentState() {
        return currentState;
    }

    public void setCurrentState(States currentState) {
        this.currentState = currentState;
    }
}
public class MSIStateMachine {

    public static int numberOfProcessors;
    public static int numberOfMemoryLocations;

    public static ArrayList<String> sharedMemoryLocations;
    public static ProcessorMachine[] processorMachines;

    //Initialization
    static{
        numberOfProcessors = 4;
        numberOfMemoryLocations = 4;

        sharedMemoryLocations = new ArrayList<>(numberOfMemoryLocations);
        sharedMemoryLocations.add("Apple");
        sharedMemoryLocations.add("Google");
        sharedMemoryLocations.add("Cisco");
        sharedMemoryLocations.add("Dell");

        processorMachines = new ProcessorMachine[numberOfProcessors];
        for(int i=0;i<numberOfProcessors;i++)
        {
            processorMachines[i] = new ProcessorMachine();
        }
    }

    public static void main(String[] args) {

        Scanner reader = new Scanner(System.in); // Reading from System.in

        System.out.println("MSI Protocol\n");
        System.out.print("Intitial Data in memory:");
        print(sharedMemoryLocations);

        String input;
        int processorId;
        int memoryLocation;
        do {
            System.out.println("\n \nPlease choose one of the below operation or Enter as :");
            System.out.println(
                    "P ---> for Print state" +"\nR ---> for Read Data"+ "\nW ---> for Write Data"+ "\nE ---> for Exit");
            System.out.println("Choose one: ");
            input = reader.next().toUpperCase();
          
            
            if(input.equals("P")) {
   
                System.out.print("\nCurrent Data in Memory: ");
                print(sharedMemoryLocations);
                for (int i = 0; i < numberOfProcessors; i++) {
                    System.out.printf("\n Processor %d: \n", i+1);

                    if (processorMachines[i].isValid) {
                        States cacheStatus = processorMachines[i].getCacheStore().getCurrentState();

                        switch (cacheStatus) {
                            case INVALID:
                                System.out.println("State: INVALID");
                                break;
                            case SHARED:
                                System.out.print("Data in the processor:");
                                print(processorMachines[i].getCacheStore().getMemoryStore());
                                System.out.println("State: SHARED");
                                break;

                            case MODIFIED:
                                System.out.print("Data in the processor:");
                                print(processorMachines[i].getCacheStore().getMemoryStore());
                                System.out.println("State: MODIFIED");
                                break;
                            default:
                                break;
                        }
                    } else {
                        System.out.println("Initially No Data in Processor. State: INVALID.");
                    }
                }
                
        }
        
                else if (input.equals("R")) {
           
                
                do {
                    System.out.println("Choose Processor value should be between 1 to 4");
                    while (!reader.hasNextInt()) {
                        System.out.println("Wrong input. Choose Processor value should be between 1 to 4, Enter Here:");
                        reader.next();
                    }
                    processorId = reader.nextInt();
                } while (processorId < 1 || processorId > 4);

                do {
                    System.out.println("Choose Memory Location value should be between 1 to 4: ");
                    while (!reader.hasNextInt()) {
                        System.out.println("Wrong Input. Memory Location value should be between 1 to 4, Enter Here:");
                        reader.next();
                    }
                    memoryLocation = reader.nextInt();
                } while (memoryLocation < 1 || memoryLocation > 4);
                readDataInBlock(processorId-1, memoryLocation-1);
                
                }
                else if (input.equals("W")) {
                	
                    String newValue;

                    do {
                        System.out.println("Choose Processor value should be between 1 to 4 ");
                        while (!reader.hasNextInt()) {
                            System.out.println("Wrong Input. Enter value Between 1 to 4, Enter Here:");
                            reader.next();
                        }
                        processorId = reader.nextInt();
                    } while (processorId < 1 || processorId > 4);

                    do {
                        System.out.println("Choose Memory Location value should be between 1 to 4:");
                        while (!reader.hasNextInt()) {
                            System.out.println("Wrong Input. Memory Location value should be between 1 to 4, Enter Here:");
                            reader.next();
                        }
                        memoryLocation = reader.nextInt();
                    } while (memoryLocation < 1 || memoryLocation > 4);

                    System.out.println("Please enter new value here: ");
                    while (!reader.hasNext()) {
                        System.out.println("Invalid");
                        reader.next();
                    }
                    newValue = reader.next();
                    writeDataInBlock(newValue, processorId-1,memoryLocation-1);
                
                }
                else if(input.equals("E")){
                	System.out.println("Exiting the Protocol");
                	break;
                }
                else {
                	System.out.println("Wrong input! Give input as one of the following: 'p' 'r' 'w' or 'e'");
                	
                }
            
            }while (!"e".equals(input));
        System.out.println("State Machine terminated!!!");
        reader.close();

    }

    private static void print(ArrayList<String> value){
        System.out.print("[ ");
        for (String s : value) {
            System.out.printf(" %s ", s);
        }
        System.out.print(" ]\n");
    }

    private static void readDataInBlock(int processorId, int memoryLocation) {
        if(processorMachines[processorId].isValid)
        {
            System.out.println("Data is available in cache");
            States currentState = processorMachines[processorId].getCacheStore().currentState;
            switch (currentState) {
                case INVALID:
                    System.out.println(
                            "ReadMiss request is sent in the bus as Processor is in INVALID and state changed to SHARED. Data is read from the memory.");
                    readMissRequest(processorId);
                    processorMachines[processorId].getCacheStore().setCurrentState(States.SHARED);
                    processorMachines[processorId].getCacheStore().setMemoryStore(sharedMemoryLocations);
                    System.out.println("Data: "+ processorMachines[processorId].getCacheStore().getMemoryStore().get(memoryLocation));
                    break;
                case SHARED:
                    System.out.println("Local Read as Processor State Machine is in SHARED state.");
                    System.out.println("Data: "+ processorMachines[processorId].getCacheStore().getMemoryStore().get(memoryLocation));
                    break;

                case MODIFIED:
                    System.out.println("Local Read as Processor State Machine is in MODIFIED state.");
                    System.out.println("Data: "+ processorMachines[processorId].getCacheStore().getMemoryStore().get(memoryLocation));
                    break;
                default:
                    break;
            }
        }
        else {
            System.out.println("ReadMiss Request is sent to the bus and then state is changed to SHARED from INVALID");
            readMissRequest(processorId);
            processorMachines[processorId].getCacheStore().setCurrentState(States.SHARED);
            processorMachines[processorId].getCacheStore().setMemoryStore(sharedMemoryLocations);
            System.out.println("Data : " + sharedMemoryLocations.get(memoryLocation));
        }
        processorMachines[processorId].setValid(true);
    }

    private static void writeDataInBlock(String newValue, int processorId, int memoryLocation) {
        if(!processorMachines[processorId].isValid)
        {
            System.out.println("Data not found in cache state is changed from INVALID to MODIFIED .");
            writeMissRequest(processorId);

            processorMachines[processorId].getCacheStore().setCurrentState(States.MODIFIED);
            processorMachines[processorId].getCacheStore().setMemoryStore(sharedMemoryLocations);
            processorMachines[processorId].getCacheStore().getMemoryStore().set(memoryLocation,newValue);

            System.out.println("New Data: "+
                            processorMachines[processorId].getCacheStore().getMemoryStore().get(memoryLocation));
        }
        else
        {
            System.out.println("Data is in cache");
            States currentState = processorMachines[processorId].getCacheStore().currentState;
            switch (currentState) {
                case INVALID:
                    System.out.println(
                            "Processor is in INVALID state, As new data need be written WriteMiss request is sent and state has changed to MODIFIED");
                    writeMissRequest(processorId);
                    processorMachines[processorId].getCacheStore().setCurrentState(States.MODIFIED);
                    processorMachines[processorId].getCacheStore().setMemoryStore(sharedMemoryLocations);
                    processorMachines[processorId].getCacheStore().getMemoryStore().set(memoryLocation,newValue);
                    System.out.println("New Data: "+
                            processorMachines[processorId].getCacheStore().getMemoryStore().get(memoryLocation));
                    break;
                case SHARED:
                    System.out.println("At present Processor is in SHARED state, As new data need be written invalidation request sent to the bus and then state changed to MODIFIED");
                    invalidationRequest(processorId);
                    processorMachines[processorId].getCacheStore().setCurrentState(States.MODIFIED);
                    processorMachines[processorId].getCacheStore().getMemoryStore().set(memoryLocation,newValue);
                    System.out.println("New Data: "+
                            processorMachines[processorId].getCacheStore().getMemoryStore().get(memoryLocation));
                    break;
                case MODIFIED:
                    processorMachines[processorId].getCacheStore().getMemoryStore().set(memoryLocation,newValue);
                    System.out.println("As processor is already in MODIFIED State, a Local Write is made still the data is in MODIFIED state.");
                    System.out.println("New Data: "+
                            processorMachines[processorId].getCacheStore().getMemoryStore().get(memoryLocation));
                    break;
                default:
                    break;
            }
        }
        processorMachines[processorId].setValid(true);
    }

    private static void invalidationRequest(int processorId) {
        for (int i = 0; i < numberOfProcessors; i++) {
            if(processorMachines[i].isValid) {
                processorMachines[i].setValid(false);
            }
        }
    }

    private static void writeMissRequest(int processorId) {
        for (int i = 0; i < numberOfProcessors; i++) {
            if(processorMachines[i].isValid &&
                    processorMachines[i].getCacheStore().getCurrentState() == States.MODIFIED)
            {
                dataWriteBackToMemory(processorMachines[i].getCacheStore().getMemoryStore());
                processorMachines[i].getCacheStore().setCurrentState(States.INVALID);
            }
            else if (processorMachines[i].isValid &&
                    processorMachines[i].getCacheStore().getCurrentState() == States.SHARED) {
                processorMachines[i].getCacheStore().setCurrentState(States.INVALID);
            }
        }
    }

    private static void readMissRequest(int processorId) {
        for (int i = 0; i < numberOfProcessors; i++) {
            if (processorMachines[i].isValid &&
                    processorMachines[i].getCacheStore().getCurrentState() == States.MODIFIED) {
                dataWriteBackToMemory(processorMachines[i].getCacheStore().getMemoryStore());
                processorMachines[i].getCacheStore().setCurrentState(States.SHARED);
            }
        }
    }

    private static void dataWriteBackToMemory(ArrayList<String> cacheData) {
        for(int i=0;i<numberOfMemoryLocations;i++)
        {
            sharedMemoryLocations.set(i,cacheData.get(i));
        }
    }
}

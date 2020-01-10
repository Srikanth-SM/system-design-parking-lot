package org.example;
import java.util.*;


public class ParkingLot {
    int parkingLotCapacity;
    int occupiedSlots;
    PriorityQueue<Integer> emptySlots; // used for finding the next empty parking slot.

    HashMap<Integer, String> slotRegistrationNumberMap; // {slot1:registration1}
    Map<String, Car> registrationNumberCarMap; // {registration1:Car}
    Map<String, Set<Integer>> colourSlotSetMap; //{colour:[slot1,slot2]}
    Map<String, Set<String>> colourRegistrationNumberSetMap; // {colour:[regis1,regis2]}

    public ParkingLot(int capacity) {
        this.parkingLotCapacity = capacity;
        this.occupiedSlots = 0;
        emptySlots = new PriorityQueue<>();
        slotRegistrationNumberMap = new HashMap<>();
        registrationNumberCarMap = new HashMap<>();
        colourSlotSetMap = new HashMap<>();
        colourRegistrationNumberSetMap = new HashMap<>();
    }

    @Override
    public String toString() {
        return "ParkingLot{" +
                "parkingLotCapacity=" + parkingLotCapacity +
                ", occupiedSlots=" + occupiedSlots +

                ", slotRegistrationNumberMap=" + slotRegistrationNumberMap + "\n" +
                ", registrationNumberCarMap=" + registrationNumberCarMap +
                ", colourSlotSetMap=" + colourSlotSetMap + "\n" +
                ", colourRegistrationNumberSetMap=" + colourRegistrationNumberSetMap +
                '}';
    }

    public String park(String registrationNumber, String colour) throws ParkingLotExceedCapacityException {
        if (occupiedSlots >= parkingLotCapacity) {
            throw new ParkingLotExceedCapacityException("Sorry, parking lot is full");
        }
        int slot = occupiedSlots + 1;
        if (emptySlots.size() > 0) {
            slot = emptySlots.poll();
        }

        Car carToPark = new Car(registrationNumber, colour, slot);
        slotRegistrationNumberMap.put(slot, registrationNumber);
        registrationNumberCarMap.put(registrationNumber, carToPark);

        Set<Integer> slotSetMapByCarColour = colourSlotSetMap.getOrDefault(colour, new HashSet<>());
        slotSetMapByCarColour.add(carToPark.slot);
        colourSlotSetMap.put(colour, slotSetMapByCarColour);

        Set<String> registrationSlotMapByCarColour = colourRegistrationNumberSetMap.getOrDefault(colour, new HashSet<>());
        registrationSlotMapByCarColour.add(carToPark.registrationNumber);
        colourRegistrationNumberSetMap.put(colour, registrationSlotMapByCarColour);
        occupiedSlots++;
        return "Allocated slot number: " + (carToPark.slot);
    }

    // returns set of registrations for one car colour  
    public Set<String> getRegistrationNumbersForCarsWithColour(String colour) {
        if (colourRegistrationNumberSetMap.get(colour) == null) {
            throw new NoSuchElementException("Not Found");
        }
        return colourRegistrationNumberSetMap.get(colour);
    }

    // return slot of a vehicle based on registration number
    public int getSlotNumberForRegistrationNumber(String registrationNumber) {
        if (registrationNumberCarMap.get(registrationNumber) == null) {
            throw new NoSuchElementException("Not Found");
        }
        return registrationNumberCarMap.get(registrationNumber).slot;
    }

    // returns set of slots for one car colour  
    public Set<Integer> getSlotNumbersForCarsWithColour(String colour) {
        if (colourSlotSetMap.get(colour) == null) {
            throw new NoSuchElementException("Not Found");
        }
        return colourSlotSetMap.get(colour);
    }

    // Display the Parking lot dashboard TimeComplexity-o(n)
    public void status() {
        System.out.println("Slot No.\tRegistration No.\tColor");
        System.out.println(this);
        for (int i = 1; i <= occupiedSlots; i++) {
            if (slotRegistrationNumberMap.get(i) != null) {

                Car c = registrationNumberCarMap.get(slotRegistrationNumberMap.get(i));
                System.out.println((i) + "\t" + c.registrationNumber + "\t" + c.colour);
            }
        }
    }

    // For leaving a car from parkingLot Time complexity O(log n )
    public String leaveParkingLot(int leavePosition) {
        if (slotRegistrationNumberMap.get(leavePosition) == null) {
            throw new NoSuchElementException(" No Car present in that position");
        }

        String registrationNumberToRemoveFromLot = slotRegistrationNumberMap.get(leavePosition);
        Car carToRemove = registrationNumberCarMap.get(registrationNumberToRemoveFromLot);
        slotRegistrationNumberMap.remove(leavePosition);


        // remove the leavePosition  from {colour:set[slot]} map -> O(1)
        Set<Integer> colourSlotSetFromWhereLeavePostionHasToBeRemoved = colourSlotSetMap.get(carToRemove.colour);
        boolean isLeavePosRemoved = colourSlotSetFromWhereLeavePostionHasToBeRemoved.remove(leavePosition);


        // remove the registrationNumberToRemoveFromLot from {colour:set[registrations]} map -> O(1) (time complexity)
        boolean isRegistrationNumberRemoved = false;
        if (isLeavePosRemoved) {
            Set<String> colourRegistrationSetFromWhereRegistrationHasToBeRemoved = colourRegistrationNumberSetMap.get(carToRemove.colour);
            isRegistrationNumberRemoved = colourRegistrationSetFromWhereRegistrationHasToBeRemoved.remove(registrationNumberToRemoveFromLot);
            if (colourRegistrationNumberSetMap.get(carToRemove.colour).size() == 0) {
                colourRegistrationNumberSetMap.remove(carToRemove.colour);
            }
            if (colourSlotSetMap.get(carToRemove.colour).size() == 0) {
                colourSlotSetMap.remove(carToRemove.colour);
            }
        }

        // remove the car from {regis:Car} map -> O(1)
        boolean isCarRemovedSuccess = false;
        if (isRegistrationNumberRemoved)
            isCarRemovedSuccess = registrationNumberCarMap.remove(registrationNumberToRemoveFromLot, registrationNumberCarMap.get(registrationNumberToRemoveFromLot));

        if (isLeavePosRemoved && isRegistrationNumberRemoved && isCarRemovedSuccess) {
            occupiedSlots--;
            emptySlots.add(leavePosition); // add an element into empty slots priorityQueue -> O(logn)
            return "Slot number " + (leavePosition) + " is free";
        }
        throw new NoSuchElementException("Car not present at postion: " + leavePosition);
    }


}

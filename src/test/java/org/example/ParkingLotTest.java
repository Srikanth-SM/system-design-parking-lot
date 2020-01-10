package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ParkingLotTest {
    ParkingLot parkingLot;

    @BeforeEach
    public void init() {
        parkingLot = new ParkingLot(5);
    }

    @Test
    public void createParkingLot() {
        ParkingLot parkingLot = new ParkingLot(6);
        assertEquals(parkingLot.parkingLotCapacity, 6);
        assertEquals(parkingLot.occupiedSlots, 0);
    }

    @Test
    public void testPark() throws ParkingLotExceedCapacityException {
        parkingLot = new ParkingLot(3);

        parkingLot.park("a", "white");
        assertEquals("white", parkingLot.registrationNumberCarMap.get("a").colour, "The car colour should be white");
        assertEquals(1, parkingLot.colourSlotSetMap.get("white").size(), "The total cars with white as their colour: 1");
        assertEquals(1, parkingLot.colourRegistrationNumberSetMap.get("white").size(), "The total cars with white as theire colour: 1");


        parkingLot.park("ab", "white");
        assertEquals(2, parkingLot.colourSlotSetMap.get("white").size(), "The total cars with white as their colour: 2");
        assertEquals(2, parkingLot.colourRegistrationNumberSetMap.get("white").size(), "The total cars with white as theire colour: 2");


        parkingLot.park("abc", "red");
        assertEquals("white", parkingLot.registrationNumberCarMap.get("a").colour, "The car colour should be white");
        assertEquals(1, parkingLot.colourSlotSetMap.get("red").size(), "The total slots of  cars with red as their colour: 1");
        assertEquals(1, parkingLot.colourRegistrationNumberSetMap.get("red").size(), "The total registrations of cars with red as their colour: 1");

        int carsWithWhiteColour = parkingLot.colourSlotSetMap.get("white").size();
        int carsWithRedColour = parkingLot.colourSlotSetMap.get("red").size();
        assertEquals(parkingLot.occupiedSlots, carsWithRedColour + carsWithWhiteColour, "The cars with both red and white must be equal to total occupied slots");
        assertThrows(ParkingLotExceedCapacityException.class, () -> parkingLot.park("b", "white"));
    }

    @Test
    public void testGetRegistrationNumbersForCarsWithColour() throws ParkingLotExceedCapacityException {
        parkingLot.park("KA-09-1234", "RED");
        assertEquals(1, parkingLot.getRegistrationNumbersForCarsWithColour("RED").size());
        parkingLot.park("KA-09-1235", "WHITE");
        assertEquals(1, parkingLot.getRegistrationNumbersForCarsWithColour("WHITE").size());
        assertEquals(1, parkingLot.getRegistrationNumbersForCarsWithColour("RED").size());
        parkingLot.park("KA-09-1236", "RED");
        assertEquals(2, parkingLot.getRegistrationNumbersForCarsWithColour("RED").size());
    }

    @Test
    public void testGetSlotNumbersForCarsWithColour() throws ParkingLotExceedCapacityException {
        parkingLot.park("KA-09-1234", "RED");
        assertEquals(1, parkingLot.getSlotNumbersForCarsWithColour("RED").size());
        parkingLot.park("KA-09-1234", "WHITE");
        assertEquals(1, parkingLot.getSlotNumbersForCarsWithColour("WHITE").size());
        assertEquals(1, parkingLot.getSlotNumbersForCarsWithColour("RED").size());
        parkingLot.park("KA-09-1236", "RED");
        assertEquals(2, parkingLot.getSlotNumbersForCarsWithColour("RED").size());
    }

    @Test
    public void testLeaveParkingLot() throws ParkingLotExceedCapacityException {
        parkingLot.park("KA-09-1234", "RED");
        parkingLot.park("KA-09-1235", "RED");
        parkingLot.leaveParkingLot(1);
        assertEquals(1, parkingLot.occupiedSlots);
        parkingLot.park("KA-09-1236", "RED");
    }

    @Test
    public void finalIntegrationTest() throws ParkingLotExceedCapacityException {
        ParkingLot p = new ParkingLot(6);
        assertEquals(6, p.parkingLotCapacity);
        assertEquals("Allocated slot number: 1", p.park("KA-01-HH-1234", "White"));
        assertEquals("Allocated slot number: 2", p.park("KA-01-HH-9999", "White"));
        assertEquals("Allocated slot number: 3", p.park("KA-01-BB-0001", "Black"));
        assertEquals("Allocated slot number: 4", p.park("KA-01-HH-7777", "Red"));
        assertEquals("Allocated slot number: 5", p.park("KA-01-HH-2701", "Blue"));
        assertEquals("Allocated slot number: 6", p.park("KA-01-HH-3141", "Black"));
        assertEquals("Slot number 4 is free", p.leaveParkingLot(4));
        assertEquals("Allocated slot number: 4", p.park("KA-01-P-333", "White"));
        p.status();
        assertEquals(assertThrows(ParkingLotExceedCapacityException.class, () -> p.park("DL-12-AA-9999", "White")).getMessage(), "Sorry, parking lot is full");
        String temp = p.getRegistrationNumbersForCarsWithColour("White").toString();
        assertEquals("KA-01-HH-1234, KA-01-HH-9999, KA-01-P-333", temp.substring(1, temp.length() - 1));
        temp = p.getSlotNumbersForCarsWithColour("White").toString();
        assertEquals("1, 2, 4", temp.substring(1, temp.length() - 1));
        assertEquals(6, p.getSlotNumberForRegistrationNumber("KA-01-HH-3141"));
        assertEquals(assertThrows(NoSuchElementException.class, () -> p.getSlotNumberForRegistrationNumber("MH-04-AY-1111")).getMessage(), "Not Found");
    }
}

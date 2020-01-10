package org.example;

public class ParkingLotExceedCapacityException extends Exception {
    public ParkingLotExceedCapacityException(String message){
        super(message);
    }
}

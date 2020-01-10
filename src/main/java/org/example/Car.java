package org.example;

public class Car  {
    String registrationNumber;
    String colour;
    int slot;

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public Car(String registrationNumber, String colour,int slot) {
        this.registrationNumber = registrationNumber;
        this.colour = colour;
        this.slot = slot;
    }

    @Override
    public String toString() {
        return "Car{" +
                "registrationNumber='" + registrationNumber + '\'' +
                ", colour='" + colour + '\'' +
                ", slot=" + slot +
                '}';
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }
}

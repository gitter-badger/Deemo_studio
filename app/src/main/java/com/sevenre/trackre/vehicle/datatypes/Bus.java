package com.sevenre.trackre.vehicle.datatypes;

public class Bus {
    String busNo;
    String busId;

    public Bus(String busNo, String busId) {
        this.busNo = busNo;
        this.busId = busId;
    }

    public String getBusNo() {
        return busNo;
    }

    public String getBusId() {
        return busId;
    }

    @Override
    public String toString() {
        return  busNo;
    }
}

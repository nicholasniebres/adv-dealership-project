package com.skills4it.dealership.models;

import com.skills4it.dealership.models.enums.VehicleType;

import java.util.Objects;

public class Vehicle {
    private String vin;
    private int year;
    private String make;
    private String model;
    private VehicleType vehicleType;
    private String color;
    private int odometer;
    private double price;

    public Vehicle(String vin, int year, String make, String model, VehicleType vehicleType, String color, int odometer, double price) {
        this.vin = vin;
        this.year = year;
        this.make = make;
        this.model = model;
        this.vehicleType = vehicleType;
        this.color = color;
        this.odometer = odometer;
        this.price = price;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getOdometer() {
        return odometer;
    }

    public void setOdometer(int odometer) {
        this.odometer = odometer;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String toCsvLine() {
        return String.join("|",
                String.valueOf(vin),
                String.valueOf(year),
                make,
                model,
                vehicleType.getDisplayName(),
                color,
                String.valueOf(odometer),
                String.format("%.2f", price));
    }

    @Override
    public String toString() {
        return String.format("%s %-6d %-12s %-15s %-8s %-10s %,10d $%,10.2f",
                vin, year, make, model, vehicleType.getDisplayName(), color, odometer, price);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vehicle vehicle)) return false;
        return vin == vehicle.vin;
    }

    @Override
    public int hashCode() {
        return Objects.hash(vin);
    }
}

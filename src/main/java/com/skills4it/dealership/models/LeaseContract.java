package com.skills4it.dealership.models;

public class LeaseContract extends Contract {

    // Fields for LeaseContract
    private double expectedEndingValue;
    private double leaseFee;

    public LeaseContract(String date, String customerName, String customerEmail, Vehicle vehicleSold) {
        super(date, customerName, customerEmail, vehicleSold);
        // Calculated based on original price
        this.expectedEndingValue = vehicleSold.getPrice() * 0.50;
        this.leaseFee = vehicleSold.getPrice() * 0.07;
    }

    // Getters
    public double getExpectedEndingValue() {
        return expectedEndingValue;
    }

    public double getLeaseFee() {
        return leaseFee;
    }

    // Setters
    public void setExpectedEndingValue(double expectedEndingValue) {
        this.expectedEndingValue = expectedEndingValue;
    }

    public void setLeaseFee(double leaseFee) {
        this.leaseFee = leaseFee;
    }

    @Override
    public double getTotalPrice() {
        // Total price for a lease is the value used plus the lease fee
        return (getVehicleSold().getPrice() - expectedEndingValue) + leaseFee;
    }

    @Override
    public double getMonthlyPayment() {
        double totalPrice = getTotalPrice();
        double annualRate = 0.04; // 4.0% interest
        int months = 36; // 36 months duration

        double monthlyRate = annualRate / 12;

        // Formula for the monthly lease payment
        return (totalPrice * monthlyRate) / (1 - Math.pow(1 + monthlyRate, -months));
    }
}
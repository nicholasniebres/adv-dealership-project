package com.skills4it.dealership.models;

public class SalesContract extends Contract {

    // Constants for fees and rates
    private static final double SALES_TAX_RATE = 0.05;
    private static final double RECORDING_FEE = 100.00;
    private static final double PROCESSING_FEE_UNDER_10000 = 295.00;
    private static final double PROCESSING_FEE_10000_OR_MORE = 495.00;

    // Additional fields for SalesContract
    private boolean finance;

    public SalesContract(String date, String customerName, String customerEmail, Vehicle vehicleSold, boolean finance) {
        super(date, customerName, customerEmail, vehicleSold);
        this.finance = finance;
    }

    // Getters and Setters
    public boolean isFinance() {
        return finance;
    }

    @Override
    public double getTotalPrice() {
        double vehiclePrice = getVehicleSold().getPrice();
        double salesTax = vehiclePrice * SALES_TAX_RATE;
        double processingFee;

        if (vehiclePrice < 10000) {
            processingFee = PROCESSING_FEE_UNDER_10000;
        } else {
            processingFee = PROCESSING_FEE_10000_OR_MORE;
        }

        return vehiclePrice + salesTax + RECORDING_FEE + processingFee;
    }

    @Override
    public double getMonthlyPayment() {
        if (!finance) {
            return 0.0;
        }

        double totalPrice = getTotalPrice();
        double interestRate;
        int months;

        // Terms based on total price
        if (totalPrice >= 10000) {
            interestRate = 0.0425; // 4.25%
            months = 48;
        } else {
            interestRate = 0.0525; // 5.25%
            months = 24;
        }

        // Monthly payment formula
        double monthlyRate = interestRate / 12;
        double monthlyPayment = (totalPrice * monthlyRate) / (1 - Math.pow(1 + monthlyRate, -months));

        return monthlyPayment;
    }
}

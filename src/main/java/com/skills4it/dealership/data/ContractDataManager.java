package com.skills4it.dealership.models;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ContractDataManager {

//

/*    public void saveContract(Contract contract) {
        // Open the file in append mode (true) so all contracts go into the same file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {

            // Get common vehicle data
            Vehicle v = contract.getVehicleSold();

            // Build the base string that both contracts share
            String baseData = String.format("%s|%s|%s|%s|%s|%d|%s|%s|%s|%s|%d|%.2f",
                    (contract instanceof SalesContract) ? "SALE" : "LEASE",
                    contract.getContractDate(),
                    contract.getCustomerName(),
                    contract.getCustomerEmail(),
                    v.getVin(),
                    v.getYear(),
                    v.getMake(),
                    v.getModel(),
                    v.getVehicleType(),
                    v.getColor(),
                    v.getOdometer(),
                    v.getPrice()
            );

            String finalLine = "";

            // Handle SALE specific fields format
            if (contract instanceof SalesContract) {
                SalesContract sales = (SalesContract) contract;

                // Calculate individual components matching your headers
                double salesTax = v.getPrice() * 0.05; // 5% Tax
                double processingFee = (v.getPrice() < 10000) ? 295.00 : 495.00;
                String financeOption = sales.isFinance() ? "YES" : "NO";

                // Format: BASE_DATA|SALES_TAX|RECORDING_FEE|PROCESSING_FEE|TOTAL_PRICE|FINANCE_OPTION|MONTHLY_PAYMENT
                finalLine = String.format("%s|%.2f|100.00|%.2f|%.2f|%s|%.2f",
                        baseData,
                        salesTax,
                        processingFee,
                        sales.getTotalPrice(),
                        financeOption,
                        sales.getMonthlyPayment()
                );

                // Handle LEASE specific fields format
            } else if (contract instanceof LeaseContract) {
                LeaseContract l = (LeaseContract) contract;

                // Format: BASE_DATA|EXPECTED_ENDING_VALUE|LEASE_FEE|TOTAL_PRICE|MONTHLY_PAYMENT
                finalLine = String.format("%s|%.2f|%.2f|%.2f|%.2f",
                        baseData,
                        l.getExpectedEndingValue(),
                        l.getLeaseFee(),
                        l.getTotalPrice(),
                        l.getMonthlyPayment()
                );
            }

            // Write the line to the file and add a new line character
            writer.write(finalLine);
            writer.newLine();

            System.out.println("Contract successfully appended to " + FILE_NAME);

        } catch (IOException e) {
            System.out.println("Error appending contract to file: " + e.getMessage());
        }
    }*/
}
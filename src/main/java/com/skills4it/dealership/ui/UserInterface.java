package com.skills4it.dealership.ui;

import com.skills4it.dealership.data.DealershipFileManager;
import com.skills4it.dealership.models.*;
import com.skills4it.dealership.models.enums.VehicleType;
import com.skills4it.dealership.ui.enums.MenuOption;

import java.util.List;
import java.util.Scanner;

public class UserInterface {

    private final Scanner scanner;
    private final DealershipFileManager fileManager;
    private Dealership dealership;
    private static final String FILE_NAME = ("contracts.csv");

    public UserInterface() {
        this.scanner = new Scanner(System.in);
        this.fileManager = new DealershipFileManager();
    }

    public void display() {
        init();

        MenuOption selectedOption;
        do {
            displayHeader();
            displayMenu();
            int choice = readInt("Choose an option: ");
            selectedOption = MenuOption.fromCode(choice).orElse(null);
            handleMenuChoice(selectedOption);
        } while (selectedOption != MenuOption.QUIT);

        System.out.println("Goodbye!");
    }

    private void init() {
        this.dealership = fileManager.getDealership();
    }

    private void displayHeader() {
        System.out.println();
        System.out.println("=============================================");
        System.out.println("Welcome to the app: " + dealership.getName());
        System.out.println(dealership.getAddress() + " | " + dealership.getPhone());
        System.out.println("=============================================");
    }

    private void displayMenu() {
        for (MenuOption option : MenuOption.values()) {
            System.out.printf("%-3d - %s%n", option.getCode(), option.getLabel());
        }
        System.out.println();
    }

    private void handleMenuChoice(MenuOption option) {
        if (option == null) {
            System.out.println("Invalid option. Please try again.");
            return;
        }

        switch (option) {
            case FIND_BY_PRICE -> processGetByPriceRequest();
            case FIND_BY_MAKE_MODEL -> processGetByMakeModelRequest();
            case FIND_BY_YEAR -> processGetByYearRequest();
            case FIND_BY_COLOR -> processGetByColorRequest();
            case FIND_BY_MILEAGE -> processGetByMileageRequest();
            case FIND_BY_TYPE -> processGetByVehicleTypeRequest();
            case LIST_ALL -> processAllVehiclesRequest();
            case ADD_VEHICLE -> processAddVehicleRequest();
            case REMOVE_VEHICLE -> processRemoveVehicleRequest();
            case CONTRACT_TYPE -> processSellLeaseRequest();
            case QUIT -> { }
        }
    }

    public void processSellLeaseRequest() {
        Scanner scanner = new Scanner(System.in);

        // Step 1: Find and extract the vehicle immediately
        System.out.print("Enter the VIN of the vehicle to sell/lease: ");
        String vin = scanner.nextLine();
        Vehicle vehicle = dealership.removeVehicleByVin(vin);

        if (vehicle == null) {
            System.out.println("Vehicle not found.");
            return;
        }

        // Step 2: Collect basic customer information
        System.out.print("Enter contract date (YYYYMMDD): ");
        String date = scanner.nextLine();
        System.out.print("Enter customer name: ");
        String name = scanner.nextLine();
        System.out.print("Enter customer email: ");
        String email = scanner.nextLine();

        // Step 3: Ask if it's a Sale or Lease
        System.out.print("Is this a SALE or LEASE? ");
        String type = scanner.nextLine().trim().toUpperCase();

        // Creating an empty object from Contract superclass
        Contract contract = null;

        if (type.equals("SALE")) {
            System.out.print("Will this be financed? (yes/no): ");
            boolean finance = scanner.nextLine().trim().equalsIgnoreCase("yes");

            // New object for using the specific perimeters to SaleContract subclass
            contract = new SalesContract(date, name, email, vehicle, finance);

        } else if (type.equals("LEASE")) {
            int currentYear = 2026;
            if ((currentYear - vehicle.getYear()) > 3) {
                System.out.println("Error: Vehicles older than 3 years cannot be leased.");
                // Put the vehicle back into inventory if the lease deal fails validation
                dealership.addVehicle(vehicle);
                return;
            }

            // New object using specific parameters for LeaseContract sublass
            contract = new LeaseContract(date, name, email, vehicle);

        } else {
            System.out.println("Invalid contract type.");
            // Put the vehicle back into inventory if an invalid type was typed
            dealership.addVehicle(vehicle);
            return;
        }

        // Step 4: Display calculated pricing to the user
        System.out.println("\n--- Contract Summary ---");
        System.out.printf("Total Price: $%.2f\n", contract.getTotalPrice());
        System.out.printf("Monthly Payment: $%.2f\n", contract.getMonthlyPayment());

        // Step 5: Direct BufferedWriter to the CSV file
        try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter("contracts.csv", true))) {

            // Build common base data layout shared by both files
            String baseData = String.format("%s|%s|%s|%s|%s|%d|%s|%s|%s|%s|%d|%.2f",
                    type,
                    contract.getContractDate(),
                    contract.getCustomerName(),
                    contract.getCustomerEmail(),
                    vehicle.getVin(),
                    vehicle.getYear(),
                    vehicle.getMake(),
                    vehicle.getModel(),
                    vehicle.getVehicleType(),
                    vehicle.getColor(),
                    vehicle.getOdometer(),
                    vehicle.getPrice()
            );

            String finalLine = "";

            if (contract instanceof SalesContract) {
                SalesContract sales = (SalesContract) contract;
                double salesTax = vehicle.getPrice() * 0.05;
                double processingFee = (vehicle.getPrice() < 10000) ? 295.00 : 495.00;
                String financeOption = sales.isFinance() ? "YES" : "NO";

                finalLine = String.format("%s|%.2f|100.00|%.2f|%.2f|%s|%.2f",
                        baseData, salesTax, processingFee, sales.getTotalPrice(), financeOption, sales.getMonthlyPayment());

            } else if (contract instanceof LeaseContract) {
                LeaseContract lease = (LeaseContract) contract;

                finalLine = String.format("%s|%.2f|%.2f|%.2f|%.2f",
                        baseData, lease.getExpectedEndingValue(), lease.getLeaseFee(), lease.getTotalPrice(), lease.getMonthlyPayment());
            }

            // Output to CSV row
            writer.write(finalLine);
            writer.newLine();
            System.out.println("Transaction successfully appended to file!");

        } catch (java.io.IOException e) {
            System.out.println("Critical Error writing contract row to CSV: " + e.getMessage());
        }
    }

    private void processGetByPriceRequest() {
        double minPrice = readDouble("Minimum price: ");
        double maxPrice = readDouble("Maximum price: ");
        displayVehicles(dealership.getVehiclesByPrice(minPrice, maxPrice));
    }

    private void processGetByMakeModelRequest() {
        String make = readString("Make, leave empty for any: ");
        String model = readString("Model, leave empty for any: ");
        displayVehicles(dealership.getVehiclesByMakeModel(make, model));
    }

    private void processGetByYearRequest() {
        int minYear = readInt("Minimum year: ");
        int maxYear = readInt("Maximum year: ");
        displayVehicles(dealership.getVehiclesByYear(minYear, maxYear));
    }

    private void processGetByColorRequest() {
        String color = readString("Color: ");
        displayVehicles(dealership.getVehiclesByColor(color));
    }

    private void processGetByMileageRequest() {
        int minMileage = readInt("Minimum mileage: ");
        int maxMileage = readInt("Maximum mileage: ");
        displayVehicles(dealership.getVehiclesByMileage(minMileage, maxMileage));
    }

    private void processGetByVehicleTypeRequest() {
        VehicleType vehicleType = readVehicleType("Vehicle type (" + VehicleType.getAllowedValuesText() + "): ");
        displayVehicles(dealership.getVehiclesByType(vehicleType));
    }

    private void processAllVehiclesRequest() {
        displayVehicles(dealership.getAllVehicles());
    }

    private void processAddVehicleRequest() {
        System.out.println("Add a new vehicle");

        String vin = scanner.nextLine();
        if (dealership.findVehicleByVin(vin).isPresent()) {
            System.out.println("A vehicle with this VIN already exists. Vehicle was not added.");
            return;
        }

        int year = readYear("Year: ");
        String make = readRequiredString("Make: ");
        String model = readRequiredString("Model: ");
        VehicleType vehicleType = readVehicleType("Vehicle type (" + VehicleType.getAllowedValuesText() + "): ");
        String color = readRequiredString("Color: ");
        int odometer = readPositiveInt("Odometer: ");
        double price = readPositiveDouble("Price: ");

        Vehicle vehicle = new Vehicle(vin, year, make, model, vehicleType, color, odometer, price);
        dealership.addVehicle(vehicle);
        fileManager.saveDealership(dealership);

        System.out.println("Vehicle added and inventory saved.");
    }

    private void processRemoveVehicleRequest() {
        int vin = readPositiveInt("Enter VIN of vehicle to remove: ");

        dealership.findVehicleByVin(String.valueOf(vin)).ifPresentOrElse(vehicle -> {
            System.out.println("Vehicle found:");
            displayVehicles(List.of(vehicle));

            String confirmation = readString("Remove this vehicle? yes/no: ");
            if (confirmation.equalsIgnoreCase("yes") || confirmation.equalsIgnoreCase("y")) {
                dealership.removeVehicleByVin(String.valueOf(vin));
                fileManager.saveDealership(dealership);
                System.out.println("Vehicle removed and inventory saved.");
            } else {
                System.out.println("Remove cancelled.");
            }
        }, () -> System.out.println("No vehicle found with VIN " + vin + "."));
    }

    private void displayVehicles(List<Vehicle> vehicles) {
        if (vehicles == null || vehicles.isEmpty()) {
            System.out.println("No vehicles found.");
            pause();
            return;
        }

        System.out.println();
        System.out.printf("%-8s %-6s %-12s %-15s %-8s %-10s %10s %11s%n",
                "VIN", "YEAR", "MAKE", "MODEL", "TYPE", "COLOR", "ODOMETER", "PRICE");
        System.out.println("---------------------------------------------------------------------------------------");

        for (Vehicle vehicle : vehicles) {
            System.out.println(vehicle);
        }

        System.out.println("---------------------------------------------------------------------------------------");
        System.out.println("Total vehicles: " + vehicles.size());
        pause();
    }

    private String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private String readRequiredString(String prompt) {
        while (true) {
            String value = readString(prompt);
            if (!value.isBlank()) {
                return value;
            }
            System.out.println("This field is required. Please try again.");
        }
    }

    private VehicleType readVehicleType(String prompt) {
        while (true) {
            String input = readRequiredString(prompt);
            var vehicleType = VehicleType.fromString(input);

            if (vehicleType.isPresent()) {
                return vehicleType.get();
            }

            System.out.println("Invalid vehicle type. Allowed values: " + VehicleType.getAllowedValuesText());
        }
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    private int readPositiveInt(String prompt) {
        while (true) {
            int number = readInt(prompt);
            if (number >= 0) {
                return number;
            }
            System.out.println("Please enter a positive number.");
        }
    }

    private int readYear(String prompt) {
        while (true) {
            int year = readInt(prompt);
            if (year >= 1886 && year <= 2100) {
                return year;
            }
            System.out.println("Please enter a realistic vehicle year between 1886 and 2100.");
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number, for example 1995.00.");
            }
        }
    }

    private double readPositiveDouble(String prompt) {
        while (true) {
            double number = readDouble(prompt);
            if (number >= 0) {
                return number;
            }
            System.out.println("Please enter a positive number.");
        }
    }

    private void pause() {
        System.out.println();
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
    }
}

package com.skills4it.dealership.ui.enums;

import java.util.Arrays;
import java.util.Optional;

public enum MenuOption {
    FIND_BY_PRICE(1, "Find vehicles within a price range"),
    FIND_BY_MAKE_MODEL(2, "Find vehicles by make/model"),
    FIND_BY_YEAR(3, "Find vehicles by year range"),
    FIND_BY_COLOR(4, "Find vehicles by color"),
    FIND_BY_MILEAGE(5, "Find vehicles by mileage range"),
    FIND_BY_TYPE(6, "Find vehicles by type"),
    LIST_ALL(7, "List all vehicles"),
    ADD_VEHICLE(8, "Add a vehicle"),
    REMOVE_VEHICLE(9, "Remove a vehicle"),
    CONTRACT_TYPE(10, "Sell or lease a vehicle"),
    QUIT(0, "Quit");

    private final int code;
    private final String label;

    MenuOption(int code, String label) {
        this.code = code;
        this.label = label;
    }

    public int getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    public static Optional<MenuOption> fromCode(int code) {
        return Arrays.stream(values())
                .filter(option -> option.code == code)
                .findFirst();
    }
}

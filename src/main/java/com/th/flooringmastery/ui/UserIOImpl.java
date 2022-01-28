/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.th.flooringmastery.ui;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author workstation
 */
public class UserIOImpl implements UserIO {

    Scanner scanner = new Scanner(System.in);

    @Override
    public int readInt(String message) {
        boolean invalidInput = true;
        int num = 0;
        while (invalidInput) {
            try {
                String stringValue = readString(message);
                num = Integer.parseInt(stringValue);
                invalidInput = false;
            } catch (NumberFormatException e) {
                this.print("Input error. Please try again.");
            }
        }
        return num;
    }

    @Override
    public BigDecimal readBigDecimal(String message) {

        Boolean loop;
        BigDecimal number = null;
        do {
            loop = false;
            System.out.println(message);

            try {
                number = new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                loop = true;
            }

        } while (loop);
        return number;

    }

    @Override
    public LocalDate readDate(String message) {
        Boolean loop;
        LocalDate ld = null;
        do {
            loop = false;
            try {
                String input = this.readString(message);
                ld = LocalDate.parse(input, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            } catch (DateTimeParseException e) {
                loop = true;
                this.print("Wrong Date Format");
            }

        } while (loop);
        return ld;

    }

    @Override
    public void print(String message) {
        System.out.println(message);
    }

    @Override
    public String readString(String message) {
        System.out.println(message);
        String string = scanner.nextLine();
        return string;
    }

    @Override
    public int readInt(String message, int min, int max) {
        int result;
        do {
            result = readInt(message);
        } while (result < min || result > max);

        return result;
    }

    @Override
    public LocalDate readNewOrderDate(String message) {
        Boolean futureDate = false;
        LocalDate ld = null;
        LocalDate now = LocalDate.now();
        while (!futureDate) {
            ld = this.readDate(message);

            if (ld.compareTo(now) > -1) {
                futureDate = true;
            } else {
                this.print("Order Date Can't Be In The Past");
            }

        }
        return ld;
    }

    @Override
    public BigDecimal readOrderBigDecimal(String message) {
        Boolean validArea = false;
        BigDecimal area = null;

        while (!validArea) {
            area = this.readBigDecimal(message);
            int compare = area.compareTo(new BigDecimal("100"));
            if (compare >= 0) {
                validArea = true;
            } else {
                this.print("The Minimum Order Size is 100 sqft");
            }
        }
        return area;
    }

    @Override
    public String readOrderName(String message) {
        Boolean validName = false;
        String name = null;
        while (!validName) {
            name = this.readString(message);

            String regex = "^[a-zA-Z0-9,. ]+$";
            Pattern pattern = Pattern.compile(regex);
            Matcher match = pattern.matcher(name);
            if (match.matches()) {
                validName = true;
            } else {
                this.print("Invalid Name Cannot Be Blank And Only Containing [A-Z][0-9][. ,]");
            }
        }
        return name;
    }

    @Override
    public String readEditOrderName(String message) {

        String name = this.readString(message);
        if (name.isBlank()) {
            return null;
        }

        String regex = "^[a-zA-Z0-9,. ]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher match = pattern.matcher(name);

        if (match.matches()) {
            return name;
        } else {
            this.print("Invalid Name. Information Won't Be Changed");
            return null;
        }
    }

    @Override
    public BigDecimal readEditBigDecimal(String message) {
        Boolean loop;
        BigDecimal number = null;
        do {
            loop = false;
            System.out.println(message);
            String input = scanner.nextLine();
            if (input.isBlank()) {
                return null;
            }

            try {
                number = new BigDecimal(input);
            } catch (NumberFormatException e) {
                loop = true;
                this.print("Invalid Input.");
            }
            if (number != null) {
                if (number.compareTo(new BigDecimal("100")) == -1) {
                    loop = true;
                    this.print("Area Must Be At Least 100spft.");
                }
            }

        } while (loop);
        return number;
    }

}

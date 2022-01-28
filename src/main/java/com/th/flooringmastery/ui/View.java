/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.th.flooringmastery.ui;

import com.th.flooringmastery.dto.Order;
import com.th.flooringmastery.dto.Product;
import com.th.flooringmastery.dto.Tax;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.util.StringUtils;

/**
 *
 * @author workstation
 */
public class View {

    private final UserIO io;

    public View(UserIO io) {
        this.io = io;
    }

    public int displayMenuGetChoice() {
        io.print("*************************************");
        io.print("* <<Flooring Program>>");
        io.print("* 1. Display Orders");
        io.print("* 2. Add an Order");
        io.print("* 3. Edit an Order");
        io.print("* 4. Remove an Order");
        io.print("* 5. Export All Data");
        io.print("* 6. Quit");
        io.print("*************************************");
        return io.readInt("Choose A Menu Option", 1, 6);

    }

    public LocalDate getdate() {
        return io.readDate("Enter Date To Display Orders - MM/DD/YYYY");
    }
    
    public LocalDate getOrderDate() {
        return io.readDate("Enter Date Of Order - MM/DD/YYYY");
    }
    public LocalDate getNewOrderDate() {
        return io.readNewOrderDate("Enter Date Of Order - MM/DD/YYYY");
    }
    
    public int getOrderNumber() {
        return io.readInt("Enter Order's Number");
    }

    public void displayOrders(List<Order> orders) {
        if (orders != null) {
            orders.stream().forEach((order) -> this.displayOrder(order));
        } else {
            io.print("No Orders Found For This Date");

        }

    }

    public void displayOrder(Order order) {
        
        io.print("\n#" + String.valueOf(order.getOrderNumber()) + " " + order.getCustomerName() + " " + order.getDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        io.print("State: " + order.getTax().getStateName());
        io.print("Product: " + order.getProduct().getType());
        io.print("Area: " + order.getArea() + "sqft");
        io.print("Material Cost: " + "$" + order.getMaterialCost());
        io.print("Labor Cost: " + "$" + order.getLaborCost());
        io.print("Tax Cost: " + "$" + order.getTaxCost());
        io.print("Total Cost: " + "$" + order.getTotalCost());
        io.print("");

    }

    public void displayPause() {
        io.readString("Press Enter To Continue");
    }

    public Order getNewOrder(List<Product> products, List<Tax> taxes) {

        Order newOrder = new Order(this.getNewOrderDate());
        newOrder.setCustomerName(this.getOrderName());
        newOrder.setTax(this.getOrderState(taxes));
        newOrder.setProduct(this.getOrderProduct(products));
        newOrder.setArea(this.getOrderArea());

        return newOrder;
    }

    private String getOrderName() {
        return io.readOrderName("Enter Customer's Name");
    }

    private Tax getOrderState(List<Tax> taxes) {
        Boolean validState = false;
        Tax tax = null;
        String states = StringUtils.collectionToDelimitedString(taxes.stream().map((t) -> t.getState()).collect(Collectors.toList()), ",");

        while (!validState) {
            String state = io.readString("Enter State Abbreviation Only Available to (" + states + ")").toUpperCase();
            try {
                tax = taxes.stream().filter((t) -> t.getState().equals(state)).findFirst().get();
                validState = true;
            } catch (NoSuchElementException e) {

            }

        }
        return tax;

    }

    private Product getOrderProduct(List<Product> products) {
        Boolean validProduct = false;
        Product product = null;
        if (products != null) {
            products.stream().forEach((p) -> io.print(p.getType() + ": Price Per sqft - $" + p.getSquareFootCost() + " Labor Cost Per sqft - $" + p.getLaborCost()));
        }

        while (!validProduct) {
            String input = io.readString("Enter Product Choice");
            try {
                if (products != null) {
                    product = products.stream().filter((p) -> p.getType().equals(input)).findFirst().get();
                    validProduct = true;
                } else {
                    return null;
                }

            } catch (NoSuchElementException e) {

            }
        }
        return product;
    }

    private BigDecimal getOrderArea() {
        return io.readOrderBigDecimal("Enter Order Area sqft");
    }

    public Boolean showOrderConfirmation(Order order) {
        io.print("=== Order Confirmation ===");
        this.displayOrder(order);
        Boolean confirmation = false;
        Boolean loop = true;
        while (loop) {
            String confirm = io.readString("Enter Y/N To Confirm Order").toUpperCase();

            switch (confirm) {
                case "Y":
                    confirmation = true;
                    loop = false;
                    break;
                case "N":
                    confirmation = false;
                    loop = false;
                default:
                    break;

            }
        }
        return confirmation;

    }
    
    public Boolean showEditOrderConfirmation(Order order) {
        io.print("=== Edit Order Confirmation ===");
        this.displayOrder(order);
        Boolean confirmation = false;
        Boolean loop = true;
        while (loop) {
            String confirm = io.readString("Enter Y/N To Confirm Edited Order").toUpperCase();

            switch (confirm) {
                case "Y":
                    confirmation = true;
                    loop = false;
                    break;
                case "N":
                    confirmation = false;
                    loop = false;
                default:
                    break;

            }
        }
        return confirmation;
    }
    
    public Boolean showRemoveConfirmation(Order order) {
        io.print("=== Remove Order Confirmation ===");
        this.displayOrder(order);
        Boolean confirmation = false;
        Boolean loop = true;
        while (loop) {
            String confirm = io.readString("Enter Y/N To Confirm Removing This Order").toUpperCase();

            switch (confirm) {
                case "Y":
                    confirmation = true;
                    loop = false;
                    break;
                case "N":
                    confirmation = false;
                    loop = false;
                default:
                    break;

            }
        }
        return confirmation;
    }
    
    public Order getEditedOrder(Order order,List<Tax> taxes, List<Product> products) {
        io.print("=== Editing ===");
        this.displayOrder(order);
        order.setCustomerName(this.editCustomerName(order));
        order.setTax(this.editState(order, taxes));
        order.setProduct(this.editProduct(order, products));
        order.setArea(this.editArea(order));
        
        return order;
    }
    
    private String editCustomerName(Order order) {
        String newName = io.readEditOrderName("Enter Customer Name (" + order.getCustomerName() + "):");
        if (newName != null) {
            return newName;
        } else {
            return order.getCustomerName();
        }
    }
    
    private Tax editState(Order order, List<Tax> taxes) {
        Boolean validState = false;
        Tax tax = null;
        String states = StringUtils.collectionToDelimitedString(taxes.stream().map((t) -> t.getState()).collect(Collectors.toList()), ",");

        while (!validState) {
            String state = io.readString("Valid States (" + states + ") Enter New State (" + order.getTax().getState() + "):").toUpperCase();
            if (state.isBlank()) {
                return order.getTax();
            }
            try {
                tax = taxes.stream().filter((t) -> t.getState().equals(state)).findFirst().get();
                validState = true;
            } catch (NoSuchElementException e) {

            }

        }
        return tax;
        
       
        
    }
    
    private Product editProduct(Order order, List<Product> products) {
         Boolean validProduct = false;
        Product product = null;
        if (products != null) {
            products.stream().forEach((p) -> io.print(p.getType() + ": Price Per sqft - $" + p.getSquareFootCost() + " Labor Cost Per sqft - $" + p.getLaborCost()));
        }

        while (!validProduct) {
            String input = io.readString("Enter New Product Choice (" + order.getProduct().getType() + "):");
            if (input.isBlank()) {
                return order.getProduct();
            }
            try {
                if (products != null) {
                    product = products.stream().filter((p) -> p.getType().equals(input)).findFirst().get();
                    validProduct = true;
                } else {
                    return null;
                }

            } catch (NoSuchElementException e) {

            }
        }
        return product;
    }
    
    private BigDecimal editArea(Order order) {
         BigDecimal area = io.readEditBigDecimal("Enter New Area (" + order.getArea().toString() + "):");
         if (area != null) {
             return area;
         } else {
             return order.getArea();
         }
    }
    
    public void showError(Exception e) {
        io.print("ERROR: " + e);
    }
    
    public void showNoOrderFound(){
        io.readString("No Order Found. Press Enter To Continue.");
                
    }
    
    public void showExportConfirmation(){
        io.readString("=== Data Exported ===\nPress Enter To Continue.");
    }
    
    public void showOrderCreated(){
        io.readString("Order Saved. Press Enter To Continue.");
    }
    
    public void showOrderNotCreated(){
        io.readString("Order Was Not Saved. Press Enter To Continue.");
    }

}

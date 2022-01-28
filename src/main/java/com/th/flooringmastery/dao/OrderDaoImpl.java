/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.th.flooringmastery.dao;

import com.th.flooringmastery.dto.Order;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 *
 * @author workstation
 */
public class OrderDaoImpl implements OrderDao {

    private final String ORDER_FOLDER;
    TaxDao taxDao;
    ProductDao productDao;
    HashMap<LocalDate, HashMap<Integer, Order>> orders = new HashMap<>();
    private final String DELIMITER = ",";

    @Override
    public Order addOrder(Order order) throws PersistenceException {
        this.loadOrders();
        LocalDate ld = order.getDate();

        if (!orders.containsKey(ld)) {
            HashMap<Integer, Order> innerOrders = new HashMap<>();
            innerOrders.put(order.getOrderNumber(), order);
            orders.put(ld, innerOrders);
        } else {
            HashMap<Integer, Order> innerOrders = orders.get(ld);
            innerOrders.put(order.getOrderNumber(), order);
        }
        this.writeOrders();
        return order;

    }

    @Override
    public List<Order> getOrders(LocalDate date) throws PersistenceException {
        this.loadOrders();

        if (orders.containsKey(date)) {
            HashMap<Integer, Order> hashOrders = orders.get(date);
            return new ArrayList<>(hashOrders.values());
        }
        return null;

    }

    @Override
    public Order getOrder(LocalDate date, int orderNumber) throws PersistenceException {
        this.loadOrders();
        if (orders.containsKey(date)) {
            return orders.get(date).get(orderNumber);
        }
        return null;
    }

    @Override
    public List<Order> getAllOrders() throws PersistenceException {
        this.loadOrders();
        List<Order> allOrders = new ArrayList<>();
        for (LocalDate key : orders.keySet()) {
            HashMap<Integer, Order> orderList = orders.get(key);
            if (!orderList.isEmpty()) {
                allOrders.addAll(orderList.values());
            }

        }
        return allOrders;
    }

    @Override
    public void editOrder(Order editOrder) throws PersistenceException {
        this.loadOrders();
        orders.get(editOrder.getDate()).put(editOrder.getOrderNumber(), editOrder);
        this.writeOrders();
    }

    @Override
    public Order removeOrder(Order removeOrder) throws PersistenceException {
        this.loadOrders();
        orders.get(removeOrder.getDate()).remove(removeOrder.getOrderNumber());
        this.writeOrders();
        return removeOrder;

    }

    @Override
    public void exportOrders() throws PersistenceException {
        List<Order> sortedOrders = this.getAllOrders().stream().sorted(Comparator.comparingInt(Order::getOrderNumber)).collect(Collectors.toList());

        File exports = new File("Backup/DataExport.txt");

        PrintWriter out;
        try {
            out = new PrintWriter(new FileWriter(exports));
        } catch (IOException e) {
            throw new PersistenceException("Could not save Orders");
        }

        String orderText;

        out.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total,Date");

        for (Order currentOrder : sortedOrders) {
            orderText = this.marshallOrder(currentOrder);
            out.println(orderText + DELIMITER + currentOrder.getDate().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
            out.flush();
        }
        out.close();

    }

    private Order unmarshallOrder(String orderAsText) throws PersistenceException {
        String[] orderTokens = orderAsText.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        Order orderFromFile = new Order(Integer.parseInt(orderTokens[0]));

        orderFromFile.setCustomerName(orderTokens[1].replaceAll("\"", ""));
        orderFromFile.setArea(new BigDecimal(orderTokens[5]));
        orderFromFile.setMaterialCost(new BigDecimal(orderTokens[8]));
        orderFromFile.setLaborCost(new BigDecimal(orderTokens[9]));
        orderFromFile.setTaxCost(new BigDecimal(orderTokens[10]));
        orderFromFile.setTotalCost(new BigDecimal(orderTokens[11]));
        orderFromFile.setProduct(productDao.getProduct(orderTokens[4]));
        orderFromFile.setTax(taxDao.getTax(orderTokens[2]));
        return orderFromFile;
    }

    private void loadOrders() throws PersistenceException {

        File orderFolder = new File(ORDER_FOLDER);

        File[] orderFiles = orderFolder.listFiles();

        for (File orderFile : orderFiles) {

            Scanner scanner;
            try {
                scanner = new Scanner(new BufferedReader(new FileReader(orderFile)));
            } catch (FileNotFoundException e) {
                throw new PersistenceException("Could not load order");
            }

            String currentLine;
            Order currentOrder;
            String fileName = orderFile.getName().substring(7, 15);

            LocalDate orderld = LocalDate.parse(fileName, DateTimeFormatter.ofPattern("MMddyyyy"));
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            while (scanner.hasNextLine()) {
                currentLine = scanner.nextLine();
                currentOrder = unmarshallOrder(currentLine);
                currentOrder.setDate(orderld);
                LocalDate ld = currentOrder.getDate();

                if (!orders.containsKey(ld)) {
                    HashMap<Integer, Order> innerOrders = new HashMap<>();
                    innerOrders.put(currentOrder.getOrderNumber(), currentOrder);
                    orders.put(ld, innerOrders);
                } else {
                    HashMap<Integer, Order> innerOrders = orders.get(ld);
                    innerOrders.put(currentOrder.getOrderNumber(), currentOrder);
                }
            }
            scanner.close();
        }

    }

    private String marshallOrder(Order order) {
        String orderText = String.valueOf(order.getOrderNumber()) + DELIMITER;
        if (order.getCustomerName().contains(",")) {
            orderText += "\"" + order.getCustomerName() + "\"" + DELIMITER;
        } else {
            orderText += order.getCustomerName() + DELIMITER;
        }

        orderText += order.getTax().getState() + DELIMITER;
        orderText += order.getTax().getTaxRate() + DELIMITER;
        orderText += order.getProduct().getType() + DELIMITER;
        orderText += order.getArea().toString() + DELIMITER;
        orderText += order.getProduct().getSquareFootCost() + DELIMITER;
        orderText += order.getProduct().getLaborCost() + DELIMITER;
        orderText += order.getMaterialCost().toString() + DELIMITER;
        orderText += order.getLaborCost().toString() + DELIMITER;
        orderText += order.getTaxCost().toString() + DELIMITER;
        orderText += order.getTotalCost().toString();

        return orderText;
    }

    public void writeOrders() throws PersistenceException {

        List<LocalDate> orderDates = new ArrayList<>(orders.keySet());

        Collections.sort(orderDates);

        for (LocalDate date : orderDates) {
            String fileName = ORDER_FOLDER + "/Orders_" + date.format(DateTimeFormatter.ofPattern("MMddyyyy")) + ".txt";
            File orderFile = new File(fileName);
            List<Order> allOrders = new ArrayList<>(orders.get(date).values());

            PrintWriter out;
            try {
                out = new PrintWriter(new FileWriter(orderFile));
            } catch (IOException e) {
                throw new PersistenceException("Could not save Orders");
            }

            String orderText;

            out.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total");
            if (allOrders.isEmpty()) {
                orderFile.delete();
            }
            for (Order currentOrder : allOrders) {
                orderText = this.marshallOrder(currentOrder);
                out.println(orderText);
                out.flush();
            }
            out.close();

        }

    }

    public OrderDaoImpl(ProductDao productDao, TaxDao taxDao) {
        this.productDao = productDao;
        this.taxDao = taxDao;
        ORDER_FOLDER = "Orders";
    }

    public OrderDaoImpl(String orderTextFile, ProductDao productDao, TaxDao taxDao) {
        this.productDao = productDao;
        this.taxDao = taxDao;
        ORDER_FOLDER = orderTextFile;
    }

}

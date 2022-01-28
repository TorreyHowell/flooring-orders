/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.th.flooringmastery.service;

import com.th.flooringmastery.dao.OrderDao;
import com.th.flooringmastery.dao.PersistenceException;
import com.th.flooringmastery.dao.ProductDao;
import com.th.flooringmastery.dao.TaxDao;
import com.th.flooringmastery.dto.Order;
import com.th.flooringmastery.dto.Product;
import com.th.flooringmastery.dto.Tax;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author workstation
 */
public class ServiceImpl implements Service {

    ProductDao productDao;
    TaxDao taxDao;
    OrderDao orderDao;

    public ServiceImpl(ProductDao productDao, TaxDao taxDao, OrderDao orderDao) {
        this.productDao = productDao;
        this.taxDao = taxDao;
        this.orderDao = orderDao;
    }

    @Override
    public List<Order> readOrders(LocalDate date) {
        try {
            return orderDao.getOrders(date);
        } catch (PersistenceException e) {
            return null;
        }
    }

    @Override
    public Order readOrder(LocalDate date, int orderNumber) throws PersistenceException {
        return orderDao.getOrder(date, orderNumber);
    }

    @Override
    public Order createOrder(Order newOrder) throws PersistenceException {
        return orderDao.addOrder(newOrder);
    }

    @Override
    public Order deleteOrder(Order removeOrder) throws PersistenceException {
        return orderDao.removeOrder(removeOrder);
    }

    @Override
    public Order updateOrder(Order updateOrder) throws PersistenceException {
        orderDao.editOrder(updateOrder);
        return updateOrder;
    }

    @Override
    public Order calculateOrder(Order order) throws InvalidDateException, InvalidAreaException, InvalidCustomerNameException, PersistenceException {
        if (order.getDate().compareTo(LocalDate.now()) < 0) {
            throw new InvalidDateException("Invalid Date");
        }
        if (order.getArea().compareTo(new BigDecimal("100")) < 0) {
            throw new InvalidAreaException("Invalid Area");
        }
        String regex = "^[a-zA-Z0-9,. ]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher match = pattern.matcher(order.getCustomerName());

        if (!match.matches()) {
            throw new InvalidCustomerNameException("Invalid Customer Name");
        }

        order.setOrderNumber(this.assignOrderNumber());

        order.setMaterialCost(calculateMaterialCost(order));
        order.setLaborCost(calculateLaborCost(order));
        order.setTaxCost(calculateTaxCost(order));
        order.setTotalCost(calculateTotalCost(order));
        return order;
    }

    @Override
    public void exportOrders() throws PersistenceException {
        orderDao.exportOrders();
    }

    @Override
    public List<Tax> readTaxes() throws PersistenceException {
        return new ArrayList<>(taxDao.getAllTax());
    }

    @Override
    public List<Product> readProducts() throws PersistenceException {
        return new ArrayList<>(productDao.getAllProducts());
    }

    private static BigDecimal calculateMaterialCost(Order order) {
        BigDecimal area = order.getArea().setScale(2, RoundingMode.HALF_UP);
        BigDecimal sqftCost = order.getProduct().getSquareFootCost().setScale(2, RoundingMode.HALF_UP);
        BigDecimal materialCost = area.multiply(sqftCost).setScale(2, RoundingMode.HALF_UP);
        return materialCost;
    }

    private static BigDecimal calculateLaborCost(Order order) {
        BigDecimal area = order.getArea().setScale(2, RoundingMode.HALF_UP);
        BigDecimal sqftCost = order.getProduct().getLaborCost().setScale(2, RoundingMode.HALF_UP);
        BigDecimal laborCost = area.multiply(sqftCost).setScale(2, RoundingMode.HALF_UP);
        return laborCost;
    }

    private static BigDecimal calculateTaxCost(Order order) {
        BigDecimal materialLaborCost = order.getLaborCost().add(order.getMaterialCost());
        BigDecimal taxRate = order.getTax().getTaxRate().divide(new BigDecimal("100"));
        BigDecimal taxCost = materialLaborCost.multiply(taxRate).setScale(2, RoundingMode.HALF_UP);
        return taxCost;

    }

    private static BigDecimal calculateTotalCost(Order order) {
        return order.getMaterialCost().add(order.getLaborCost()).add(order.getTaxCost()).setScale(2, RoundingMode.HALF_UP);
    }

    private int assignOrderNumber() throws PersistenceException {
        List<Order> allOrders = orderDao.getAllOrders();
        int max = allOrders.stream().mapToInt((o) -> o.getOrderNumber()).max().orElse(0);
        max++;
        return max;

    }

    @Override
    public Order calculateEditedOrder(Order order) throws InvalidAreaException, InvalidCustomerNameException, PersistenceException {

        if (order.getArea().compareTo(new BigDecimal("100")) < 0) {
            throw new InvalidAreaException("Invalid Area");
        }
        String regex = "^[a-zA-Z0-9,. ]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher match = pattern.matcher(order.getCustomerName());

        if (!match.matches()) {
            throw new InvalidCustomerNameException("Invalid Customer Name");
        }

        order.setMaterialCost(calculateMaterialCost(order));
        order.setLaborCost(calculateLaborCost(order));
        order.setTaxCost(calculateTaxCost(order));
        order.setTotalCost(calculateTotalCost(order));
        return order;
    }

}

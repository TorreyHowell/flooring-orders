/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.th.flooringmastery.service;

import com.th.flooringmastery.dao.PersistenceException;
import com.th.flooringmastery.dto.Order;
import com.th.flooringmastery.dto.Product;
import com.th.flooringmastery.dto.Tax;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author workstation
 */
public interface Service {

    List<Order> readOrders(LocalDate date);

    Order readOrder(LocalDate date, int orderNumber) throws PersistenceException;

    Order createOrder(Order newOrder) throws PersistenceException;

    Order deleteOrder(Order removeOrder) throws PersistenceException;

    Order updateOrder(Order updateOrder) throws PersistenceException;

    Order calculateOrder(Order orderToCalculate) throws InvalidDateException, InvalidAreaException, InvalidCustomerNameException, PersistenceException;

    void exportOrders() throws PersistenceException;

    List<Tax> readTaxes() throws PersistenceException;

    List<Product> readProducts() throws PersistenceException;

    Order calculateEditedOrder(Order order) throws InvalidAreaException, InvalidCustomerNameException, PersistenceException;

}

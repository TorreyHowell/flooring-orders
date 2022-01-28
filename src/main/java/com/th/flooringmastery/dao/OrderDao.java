/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.th.flooringmastery.dao;

import com.th.flooringmastery.dto.Order;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author workstation
 */
public interface OrderDao {

    Order addOrder(Order order) throws PersistenceException;

    List<Order> getOrders(LocalDate date) throws PersistenceException;

    Order getOrder(LocalDate date, int orderNumber) throws PersistenceException;

    List<Order> getAllOrders() throws PersistenceException;

    void editOrder(Order editOrder) throws PersistenceException;

    Order removeOrder(Order removeOrder) throws PersistenceException;

    void exportOrders() throws PersistenceException;

}

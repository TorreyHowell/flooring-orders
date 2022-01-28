/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.th.flooringmastery.service;

import com.th.flooringmastery.dao.OrderDao;
import com.th.flooringmastery.dao.PersistenceException;
import com.th.flooringmastery.dto.Order;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author torrey
 */
public class OrderDaoStubImpl implements OrderDao {

    public Order onlyOrder;
    

    public OrderDaoStubImpl() {
        onlyOrder = new Order("Torrey Howell");
        onlyOrder.setOrderNumber(1);
        onlyOrder.setDate(LocalDate.parse("2021-01-01"));
        onlyOrder.setArea(new BigDecimal("300"));
    }
    
    

    @Override
    public Order addOrder(Order order) {
        if (order.equals(onlyOrder)) {
            return onlyOrder;
        } else {
            return null;
        }
    }

    @Override
    public List<Order> getOrders(LocalDate date) {
        List<Order> orderList = new ArrayList<>();
        orderList.add(onlyOrder);
        return orderList;
    }

    @Override
    public Order getOrder(LocalDate date, int orderNumber) {
         if (date.equals(onlyOrder.getDate()) && orderNumber == onlyOrder.getOrderNumber()) {
            return onlyOrder;
        } else {
            return null;
        }
    }

    @Override
    public List<Order> getAllOrders() {
        List<Order> orderList = new ArrayList<>();
        orderList.add(onlyOrder);
        return orderList;
    }

    @Override
    public void editOrder(Order editOrder) {
        // do nothing
    }

    @Override
    public Order removeOrder(Order removeOrder) {
         if (removeOrder.equals(onlyOrder)) {
            return onlyOrder;
        } else {
            return null;
        }
    }

    @Override
    public void exportOrders() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

   
}

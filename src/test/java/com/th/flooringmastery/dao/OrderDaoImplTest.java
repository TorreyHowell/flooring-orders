/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.th.flooringmastery.dao;

import com.th.flooringmastery.dto.Order;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author torrey
 */
public class OrderDaoImplTest {

    OrderDao testOrderDao;
    ProductDao testProductDao;
    TaxDao testTaxDao;

    public OrderDaoImplTest() throws IOException {

        ApplicationContext appContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        testOrderDao = appContext.getBean("orderDao", OrderDaoImpl.class);
        testProductDao = appContext.getBean("productDao", ProductDaoImpl.class);
        testTaxDao = appContext.getBean("taxDao", TaxDaoImpl.class);

    }

    @BeforeAll
    public static void setUpClass() {

    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        File testOrders = new File("TestOrders");
        File[] files = testOrders.listFiles();
        for (File file : files) {
            file.delete();
        }

    }

    @AfterEach
    public void tearDown() {

    }

    @Test
    public void testAddGetOrder() throws PersistenceException {
        Order order = new Order("Torrey Howell");

        order.setOrderNumber(1);
        order.setProduct(testProductDao.getProduct("Carpet"));
        order.setTax(testTaxDao.getTax("TX"));
        order.setArea(new BigDecimal("200"));
        LocalDate ld = LocalDate.parse("2020-01-01");
        order.setDate(ld);
        order.setLaborCost(new BigDecimal("100"));
        order.setMaterialCost(new BigDecimal("100"));
        order.setTaxCost(new BigDecimal("100"));
        order.setTotalCost(new BigDecimal("100"));

        testOrderDao.addOrder(order);

        Order retrievedOrder = testOrderDao.getOrder(ld, 1);

        assertEquals(order, retrievedOrder);
    }

    @Test
    public void testGetOrders() throws PersistenceException {

        Order orderOne = new Order("Torrey Howell");

        orderOne.setOrderNumber(1);
        orderOne.setProduct(testProductDao.getProduct("Carpet"));
        orderOne.setTax(testTaxDao.getTax("TX"));
        orderOne.setArea(new BigDecimal("200"));
        LocalDate ld = LocalDate.parse("2020-01-01");
        orderOne.setDate(ld);
        orderOne.setLaborCost(new BigDecimal("100"));
        orderOne.setMaterialCost(new BigDecimal("100"));
        orderOne.setTaxCost(new BigDecimal("100"));
        orderOne.setTotalCost(new BigDecimal("100"));

        testOrderDao.addOrder(orderOne);

        Order orderTwo = new Order("Torrey Howell");

        orderTwo.setOrderNumber(2);
        orderTwo.setProduct(testProductDao.getProduct("Carpet"));
        orderTwo.setTax(testTaxDao.getTax("TX"));
        orderTwo.setArea(new BigDecimal("200"));
        LocalDate ld2 = LocalDate.parse("2020-01-02");
        orderTwo.setDate(ld);
        orderTwo.setLaborCost(new BigDecimal("100"));
        orderTwo.setMaterialCost(new BigDecimal("100"));
        orderTwo.setTaxCost(new BigDecimal("100"));
        orderTwo.setTotalCost(new BigDecimal("100"));

        testOrderDao.addOrder(orderTwo);

        Order order = new Order("Torrey Howell");

        order.setOrderNumber(3);
        order.setProduct(testProductDao.getProduct("Carpet"));
        order.setTax(testTaxDao.getTax("TX"));
        order.setArea(new BigDecimal("200"));
        LocalDate ld3 = LocalDate.parse("2020-01-02");
        order.setDate(ld3);
        order.setLaborCost(new BigDecimal("100"));
        order.setMaterialCost(new BigDecimal("100"));
        order.setTaxCost(new BigDecimal("100"));
        order.setTotalCost(new BigDecimal("100"));

        testOrderDao.addOrder(order);

        List<Order> orders = testOrderDao.getOrders(ld);

        assertTrue(orders.size() == 2);

    }

    @Test
    public void testGetAllOrders() throws PersistenceException {

        Order orderOne = new Order("Torrey Howell");

        orderOne.setOrderNumber(3);
        orderOne.setProduct(testProductDao.getProduct("Carpet"));
        orderOne.setTax(testTaxDao.getTax("TX"));
        orderOne.setArea(new BigDecimal("200"));
        LocalDate ld = LocalDate.parse("2020-01-02");
        orderOne.setDate(ld);
        orderOne.setLaborCost(new BigDecimal("100"));
        orderOne.setMaterialCost(new BigDecimal("100"));
        orderOne.setTaxCost(new BigDecimal("100"));
        orderOne.setTotalCost(new BigDecimal("100"));

        testOrderDao.addOrder(orderOne);

        Order orderTwo = new Order("Torrey Howell");

        orderTwo.setOrderNumber(3);
        orderTwo.setProduct(testProductDao.getProduct("Carpet"));
        orderTwo.setTax(testTaxDao.getTax("TX"));
        orderTwo.setArea(new BigDecimal("200"));
        LocalDate ld2 = LocalDate.parse("2020-01-03");
        orderTwo.setDate(ld2);
        orderTwo.setLaborCost(new BigDecimal("100"));
        orderTwo.setMaterialCost(new BigDecimal("100"));
        orderTwo.setTaxCost(new BigDecimal("100"));
        orderTwo.setTotalCost(new BigDecimal("100"));

        testOrderDao.addOrder(orderTwo);

        Order order = new Order("Torrey Howell");

        order.setOrderNumber(3);
        order.setProduct(testProductDao.getProduct("Carpet"));
        order.setTax(testTaxDao.getTax("TX"));
        order.setArea(new BigDecimal("200"));
        LocalDate ld3 = LocalDate.parse("2020-01-01");
        order.setDate(ld3);
        order.setLaborCost(new BigDecimal("100"));
        order.setMaterialCost(new BigDecimal("100"));
        order.setTaxCost(new BigDecimal("100"));
        order.setTotalCost(new BigDecimal("100"));

        testOrderDao.addOrder(order);

        List<Order> orders = testOrderDao.getAllOrders();

        assertTrue(orders.size() == 3);

    }

    @Test
    public void testEditOrder() throws PersistenceException {

        Order order = new Order("Torrey Howell");

        order.setOrderNumber(1);
        order.setProduct(testProductDao.getProduct("Carpet"));
        order.setTax(testTaxDao.getTax("TX"));
        order.setArea(new BigDecimal("200"));
        LocalDate ld = LocalDate.parse("2020-01-01");
        order.setDate(ld);
        order.setLaborCost(new BigDecimal("100"));
        order.setMaterialCost(new BigDecimal("100"));
        order.setTaxCost(new BigDecimal("100"));
        order.setTotalCost(new BigDecimal("100"));

        testOrderDao.addOrder(order);

        Order orderTwo = new Order("Torrey Howell Inc");

        orderTwo.setOrderNumber(1);
        orderTwo.setProduct(testProductDao.getProduct("Carpet"));
        orderTwo.setTax(testTaxDao.getTax("CA"));
        orderTwo.setArea(new BigDecimal("250"));
        orderTwo.setDate(ld);
        orderTwo.setLaborCost(new BigDecimal("100"));
        orderTwo.setMaterialCost(new BigDecimal("100"));
        orderTwo.setTaxCost(new BigDecimal("100"));
        orderTwo.setTotalCost(new BigDecimal("100"));

        

        testOrderDao.editOrder(orderTwo);

        assertEquals(testOrderDao.getOrder(ld, 1), orderTwo);

    }

    @Test
    public void testRemoveOrder() throws PersistenceException {

        Order order = new Order("Torrey Howell");

        order.setOrderNumber(1);
        order.setProduct(testProductDao.getProduct("Carpet"));
        order.setTax(testTaxDao.getTax("TX"));
        order.setArea(new BigDecimal("200"));
        LocalDate ld = LocalDate.parse("2020-01-01");
        order.setDate(ld);
        order.setLaborCost(new BigDecimal("100"));
        order.setMaterialCost(new BigDecimal("100"));
        order.setTaxCost(new BigDecimal("100"));
        order.setTotalCost(new BigDecimal("100"));

        testOrderDao.addOrder(order);
        List<Order> orders = testOrderDao.getAllOrders();

        assertTrue(orders.contains(order));

        testOrderDao.removeOrder(order);

        orders = testOrderDao.getAllOrders();

        assertFalse(orders.contains(order));
    }

}

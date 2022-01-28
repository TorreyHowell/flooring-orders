/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.th.flooringmastery.service;

import com.th.flooringmastery.dao.PersistenceException;
import com.th.flooringmastery.dao.ProductDao;
import com.th.flooringmastery.dao.ProductDaoImpl;
import com.th.flooringmastery.dao.TaxDao;
import com.th.flooringmastery.dao.TaxDaoImpl;
import com.th.flooringmastery.dto.Order;
import java.math.BigDecimal;
import java.time.LocalDate;
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
public class ServiceImplTest {

    private final Service service;
    private final ProductDao testProductDao;
    private final TaxDao testTaxDao;

    public ServiceImplTest() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        service = ctx.getBean("service", ServiceImpl.class);
        testProductDao = ctx.getBean("productDao", ProductDaoImpl.class);
        testTaxDao = ctx.getBean("taxDao", TaxDaoImpl.class);
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testCalculateAndCreateValidOrder() throws PersistenceException {

        Order order = new Order("Torrey Howell");
        order.setOrderNumber(1);
        order.setDate(LocalDate.now());
        order.setProduct(testProductDao.getProduct("Tile"));
        order.setTax(testTaxDao.getTax("TX"));
        order.setArea(new BigDecimal("300"));

        Order calculatedOrder;
        try {
            calculatedOrder = service.calculateOrder(order);
        } catch (InvalidDateException | InvalidAreaException | InvalidCustomerNameException e) {
            fail("Order was valid no exception should have been thrown. " + e);
        }

    }

    @Test
    public void testInvalidName() throws PersistenceException {

        Order order = new Order("       / & $ ");
        order.setOrderNumber(1);
        order.setDate(LocalDate.now());
        order.setProduct(testProductDao.getProduct("Tile"));
        order.setTax(testTaxDao.getTax("TX"));
        order.setArea(new BigDecimal("300"));

        Order calculatedOrder;
        try {
            calculatedOrder = service.calculateOrder(order);
            fail("expected invalid customer name exception");
        } catch (InvalidDateException | InvalidAreaException e) {
            fail("Wrong exception thrown.");
        } catch (InvalidCustomerNameException e) {

        }

    }

    @Test
    public void testInvalidArea() throws PersistenceException {

        Order order = new Order("Torrey Howell");
        order.setOrderNumber(1);
        order.setDate(LocalDate.now());
        order.setProduct(testProductDao.getProduct("Tile"));
        order.setTax(testTaxDao.getTax("TX"));
        order.setArea(new BigDecimal("99"));

        Order calculatedOrder;

        try {
            calculatedOrder = service.calculateOrder(order);
            fail("expected invalid area exception");
        } catch (InvalidDateException | InvalidCustomerNameException e) {
            fail("Wrong exception thrown.");
        } catch (InvalidAreaException e) {

        }

    }

    @Test
    public void testInvalidDateException() throws PersistenceException {

        Order order = new Order("Torrey Howell");
        order.setOrderNumber(1);
        order.setDate(LocalDate.now().minusDays(1));
        order.setProduct(testProductDao.getProduct("Tile"));
        order.setTax(testTaxDao.getTax("TX"));
        order.setArea(new BigDecimal("99"));

        Order calculatedOrder;

        try {
            calculatedOrder = service.calculateOrder(order);
            fail("Expected invalid date exception");
        } catch (InvalidAreaException | InvalidCustomerNameException e) {
            fail("Wrong exception thrown");
        } catch (InvalidDateException e) {

        }

    }

    @Test
    public void testReadAllOrders() throws PersistenceException {
        Order testClone = new Order("Torrey Howell");
        testClone.setOrderNumber(1);
        testClone.setDate(LocalDate.parse("2021-01-01"));
        testClone.setArea(new BigDecimal("300"));

        assertEquals(1, service.readOrders(LocalDate.parse("2021-01-01")).size());
        assertTrue(service.readOrders(LocalDate.parse("2021-01-01")).contains(testClone));
    }

    @Test
    public void testReadOrder() throws PersistenceException {
        Order testClone = new Order("Torrey Howell");
        testClone.setOrderNumber(1);
        testClone.setDate(LocalDate.parse("2021-01-01"));
        testClone.setArea(new BigDecimal("300"));

        Order shouldBeMe = service.readOrder(LocalDate.parse("2021-01-01"), 1);
        assertNotNull(shouldBeMe);
        assertEquals(testClone, shouldBeMe);

        Order shouldBeNull = service.readOrder(LocalDate.parse("2021-01-01"), 550);
        assertNull(shouldBeNull);
    }

    @Test
    public void testRemoveOrder() throws PersistenceException {
        Order testClone = new Order("Torrey Howell");
        testClone.setOrderNumber(1);
        testClone.setDate(LocalDate.parse("2021-01-01"));
        testClone.setArea(new BigDecimal("300"));
        
        

        Order shouldBeMe = service.deleteOrder(testClone);
        assertNotNull(shouldBeMe);
        assertEquals(testClone, shouldBeMe);

        
    }
    
    @Test void testCalculations() throws PersistenceException, InvalidAreaException, InvalidCustomerNameException {
        Order testClone = new Order("Torrey Howell");
        testClone.setOrderNumber(1);
        testClone.setDate(LocalDate.parse("2021-01-01"));
        testClone.setArea(new BigDecimal("300"));
        testClone.setTax(testTaxDao.getTax("TX"));
        testClone.setProduct(testProductDao.getProduct("Tile"));
        
        Order calculatedOrder = service.calculateEditedOrder(testClone);
        
        assertEquals(calculatedOrder.getLaborCost(), new BigDecimal("1245.00"));
        assertEquals(calculatedOrder.getMaterialCost(), new BigDecimal("1050.00"));
        assertEquals(calculatedOrder.getTaxCost(), new BigDecimal("102.13"));
        assertEquals(calculatedOrder.getTotalCost(), new BigDecimal("2397.13"));
    }

}

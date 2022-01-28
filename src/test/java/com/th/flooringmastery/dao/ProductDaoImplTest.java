/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.th.flooringmastery.dao;

import com.th.flooringmastery.dto.Product;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author torrey
 */
public class ProductDaoImplTest {
    
    ProductDao testProductDao;
    
    public ProductDaoImplTest() {
        ApplicationContext appContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        testProductDao = appContext.getBean("productDao", ProductDaoImpl.class);
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
    public void testGetProduct() throws PersistenceException {
        Product testProductOne = testProductDao.getProduct("Tile");
        Product testProductTwo = testProductDao.getProduct("Carpet");
        
        assertEquals(testProductOne.getType(), "Tile");
        assertEquals(testProductTwo.getType(), "Carpet");
    }
    
    @Test
    public void testGetAllProduct() throws PersistenceException {
        List<Product> testList = testProductDao.getAllProducts();
        
        assertEquals(testList.size(), 4);
    }
    
}

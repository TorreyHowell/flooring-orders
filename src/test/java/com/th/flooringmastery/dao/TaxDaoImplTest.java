/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.th.flooringmastery.dao;

import com.th.flooringmastery.dto.Tax;
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
public class TaxDaoImplTest {
    
    TaxDao testTaxDao;
    
    public TaxDaoImplTest() {
        ApplicationContext appContext = new ClassPathXmlApplicationContext("applicationContext.xml");
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
    }
    
    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testGetTax() throws PersistenceException {
        Tax testTaxOne = testTaxDao.getTax("TX");
        Tax testTaxTwo = testTaxDao.getTax("KY");
        
        assertEquals(testTaxOne.getStateName(), "Texas");
        assertEquals(testTaxTwo.getStateName(), "Kentucky");
    }
    
    @Test
    public void testGetAllTax() throws PersistenceException {
        List<Tax> testList = testTaxDao.getAllTax();
        
        assertEquals(testList.size(), 4);
    }
    
}

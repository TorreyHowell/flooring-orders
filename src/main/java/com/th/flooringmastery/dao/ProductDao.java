/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.th.flooringmastery.dao;

import com.th.flooringmastery.dto.Product;
import java.util.List;

/**
 *
 * @author workstation
 */
public interface ProductDao {

    List<Product> getAllProducts() throws PersistenceException;

    Product getProduct(String product) throws PersistenceException;

}

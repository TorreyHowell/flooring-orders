/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.th.flooringmastery.dao;

import com.th.flooringmastery.dto.Tax;
import java.util.List;

/**
 *
 * @author workstation
 */
public interface TaxDao {

    List<Tax> getAllTax() throws PersistenceException;

    Tax getTax(String state) throws PersistenceException;
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.th.flooringmastery.dao;

import com.th.flooringmastery.dto.Tax;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author workstation
 */
public class TaxDaoImpl implements TaxDao {

    HashMap<String, Tax> taxes = new HashMap<>();
    private final String TAX_FILE;
    public static final String DELIMITER = ",";

    @Override
    public List<Tax> getAllTax() throws PersistenceException {
        this.loadTaxes();

        return new ArrayList<>(taxes.values());
    }

    @Override
    public Tax getTax(String state) throws PersistenceException {
        this.loadTaxes();

        return taxes.get(state);
    }

    public Tax unmarshallTaxes(String taxAsText) {
        String[] taxTokens = taxAsText.split(DELIMITER);
        String state = taxTokens[0];
        Tax taxFromFile = new Tax(state);
        taxFromFile.setStateName(taxTokens[1]);
        taxFromFile.setTaxRate(new BigDecimal(taxTokens[2]));
        return taxFromFile;
    }

    private void loadTaxes() throws PersistenceException {
        Scanner scanner;
        try {
            scanner = new Scanner(new BufferedReader(new FileReader(TAX_FILE)));
        } catch (FileNotFoundException e) {
            throw new PersistenceException("could not load taxes");
        }

        String currentLine;
        Tax currentTax;

        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentTax = this.unmarshallTaxes(currentLine);
            taxes.put(currentTax.getState(), currentTax);
        }
        scanner.close();
    }

    public TaxDaoImpl() {
        this.TAX_FILE = "Data/taxes.txt";
    }

    public TaxDaoImpl(String taxFile) {
        this.TAX_FILE = taxFile;
    }

}

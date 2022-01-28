/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.th.flooringmastery.dao;

import com.th.flooringmastery.dto.Product;
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
public class ProductDaoImpl implements ProductDao {

    HashMap<String, Product> products = new HashMap<>();
    public static final String DELIMITER = ",";
    private final String PRODUCT_FILE;

    @Override
    public List<Product> getAllProducts() throws PersistenceException {
        this.loadProducts();

        return new ArrayList<>(products.values());
    }

    @Override
    public Product getProduct(String product) throws PersistenceException {
        this.loadProducts();
        return products.get(product);
    }

    private Product unmarshallProduct(String productAsText) {
        String[] productTokens = productAsText.split(DELIMITER);
        String type = productTokens[0];
        Product productFromFile = new Product(type);
        productFromFile.setSquareFootCost(new BigDecimal(productTokens[1]));
        productFromFile.setLaborCost(new BigDecimal(productTokens[2]));
        return productFromFile;
    }

    private void loadProducts() throws PersistenceException {
        Scanner scanner;
        try {
            scanner = new Scanner(new BufferedReader(new FileReader(PRODUCT_FILE)));
        } catch (FileNotFoundException e) {
            throw new PersistenceException("Could not load products");
        }

        String currentLine;
        Product currentProduct;

        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentProduct = unmarshallProduct(currentLine);
            products.put(currentProduct.getType(), currentProduct);
        }
        scanner.close();
    }

    public ProductDaoImpl() {
        PRODUCT_FILE = "Data/products.txt";
    }

    public ProductDaoImpl(String productTextFile) {
        PRODUCT_FILE = productTextFile;
    }

}

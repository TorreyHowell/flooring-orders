/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.th.flooringmastery.dto;

import java.math.BigDecimal;
import java.util.Objects;

/**
 *
 * @author workstation
 */
public class Product {

    private String type;

    private BigDecimal squareFootCost;

    private BigDecimal laborCost;

    public Product(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getSquareFootCost() {
        return squareFootCost;
    }

    public void setSquareFootCost(BigDecimal squareFootCost) {
        this.squareFootCost = squareFootCost;
    }

    public BigDecimal getLaborCost() {
        return laborCost;
    }

    public void setLaborCost(BigDecimal laborCost) {
        this.laborCost = laborCost;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.type);
        hash = 79 * hash + Objects.hashCode(this.squareFootCost);
        hash = 79 * hash + Objects.hashCode(this.laborCost);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Product other = (Product) obj;
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        if (!Objects.equals(this.squareFootCost, other.squareFootCost)) {
            return false;
        }
        if (!Objects.equals(this.laborCost, other.laborCost)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Product{" + "type=" + type + ", squareFootCost=" + squareFootCost + ", laborCost=" + laborCost + '}';
    }

}

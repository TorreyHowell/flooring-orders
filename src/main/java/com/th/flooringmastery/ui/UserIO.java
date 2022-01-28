/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.th.flooringmastery.ui;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 * @author workstation
 */
public interface UserIO {

    String readString(String message);

    int readInt(String message);

    int readInt(String message, int min, int max);

    BigDecimal readBigDecimal(String message);

    LocalDate readDate(String message);

    void print(String message);

    LocalDate readNewOrderDate(String message);

    BigDecimal readOrderBigDecimal(String message);

    String readOrderName(String message);

    String readEditOrderName(String message);

    BigDecimal readEditBigDecimal(String message);

}

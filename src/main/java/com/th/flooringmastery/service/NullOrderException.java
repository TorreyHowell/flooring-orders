/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.th.flooringmastery.service;

/**
 *
 * @author torrey
 */
public class NullOrderException extends Exception {

    public NullOrderException(String message) {
        super(message);
    }

    public NullOrderException(String message, Throwable cause) {
        super(message, cause);
    }

}

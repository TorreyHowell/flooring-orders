/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.th.flooringmastery.controller;

import com.th.flooringmastery.dao.PersistenceException;
import com.th.flooringmastery.dto.Order;
import com.th.flooringmastery.service.InvalidAreaException;
import com.th.flooringmastery.service.InvalidCustomerNameException;
import com.th.flooringmastery.service.InvalidDateException;
import com.th.flooringmastery.service.Service;
import com.th.flooringmastery.ui.View;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author workstation
 */
public class Controller {
    
    private final Service service;
    private final View view;
    
    public Controller(View view, Service service){
        this.service = service;
        this.view = view;
    }
    
    public void run() {
        Boolean menuLoop = true;
        while(menuLoop) {
            int menuChoice = getMenuSelection();
            
            switch(menuChoice) {
                case 1:
                    this.displayOrders();
                    break;
                case 2:
                    this.addAnOrder();
                    break;
                case 3:
                    this.editAnOrder();
                    break;
                case 4:
                    this.removeAnOrder();
                    break;
                case 5:
                    this.exportOrders();
                    break;
                case 6:
                    menuLoop = false;
                    break;
                default: 
                    break;
            }
        }
    }
    
    private int getMenuSelection(){
        return view.displayMenuGetChoice();
    }
    
    private void displayOrders() {
        LocalDate ld = view.getdate();
        List<Order> Orders = service.readOrders(ld);
        view.displayOrders(Orders);
        view.displayPause();
    }
    
    private void addAnOrder() {
        Order newOrder;
       
        try {
            newOrder = view.getNewOrder(service.readProducts(), service.readTaxes());
            service.calculateOrder(newOrder);
        }catch(InvalidDateException | InvalidAreaException | InvalidCustomerNameException | PersistenceException e) {
            view.showError(e);
            return;
        }
        if (view.showOrderConfirmation(newOrder)) {
            try {
                service.createOrder(newOrder);
                view.showOrderCreated();
            } catch(PersistenceException e) {
                view.showError(e);
            }
        } else{
            view.showOrderCreated();
        }
    }
    
    private void editAnOrder() {
        Order edited;
        try {
            edited = service.readOrder(view.getOrderDate(), view.getOrderNumber());
            if (edited == null){
                view.showNoOrderFound();
                return;
            }
            edited = view.getEditedOrder(edited, service.readTaxes(), service.readProducts());
        } catch(PersistenceException e) {
            view.showError(e);
            return;
        }
        
        try {
            service.calculateEditedOrder(edited);
        } catch(InvalidAreaException | InvalidCustomerNameException | PersistenceException e) {
            view.showError(e);
            return;
        }
        
        if(view.showEditOrderConfirmation(edited)) {
            try{
               service.updateOrder(edited);
               view.showOrderCreated();
            } catch(PersistenceException e){
                view.showError(e);
                
            }
            
        } else {
            view.showOrderNotCreated();
        }
        
    }
    
    private void removeAnOrder() {
        Order remove;
        
        try {
            remove = service.readOrder(view.getOrderDate(), view.getOrderNumber());
            if (remove == null){
                view.showNoOrderFound();
                return;
            }
            if (view.showRemoveConfirmation(remove)) {
            service.deleteOrder(remove);
            }
        } catch(PersistenceException e) {
            view.showError(e);
        
        }
        
        
    }
    
    private void exportOrders() {
        try{
            service.exportOrders();
        }catch(PersistenceException e){
            view.showError(e);
        }
        view.showExportConfirmation();
    }
    
}

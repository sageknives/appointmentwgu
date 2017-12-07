/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointment.services;

import appointment.models.CustomerInterface;

/**
 *
 * @author sagegatzke
 */
public interface CustomerServiceInterface {

    CustomerInterface addCustomer(CustomerInterface appointment);

    CustomerInterface getCustomer(int customerId);

    CustomerInterface[] getCustomers();

    CustomerInterface updateCustomer(CustomerInterface customer);
    
}

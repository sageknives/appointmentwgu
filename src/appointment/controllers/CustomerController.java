/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointment.controllers;

import appointment.models.AddressInterface;
import appointment.models.Customer;
import appointment.models.CustomerInterface;
import appointment.models.UserInterface;
import appointment.services.CustomerService;
import appointment.services.CustomerServiceInterface;
import java.time.LocalDateTime;

/**
 *
 * @author sagegatzke
 */
public class CustomerController implements CustomerControllerInterface {

    private CustomerServiceInterface customerService = new CustomerService();
    private AddressControllerInterface addressController;
    private final CommunicatorInterface communicator;
    private UserInterface user;

    public CustomerController(CommunicatorInterface communicator, UserInterface user) {
        this.communicator = communicator;
        this.user = user;
        this.addressController = new AddressController(this.communicator, user);
    }

    @Override
    public CustomerInterface getCustomer() {
        return this.getCustomer(null);
    }
    @Override
    public CustomerInterface getCustomer(CustomerInterface currentCustomer) {
        CustomerInterface customer = new Customer();
        CustomerInterface[] customers = this.getCustomers();
        int id = -1;
        while (true) {
            communicator.out("0) Create new Customer");
            for (int i = 0; i < customers.length; i++) {
                if(currentCustomer != null && currentCustomer.getCustomerId() == customers[i].getCustomerId()){
                    communicator.out((i+1) + "*) " + customers[i].getCustomerName());
                }else{
                    communicator.out((i+1) + ") " + customers[i].getCustomerName());
                }
            }
            communicator.out("x) to go back");
            communicator.out("or press return to keep the current selection");
            String response = communicator.askFor("Choose an option: ");
            if (response.equals("0")) {
                customer = this.addCustomer();
                return customer;
            }
            if (response.equals("x")) {
                //should probably throw something instead of nulling back
                return null;
            }
            if(response.equals("") && currentCustomer != null){
                return currentCustomer;
            }
            if (communicator.isInt(response)) {
                int selection = Integer.parseInt(response)-1;
                if (selection >= 0 && selection < customers.length) {
                    customer = customers[selection];
                    return customer;
                }
            }
            communicator.out("Invalid option");
        }
    }

    @Override
    public CustomerInterface[] getCustomers() {
        return this.customerService.getCustomers();
    }

    @Override
    public CustomerInterface addCustomer() {
//        CountryInterface country = new Country();
//        CityInterface city = new City();
//        city.setCountry(country);
//        AddressInterface address = new Address();
//        address.setCity(city);
        CustomerInterface customer = new Customer();
//        customer.setAddress(address);

        communicator.out("Add Customer:");
        while (true) {
            customer.setCustomerName(communicator.askFor("Please enter the Customer's full name: ", customer.getCustomerName()));
            AddressInterface address = addressController.addAddress();
            customer.setAddress(address);
            showCustomer(customer);
            communicator.out("Customer Confirmation:");
            if (communicator.confirm()) {
                customer.setCreatedBy(this.user.getUserName());
                customer.setLastUpdatedBy(this.user.getUserName());
                customer.setCreatedDate(LocalDateTime.now());
                customer.setLastUpdate(LocalDateTime.now());
                customer = customerService.addCustomer(customer);
                break;
            }
        }
        return customer;
    }

    @Override
    public CustomerInterface updateCustomer() {
        communicator.out("Customers:");
        CustomerInterface[] customers = this.getCustomers();
        CustomerInterface customer = null;
        while (true) {

            for (int i = 0; i < customers.length; i++) {
                System.out.println((i+1) + ") " + customers[i].getCustomerName());
            }
            communicator.out("0) Create new Customer");
            communicator.out("x) to go back");
            String response = communicator.askFor("Choose an option: ");
            if (response.equals("0")) {
                return this.addCustomer();
            }
            if (response.equals("x")) {
                return null;
            }
            if (communicator.isInt(response)) {
                int selection = Integer.parseInt(response)-1;
                if (selection >= 0 && selection < customers.length) {
                    customer = customers[selection];
                    break;
                }
            }
            communicator.out("Invalid option");
        }

        while (customer != null) {
            customer.setCustomerName(communicator.askFor("Please enter the Customer's full name: ", customer.getCustomerName()));
            AddressInterface address = addressController.updateAddress(customer.getAddress());
            customer.setAddress(address);
            showCustomer(customer);
            communicator.out("Customer Confirmation:");
            if (communicator.confirm()) {
                customer.setLastUpdatedBy(this.user.getUserName());
                customer.setLastUpdate(LocalDateTime.now());
                customer = customerService.updateCustomer(customer);
                break;
            }
        }
        return customer;
    }

    public void showCustomer(CustomerInterface customer) {
        communicator.out("Customer Name: " + customer.getCustomerName());
        addressController.showAddress(customer.getAddress());
    }
}

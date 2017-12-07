/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointment.models;

import java.time.LocalDateTime;

/**
 *
 * @author sagegatzke
 */
public class Customer extends Base implements CustomerInterface {
    private int customerId;
    private String customerName;
    private AddressInterface address;
    private int active;
    
    @Override
    public int getCustomerId(){ return this.customerId; }
    @Override
    public String getCustomerName(){ return this.customerName; }
    @Override
    public AddressInterface getAddress(){ return this.address; }
    @Override
    public int getActive(){ return this.active; }
    
    @Override
    public void setCustomerId(int customerId){ this.customerId = customerId; }
    @Override
    public void setCustomerName(String customerName){ this.customerName = customerName; }
    @Override
    public void setAddress(AddressInterface address){ this.address = address; }
    @Override
    public void setActive(int active){ this.active = active; }

    public Customer(int customerId, String customerName, AddressInterface address, int active, String createdBy, LocalDateTime createdDate, String lastUpdatedBy,LocalDateTime lastUpdated){
        super(createdBy,createdDate,lastUpdatedBy,lastUpdated);
        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
        this.active = active;
    }
    
    public Customer(){
        super();
        this.customerId = -1;
    }
}

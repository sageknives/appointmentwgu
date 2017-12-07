/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointment.models;

/**
 *
 * @author sagegatzke
 */
public interface CustomerInterface extends BaseInterface{

    int getActive();

    AddressInterface getAddress();

    int getCustomerId();

    String getCustomerName();

    void setActive(int active);

    void setAddress(AddressInterface address);

    void setCustomerId(int customerId);

    void setCustomerName(String customerName);
    
}

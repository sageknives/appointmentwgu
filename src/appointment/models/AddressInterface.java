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
public interface AddressInterface extends BaseInterface {

    String getAddress();

    String getAddress2();

    int getAddressId();

    CityInterface getCity();

    String getPhone();

    String getPostalCode();

    void setAddress(String address);

    void setAddress2(String address1);

    void setAddressId(int addressId);

    void setCity(CityInterface city);

    void setPhone(String phone);

    void setPostalCode(String postalCode);
    
}

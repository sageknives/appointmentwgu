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
public class Address extends Base implements AddressInterface {
    private int addressId;
    private String address;
    private String address2;
    private CityInterface city;
    private String postalCode;
    private String phone;
    
    @Override
    public int getAddressId(){ return this.addressId; }
    @Override
    public String getAddress(){ return this.address; }
    @Override
    public String getAddress2(){ return this.address2; }
    @Override
    public CityInterface getCity(){ return this.city; }
    @Override
    public String getPostalCode(){ return this.postalCode; }
    @Override
    public String getPhone(){ return this.phone; }
    @Override
    public void setAddressId(int addressId){ this.addressId = addressId; }
    @Override
    public void setAddress(String address){ this.address = address; }
    @Override
    public void setAddress2(String address2){ this.address2 = address2; }
    @Override
    public void setCity(CityInterface city){ this.city = city; }
    @Override
    public void setPostalCode(String postalCode){ this.postalCode = postalCode; }
    @Override
    public void setPhone(String phone){ this.phone = phone; }

    public Address(int addressId, String address, String address2, CityInterface city, String postalCode, String phone, String createdBy, LocalDateTime createdDate, String lastUpdatedBy,LocalDateTime lastUpdated){
        super(createdBy,createdDate,lastUpdatedBy,lastUpdated);
        this.addressId = addressId;
        this.address = address;
        this.address2 = address2;
        this.city = city;
        this.postalCode = postalCode;
        this.phone = phone;
    }
    
    public Address(){
        super();
        this.addressId = -1;
    }
}

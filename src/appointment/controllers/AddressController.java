/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointment.controllers;

import appointment.models.Address;
import appointment.models.AddressInterface;
import appointment.models.CityInterface;
import appointment.models.UserInterface;
import appointment.services.AddressService;
import appointment.services.AddressServiceInterface;
import java.time.LocalDateTime;

/**
 *
 * @author sagegatzke
 */
public class AddressController implements AddressControllerInterface {

    private AddressServiceInterface addressService = new AddressService();
    private CityControllerInterface cityController;
    private CommunicatorInterface communicator;
    private UserInterface user;

    public AddressController(CommunicatorInterface communicator, UserInterface user) {
        this.communicator = communicator;
        this.user = user;
        this.cityController = new CityController(this.communicator, user);
    }

    @Override
    public AddressInterface addAddress() {
        AddressInterface address = new Address();
        CityInterface city = null;
        boolean firstTime = true;
        while (true) {
            if(firstTime) {
                city = cityController.addCity();
                firstTime = false;
            }
            else{
                city = cityController.updateCity(address.getCity());
            }
            
            address.setCity(city);
            address = updateAddressDetails(address);
            showAddress(address);
            communicator.out("Address Confirmation:");
            if (communicator.confirm()) {
                address.setCreatedBy(this.user.getUserName());
                address.setLastUpdatedBy(this.user.getUserName());
                address.setCreatedDate(LocalDateTime.now());
                address.setLastUpdate(LocalDateTime.now());
                break;
            }
        }
        return addressService.addAddress(address);
    }

    @Override
    public AddressInterface updateAddress(AddressInterface address) {
        while (true) {
            CityInterface city = cityController.updateCity(address.getCity());
            address.setCity(city);
            address = updateAddressDetails(address);
            showAddress(address);
            communicator.out("Address Confirmation:");
            if (communicator.confirm()) {
                address.setLastUpdatedBy(this.user.getUserName());
                address.setLastUpdate(LocalDateTime.now());
                break;
            }
        }
        return addressService.updateAddress(address);
    }

    private AddressInterface updateAddressDetails(AddressInterface address) {
        address.setAddress(communicator.askFor("Please enter the Customer's Address 1", address.getAddress()));
        address.setAddress2(communicator.askFor("Please enter the Customer's Address 2", address.getAddress2()));
        address.setPostalCode(communicator.askFor("Please enter the Customer's Postal Code", address.getPostalCode()));
        address.setPhone(communicator.askFor("Please enter the Customer's Phone number", address.getPhone()));
        return address;
    }

    @Override
    public void showAddress(AddressInterface address) {
        communicator.out(address.getAddress());
        communicator.out(address.getAddress2());
        communicator.out(address.getPhone());
        communicator.out(address.getPostalCode());
        cityController.showCity(address.getCity());
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointment.controllers;

import appointment.models.AddressInterface;

/**
 *
 * @author sagegatzke
 */
public interface AddressControllerInterface {

    AddressInterface addAddress();

    AddressInterface updateAddress(AddressInterface address);
    
    void showAddress(AddressInterface address);
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointment.services;

import appointment.models.AddressInterface;

/**
 *
 * @author sagegatzke
 */
public interface AddressServiceInterface {

    AddressInterface addAddress(AddressInterface address);

    AddressInterface updateAddress(AddressInterface address);
    
}

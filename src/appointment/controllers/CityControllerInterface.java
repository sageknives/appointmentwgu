/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointment.controllers;

import appointment.models.CityInterface;

/**
 *
 * @author sagegatzke
 */
public interface CityControllerInterface {

    CityInterface addCity();

    CityInterface updateCity(CityInterface city);
    
    CityInterface[] getCities(int countryId);
    
    void showCity(CityInterface city);
}

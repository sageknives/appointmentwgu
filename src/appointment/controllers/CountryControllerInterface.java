/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointment.controllers;

import appointment.models.CountryInterface;

/**
 *
 * @author sagegatzke
 */
public interface CountryControllerInterface {

    CountryInterface addCountry(CountryInterface country);

    CountryInterface[] getCountries();
    
    void showCountry(CountryInterface country);
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointment.controllers;

import appointment.models.City;
import appointment.models.CityInterface;
import appointment.models.Country;
import appointment.models.CountryInterface;
import appointment.models.UserInterface;
import appointment.services.CityService;
import appointment.services.CityServiceInterface;
import java.time.LocalDateTime;

/**
 *
 * @author sagegatzke
 */
public class CityController implements CityControllerInterface {

    private CityServiceInterface cityService = new CityService();
    private CountryControllerInterface countryController;
    private CommunicatorInterface communicator;
    private UserInterface user;

    public CityController(CommunicatorInterface communicator, UserInterface user) {
        this.communicator = communicator;
        this.user = user;
        this.countryController = new CountryController(this.communicator, user);
    }

    @Override
    public CityInterface addCity() {
        CityInterface city = new City();
        city.setCountry(new Country());
        return this.updateCity(city);
    }

    @Override
    public CityInterface updateCity(CityInterface currentCity) {
        CountryInterface country = this.countryController.addCountry(currentCity.getCountry());
        CityInterface[] cities = this.getCities(country.getCountryId());
        while (true) {
            for (int i = 0; i < cities.length; i++) {
                if (currentCity.getCityId() == cities[i].getCityId()) {
                    communicator.out(i + "*) " + cities[i].getCity());
                } else {
                    communicator.out(i + ") " + cities[i].getCity());
                }
            }
            String result = communicator.askFor("Choose a city or type a new one");
            if (communicator.isInt(result)) {
                int index = Integer.parseInt(result);
                if (index < cities.length && index > -1) {
                    return cities[index];
                } else {
                    communicator.out("Invalid Option");
                }
            } else {
                CityInterface newCity = new City();
                newCity.setCity(result);
                newCity.setCityId(-1);
                newCity.setCountry(country);
                newCity.setCreatedBy(this.user.getUserName());
                newCity.setLastUpdatedBy(this.user.getUserName());
                newCity.setCreatedDate(LocalDateTime.now());
                newCity.setLastUpdate(LocalDateTime.now());
                return this.cityService.addCity(newCity);
            }
        }
    }

    @Override
    public CityInterface[] getCities(int countryId) {
        return this.cityService.getCities(countryId);
    }
    
    @Override
    public void showCity(CityInterface city){
        communicator.out("City: " + city.getCity());
        countryController.showCountry(city.getCountry());
    }
}

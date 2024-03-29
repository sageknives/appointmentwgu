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

    private final CityServiceInterface cityService = new CityService();
    private final CountryControllerInterface countryController;
    private final CommunicatorInterface communicator;
    private final UserInterface user;

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
                    communicator.out((i+1) + "*) " + cities[i].getCity());
                } else {
                    communicator.out((i+1) + ") " + cities[i].getCity());
                }
            }
            String result = communicator.askFor("Choose a city or type a new one");
            if (communicator.isInt(result)) {
                int index = Integer.parseInt(result)-1;
                if (index >= 0 && index < cities.length) {
                    currentCity = cities[index];
                    currentCity.setCountry(country);
                    return currentCity;
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
                currentCity = this.cityService.addCity(newCity);
                currentCity.setCountry(country);
                return currentCity;
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

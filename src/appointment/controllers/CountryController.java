/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointment.controllers;

import appointment.models.Country;
import appointment.models.CountryInterface;
import appointment.models.UserInterface;
import appointment.services.CountryService;
import appointment.services.CountryServiceInterface;
import java.time.LocalDateTime;

/**
 *
 * @author sagegatzke
 */
public class CountryController implements CountryControllerInterface {

    private CountryServiceInterface countryService = new CountryService();
    private CommunicatorInterface communicator;
    private UserInterface user;

    public CountryController(CommunicatorInterface communicator, UserInterface user) {
        this.communicator = communicator;
        this.user = user;
    }

    @Override
    public CountryInterface addCountry(CountryInterface country) {
        CountryInterface[] countries = this.getCountries();
        while (true) {
            for (int i = 0; i < countries.length; i++) {
                if (country.getCountryId() == countries[i].getCountryId()) {
                    communicator.out((i+1) + "*) " + countries[i].getCountry());
                } else {
                    communicator.out((i+1) + ") " + countries[i].getCountry());
                }
            }
            String result = communicator.askFor("Choose a Country or type a new one");

            if (communicator.isInt(result)) {
                int index = Integer.parseInt(result)-1;
                if (index >= 0 && index < countries.length) {
                    country = countries[index];
                    return country;
                } else {
                    communicator.out("Invalid Option");
                }
            } else {
                CountryInterface newCountry = new Country();
                newCountry.setCountry(result);
                newCountry.setCreatedBy(this.user.getUserName());
                newCountry.setLastUpdatedBy(this.user.getUserName());
                newCountry.setCreatedDate(LocalDateTime.now());
                newCountry.setLastUpdate(LocalDateTime.now());
                country = countryService.addCountry(newCountry);
                return country;
            }
        }
    }

    @Override
    public CountryInterface[] getCountries() {
        return countryService.getCountries();
    }
    
    @Override
    public void showCountry(CountryInterface country){
        communicator.out("Country: " + country.getCountry());
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointment.controllers;

import appointment.models.ConsultantInterface;
import appointment.models.InvalidCredentialsException;
import appointment.models.UserInterface;

/**
 *
 * @author sagegatzke
 */
public interface UserControllerInterface {

    UserInterface login(UserInterface user) throws InvalidCredentialsException;
    UserInterface register(UserInterface user);
    ConsultantInterface[] getConsultants();

}

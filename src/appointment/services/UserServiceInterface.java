/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointment.services;

import appointment.models.UserInterface;

/**
 *
 * @author sagegatzke
 */
public interface UserServiceInterface {

    UserInterface getUser(UserInterface user);
    
    UserInterface createUser(UserInterface user);
    
}

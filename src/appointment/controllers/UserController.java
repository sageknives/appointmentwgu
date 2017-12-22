/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointment.controllers;

import appointment.models.ConsultantInterface;
import javafx.stage.Stage;
import appointment.models.UserInterface;
import appointment.services.UserService;
import appointment.services.UserServiceInterface;

/**
 *
 * @author sagegatzke
 */
public class UserController implements UserControllerInterface {
    private UserServiceInterface userService = new UserService();
    private UserInterface user;
    
    public UserController(UserInterface user){
        this.user = user;
    }
    @Override
    public UserInterface login(UserInterface user){
        if(user.getUserName().length() == 0 || user.getPassword().length() == 0){
            return null;
        }
        return this.userService.getUser(user);
    }
    
    @Override
    public UserInterface register(UserInterface user){
        if(user.getUserName().length() == 0 || user.getPassword().length() == 0){
            return null;
        }
        UserInterface dbUser = this.userService.createUser(user);
        return dbUser;
    }
    
    public ConsultantInterface[] getConsultants(){
        return this.userService.getConsultants();
    }
    
    
}

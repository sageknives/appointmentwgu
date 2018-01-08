/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointment.controllers;

import appointment.models.ConsultantInterface;
import appointment.models.InvalidCredentialsException;
import appointment.models.UserInterface;
import appointment.services.UserService;
import appointment.services.UserServiceInterface;
import java.util.ResourceBundle;

/**
 *
 * @author sagegatzke
 */
public class UserController implements UserControllerInterface {

    private UserServiceInterface userService = new UserService();
    private UserInterface user;
    private ResourceBundle rb;

    public UserController(UserInterface user) {
        this.user = user;
    }

    public UserController(UserInterface user, ResourceBundle rb) {
        this.user = user;
        this.rb = rb;
    }

    @Override
    public UserInterface login(UserInterface user) throws InvalidCredentialsException {
        if (user.getUserName().length() == 0 || user.getPassword().length() == 0) {
            throw new InvalidCredentialsException(this.rb.getString("requiredMessage"));
        }
        return this.userService.getUser(user);
    }

    @Override
    public UserInterface register(UserInterface user) {
        if (user.getUserName().length() == 0 || user.getPassword().length() == 0) {
            return null;
        }
        UserInterface dbUser = this.userService.createUser(user);
        return dbUser;
    }

    @Override
    public ConsultantInterface[] getConsultants() {
        return this.userService.getConsultants();
    }

}

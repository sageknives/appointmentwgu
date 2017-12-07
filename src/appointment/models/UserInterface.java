/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointment.models;

/**
 *
 * @author sagegatzke
 */
public interface UserInterface extends BaseInterface{

    int getActive();

    String getPassword();

    int getUserId();

    String getUserName();

    void setActive(int active);

    void setPassword(String password);

    void setUserId(int userId);

    void setUserName(String userName);
    
}

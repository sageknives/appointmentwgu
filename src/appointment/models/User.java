/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointment.models;

import java.time.LocalDateTime;

/**
 *
 * @author sagegatzke
 */
public class User extends Base implements UserInterface{
    private int userId;
    private String userName;
    private String password;
    private int active;
    
    @Override
    public int getUserId(){ return this.userId; }
    @Override
    public void setUserId(int userId){ this.userId = userId; }
    @Override
    public String getUserName(){ return this.userName; }
    @Override
    public void setUserName(String userName){ this.userName = userName; }
    @Override
    public String getPassword(){ return this.password; }
    @Override
    public void setPassword(String password){ this.password = password; }
    @Override
    public int getActive(){ return this.active; }
    @Override
    public void setActive(int active){ this.active = active; }
    
    
    public User(int userId, String userName, String password,int active, String createdBy, LocalDateTime createdDate, String lastUpdatedBy,LocalDateTime lastUpdated){
        super(createdBy,createdDate,lastUpdatedBy,lastUpdated);
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.active = active;
    }
    
    public User(){
        super();
        this.userId = -1;
        this.userName = "";
        this.password = "";
        this.active = -1;
    }
}

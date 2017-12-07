/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointment.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.UUID;
import appointment.models.User;
import appointment.models.UserInterface;

/**
 *
 * @author sagegatzke
 */
public class UserService extends BaseService implements UserServiceInterface{
    
    @Override
    public UserInterface getUser(UserInterface user){
        return findUser(user);
    }
    
    private UserInterface findUser(UserInterface user) {
        UserInterface dbUser = new User();
        String validUserQuery = "SELECT * FROM user WHERE userName = '" + user.getUserName() + "' AND password = '" + user.getPassword() + "'";
        try {
            Statement findUserStatement = conn.createStatement();
            ResultSet result = findUserStatement.executeQuery(validUserQuery);
            if (result.first()) {
                dbUser.setUserName(result.getString("userName"));
                dbUser.setUserId(result.getInt("userId"));
                dbUser.setActive(result.getInt("active"));
                dbUser.setPassword(result.getString("password"));
                dbUser.setCreatedBy(result.getString("createBy"));
                dbUser.setCreatedDate(result.getTimestamp("createDate").toLocalDateTime());
                dbUser.setLastUpdate(result.getTimestamp("lastUpdate").toLocalDateTime());
                dbUser.setLastUpdatedBy(result.getString("lastUpdatedBy"));
            } else {
                dbUser = null;
            }

            findUserStatement.closeOnCompletion();

        } catch (SQLException ex) {
            dbUser = null;
            ex.printStackTrace();
        }
        return dbUser;
    }
    
    @Override
    public UserInterface createUser(UserInterface user){
        try{
            int lastUserId = 1;
            Statement getLastIdStatement = conn.createStatement();
            String currentIdQuery = "SELECT MAX(userId) AS userId FROM user;";
            ResultSet currentId = getLastIdStatement.executeQuery(currentIdQuery);
            if (currentId.wasNull()){
                lastUserId = 1;
            } else {
                while(currentId.next()){
                    lastUserId = currentId.getInt("userId")+1;
                    user.setUserId(lastUserId);
                }
            }
        
            getLastIdStatement.closeOnCompletion();
            user.setCreatedBy(user.getUserName());
            user.setLastUpdatedBy(user.getUserName());
            user.setCreatedDate(LocalDateTime.now());
            user.setLastUpdate(LocalDateTime.now());
            String values = lastUserId + "," + 
                quoted(user.getUserName()) + "," + 
                quoted(user.getPassword()) + "," +
                user.getActive() + "," + 
                quoted(user.getCreatedBy()) + "," + 
                quoted(user.getCreatedDate()) + "," +
                quoted(user.getLastUpdate()) + "," +
                quoted(user.getLastUpdatedBy());
            String query = "INSERT INTO user (userId, userName, password,active,createBy,createDate,lastUpdate,lastUpdatedBy) VALUES ("+values+");";

            Statement createStatement = conn.createStatement();
            createStatement.execute(query);
            createStatement.closeOnCompletion();
        }catch(SQLException exception){
            exception.printStackTrace();
        }
        return user;
    }
    
    private String quoted(String data){
        return "'" + data + "'";
    }
    private String quoted(LocalDateTime data){
        return "'" + data + "'";
    }
}

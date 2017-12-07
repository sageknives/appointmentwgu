/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointment.services;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author sagegatzke
 */
public abstract class BaseService {
    protected static Connection conn= null;
    
    static {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://52.206.157.109/U03uop", "U03uop", "53688089065");
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
    }
}

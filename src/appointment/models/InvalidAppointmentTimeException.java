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
public class InvalidAppointmentTimeException extends Exception {
    public InvalidAppointmentTimeException(String message){
        super(message);
    }
}

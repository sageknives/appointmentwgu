/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointment.controllers;

import appointment.models.AppointmentInterface;
import appointment.models.UserInterface;

/**
 *
 * @author sagegatzke
 */
public interface AppointmentControllerInterface {

    AppointmentInterface addAppointment();

    AppointmentInterface getAppointment();

    AppointmentInterface[] getAppointments();

    AppointmentInterface updateAppointment();
    
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointment.controllers;

import appointment.models.AppointmentInterface;
import appointment.models.SwitchInterface;
import appointment.models.UserInterface;
import java.util.HashMap;

/**
 *
 * @author sagegatzke
 */
public interface AppointmentControllerInterface {

    AppointmentInterface addAppointment();

    void showWeeklySchedule();
    
    void showMonthlySchedule();

    AppointmentInterface[] getAppointments();

    AppointmentInterface[] getAllAppointments();
    
    AppointmentInterface updateAppointment();
    
    void showAppointment(AppointmentInterface appointment);
    
    HashMap getTypeCountPerMonth();
    
    AppointmentInterface addOrUpdate(boolean isNew, SwitchInterface switchInterface);
}

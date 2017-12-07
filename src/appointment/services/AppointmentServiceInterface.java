/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointment.services;

import appointment.models.AppointmentInterface;

/**
 *
 * @author sagegatzke
 */
public interface AppointmentServiceInterface {

    AppointmentInterface addAppointment(AppointmentInterface appointment);

    AppointmentInterface getAppointment(int appointmentId);

    AppointmentInterface[] getAppointments(String userName);

    AppointmentInterface updateAppointment(AppointmentInterface appointment);
    
}

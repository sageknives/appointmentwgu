/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointment.controllers;

import appointment.models.Appointment;
import appointment.models.AppointmentInterface;
import appointment.models.User;
import appointment.models.UserInterface;
import appointment.services.AppointmentService;
import appointment.services.AppointmentServiceInterface;
import java.util.Scanner;

/**
 *
 * @author sagegatzke
 */
public class AppointmentController implements AppointmentControllerInterface {

    private AppointmentServiceInterface appointmentService = new AppointmentService();
    private CustomerControllerInterface customerController;
    private CommunicatorInterface communicator;
    private UserInterface user;

    public AppointmentController(CommunicatorInterface communicator, UserInterface user, CustomerControllerInterface customerController) {
        this.communicator = communicator;
        this.user = user;
        this.customerController = customerController;
    }

    @Override
    public AppointmentInterface getAppointment() {
        AppointmentInterface[] appointments = this.getAppointments();
        int id = -1;
        while(true){
            //list appointments
            id = 0;
            break;
        }
        return this.appointmentService.getAppointment(id);
    }

    @Override
    public AppointmentInterface[] getAppointments() {
        return this.appointmentService.getAppointments(user.getUserName());
    }

    @Override
    public AppointmentInterface addAppointment() {
        
        return this.appointmentService.addAppointment(new Appointment());
    }

    @Override
    public AppointmentInterface updateAppointment() {
        
        return this.appointmentService.updateAppointment(new Appointment());
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointment.controllers;

import appointment.models.Appointment;
import appointment.models.AppointmentInterface;
import appointment.models.CustomerInterface;
import appointment.models.User;
import appointment.models.UserInterface;
import appointment.services.AppointmentService;
import appointment.services.AppointmentServiceInterface;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
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
        while (true) {
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
        AppointmentInterface[] appointments = this.appointmentService.getAppointments(this.user.getUserName());
        AppointmentInterface appointment = new Appointment();
        this.communicator.out("Creating Appointment");
        this.communicator.out("Choose a customer or add a new one");
        CustomerInterface customer = this.customerController.getCustomer();
        appointment.setCustomer(customer);
        appointment.setTitle(communicator.askFor("Please enter a title"));
        appointment.setDescription(communicator.askFor("Please enter a description"));
        appointment.setLocation(communicator.askFor("Please enter a location"));
        appointment.setContact(communicator.askFor("Please enter a contact"));
        appointment.setUrl(communicator.askFor("Please enter a url"));
        while (true) {
            appointment.setStart(this.askForDateTime("start",null));
            if(!this.isAvailable(appointment.getStart(),appointments)){
                this.communicator.out("Not an available time");
                continue;
            }
            appointment.setEnd(this.askForDateTime("end",appointment.getStart()));
            if(!this.isAvailable(appointment.getEnd(),appointments)){
                this.communicator.out("Not an available time");
                continue;
            }
            break;
        }
        return this.appointmentService.addAppointment(new Appointment());
    }

    private boolean isAvailable(ZonedDateTime zdt, AppointmentInterface[] appointments) {
        for(AppointmentInterface appointment: appointments){
            if(appointment.getStart().isBefore(zdt) && appointment.getEnd().isAfter(zdt)){
                this.communicator.out("is between");
                return false;
            }
        }
        return true;
    }

    private ZonedDateTime askForDateTime(String name, ZonedDateTime startTime) {
        this.communicator.out("Please enter a " + name + " date and time");
        while (true) {
            int yearRestriction = startTime == null? -1:startTime.getYear();
            int year = this.communicator.askForInt("Enter a year", yearRestriction, -1);
            int monthRestiction = 1;
            if(startTime != null && startTime.getYear() == year){
                monthRestiction = startTime.getMonthValue();
            }
            int month = this.communicator.askForInt("Enter a month", monthRestiction, 12);
            YearMonth yearMonth = YearMonth.of(year, month);
            int dateRestriction = 1;
            if(startTime != null && startTime.getYear() == year && startTime.getMonthValue() == month){
                dateRestriction = startTime.getDayOfMonth();
            }
            int date = this.communicator.askForInt("Enter a Date", dateRestriction, yearMonth.lengthOfMonth());
            int hourRestriction = 0;
            if(startTime != null && startTime.getYear() == year && startTime.getMonthValue() == month && startTime.getDayOfMonth() == date){
                hourRestriction = startTime.getHour();
            }
            int hour = this.communicator.askForInt("Enter an hour from 0 to 23", hourRestriction, 23);
            //stopped here figure out hour and minutes.
            if(startTime != null && startTime.getYear() == year && startTime.getMonthValue() == month && startTime.getDayOfMonth() == date){
                hourRestriction = startTime.getHour();
            }
            int minutes = this.communicator.askForInt("Enter minutes", 0, 59);

            ZonedDateTime zdt = ZonedDateTime.of(year, month, date, hour, minutes, 0, 0, ZoneId.systemDefault());
            this.communicator.out(zdt.toString());
            this.communicator.out("Is this correct?");
            if (this.communicator.confirm()) {
                return zdt;
            }
        }
    }

    @Override
    public AppointmentInterface updateAppointment() {

        return this.appointmentService.updateAppointment(new Appointment());
    }
}

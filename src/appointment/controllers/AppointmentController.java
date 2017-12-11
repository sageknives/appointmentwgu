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
        while (true) {
            appointment = updateAppointmentDetails(appointment, appointments);
            showAppointment(appointment);
            communicator.out("Appointment Confirmation:");
            if (communicator.confirm()) {
                appointment.setCreatedBy(this.user.getUserName());
                appointment.setLastUpdatedBy(this.user.getUserName());
                appointment.setCreatedDate(LocalDateTime.now());
                appointment.setLastUpdate(LocalDateTime.now());
                break;
            }
        }
        return this.appointmentService.addAppointment(appointment);
    }

    private boolean isAvailable(ZonedDateTime zdt, AppointmentInterface[] appointments) {
        for (AppointmentInterface appointment : appointments) {
            if (appointment.getStart().isBefore(zdt) && appointment.getEnd().isAfter(zdt)) {
                this.communicator.out("is between");
                return false;
            }
        }
        return true;
    }

    private ZonedDateTime askForDateTime(String name, ZonedDateTime startTime) {
        this.communicator.out("Please enter a " + name + " date and time");
        while (true) {
            int yearRestriction = startTime == null ? -1 : startTime.getYear();
            int year = this.communicator.askForInt("Enter a year", yearRestriction, -1);
            int monthRestiction = 1;
            boolean sameYear = startTime != null && startTime.getYear() == year;
            if (sameYear) {
                monthRestiction = startTime.getMonthValue();
            }
            int month = this.communicator.askForInt("Enter a month", monthRestiction, 12);
            YearMonth yearMonth = YearMonth.of(year, month);
            int dateRestriction = 1;
            boolean sameMonth = sameYear ? startTime.getMonthValue() == month : false;
            if (sameMonth) {
                dateRestriction = startTime.getDayOfMonth();
            }
            int date = this.communicator.askForInt("Enter a Date", dateRestriction, yearMonth.lengthOfMonth());
            int hourRestriction = 0;
            boolean sameDate = sameMonth ? startTime.getDayOfMonth() == date : false;
            if (sameDate) {
                hourRestriction = startTime.getHour();
            }
            int hour = this.communicator.askForInt("Enter hour", hourRestriction, 23);
            int minuteRestriction = 0;
            boolean sameHour = sameDate ? startTime.getHour() == hour : false;
            if (sameHour) {
                minuteRestriction = startTime.getMinute();
            }
            int minutes = this.communicator.askForInt("Enter minutes", minuteRestriction, 59);

            ZonedDateTime zdt = ZonedDateTime.of(year, month, date, hour, minutes, 0, 0, ZoneId.systemDefault());
            this.communicator.out(zdt.toString());
            this.communicator.out("Is this correct?");
            communicator.out(name + " Confirmation:");
            if (this.communicator.confirm()) {
                return zdt;
            }
        }
    }

    @Override
    public AppointmentInterface updateAppointment() {
        AppointmentInterface[] appointments = this.appointmentService.getAppointments(this.user.getUserName());
        //pick appointment to update
        communicator.out("Appointments:");
        AppointmentInterface appointment = null;
        while (true) {

            for (int i = 0; i < appointments.length; i++) {
                System.out.println((i + 1) + ") " + appointments[i].getTitle());
            }
            communicator.out("0) Create new Appointment");
            communicator.out("x) to go back");
            String response = communicator.askFor("Choose an option: ");
            if (response.equals("0")) {
                return this.addAppointment();
            }
            if (response.equals("x")) {
                return null;
            }
            if (communicator.isInt(response)) {
                int selection = Integer.parseInt(response) - 1;
                if (selection >= 0 && selection < appointments.length) {
                    appointment = appointments[selection];
                    break;
                }
            }
            communicator.out("Invalid option");
        }

        this.communicator.out("Update Appointment");
        while (true) {
            appointment = updateAppointmentDetails(appointment, appointments);
            showAppointment(appointment);
            communicator.out("Appointment Confirmation:");
            if (communicator.confirm()) {
                appointment.setLastUpdatedBy(this.user.getUserName());
                appointment.setLastUpdate(LocalDateTime.now());
                break;
            }
        }
        return this.appointmentService.updateAppointment(appointment);
    }

    private AppointmentInterface updateAppointmentDetails(AppointmentInterface appointment, AppointmentInterface[] appointments) {
        this.communicator.out("Choose a customer or add a new one");
        CustomerInterface customer = this.customerController.getCustomer(appointment.getCustomer());
        appointment.setCustomer(customer);
        appointment.setTitle(communicator.askFor("Please enter a title", appointment.getTitle()));
        appointment.setDescription(communicator.askFor("Please enter a description", appointment.getDescription()));
        appointment.setLocation(communicator.askFor("Please enter a location", appointment.getLocation()));
        appointment.setContact(communicator.askFor("Please enter a contact", appointment.getContact()));
        appointment.setUrl(communicator.askFor("Please enter a url", appointment.getUrl()));
        while (true) {
            if (appointment.getStart() != null) {
                showDateTime(appointment.getStart(), "Appointment starts at ");
                communicator.out("Do you want to edit when the appointment starts?");
                if (communicator.confirm()) {
                    appointment.setStart(this.askForDateTime("start", null));
                    if (!this.isAvailable(appointment.getStart(), appointments)) {
                        this.communicator.out("Not an available time");
                        continue;
                    }
                }
            } else {
                appointment.setStart(this.askForDateTime("start", null));
                if (!this.isAvailable(appointment.getStart(), appointments)) {
                    this.communicator.out("Not an available time");
                    continue;
                }
            }
            if (appointment.getEnd() != null) {
                showDateTime(appointment.getEnd(), "Appointment ends at ");
                communicator.out("Do you want to edit when the appointment ends?");
                if (communicator.confirm()) {
                    appointment.setEnd(this.askForDateTime("end", appointment.getStart()));
                    if (!this.isAvailable(appointment.getEnd(), appointments)) {
                        this.communicator.out("Not an available time");
                        continue;
                    }
                }
            } else {
                appointment.setEnd(this.askForDateTime("end", appointment.getStart()));
                if (!this.isAvailable(appointment.getEnd(), appointments)) {
                    this.communicator.out("Not an available time");
                    continue;
                }
            }
            break;
        }
        return appointment;
    }

    private void showDateTime(ZonedDateTime zdt, String title) {
        communicator.out(title + zdt.toString());
    }

    @Override
    public void showAppointment(AppointmentInterface appointment) {
        communicator.out("Title: " + appointment.getTitle());
        communicator.out("Description: " + appointment.getDescription());
        communicator.out("Location: " + appointment.getLocation());
        communicator.out("Contact: " + appointment.getContact());
        communicator.out("Url: " + appointment.getUrl());
        communicator.out("Customer: " + appointment.getCustomer().getCustomerName());
        showDateTime(appointment.getStart(), "Start: ");
        showDateTime(appointment.getEnd(), "End: ");
    }
}

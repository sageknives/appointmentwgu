/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointment.controllers;

import appointment.models.Appointment;
import appointment.models.AppointmentInterface;
import appointment.models.CustomerInterface;
import appointment.models.InvalidAppointmentTimeException;
import appointment.models.SwitchInterface;
import appointment.models.UserInterface;
import appointment.services.AppointmentService;
import appointment.services.AppointmentServiceInterface;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashMap;

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
    public void showWeeklySchedule() {
        AppointmentInterface[] appointments = this.getAppointments();
        LocalDate sld = LocalDateTime.now().toLocalDate();
        while (true) {
            if (sld.getDayOfWeek().getValue() == 1) {
                break;
            }
            sld = sld.plusDays(-1);
        }
        ZonedDateTime end = sld.plusDays(8).atStartOfDay(ZoneId.systemDefault());
        ZonedDateTime start = sld.atStartOfDay(ZoneId.systemDefault());
        MessageInterface display = message -> {
            communicator.lineBreak();
            communicator.out(message);
        };

        appointments = Arrays.stream(appointments).filter(a -> {
            return a.getStart().isAfter(start) && a.getEnd().isBefore(end);
        }).toArray(size -> new Appointment[size]);
        if (appointments.length > 0) {
            communicator.showAlert("This week's schedule is:", display);
            for (int i = 0; i < appointments.length; i++) {
                this.showAppointment(appointments[i]);
            }
        } else {
            communicator.showAlert("Nothing currently scheduled:", display);
        }

    }

    @Override
    public void showMonthlySchedule() {
        AppointmentInterface[] appointments = this.getAppointments();
        LocalDate sld = LocalDateTime.now().toLocalDate();
        while (true) {
            if (sld.getDayOfMonth() == 1) {
                break;
            }
            sld = sld.plusDays(-1);
        }
        LocalDate eld = LocalDate.of(sld.getYear(), sld.getMonth(), 28);
        while (true) {
            if (eld.getMonthValue() != sld.getMonthValue()) {
                break;
            }
            eld = eld.plusDays(1);
        }
        ZonedDateTime end = eld.atStartOfDay(ZoneId.systemDefault());
        ZonedDateTime start = sld.atStartOfDay(ZoneId.systemDefault());
        //get now figure out the beginning of the month and the end
        appointments = Arrays.stream(appointments).filter(a -> {
            return a.getStart().isAfter(start) && a.getEnd().isBefore(end);
        }).toArray(size -> new Appointment[size]);
        MessageInterface display = message -> {
            communicator.lineBreak();
            communicator.out(message);
        };
        if (appointments.length > 0) {
            communicator.showAlert("Your monthly schedule is:", display);
            for (int i = 0; i < appointments.length; i++) {
                this.showAppointment(appointments[i]);
            }
        } else {
            communicator.showAlert("Your monthly schedule is empty:", display);
        }
    }

    @Override
    public AppointmentInterface[] getAppointments() {
        return this.appointmentService.getAppointments(user.getUserName());
    }

    @Override
    public AppointmentInterface[] getAllAppointments() {
        return this.appointmentService.getAppointments(null);
    }

    @Override
    public AppointmentInterface addOrUpdate(boolean isNew, SwitchInterface switchInterface){
        return switchInterface.choose(isNew);
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
            String response = communicator.askFor("Choose an option: ");
            if (response.equals("0")) {
                return this.addAppointment();
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

    private void verifyAvailability(ZonedDateTime zdt, AppointmentInterface[] appointments) throws InvalidAppointmentTimeException {
        if (zdt.getDayOfWeek() == DayOfWeek.SATURDAY
                || zdt.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new InvalidAppointmentTimeException("Weekends are not part of current bussiness hours.");
        }
        if (zdt.getHour() < 9 || zdt.getHour() > 17) {
            throw new InvalidAppointmentTimeException("Current bussiness hours are between 9-5");
        }
        for (AppointmentInterface appointment : appointments) {
            if (appointment.getStart().isBefore(zdt) && appointment.getEnd().isAfter(zdt)) {
                throw new InvalidAppointmentTimeException("Appointment already during that time.");
            }
        }
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

    

    private AppointmentInterface updateAppointmentDetails(AppointmentInterface appointment, AppointmentInterface[] appointments) {
        this.communicator.out("Choose a customer or add a new one");
        CustomerInterface customer = this.customerController.getCustomer(appointment.getCustomer());
        appointment.setCustomer(customer);
        appointment.setTitle(communicator.askFor("Please enter a title", appointment.getTitle()));
        appointment.setDescription(communicator.askFor("Please enter a description", appointment.getDescription()));
        appointment.setLocation(communicator.askFor("Please enter a location", appointment.getLocation()));
        appointment.setContact(communicator.askFor("Please enter an appointment type", appointment.getContact()));
        appointment.setUrl(communicator.askFor("Please enter a url", appointment.getUrl()));
        while (true) {
            try {
                if (appointment.getStart() != null) {
                    showDateTime(appointment.getStart(), "Appointment starts at ");
                    communicator.out("Do you want to edit when the appointment starts?");
                    if (communicator.confirm()) {
                        ZonedDateTime newStart = this.askForDateTime("start", null);
                        this.verifyAvailability(newStart, appointments);
                        appointment.setStart(newStart);

                    }
                } else {
                    ZonedDateTime newStart = this.askForDateTime("start", null);
                    this.verifyAvailability(newStart, appointments);
                    appointment.setStart(newStart);
                }
                if (appointment.getEnd() != null) {
                    showDateTime(appointment.getEnd(), "Appointment ends at ");
                    communicator.out("Do you want to edit when the appointment ends?");
                    if (communicator.confirm()) {
                        ZonedDateTime newEnd = this.askForDateTime("end", appointment.getStart());
                        this.verifyAvailability(newEnd, appointments);
                        appointment.setStart(newEnd);
                    }
                } else {
                    ZonedDateTime newEnd = this.askForDateTime("end", appointment.getStart());
                    this.verifyAvailability(newEnd, appointments);
                    appointment.setStart(newEnd);
                }
                break;
            } catch (InvalidAppointmentTimeException e) {
                communicator.out(e.getMessage());
            }
        }
        return appointment;
    }

    private void showDateTime(ZonedDateTime zdt, String title) {
        communicator.out(title + zdt.toString());
    }

    @Override
    public void showAppointment(AppointmentInterface appointment) {
        communicator.lineBreak();
        communicator.out("Title: " + appointment.getTitle());
        communicator.out("Description: " + appointment.getDescription());
        communicator.out("Location: " + appointment.getLocation());
        communicator.out("Appointment Type: " + appointment.getContact());
        communicator.out("Url: " + appointment.getUrl());
        communicator.out("Customer: " + appointment.getCustomer().getCustomerName());
        showDateTime(appointment.getStart(), "Start: ");
        showDateTime(appointment.getEnd(), "End: ");
    }

    @Override
    public HashMap getTypeCountPerMonth() {
        return this.appointmentService.getTypeCountPerMonth();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointment.models;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 *
 * @author sagegatzke
 */
public class Appointment extends Base implements AppointmentInterface {

    private int appointmentId;
    private CustomerInterface customer;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String url;
    private ZonedDateTime start;
    private ZonedDateTime end;

    @Override
    public int getAppointmentId() {
        return this.appointmentId;
    }

    @Override
    public CustomerInterface getCustomer() {
        return this.customer;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getLocation() {
        return this.location;
    }

    @Override
    public String getContact() {
        return this.contact;
    }

    @Override
    public String getUrl() {
        return this.url;
    }

    @Override
    public ZonedDateTime getStart() {
        return this.start;
    }

    @Override
    public ZonedDateTime getEnd() {
        return this.end;
    }

    @Override
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    @Override
    public void setCustomer(CustomerInterface customer) {
        this.customer = customer;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    @Override
    public void setEnd(ZonedDateTime end) {
        this.end = end;
    }

    public Appointment(int appointmentId, CustomerInterface customer, String title, String description, String location, String contact, String url, ZonedDateTime start, ZonedDateTime end, String createdBy, LocalDateTime createdDate, String lastUpdatedBy,LocalDateTime lastUpdated) {
        super(createdBy,createdDate,lastUpdatedBy,lastUpdated);
        this.appointmentId = appointmentId;
        this.customer = customer;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.url = url;
        this.start = start;
        this.end = end;
    }

    public Appointment() {
        super();
        this.appointmentId = -1;
    }
}

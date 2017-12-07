/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointment.models;

import java.time.ZonedDateTime;

/**
 *
 * @author sagegatzke
 */
public interface AppointmentInterface extends BaseInterface {

    int getAppointmentId();

    String getContact();

    CustomerInterface getCustomer();

    String getDescription();

    ZonedDateTime getEnd();

    String getLocation();

    ZonedDateTime getStart();

    String getTitle();

    String getUrl();

    void setAppointmentId(int appointmentId);

    void setContact(String contact);

    void setCustomer(CustomerInterface customer);

    void setDescription(String description);

    void setEnd(ZonedDateTime end);

    void setLocation(String location);

    void setStart(ZonedDateTime start);

    void setTitle(String title);

    void setUrl(String url);
    
}

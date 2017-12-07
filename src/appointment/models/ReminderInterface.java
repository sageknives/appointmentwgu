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
public interface ReminderInterface extends BaseInterface {

    String getReminderCol();

    ZonedDateTime getReminderDate();

    int getReminderId();

    SnoozeIncrementInterface getSnoozeIncrement();

    void setReminderCol(String reminderCol);

    void setReminderDate(ZonedDateTime reminderDate);

    void setReminderId(int reminderId);

    void setSnoozeIncrement(SnoozeIncrementInterface snoozeIncrement);
    
}

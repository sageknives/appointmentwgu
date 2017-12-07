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
public class Reminder extends Base implements ReminderInterface {

    private int reminderId;
    private ZonedDateTime reminderDate;
    private SnoozeIncrementInterface snoozeIncrement;
    private String reminderCol;

    @Override
    public int getReminderId() {
        return this.reminderId;
    }

    @Override
    public ZonedDateTime getReminderDate() {
        return this.reminderDate;
    }

    @Override
    public SnoozeIncrementInterface getSnoozeIncrement() {
        return this.snoozeIncrement;
    }

    @Override
    public String getReminderCol() {
        return this.reminderCol;
    }

    @Override
    public void setReminderId(int reminderId) {
        this.reminderId = reminderId;
    }

    @Override
    public void setReminderDate(ZonedDateTime reminderDate) {
        this.reminderDate = reminderDate;
    }

    @Override
    public void setSnoozeIncrement(SnoozeIncrementInterface snoozeIncrement) {
        this.snoozeIncrement = snoozeIncrement;
    }

    @Override
    public void setReminderCol(String reminderCol) {
        this.reminderCol = reminderCol;
    }

    public Reminder(int reminderId, ZonedDateTime reminderDate, SnoozeIncrement snoozeIncrement, String reminderCol, String createdBy, LocalDateTime createdDate, String lastUpdatedBy, LocalDateTime lastUpdated) {
        super(createdBy, createdDate, lastUpdatedBy, lastUpdated);
        this.reminderId = reminderId;
        this.reminderDate = reminderDate;
        this.snoozeIncrement = snoozeIncrement;
        this.reminderCol = reminderCol;
    }
}

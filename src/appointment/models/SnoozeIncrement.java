/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointment.models;

import java.time.LocalDateTime;

/**
 *
 * @author sagegatzke
 */
public class SnoozeIncrement extends Base implements SnoozeIncrementInterface {

    private int incrementId;
    private String incrementTypeDesciption;

    @Override
    public int getIncrementId() {
        return this.incrementId;
    }

    @Override
    public String getIncrementTypeDescription() {
        return this.incrementTypeDesciption;
    }

    @Override
    public void setIncrementId(int incrementId) {
        this.incrementId = incrementId;
    }

    @Override
    public void setIncrementTypeDescription(String incrementTypeDescription) {
        this.incrementTypeDesciption = incrementTypeDescription;
    }

    public SnoozeIncrement(int incrementId, String incrementTypeDescription, String createdBy, LocalDateTime createdDate, String lastUpdatedBy, LocalDateTime lastUpdated) {
        super(createdBy, createdDate, lastUpdatedBy, lastUpdated);
        this.incrementId = incrementId;
        this.incrementTypeDesciption = incrementTypeDescription;
    }
}

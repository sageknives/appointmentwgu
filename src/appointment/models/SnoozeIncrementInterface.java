/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointment.models;

/**
 *
 * @author sagegatzke
 */
public interface SnoozeIncrementInterface extends BaseInterface{

    int getIncrementId();

    String getIncrementTypeDescription();

    void setIncrementId(int incrementId);

    void setIncrementTypeDescription(String incrementTypeDescription);
    
}

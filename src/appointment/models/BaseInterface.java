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
public interface BaseInterface {

    /**
     * @return the createdBy
     */
    String getCreatedBy();

    /**
     * @return the createdDate
     */
    LocalDateTime getCreatedDate();

    /**
     * @return the lastUpdate
     */
    LocalDateTime getLastUpdate();

    /**
     * @return the lastUpdatedBy
     */
    String getLastUpdatedBy();

    /**
     * @param createdBy the createdBy to set
     */
    void setCreatedBy(String createdBy);

    /**
     * @param createdDate the createdDate to set
     */
    void setCreatedDate(LocalDateTime createdDate);

    /**
     * @param lastUpdate the lastUpdate to set
     */
    void setLastUpdate(LocalDateTime lastUpdate);

    /**
     * @param lastUpdatedBy the lastUpdatedBy to set
     */
    void setLastUpdatedBy(String lastUpdatedBy);
    
}

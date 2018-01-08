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
public class Consultant implements ConsultantInterface {
    private final String consultantName;
    private int consultantId;
    
    public Consultant(String consultantName, int consultantId){
        this.consultantName = consultantName;
        this.consultantId = consultantId;
    }
    @Override
    public String getConsultantName(){
        return this.consultantName;
    }
    
    @Override
    public int getConsultantId(){
        return this.consultantId;
    }
    
    @Override
    public void setConsultantId(int consultantId){
        this.consultantId = consultantId;
    }
}

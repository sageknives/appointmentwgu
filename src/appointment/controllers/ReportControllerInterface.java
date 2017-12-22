/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointment.controllers;

/**
 *
 * @author sagegatzke
 */
public interface ReportControllerInterface {

    void generateAppointmentCountByMonthReport();

    void generateAppointmentLeaderBoardReport();

    void generateConsultantScheduleReport();
    
}

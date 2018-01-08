/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointment.controllers;

import appointment.models.AppointmentInterface;
import appointment.models.ConsultantInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author sagegatzke
 */
public class ReportController implements ReportControllerInterface {

    private final UserControllerInterface userController;
    private final CommunicatorInterface communicator;
    private final AppointmentControllerInterface appointmentController;

    public ReportController(CommunicatorInterface communicator, AppointmentControllerInterface appointmentController, UserControllerInterface userController) {
        this.communicator = communicator;
        this.appointmentController = appointmentController;
        this.userController = userController;
    }

    @Override
    public void generateAppointmentCountByMonthReport() {

        HashMap typeMap = appointmentController.getTypeCountPerMonth();
        MessageInterface out = message -> communicator.out(message);
        communicator.lineBreak();
        communicator.showAlert("List of monthly appointment types", out);

        typeMap.keySet().forEach((key) -> {
            communicator.out("Month: " + key + " types: " + typeMap.get(key));
        });

    }

    @Override
    public void generateConsultantScheduleReport() {
        AppointmentInterface[] appointments = this.appointmentController.getAllAppointments();
        ConsultantInterface[] consultants = this.userController.getConsultants();

        for (ConsultantInterface consultant : consultants) {
            this.communicator.lineBreak();
            this.communicator.out(consultant.getConsultantName() + " schedule is:");
            for (AppointmentInterface appointment : appointments) {
                if (appointment.getCreatedBy().equals(consultant.getConsultantName())) {
                    this.appointmentController.showAppointment(appointment);
                }
            }
        }
    }

    @Override
    public void generateAppointmentLeaderBoardReport() {
        AppointmentInterface[] appointments = this.appointmentController.getAllAppointments();
        ConsultantInterface[] dbConsultants = this.userController.getConsultants();
        List<ConsultantInterface> consultants = new ArrayList<>();
        for (ConsultantInterface consultant : dbConsultants) {
            consultant.setConsultantId(0);
            consultants.add(consultant);
        }

        for (AppointmentInterface appointment : appointments) {
            String consultantName = appointment.getCreatedBy();
            for (ConsultantInterface c : consultants) {
                if (c.getConsultantName().equals(consultantName)) {
                    int count = c.getConsultantId() + 1;
                    c.setConsultantId(count);
                    break;
                }
            }
        }
        Collections.sort(consultants, (a, b) -> a.getConsultantId() - b.getConsultantId());
        this.communicator.lineBreak();
        consultants.forEach((c) -> {
            this.communicator.out(c.getConsultantName() + ": " + c.getConsultantId());
        });
    }
}

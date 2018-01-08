/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointment.controllers;

import appointment.models.Appointment;
import appointment.models.AppointmentInterface;
import appointment.models.ConsultantInterface;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author sagegatzke
 */
public class ReportController implements ReportControllerInterface {

    private UserControllerInterface userController;
    private CommunicatorInterface communicator;
    private AppointmentControllerInterface appointmentController;

    public ReportController(CommunicatorInterface communicator, AppointmentControllerInterface appointmentController, UserControllerInterface userController) {
        this.communicator = communicator;
        this.appointmentController = appointmentController;
        this.userController = userController;
    }

    @Override
    public void generateAppointmentCountByMonthReport() {
        //maybe distinct groupby sql
//        AppointmentInterface[] appointments = this.appointmentController.getAllAppointments();
//        LocalDate sld = LocalDateTime.now().toLocalDate();
//        while (true) {
//            if (sld.getDayOfMonth() == 1) {
//                break;
//            }
//            sld = sld.plusDays(-1);
//        }
//        LocalDate eld = LocalDate.of(sld.getYear(), sld.getMonth(), 28);
//        while (true) {
//            if (eld.getMonthValue() != sld.getMonthValue()) {
//                break;
//            }
//            eld = eld.plusDays(1);
//        }
//        ZonedDateTime end = eld.atStartOfDay(ZoneId.systemDefault());
//        ZonedDateTime start = sld.atStartOfDay(ZoneId.systemDefault());
//        //get now figure out the beginning of the month and the end
//        appointments = Arrays.stream(appointments).filter(a -> {
//            return a.getStart().isAfter(start) && a.getEnd().isBefore(end);
//        }).toArray(size -> new Appointment[size]);
//        for (AppointmentInterface a : appointments) {
//            if (typeMap.containsKey(a.getDescription())) {
//                Integer t = (Integer) typeMap.get(a.getDescription());
//                typeMap.put(a.getDescription(), ++t);
//            } else {
//                typeMap.put(a.getDescription(), 1);
//            }
//        }
        HashMap typeMap = appointmentController.getTypeCountPerMonth();
        MessageInterface out = message -> communicator.out(message);
        communicator.lineBreak();
        communicator.showAlert("List of monthly appointment types", out);

        for (Object key : typeMap.keySet()) {
            communicator.out("Month: " + key + " types: " + typeMap.get(key));
        }

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
        List<ConsultantInterface> consultants = new ArrayList<ConsultantInterface>();
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
        for (ConsultantInterface c : consultants) {
            this.communicator.out(c.getConsultantName() + ": " + c.getConsultantId());
        }
    }
}

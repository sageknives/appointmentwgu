/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AppointmentConsole;

import appointment.controllers.AppointmentController;
import appointment.controllers.AppointmentControllerInterface;
import appointment.controllers.Communicator;
import appointment.controllers.CommunicatorInterface;
import appointment.controllers.CustomerController;
import appointment.controllers.CustomerControllerInterface;
import appointment.controllers.ReportController;
import appointment.controllers.ReportControllerInterface;
import appointment.controllers.UserController;
import appointment.controllers.UserControllerInterface;
import appointment.models.AppointmentInterface;
import appointment.models.InvalidCredentialsException;
import appointment.models.InvalidInputException;
import appointment.models.MenuState;
import appointment.models.SwitchInterface;
import appointment.models.User;
import appointment.models.UserInterface;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author sagegatzke
 */
public class AppointmentConsole {

    private MenuState menuState = MenuState.START_MENU;
    private UserInterface user;
    private UserControllerInterface userController;
    private CustomerControllerInterface customerController;
    private AppointmentControllerInterface appointmentController;
    private ReportControllerInterface reportController;
    private CommunicatorInterface communicator;
    private ResourceBundle rb;
    private ScheduledExecutorService refreshReminder;
    private final HashMap reminded = new HashMap<Integer, AppointmentInterface>();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        AppointmentConsole console = new AppointmentConsole();
        console.start();
    }

    public void start() {
        this.rb = ResourceBundle.getBundle("LoginText", Locale.getDefault());
        communicator = new Communicator(rb);
        communicator.out(rb.getString("welcomeMessage"));
        user = new User();
        userController = new UserController(user, rb);
        customerController = new CustomerController(communicator, user);
        appointmentController = new AppointmentController(communicator, user, customerController);
        reportController = new ReportController(communicator, appointmentController, userController);
        
        while (true) {
            try {
                displayMenu();
                String response = communicator.askFor(rb.getString("chooseMessage"));
                doMenuAction(response);
            } catch (InvalidInputException e) {
                communicator.out(e.getMessage());
            }
        }
    }

    public void login() {
        user.setUserName(communicator.askFor(rb.getString("usernameRequestMessage"), true));
        user.setPassword(communicator.askFor(rb.getString("passwordRequestMessage"), true));
        try {
            UserInterface dbUser = userController.login(user);
            if (dbUser == null) {
                throw new InvalidCredentialsException(rb.getString("invalidCredentials"));
            }
            communicator.lineBreak();
            communicator.out(rb.getString("successMessage") + " " + dbUser.getUserName());
            menuState = MenuState.MAIN_MENU;
            logActivity(user.getUserName(), true);
            setReminders();
        } catch (InvalidCredentialsException e) {
            communicator.out(e.getMessage());
        }
    }

    public void setReminders() {
        refreshReminder = Executors.newScheduledThreadPool(1);

        Runnable reminderThread = () -> {
            AppointmentInterface[] appointments = this.appointmentController.getAppointments();
            LocalDateTime localNow = LocalDateTime.now(ZoneId.systemDefault());
            ZonedDateTime now = ZonedDateTime.of(localNow.getYear(), localNow.getMonth().getValue(), localNow.getDayOfMonth(), localNow.getHour(), localNow.getMinute(), 0, 0, ZoneId.systemDefault());
            for (AppointmentInterface appointment : appointments) {
                ZonedDateTime start = appointment.getStart();
                if (now.until(start, ChronoUnit.MINUTES) <= 15L
                        && now.until(start, ChronoUnit.MINUTES) > 0L) {
                    if (reminded.containsKey(appointment.getAppointmentId())) {
                        AppointmentInterface foundAppointment = (AppointmentInterface) reminded.get(appointment.getAppointmentId());
                        if (foundAppointment.getStart().isAfter(appointment.getStart())
                                || foundAppointment.getStart().isEqual(appointment.getStart())) {
                            continue;
                        }
                    }
                    reminded.put(appointment.getAppointmentId(), appointment);
                    communicator.out("Appointment: " + appointment.getTitle() + " starts in " + now.until(start, ChronoUnit.MINUTES) + " minutes.");
                }
            }
        };

        ScheduledFuture reminderResult = refreshReminder.scheduleAtFixedRate(reminderThread, 0, 1, TimeUnit.MINUTES);

    }

    public void loginMenuAction(String request) throws InvalidInputException {
        switch (request) {
            case "0": {
                exitProgram();
            }
            case "1": {
                login();
                break;
            }
            default: {
                throw new InvalidInputException(rb.getString("invalidOption"));
            }
        }
    }

    public void mainMenuAction(String request) throws InvalidInputException {
        SwitchInterface choose = (isNew)->{
            if(isNew) return this.appointmentController.addAppointment();
            else return this.appointmentController.updateAppointment();
        };
        switch (request) {
            case "0": {
                logActivity(user.getUserName(), false);
                exitProgram();
                break;
            }
            case "1": {
                this.appointmentController.showWeeklySchedule();
                break;
            }
            case "2": {
                this.appointmentController.showMonthlySchedule();
                break;
            }
            case "3": {
                this.appointmentController.addOrUpdate(true, choose);
                break;
            }
            case "4": {
                this.appointmentController.addOrUpdate(false, choose);
                break;
            }
            case "5": {
                this.customerController.addCustomer();
                break;
            }
            case "6": {
                this.customerController.updateCustomer();
                break;
            }
            case "7": {
                menuState = MenuState.REPORT_MENU;
                break;
            }
            default: {
                throw new InvalidInputException(rb.getString("invalidOption"));
            }
        }
    }

    public void reportMenuAction(String request) throws InvalidInputException {
        switch (request) {
            case "0": {
                menuState = MenuState.MAIN_MENU;
                break;
            }
            case "1": {
                this.reportController.generateAppointmentCountByMonthReport();
                break;
            }
            case "2": {
                this.reportController.generateConsultantScheduleReport();
                break;
            }
            case "3": {
                this.reportController.generateAppointmentLeaderBoardReport();
                break;
            }
            default: {
                throw new InvalidInputException(rb.getString("invalidOption"));
            }
        }
    }

    public void doMenuAction(String request) throws InvalidInputException {
        switch (menuState) {
            case START_MENU: {
                loginMenuAction(request);
                break;
            }
            case MAIN_MENU: {
                mainMenuAction(request);
                break;
            }
            case REPORT_MENU: {
                reportMenuAction(request);
                break;
            }
        }
    }

    public void displayMenu() {
        switch (menuState) {
            case START_MENU: {
                showStartMenu();
                break;
            }
            case MAIN_MENU: {
                showMainMenu();
                break;
            }
            case REPORT_MENU: {
                showReportMenu();
                break;
            }
        }
    }

    public void showStartMenu() {
        communicator.lineBreak();
        communicator.out("0) Exit");
        communicator.out("1) Login");
    }

    public void showMainMenu() {
        communicator.lineBreak();

        communicator.out("Main Menu:");
        communicator.out("0) Exit");
        communicator.out("1) View weekly schedule");
        communicator.out("2) View monthly schedule");
        communicator.out("3) Add an Appointment");
        communicator.out("4) Update an Appointment");
        communicator.out("5) Add a customer");
        communicator.out("6) Update a customer");
        communicator.out("7) Generate a report");
    }

    public void showReportMenu() {
        communicator.lineBreak();
        communicator.out("Report Menu:");
        communicator.out("0) Main Menu");
        communicator.out("1) Generate number of appointments by month");
        communicator.out("2) Generate schedule of all employees");
        communicator.out("3) Generate Consultant appointment leaderboard");
    }

    public void exitProgram() {
        communicator.lineBreak();
        communicator.out("Goodbye");
        System.exit(0);
    }

    public void logActivity(String username, boolean loggingIn) {
        LocalDateTime now = LocalDateTime.now();
        String logText = now + ": " + username + (loggingIn ? " logged in." : " Logged out.");
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("./logactivity.txt", true)))) {
            out.println(logText);
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}

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
import appointment.models.MenuState;
import appointment.models.User;
import appointment.models.UserInterface;

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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        AppointmentConsole console = new AppointmentConsole();
        console.start();
    }

    public void start() {
        communicator = new Communicator();
        communicator.out("Welcome to your appointment app");
        user = new User();
        userController = new UserController(user);
        customerController = new CustomerController(communicator, user);
        appointmentController = new AppointmentController(communicator, user, customerController);
        reportController = new ReportController(communicator,appointmentController,userController);
        
        while (true) {
            displayMenu();
            String response = communicator.askFor("Choose an option: ");
            doMenuAction(response);
        }
    }

    public void login() {
        user.setUserName(communicator.askFor("Please enter your Username"));
        user.setPassword(communicator.askFor("Please enter your Password"));
        UserInterface dbUser = userController.login(user);
        if (dbUser != null) {
            communicator.lineBreak();
            communicator.out("Welcome " + dbUser.getUserName());
            menuState = MenuState.MAIN_MENU;
        } else {
            communicator.out("Invalid Credentials");
        }
    }

    public void loginMenuAction(String request) {
        switch (request) {
            case "0": {
                exitProgram();
                break;
            }
            case "1": {
                login();
                break;
            }
            default: {
                communicator.out("That is not a valid option");
            }
        }
    }

    public void mainMenuAction(String request) {
        switch (request) {
            case "0": {
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
                this.appointmentController.addAppointment();
                break;
            }
            case "4": {
                this.appointmentController.updateAppointment();
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
                communicator.out("That is not a valid option");
            }
        }
    }

    public void reportMenuAction(String request) {
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
                communicator.out("That is not a valid option");
            }
        }
    }

    public void doMenuAction(String request) {
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

//    private CustomerInterface addCustomer() {
//
//        CountryInterface country = new Country();
//        CityInterface city = new City();
//        city.setCountry(country);
//        AddressInterface address = new Address();
//        address.setCity(city);
//        CustomerInterface customer = new Customer();
//        customer.setAddress(address);
//
//        System.out.println("Add Customer:");
//        while (true) {
//            customer.setCustomerName(askFor("Please enter the Customer's full name: ", customer.getCustomerName()));
//            customer.setAddress(addAddress(customer.getAddress()));
//            showCustomer(customer);
//            if (confirm()) {
//                customer = customerController.addCustomer(customer);
//                break;
//            }
//        }
//        return customer;
//    }
//
//    private CustomerInterface updateCustomer() {
//        System.out.println("Update Customer:");
//        CustomerInterface[] customers = customerController.getCustomers();
//        CustomerInterface customer = null;
//        while (true) {
//
//            for (int i = 0; i < customers.length; i++) {
//                System.out.println(i + ") " + customers[i].getCustomerName());
//            }
//            System.out.println("n) Create new Customer");
//            System.out.println("x) to go back");
//            String response = askFor("Choose an option: ");
//            if (response.equals("n")) {
//                return addCustomer();
//            }
//            if (response.equals("x")) {
//                return null;
//            }
//            if (isInt(response)) {
//                int selection = Integer.parseInt(response);
//                if (selection > 0 && selection < customers.length) {
//                    customer = customers[selection];
//                    break;
//                }
//            }
//            System.out.println("Invalid option");
//        }
//
//        while (customer != null) {
//            customer.setCustomerName(askFor("Please enter the Customer's full name: ", customer.getCustomerName()));
//            customer.setAddress(addAddress(customer.getAddress()));
//            showCustomer(customer);
//            if (confirm()) {
//                customer = customerController.updateCustomer(customer);
//                break;
//            }
//        }
//        return customer;
//    }
//
//    private boolean isInt(String value) {
//        try {
//            Integer.parseInt(value);
//            return true;
//        } catch (NumberFormatException e) {
//            return false;
//        }
//    }
//
//    private AppointmentInterface addAppointment() {
//        CustomerInterface customer = new Customer();
//        AppointmentInterface appointment = new Appointment();
//        appointment.setCustomer(customer);
//
//        System.out.println("Add Appointment:");
//        while (true) {
//            appointment.setTitle(askFor("Please enter a Title: ", appointment.getTitle()));
//            appointment.setDescription(askFor("Please enter a description: ", customer.getCustomerName()));
//            appointment.setLocation(askFor("Please enter a location: ", customer.getCustomerName()));
//            appointment.setContact(askFor("Please enter a contact: ", customer.getCustomerName()));
//            appointment.setUrl(askFor("Please enter a url: ", customer.getCustomerName()));
//            appointment.setStart(addDateTime(appointment.getStart()));
//            appointment.setEnd(addDateTime(appointment.getEnd()));
//            appointment.setCustomer(addCustomerToAppointment(appointment.getCustomer()));
//            customer.setCustomerName(askFor("Please enter the Customer's full name: ", customer.getCustomerName()));
//            customer.setAddress(addAddress(customer.getAddress()));
//            showCustomer(customer);
//            if (confirm()) {
//                customer = customerController.addCustomer(customer);
//                break;
//            }
//        }
//        return appointment;
//    }
//
//    private AppointmentInterface updateAppointment() {
//        System.out.println("Update Appointment:");
//        AppointmentInterface appointment = new Appointment();
//        while (true) {
//            // list consultant appointments
//            //choose appointment or go back
//            break;
//        }
//
//        while (true) {
//            //update appointment and confirm
//            break;
//        }
//        return appointment;
//    }
//    private void showWeekSchedule() {
//        System.out.println("Weekly Schedule:");
//        //get weekly schedule
//    }
//
//    private void showMonthSchedule() {
//        System.out.println("Monthly Schedule:");
//        //get monthly schedule
//    }
    private void showMonthlyAppointmentReport() {
        System.out.println("Monthly appointment report:");
        //get monthly appointment report
    }

    private void showConsultScheduleReport() {
        System.out.println("Consultant Schedule Report:");
        //get consultant schedule report
    }

    private void showMagicReport() {
        System.out.println("Magic Report:");
        //get magic report
    }

//    private AddressInterface addAddress(AddressInterface address) {
//        address.setAddress(askFor("Please enter the Customer's Address 1", address.getAddress()));
//        address.setAddress1(askFor("Please enter the Customer's Address 2", address.getAddress1()));
//        //address.setCity(addCity(address.getCity()));
//        address.setPostalCode(askFor("Please enter the Customer's Postal Code", address.getPostalCode()));
//        //address.setCountry(addCountry(address.getCountry()));
//        address.setPhone(askFor("Please enter the Customer's Phone number", address.getPhone()));
//        return address;
//    }
//
//    private CityInterface addCity(CityInterface city, CountryInterface country) {
//        CityInterface[] cities = cityController.getCities(country.getCountryId());
//        while (true) {
//            for (int i = 0; i < cities.length; i++) {
//                System.out.println(i + ") " + cities[i].getCity());
//            }
//            System.out.println(cities.length + ") " + city.getCity());
//            String result = askFor("Choose a city or type a new one");
//            if (isInt(result)) {
//                int index = Integer.parseInt(result);
//                if (index < cities.length) {
//                    return cities[index];
//                } else if (index > cities.length) {
//                    System.out.println("Invalid Option");
//                } else {
//                    return city;
//                }
//            } else {
//                CityInterface newCity = new City();
//                newCity.setCity(result);
//                newCity.setCityId(-1);
//                newCity.setCountry(country);
//                CityInterface savedCity = cityController.addCity(newCity);
//                return savedCity;
//            }
//        }
//
//    }
//
//    private boolean confirm() {
//        while (true) {
//            String response = askFor("y) for yes \nn) for no");
//            if (response.equals("y")) {
//                return true;
//            }
//            if (response.equals("n")) {
//                return false;
//            }
//            System.out.println("Invalid option");
//        }
//    }
//
//    private void showCustomer(CustomerInterface customer) {
//        System.out.println("Customer Name: " + customer.getCustomerName());
//        showAddress(customer.getAddress());
//    }
//
//    private void showAddress(AddressInterface address) {
//        System.out.println("Address: " + address.getAddress());
//        System.out.println("Address1: " + address.getAddress1());
//        //System.out.println("City: " + address.getCity().getCity());
//        System.out.println("Postal Code: " + address.getPostalCode());
//        //System.out.println("Country: " + address.getCity().getCountry().getCountry());
//        System.out.println("Phone: " + address.getPhone());
//    }
//
//    private CustomerInterface addCustomerToAppointment(CustomerInterface customer) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    private ZonedDateTime addDateTime(ZonedDateTime start) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
}

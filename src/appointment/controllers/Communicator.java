/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointment.controllers;

import java.util.Scanner;

/**
 *
 * @author sagegatzke
 */
public class Communicator implements CommunicatorInterface {
        private final Scanner in = new Scanner(System.in);

    @Override
    public String askFor(String request, String suggestion) {
        out(request);
        if (suggestion != null) {
            out(suggestion);
        }
        String response = in.nextLine();
        if (response.equals("")) {
            return suggestion == null ? "" : suggestion;
        } else {
            return response;
        }
    }

    @Override
    public String askFor(String request) {
        return askFor(request, null);
    }
    
    @Override
    public void out(String message){
        System.out.println(message);
    }
    
    @Override
    public boolean confirm() {
        while (true) {
            String response = askFor("y) for yes \nn) for no");
            if (response.equals("y")) {
                return true;
            }
            if (response.equals("n")) {
                return false;
            }
            out("Invalid option");
        }
    }
    
    @Override
    public boolean isInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}

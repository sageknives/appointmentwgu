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
        this.lineBreak();
        out(request);
        if (suggestion != null) {
            out(suggestion);
        }
        this.lineBreak();
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
    public int askForInt(String request, int startRange, int endRange, int suggestion) {
        while (true) {
            this.lineBreak();
            this.out(request);
            
            if (suggestion > -1) {
                out(suggestion + "");
            }
            this.lineBreak();
            String response = in.nextLine();
            if (response.equals("") && suggestion != -1) {
                return suggestion;
            } else if(isInt(response)) {
                int intResponse = Integer.parseInt(response);
                if(intResponse < startRange){
                    out("Integer must be greater than or equal to " + startRange);
                }
                else if(intResponse < startRange || (endRange != -1 && intResponse > endRange)){
                    out("Integer between " + startRange + " and " + endRange + " is Required");
                }else{
                    return intResponse;
                }
            }else{
                out("Integer is required");
            }
        }
    }

    @Override
    public int askForInt(String request, int startRange, int endRange) {
        return askForInt(request, startRange, endRange, -1);
    }

    @Override
    public void out(String message) {
        System.out.println(message);
    }
    
    @Override
    public void lineBreak(){
        this.out("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    @Override
    public boolean confirm() {
        while (true) {
            this.lineBreak();
            String response = askFor("y) for yes \nn) for no");
            this.lineBreak();
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

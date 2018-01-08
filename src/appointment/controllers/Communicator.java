/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointment.controllers;

import appointment.models.InvalidInputException;
import appointment.models.OutOfRangeException;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 *
 * @author sagegatzke
 */
public class Communicator implements CommunicatorInterface {

    private final Scanner in = new Scanner(System.in);
    private final ResourceBundle rb;

    public Communicator(ResourceBundle rb) {
        this.rb = rb;
    }

    @Override
    public String askFor(String request, String suggestion, boolean required) {
        while (true) {
            try {
                this.lineBreak();
                out(request);
                if (suggestion != null) {
                    out(suggestion);
                }
                this.lineBreak();
                String response = in.nextLine();
                if (response.equals("")) {
                    if (required) {
                        throw new InvalidInputException(this.rb.getString("inputRequired"));
                    } else {
                        return suggestion == null ? "" : suggestion;
                    }
                } else {
                    return response;
                }
            } catch (InvalidInputException e) {
                out(e.getMessage());
            }
        }
    }

    @Override
    public String askFor(String request) {
        return askFor(request, null, false);
    }

    @Override
    public String askFor(String request, String suggestion) {
        return askFor(request, suggestion, false);
    }

    @Override
    public String askFor(String request, boolean required) {
        return askFor(request, null, required);
    }

    @Override
    public void showAlert(String message, MessageInterface sender) {
        sender.send(message);
    }

    @Override
    public int askForInt(String request, int startRange, int endRange, int suggestion) {
        while (true) {
            try {

                this.lineBreak();
                this.out(request);

                if (suggestion > -1) {
                    out(suggestion + "");
                }
                this.lineBreak();
                String response = in.nextLine();
                if (response.equals("") && suggestion != -1) {
                    return suggestion;
                } else if (isInt(response)) {
                    int intResponse = Integer.parseInt(response);
                    if (intResponse < startRange) {
                        throw new OutOfRangeException("Integer must be greater than or equal to " + startRange);
                    } else if (intResponse < startRange || (endRange != -1 && intResponse > endRange)) {
                        throw new OutOfRangeException("Integer between " + startRange + " and " + endRange + " is Required");
                    } else {
                        return intResponse;
                    }
                } else {
                    throw new InvalidInputException("Integer is Required");
                }
            } catch (InvalidInputException | OutOfRangeException e) {
                out(e.getMessage());
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
    public void lineBreak() {
        this.out("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    @Override
    public boolean confirm() {
        while (true) {
            try {
                this.lineBreak();
                String response = askFor("y) for yes \nn) for no");
                this.lineBreak();
                switch (response) {
                    case "y":
                        return true;
                    case "n":
                        return false;
                    default:
                        throw new InvalidInputException(this.rb.getString("invalidOption"));
                }
            } catch (InvalidInputException e) {
                out(e.getMessage());
            }
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

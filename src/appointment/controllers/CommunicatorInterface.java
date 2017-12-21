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
public interface CommunicatorInterface {

    String askFor(String request, String suggestion);

    String askFor(String request);

    int askForInt(String request, int startRange, int endRange, int suggestion);

    int askForInt(String request, int startRange, int endRange);

    boolean confirm();

    boolean isInt(String value);

    void out(String message);
    
    void lineBreak();

}

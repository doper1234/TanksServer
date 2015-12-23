/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tanksandroidonlineserver;

/**
 *
 * @author Chris
 */
public class ConsoleLoggerProxy {

    public void append(String message) {
        System.out.println(message);
    }
}

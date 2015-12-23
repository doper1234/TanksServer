/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tanksandroidonlineserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 *
 * @author Chris
 */
public class ClientTest {

    /**
     * @param args the command line arguments
     */
    Thread thread;
    private Socket connectToServer;
    private BufferedReader reader;
    private PrintWriter writer;
    final String iP = "192.168.1.104";
    //final String iP = "80.50.4.114";
    public static void main(String[] args) {
        ClientTest ct = new ClientTest();
        ct.setUpNetWorking();
    }
    
    
    
    public void setUpNetWorking() {

        try {
            connectToServer = new Socket(iP, 3074);
            InputStreamReader streamReader = new InputStreamReader(connectToServer.getInputStream());
            reader = new BufferedReader(streamReader);
            writer = new PrintWriter(connectToServer.getOutputStream());
            System.out.println("Connected to game");

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: disconnected from the server");
        }

    }
    
    public class IncomingReader implements Runnable {

        @Override
        public void run() {
            String message;
            
            try {

                while ((message = reader.readLine()) != null) {

                    System.out.println(message);
                    String[] result = reader.readLine().split(",");
                    

                }
            } catch (IOException | NumberFormatException ex) {
                ex.printStackTrace();
            }
        }
    }
    
}

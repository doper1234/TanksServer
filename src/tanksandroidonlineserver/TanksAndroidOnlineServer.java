/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tanksandroidonlineserver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.*;

/**
 *
 * @author Chris
 */
public final class TanksAndroidOnlineServer {

    //JTextArea logArea;
    ConsoleLoggerProxy logArea;
    private ArrayList connectedPlayerStream;
    private byte p1, p1Action, p2, p2Action, player;

    public TanksAndroidOnlineServer() {

    }

    public static void main(String[] args) {
         
        TanksAndroidOnlineServer tS = new TanksAndroidOnlineServer();
        //tS.setupGUI();
        tS.setupServer();
    }

//    private void setupGUI() {
//        JFrame frame = new JFrame("server");
//
//        logArea = new JTextArea("Welcome to the server!");
//        logArea.setEditable(false);
//        logArea.setWrapStyleWord(true);
//        JScrollPane logScroller = new JScrollPane(logArea);
//        logScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//        logScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//        frame.getContentPane().add(logScroller);
//        frame.setSize(500, 750);
//        frame.setLocationRelativeTo(null);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setVisible(true);
//    }

    public void setupServer() {
        logArea = new ConsoleLoggerProxy();
        connectedPlayerStream = new ArrayList();
        try {
            ServerSocket serverSock = new ServerSocket(3074);
            //String ip = serverSock.;
            logArea.append("\nServer established.");
            logArea.append("\nAs far as I know, ip is ");
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
            logArea.append("\nEstablished at " + in.readLine());
            while (true) {
                Socket clientSocket = serverSock.accept();
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
                connectedPlayerStream.add(writer);
                logArea.append("\ngot a connection from " + clientSocket.getInetAddress());
                logArea.append("\nGame created. Player " + connectedPlayerStream.size() + " entered");
                writer.println(connectedPlayerStream.size());  //assign player number to player
                writer.flush();
                Thread t = new Thread(new ClientHandler((clientSocket)));
                t.start();

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logArea.append("\n Could not create server.");
        }

    }

    public void sendOutPlayerLocations() {

        //Iterator it = connectedPlayerStream.iterator();

        for (int j = 0; j < connectedPlayerStream.size(); j++) {
            try {

                PrintWriter writer = (PrintWriter) connectedPlayerStream.get(j);
                if(j == 0){
                  writer.println(player +"," +p2Action);
                }else if(j == 1){
                  writer.println(player +"," + p1Action);  
                }
                writer.flush();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    public class ClientHandler implements Runnable {

        BufferedReader reader;
        Socket socket;
        //int playerNumber;
        

        public ClientHandler(Socket clientSocket) {

            try {
                socket = clientSocket;
                InputStreamReader isReader = new InputStreamReader(socket.getInputStream());
                reader = new BufferedReader(isReader);

            } catch (Exception ex) {
                logArea.append("Client socket error");
                ex.printStackTrace();

            }
        }

        @Override
        public void run() {
            String message;
            try {
                while ((message = reader.readLine()) != null) {
                    String result[] = message.split(",");
                    logArea.append("\n" + message);
                    Byte playerNumber = 0;
                    Byte playerAction = 0;
                    
                    try{
                        playerNumber = Byte.parseByte(result[0]);
                        playerAction = Byte.parseByte(result[1]);
                    }catch(NumberFormatException e){
                        logArea.append("\n" + message + "is not an int");
                    }
                    player = playerNumber;
                    if( playerNumber == 1){
                        p1Action = playerAction;
                    } else if(playerNumber == 2){
                        p2Action = playerAction;
                    } else{
                        logArea.append("\n" + message + "was sent from no player?");
                    }
                    

                }
            } catch (Exception ex) {
                ex.printStackTrace();
                logArea.append("\nClient disconnected?");
                connectedPlayerStream.clear();
            }
        }

    }

}

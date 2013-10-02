/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vvstdung89
 */
    
public class Server<T> extends Thread{
//    Fields
    private ServerSocket serverSocket;
    private T compute;
    private HashMap<String,Method> method_map;
    private int port;
//    Method
    public Server(int port_, HashMap<String,Method> _method_map, T _compute){
        try {
            port = port_;
            compute = _compute;
            method_map = _method_map;
            
            serverSocket = new ServerSocket(port);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run(){
        System.out.println("Server starts at IP " + serverSocket.getInetAddress().getHostName()
                + " at port " + port);
        
        while (true){
            try {
                Socket connection = serverSocket.accept();
                ClientHandler<T> aClient = new ClientHandler(connection,method_map,compute);
                aClient.start();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
}

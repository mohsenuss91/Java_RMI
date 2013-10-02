/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;

/**
 *
 * @author vvstdung89
 */
public class ClientHandler<T> extends Thread{
    private Socket clientSocket =null;
    private BufferedInputStream inStream = null;
    private BufferedOutputStream outStream = null;
    private final int GreetMessage = 1;
    private T compute;
    private HashMap<String,Method> method_map;
    
    public ClientHandler(Socket socket_, HashMap<String,Method> _method_map, T _compute){
        clientSocket = socket_;
        compute = _compute;
        method_map = _method_map;
    }
    
    public void sendData(String mess){
       try {
           //TODO MArshalling
           outStream.write(mess.getBytes());
           outStream.flush();
       } catch (IOException ex) {
           Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
       }

    }
    
    public void handleMessage(String str){
        sendData("Hello Tony");
        System.out.println(str);
        String[] abc = null;
        if (method_map.containsKey(str)){
            try {
                method_map.get(str).invoke(compute,abc);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void run(){
        
        try {
            String IP  = clientSocket.getInetAddress().getHostAddress();
            System.out.println("Client handler ... " + IP);
            
            inStream = new BufferedInputStream(clientSocket.getInputStream());
            outStream = new BufferedOutputStream(clientSocket.getOutputStream());
            byte[] byteBuffer = new byte[1500];
            int bytesRead = 0;
            
            try {
                while ((bytesRead = inStream.read(byteBuffer))!=-1)
                    handleMessage(new String(byteBuffer,0,bytesRead));
            } catch (IOException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            System.out.println("Close socket");
            clientSocket.close();
            
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                inStream.close();
            } catch (IOException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

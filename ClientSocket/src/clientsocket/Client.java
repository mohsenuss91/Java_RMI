/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clientsocket;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author vvstdung89
 */
public class Client extends Thread{
    private Socket clientSocket;
    private BufferedOutputStream outStream = null;
    private BufferedInputStream inStream = null;
        
    public Client(String url, int port){
        try {
            clientSocket = new Socket(url,port);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sendData(String mess){
        try {
            outStream.write(mess.getBytes());
            outStream.flush();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void run(){
        System.out.println("Client running");
        //Getting Stream
        
        try {
            outStream = new BufferedOutputStream(clientSocket.getOutputStream());
            inStream = new BufferedInputStream(clientSocket.getInputStream());
            //Listen to server 
            ClientListen aClientListen = new ClientListen(inStream);
            aClientListen.start();
            //First Communication
            
            sendData("Hello server. I am new client.");
             
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } 
       
       
        //while(true);
        
       
        
        
    }
}
class ClientListen extends Thread{
    private BufferedInputStream inStream = null;
    public ClientListen(BufferedInputStream inStream_){
        inStream = inStream_;
        //Scanner scan = new Scanner(inStream);
    }
    public void run(){
        byte[] byteBuffer = new byte[1500];
        int bytesRead = 0;
        try {
                while ((bytesRead = inStream.read(byteBuffer))!=-1)
                    System.out.println(new String(byteBuffer,0,bytesRead));
            } catch (IOException ex) {
                Logger.getLogger(ClientListen.class.getName()).log(Level.SEVERE, null, ex);
            }
        
    }
}
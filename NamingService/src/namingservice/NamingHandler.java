/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package namingservice;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vvstdung89
 */
public class NamingHandler extends Thread{
    
    private Socket clientSocket;
    private DataInputStream in_stream;
    private DataOutputStream out_stream;
    private NamingService namingService;
    
    public NamingHandler(NamingService namingService_, Socket clientSocket_){
        clientSocket = clientSocket_;
        namingService = namingService_;
    }
    
    @Override
    public void run(){
        try {
            in_stream = new DataInputStream(clientSocket.getInputStream());
            out_stream = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(NamingHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        byte[] buffer = new byte[1500];
        int byte_cnt = 0;
        try {
            while ((byte_cnt = in_stream.read(buffer)) != -1){
                String request = new String(buffer).trim();
                System.out.println(request +  " " + byte_cnt);
                if (request.startsWith("register")){
                     String address = ((InetSocketAddress)clientSocket.getRemoteSocketAddress()).getAddress().getHostAddress();
                     address = address+":"+request.split("/")[1];
                     //System.out.println(clientSocket.);
                     out_stream.write(namingService.addServer(request.split("/")[2], address).getBytes());
                     out_stream.flush();
                }
                else if (request.startsWith("request")){
                    String address = namingService.getServer(request.split("/")[1]);
                    out_stream.write(address.getBytes());
                    out_stream.flush();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(NamingHandler.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            System.out.println("Connection disconnected");
        }
        
    }
}

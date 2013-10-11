/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package namingservice;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vvstdung89
 */
public class NamingService {
    private HashMap<String,String> registry_list;   
    
    public NamingService() throws IOException{
        ServerSocket serverSocket = new ServerSocket(9999);
        registry_list = new HashMap();
        
        try {
            while (true){
                Socket connection = serverSocket.accept();
                NamingHandler aClient = new NamingHandler(this,connection);
                aClient.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(NamingService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public synchronized String addServer(String class_name, String address){
        System.out.println("put: " + class_name + " with " + address);
        if (registry_list.get(class_name) == null){
            
            registry_list.put(class_name, address);
            return "OK";
        } else {
            registry_list.put(class_name, address);
            return "REWRITE";
        }
    }
    
    public String getServer(String class_name){
        System.out.println(class_name);
        if (registry_list.get(class_name) !=null){
            return registry_list.get(class_name);
        } else {
            return new String("NO SERVICE");
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            NamingService nameService = new NamingService();
        } catch (IOException ex) {
            Logger.getLogger(NamingService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}

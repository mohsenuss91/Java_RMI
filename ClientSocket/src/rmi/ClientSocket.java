/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vvstdung89
 */
public class ClientSocket {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {

            
            NamingService.setNameServiceLocation("143.248.56.147:9999");
            CoreInterface compute = (CoreInterface) NamingService.lookup("FileManager");
            //CoreInterface compute = (CoreInterface) Stub.create("FileManager",address);
            if (compute==null){
                System.out.println("return null");
            } else {
                System.out.println("return not null");
            }
            //compute.retrieve("C:\\test.txt");
            
            String str = new String(compute.retrieve("C:\\test.txt"), "UTF-8");
            System.out.println(str);
        } catch (IOException ex) {
            Logger.getLogger(ClientSocket.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(ClientSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

package rmi;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author vvstdung89
 */
public class NamingService {
    private static InetSocketAddress url;
    
    public static Object lookup(String serviceName) throws UnknownHostException, IOException, InterruptedException{
        
        
        Socket aSocket = new Socket(url.getAddress().getHostAddress(),url.getPort());
        BufferedOutputStream bos = new BufferedOutputStream(aSocket.getOutputStream());
        BufferedInputStream bis = new BufferedInputStream(aSocket.getInputStream());
        
        byte[] buffer = new byte[1500];
        
        int count = 0;
        
        String request_msg = "request/" + serviceName;
        String res_str = "";
        
        while (bis.available() == 0){
            count++;
            if (count == 5)
                return "FAIL";
            bos.write(request_msg.getBytes());
            bos.flush();
            Thread.sleep(1000);
        }

        if (bis.read(buffer)!= -1) {
            String reply = new String(buffer);
            if (reply.equals("NO SERVICE")){
                res_str = "NO SERVICE";
            } else{
                res_str =  reply.trim();
            }
        }
        
        System.out.println("Lookup status: " + res_str);
        
        aSocket.close();
        
        InetSocketAddress socket = new InetSocketAddress(res_str.split(":")[0],Integer.parseInt(res_str.split(":")[1]));
        CoreInterface compute = (CoreInterface) Stub.create("FileManager",socket);
        
        return compute;
        
    }

    public static void setNameServiceLocation(String string) {
        String IpAddress = string.split(":")[0];
        int port = Integer.parseInt(string.split(":")[1]);
        url = new InetSocketAddress(IpAddress,port);
        System.out.println("Set NameService at " + IpAddress + " port: " + port);
    }
}

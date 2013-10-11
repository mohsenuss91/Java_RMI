/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author vvstdung89
 */
public class NamingService {

    private static InetSocketAddress namingServiceAddress;
    
    public static String register(String register_url, RemoteObject aService) throws UnknownHostException, IOException, InterruptedException{
        String serviceName = "";
        String[] split = register_url.split("/");
        int port = 0;
        
        if (split.length==2) {
            serviceName = split[1];
            port = Integer.parseInt(split[0]);
            Skeleton skeleton = new Skeleton(aService,new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(),port));
            skeleton.start();
        } else {
            Skeleton skeleton = new Skeleton(aService);
            aService.setSocket(skeleton.start().getPort());
            serviceName = register_url;
            port = aService.getSocket().getPort();
        }
        
        //REGISTER
        Socket aSocket = new Socket(namingServiceAddress.getAddress().getHostAddress(),namingServiceAddress.getPort());
        BufferedOutputStream bos = new BufferedOutputStream(aSocket.getOutputStream());
        BufferedInputStream bis = new BufferedInputStream(aSocket.getInputStream());
        byte[] buffer = new byte[1500];
        int count = 0;
        String request_msg = "register/" + port + "/" + serviceName;
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
            if (reply.equals(serviceName)){
                res_str = "OK";
            } else{
                res_str =  reply;
            }
        }
        System.out.println("Binding status: " + res_str);
        aSocket.close();
        
        return null;
        
        
        
    }
    
    public static Object lookup(String url){
        
        return null;
        
    }

    public static void setNameServiceLocation(String string) {
        String IpAddress = string.split(":")[0];
        int port = Integer.parseInt(string.split(":")[1]);
        namingServiceAddress = new InetSocketAddress(IpAddress,port);
        System.out.println("Set NameService at " + IpAddress + " port: " + port);
    }
    
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author vvstdung89
 */
public class RemoteObject {
    
    private InetSocketAddress socketAddress;
    
    public void setSocket(int port){
        socketAddress = new InetSocketAddress("localhost",port);
    }
    
    public InetSocketAddress getSocket(){
        return socketAddress;
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vvstdung89
 */
public class RemoteObject {
    
    private InetSocketAddress InetAddr;
    private Socket clientSocket;
    private DataOutputStream outStream = null;
    private DataInputStream inStream = null;
    
    public RemoteObject(InetSocketAddress InetAddr_){
        InetAddr = InetAddr_;
    }
    
    
    public void invokeRemote(ArrayList<Parameter> params_in, Parameter params_out) throws IOException {
       
            clientSocket = new Socket(InetAddr.getAddress(),InetAddr.getPort());
            outStream = new DataOutputStream(clientSocket.getOutputStream());
            inStream = new DataInputStream(clientSocket.getInputStream());
            
            sendData(params_in);
            getResult(params_out);
            clientSocket.close();
       
    }
    
    public void sendData(ArrayList<Parameter> params_in) throws IOException{
        String datatype;
        Object value;
        for (int i=0; i< params_in.size() ; i++){
            datatype = params_in.get(i).getDataType();
            value = params_in.get(i).getValue();
            if (params_in.get(i).getDataType().equals("String")) {
                outStream.writeUTF(value.toString());
            }
            if (params_in.get(i).getDataType().equals("int")) {
                outStream.writeInt((Integer) value);
            }
            if (params_in.get(i).getDataType().equals("long")) {
                outStream.writeLong((Long) value);
            }
           
        }
        
        
    }

    private void getResult(Parameter params_out) throws IOException {
        String datatype;
        Object value;
        datatype = params_out.getDataType();
        
        
        if (params_out.getDataType().equals("String")) {
            params_out.modifyValue(inStream.readUTF());
        }
        if (params_out.getDataType().equals("int")) {
            params_out.modifyValue(inStream.readInt());
        }
        if (params_out.getDataType().equals("long")) {
            params_out.modifyValue(inStream.readLong());
            //System.out.println("return: " + inStream.readLong());
        }
        if (params_out.getDataType().equals("byte[]")) {
            inStream.read((byte[]) params_out.getValue());
        }
        
    }
    
}

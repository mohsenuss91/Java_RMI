/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author vvstdung89
 */
public class Stub {

    public static RemoteObject create(String coreInterface, InetSocketAddress address) {
        RemoteObject remoteObject = null;
        
        
        //dynamic template
        if (coreInterface.equals("FileManager")){
            remoteObject = new CoreInterface(CoreInterface.class.getName(),address);
        }
        
        return remoteObject;
        
    }
    
}

class CoreInterface extends RemoteObject{
    private String className;
    CoreInterface(String className_, InetSocketAddress address) {
        
        super(address);
        className = className_;
    }
    
    public long size(String path) throws IOException{
        
        ArrayList<Parameter> params_in = new ArrayList();
        Parameter params_out = new Parameter("long", 0);
        
        Parameter a1 = new Parameter("String",className);
        Parameter a2 = new Parameter("String","size");
        Parameter a3 = new Parameter("String",path);
        
       
        params_in.add(a1);
        params_in.add(a2);
        params_in.add(a3);
        
        
        
        invokeRemote(params_in,params_out);
        
        long abc = (Long) params_out.getValue();
        System.out.println("Value return is "  +abc);
        return abc;       
    }
    
    public byte[] retrieve(String path) throws IOException{
        ArrayList<Parameter> params_in = new ArrayList();
        
        
        Parameter a1 = new Parameter("String",className);
        Parameter a2 = new Parameter("String","retrieve");
        Parameter a3 = new Parameter("String",path);
        
       
        params_in.add(a1);
        params_in.add(a2);
        params_in.add(a3);
        
        
        Parameter params_out = new Parameter("byte[]", new byte[1500]);
        
        invokeRemote(params_in,params_out);
        
        byte[] abc = (byte[]) params_out.getValue();
        System.out.println("Value return is "  +abc);
        return abc;       
        
    }

    
}
package rmi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Skeleton<T>
{
    private T compute;
    private HashMap<String,Method> method_map;
    private InetSocketAddress inetSock;
    
    public Skeleton(Class<T> c, T compute_)
    {
        //server_ is an implementation of server interface
        compute = compute_;
        
        Method[] method_array = c.getMethods();
        method_map= new HashMap();
        for (Method m : method_array){
            method_map.put(m.getName(), m);
        }
        
        
    }
    
    public Skeleton(Class<T> c, T compute_,InetSocketAddress address){
        
        //server_ is an implementation of server interface
        compute = compute_;
        //address is an InetSocketAddress 
        inetSock=address;
        
        Method[] method_array = c.getMethods();
        method_map= new HashMap();
        for (Method m : method_array){
            method_map.put(m.getName(), m);
        }
    }
    
    public synchronized void start() throws RMIException
    {
        if (inetSock==null){
            inetSock = new InetSocketAddress("127.0.0.0",8800);
        }
        Server<T> aServer = new Server(inetSock.getPort(),method_map,compute);
        aServer.start();
    }
    
    
    
}
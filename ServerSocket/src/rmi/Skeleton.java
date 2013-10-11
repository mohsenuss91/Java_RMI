package rmi;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Skeleton<T>
{
    private RemoteObject compute;
    private HashMap<String,Method> method_map;
    private InetSocketAddress inetSock;
    
    private class RemoteHandler extends Thread{
        private Socket clientSocket =null;
        private DataOutputStream outStream = null;
        private DataInputStream inStream = null;


        private RemoteObject compute;
        private HashMap<String,Method> method_map;

        public RemoteHandler(Socket socket_, HashMap<String,Method> method_map_, RemoteObject compute_){
            clientSocket = socket_;
            compute = compute_;
            method_map = method_map_;

        }

        public void run(){
            try {            
                String IP  = clientSocket.getInetAddress().getHostAddress();
                System.out.println("Client handler ... " + IP);
                outStream = new DataOutputStream(clientSocket.getOutputStream());
                inStream = new DataInputStream(clientSocket.getInputStream());

                String className = inStream.readUTF();
                String function_name = inStream.readUTF();

                ArrayList<Parameter> params_in = new ArrayList();
                Parameter params_out = null;
                //dynamic template
                if (className.equals(compute.getClass().getName()) && function_name.equals("size")){
                    Parameter a3 = new Parameter("String",new String());

                    params_in.add(a3);

                    ArrayList params = getData(params_in);

                    params_out = new Parameter("long",method_map.get("size").invoke(compute,"C:\\test.txt"));
                }
                 if (className.equals(compute.getClass().getName()) && function_name.equals("retrieve")){
                    Parameter a3 = new Parameter("String",new String());

                    params_in.add(a3);

                    ArrayList params = getData(params_in);

                    params_out = new Parameter("byte[]",method_map.get("retrieve").invoke(compute,"C:\\test.txt"));
                }

                System.out.println(params_out.getValue());
                sendResult(params_out);
           
            } catch (IOException ex) {
                Logger.getLogger(Skeleton.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(Skeleton.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(Skeleton.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(Skeleton.class.getName()).log(Level.SEVERE, null, ex);
            }
           
            finally {
                try {               
                    System.out.println("Close socket");
                    inStream.close();
                    clientSocket.close();
                } catch (IOException ex) {
                    Logger.getLogger(Skeleton.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        }

        private ArrayList getData(ArrayList<Parameter> params_in) throws IOException {
            String datatype;
            Object value;
            ArrayList res = new ArrayList();
            for (int i=0; i< params_in.size() ; i++){
                datatype = params_in.get(i).getDataType();
                if (params_in.get(i).getDataType().equals("String")) {
                    params_in.get(i).modifyValue(inStream.readUTF());
                    res.add(params_in.get(i).getValue());
                }
                if (params_in.get(i).getDataType().equals("int")) {
                    params_in.get(i).modifyValue(inStream.readInt());
                    res.add(params_in.get(i).getValue());
                }
                if (params_in.get(i).getDataType().equals("long")) {
                    params_in.get(i).modifyValue(inStream.readLong());
                    res.add(params_in.get(i).getValue());
                }
            }
            return res;
        }

        private void sendResult(Parameter params_out) throws IOException{
            String datatype;
            Object value;

            datatype = params_out.getDataType();
            value = params_out.getValue();

            if (params_out.getDataType().equals("String")) {
                outStream.writeUTF(value.toString());
            }
            if (params_out.getDataType().equals("int")) {
                outStream.writeInt((Integer) value);
            }
            if (params_out.getDataType().equals("long")) {
                outStream.writeLong((Long) value);
            }
             if (params_out.getDataType().equals("byte[]")) {
                outStream.write((byte[]) value);
            }

        }
    }
    
    private class Server extends Thread{
    
        private ServerSocket serverSocket;
        private RemoteObject compute;
        private HashMap<String,Method> method_map;
        private int port;
        
    
        public Server(int port_, HashMap<String,Method> _method_map, RemoteObject _compute) {
            
                port = port_;
                compute = _compute;
                method_map = _method_map;
            try {
                serverSocket = new ServerSocket(8800);
            } catch (IOException ex) {
                Logger.getLogger(Skeleton.class.getName()).log(Level.SEVERE, null, ex);
            }
        }


        @Override
        public void run(){
            while (true){
                try {               
                    System.out.println("Server starts at IP " + InetAddress.getByName("localhost").getHostAddress()
                    + " at port " + port);
                    Socket connection = serverSocket.accept();
                    RemoteHandler aClient = new RemoteHandler(connection,method_map,compute);
                    aClient.start();
                } catch (UnknownHostException ex) {
                    Logger.getLogger(Skeleton.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Skeleton.class.getName()).log(Level.SEVERE, null, ex);
                }
                

            }
        }
    }
        
    public Skeleton(RemoteObject compute_)
    {
        //server_ is an implementation of server interface
        compute = compute_;
        
        Method[] method_array = compute.getClass().getMethods();
        method_map= new HashMap();
        for (Method m : method_array){
            method_map.put(m.getName(), m);
        }
        
        
    }
    
    public Skeleton(RemoteObject compute_,InetSocketAddress address){
        
        //server_ is an implementation of server interface
        compute = compute_;
        //address is an InetSocketAddress 
        inetSock=address;
        
        Method[] method_array = compute.getClass().getMethods();
        method_map= new HashMap();
        for (Method m : method_array){
            method_map.put(m.getName(), m);
        }
    }
    
    public InetSocketAddress start() throws IOException 
    {
        if (inetSock==null){
            inetSock = new InetSocketAddress("127.0.0.0",8800);
        }
        Server aServer = new Server(inetSock.getPort(),method_map,compute);
        aServer.start();
        
        return inetSock;
    }
    
}






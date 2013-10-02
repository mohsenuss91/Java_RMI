/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clientsocket;

/**
 *
 * @author vvstdung89
 */
public class ClientSocket {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Client aClient = new Client("143.248.56.147",8800);
        aClient.start();
    }
}

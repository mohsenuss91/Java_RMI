/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vvstdung89
 */
public class Main {

    
    public static void main(String[] args) {
        try {
            // TODO code application logic here
            CoreImplementation compute = new CoreImplementation();
            Skeleton<CoreInterface> skeleton = new Skeleton(CoreInterface.class, compute);
            skeleton.start();
        } catch (RMIException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

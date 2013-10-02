/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vvstdung89
 */
public class CoreImplementation implements CoreInterface {

    @Override
    public long size(String path) {
       
        File file = new File(path);
        
        return file.length();
        
    }

    @Override
    public byte[] retrieve(String path) {
        byte[] buffer = null;
        BufferedInputStream input = null;
        try {
            File file = new File(path);
            input = new BufferedInputStream(new FileInputStream(file));
            buffer = new byte[(int)size(path)];
            if (input.read(buffer)==-1){
                return null;
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CoreImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CoreImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                input.close();
            } catch (IOException ex) {
                Logger.getLogger(CoreImplementation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return buffer;
    }
    
}

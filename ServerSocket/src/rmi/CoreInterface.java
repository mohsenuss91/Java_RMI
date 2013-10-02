/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi;

/**
 *
 * @author vvstdung89
 */
public interface CoreInterface {
    public long size(String path);
    public byte[] retrieve(String path);
}

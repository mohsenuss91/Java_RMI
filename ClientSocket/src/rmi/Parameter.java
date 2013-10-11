/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi;

/**
 *
 * @author vvstdung89
 */
public class Parameter {
    private String dataType;
    private Object value;
    
    public Parameter(String dataType_, Object value_){
        dataType = dataType_;
        value = value_;
    }
    
    public String getDataType(){
        return dataType;
    }
    
    public Object getValue(){
        return value;
    }
    
    public void modifyValue(Object value_){
        value = value_;
    }
}

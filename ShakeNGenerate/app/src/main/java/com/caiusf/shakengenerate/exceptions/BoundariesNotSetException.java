package com.caiusf.shakengenerate.exceptions;

/**
 * Created by caius.florea on 03-Jan-17.
 */

public class BoundariesNotSetException extends NumberFormatException{

    public BoundariesNotSetException(){
        super();
    }

    public BoundariesNotSetException(String message){
        super(message);
    }
}

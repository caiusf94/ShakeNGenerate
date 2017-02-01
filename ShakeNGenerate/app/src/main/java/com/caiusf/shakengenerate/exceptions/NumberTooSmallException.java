package com.caiusf.shakengenerate.exceptions;

/**
 * Created by caius.florea on 18-Jan-17.
 */

public class NumberTooSmallException extends NumberFormatException {

    public NumberTooSmallException(){
        super();
    }

    public NumberTooSmallException(String message){
        super(message);
    }

}

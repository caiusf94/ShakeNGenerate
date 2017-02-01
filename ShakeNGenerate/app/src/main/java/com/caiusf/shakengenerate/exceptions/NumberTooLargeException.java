package com.caiusf.shakengenerate.exceptions;

/**
 * Created by caius.florea on 03-Jan-17.
 */

public class NumberTooLargeException extends NumberFormatException {

    public NumberTooLargeException(){
        super();
    }

    public NumberTooLargeException(String message){
        super(message);
    }

}

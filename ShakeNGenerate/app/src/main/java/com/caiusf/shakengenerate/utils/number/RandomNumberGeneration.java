package com.caiusf.shakengenerate.utils.number;

import java.util.Random;

/**
 * Created by caius.florea on 17-Jan-17.
 */

public class RandomNumberGeneration {

    public static int generateRandomNumber(int lower, int upper){
        Random random = new Random();

        return random.nextInt(upper - lower + 1) + lower;
    }

    public static boolean numberTooLarge(int value) {
        if (value > 9999999) {
            return true;
        } else
            return false;
    }

    public static boolean numberTooSmall(int value) {
        if (value < -9999999) {
            return true;
        } else
            return false;
    }

    public static boolean lowerIsGreaterThanUpper(int lower, int upper){
        return lower > upper;
    }

}

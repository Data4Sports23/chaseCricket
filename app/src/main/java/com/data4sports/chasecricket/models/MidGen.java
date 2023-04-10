package com.data4sports.chasecricket.models;

import java.math.BigInteger;
import java.security.SecureRandom;

public class MidGen {

    private SecureRandom random = new SecureRandom();

    public String genId() {
        return new BigInteger(130, random).toString(32);
    }
}

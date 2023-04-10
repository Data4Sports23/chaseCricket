package com.data4sports.chasecricket.models;

import java.util.Random;

public class RandomNumber {


    static int playerMin = 1000;
    static int playerMax = 9999;
    static int bowlerMin = 100;
    static int bowlerMax = 500;
    static int batsmanMin = 500;
    static int batsmanMax = 1000;
    static int randomNumber;

    public static int generate(String str){


        if (str.equals("Player")){

            randomNumber = new Random().nextInt((playerMax - playerMin) + 1) + playerMin;
        }

        if (str.equals("Bowler")){

            randomNumber = new Random().nextInt((bowlerMax - bowlerMin) + 1) + bowlerMin;
        }

        if (str.equals("Batsman")){

            randomNumber = new Random().nextInt((batsmanMax - batsmanMin) + 1) + batsmanMin;
        }

        return randomNumber;
    }


}

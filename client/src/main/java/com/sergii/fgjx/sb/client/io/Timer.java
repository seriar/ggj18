package com.sergii.fgjx.sb.client.io;

public class Timer {

    public static double getTimeU(){
        return (double)System.nanoTime() / (double)1e6;
    }
    public static double getTime(){
        return (double)System.nanoTime() / (double)1e9;
    }

}
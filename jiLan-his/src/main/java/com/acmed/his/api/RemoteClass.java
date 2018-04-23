package com.acmed.his.api;

import java.util.Random;
import java.util.UUID;

/**
 * Created by Darren on 2018-04-17
 **/
public class RemoteClass {
    public String f1(String a, int b) {
        System.out.println(a + " " + b);
        return a;
    }

    public static void main(String[] args) {
        RemoteClass rc = new RemoteClass();
        while (true) {
            rc.f1(UUID.randomUUID().toString(), new Random().nextInt());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }
        }
    }
}



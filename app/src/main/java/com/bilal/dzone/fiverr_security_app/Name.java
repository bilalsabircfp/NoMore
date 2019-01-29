package com.bilal.dzone.fiverr_security_app;

/**
 * Created by Belal on 1/27/2017.
 */

public class Name {

    private String name, num; int id;

    public Name(String name, String num, int id) {

        this.name = name;
        this.num = num;
        this.id = id;

    }

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNum() {
        return num;
    }

}

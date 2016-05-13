package com.my.phonebook;

import android.graphics.Bitmap;

public class PersonClass {

    private Bitmap imageid;
    private String name;
    private String number;

    public PersonClass() {
    }

    public PersonClass(Bitmap imageid, String name, String number) {
        this.imageid = imageid;
        this.name = name;
        this.number = number;
    }

    public Bitmap getImageid() {
        return imageid;
    }

    public void setImageid(Bitmap imageid) {
        this.imageid = imageid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}

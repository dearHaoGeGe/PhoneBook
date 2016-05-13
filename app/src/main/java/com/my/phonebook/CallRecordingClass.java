package com.my.phonebook;

public class CallRecordingClass {

    private String name,number, place, time;
    private int imageid;

    public CallRecordingClass(String name,String number, String place, String time, int imageidd) {
        this.name = name;
        this.number = number;
        this.place = place;
        this.time = time;
        this.imageid = imageid;

    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getImageid() {
        return imageid;
    }

    public void setImageid(int imageid) {
        this.imageid = imageid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

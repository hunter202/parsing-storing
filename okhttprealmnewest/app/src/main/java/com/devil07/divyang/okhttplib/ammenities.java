/*
this class is used to provide realm linking see realm documentaion
 */
package com.devil07.divyang.okhttplib;

import io.realm.RealmObject;


public class ammenities extends RealmObject {
    private String ammen;
    private int id;

    public void setAmmen(String ammen) {
        this.ammen = ammen;
    }

    public String getAmmen() {
        return ammen;
    }
    public void setId(int id){
        this.id=id;
    }
    public int getId(){
        return id;
    }
}
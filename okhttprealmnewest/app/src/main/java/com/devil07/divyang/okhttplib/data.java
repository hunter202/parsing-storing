/*
this class is used to store those values obtained from parsing which are needed in displaying them in recyclerview
 */
package com.devil07.divyang.okhttplib;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class data extends RealmObject {
    private String adress1;
    private String adress2;
    private byte[] img;
    public data() {

    }

    public data(String adress1, String adress2, byte[] img) {

        this.adress1 = adress1;
        this.adress2 = adress2;
        this.img = img;

    }

    public String getAdress1() {
        return adress1;
    }


    public void setAdress1(String adress1) {
        this.adress1 = adress1;
    }

    public String getAdress2() {
        return adress2;
    }

    public void setAdress2(String adress2) {
        this.adress2 = adress2;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public byte[] getImg() {
        return img;
    }


}
/*
this class is used to store info of center which have been selected only right now we are just showing ammenities of the center
any other info can be easily obtained
 */
package com.devil07.divyang.okhttplib;

import io.realm.RealmList;
import io.realm.RealmObject;


public class centerdata extends RealmObject {

    private int id;
    private RealmList<ammenities> ammenss;  //multiple ammenities of a center but realm does not support string or int or float arrays so forming realm links
                                            //see realm documentation





    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }


    public void setAmmenss(RealmList<ammenities> ammenss) {
        this.ammenss=ammenss;

    }
    public RealmList<ammenities>getAmmenss() {

        return ammenss;
    }
}

/*
this class parses url/centerid  hence displaying data for a particular center
 */

package com.devil07.divyang.okhttplib;


import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.ExecutionException;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;

public class Center extends AppCompatActivity {
    TextView tv55;
    Boolean isInternetPresent = false;
    Detection cd;
    private JSONObject jsn;
    private JSONObject jsnobj;
    String imgurls;
    String posi;
    int positions;
    Realm realm;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.center_layout);
        super.onCreate(savedInstanceState);

        Bundle basket=  getIntent().getExtras();
        posi= basket.getString("key");
        positions=Integer.parseInt(posi);
        initialiseRealm();
        initialiseVariables();
        checkInternet();


    }

    private void checkInternet() {
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            /*
            if internet is present then clear the older data
             */
            realm = Realm.getDefaultInstance();
            clearAmmenities();  //clear ammennities is a user defined function called to clear data of selected center as it contains ammenities only currently hance named clearammenities

            realm.close();
            new GetCenter().execute();
        }
        else {
            String da=" ";
            realm = Realm.getDefaultInstance();
            RealmResults<centerdata> Mydata = realm.where(centerdata.class).equalTo("id",positions).findAll();
            RealmList<ammenities> amm[]=new RealmList[Mydata.size()];
            for (int i=0;i<amm.length;i++)
            {
                amm[i]=Mydata.get(i).getAmmenss();
                for (int j=0;j<amm[i].size();j++)
                {

                    da+=amm[i].get(j).getAmmen();
                }
            }
            if(Mydata.size()==0)
            {
                tv55.setText("this center info was not stored");
            }
            else
            {

                tv55.setText(da);

            }

        }

    }

    private void clearAmmenities() {
        RealmResults<centerdata> Mydat = realm.where(centerdata.class).equalTo("id",positions).findAll();
        Toast.makeText(Center.this,"ammmm"+Mydat.size(),Toast.LENGTH_SHORT).show();
        realm.beginTransaction();
        Mydat.clear();
        realm.commitTransaction();
    }

    private void initialiseVariables() {
        cd = new Detection(getApplicationContext());
        tv55 = (TextView) findViewById(R.id.tv55);
    }

    private void initialiseRealm() {
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this)
                .name(Realm.DEFAULT_REALM_NAME)  //same name as mainactivity class to make sure same realm structure
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(realmConfig);
    }

    private class GetCenter extends AsyncTask<Void, Integer, String[]> {
       String namesam[] ;
        RealmList<ammenities> mainn =new RealmList<ammenities>();


        @Override
        protected String[] doInBackground(Void... params) {
            realm = Realm.getDefaultInstance();
            RealmResults<centerdata> Mydata = realm.where(centerdata.class).equalTo("id",positions).findAll();
            realm.beginTransaction();
            Mydata.clear();
            realm.commitTransaction();
            JSONParser obj = new JSONParser();
            obj.setMAIN_URL(posi);     //setting the new url for parsing info of only clicked center
      /*
       again parsing for only the particular center
     */

            JSONObject jsonObject = obj.getDataFromWeb();
            jsn = jsonObject;
            try {
                if (jsn != null) {

                        String name = jsn.getString("name");
                        String about = jsn.getString("about");
                        int ID=jsn.getInt("id");
                        JSONArray jb = jsn.getJSONArray("amenities");


                        namesam=new String[jb.length()];  //this variable is used because all ammenties associated with a particular center are stored in this string
                                                          // for displaying purpose later will be produced as per the ui
                        int counter=jb.length();
                        for (int j = 0; j < jb.length(); j++) {
                            JSONObject jsonobj = jb.getJSONObject(j);
                            RealmResults<ammenities> datt=realm.where(ammenities.class).equalTo("ammen",jsonobj.getString("name")).findAll();

                            mainn.add(datt.get(0));  //0 because all are unique values in ammenities class so only one object in datt
                            namesam[j] = jsonobj.getString("name");
                        }

                        JSONArray jc = jsn.getJSONArray("categories");

                        for (int k = 0; k < jc.length(); k++) {
                            JSONObject jsonobj = jc.getJSONObject(k);
                            String id = jsonobj.getString("id");
                            String names = jsonobj.getString("name");

                        }

                        String address_1 = jsn.getString("address_1");

                        String address_2 = jsn.getString("address_2");

                        String city = jsn.getString("city");
                        String lat = jsn.getString("latitude");
                        String lng = jsn.getString("longitude");


                        JSONArray jd = jsn.getJSONArray("photos");
                        for (int l = 0; l < jd.length(); l++) {
                            JSONObject jsonobj = jd.getJSONObject(l);
                            String id = jsonobj.getString("id");
                            String names = jsonobj.getString("image");
                            imgurls = names;
                            String fea = jsonobj.getString("is_featured");
                            // data+="id="+id+"\nname="+names+"\nfeatured="+fea+"\n";
                            //  data+=id+names+fea;
                        }
                        realm.beginTransaction();
                         centerdata y=realm.createObject(centerdata.class);
                        y.setId(ID);
                        y.setAmmenss(mainn);  // here we are linking the ammenities for a particular center with data class
                     /* not required code

                        RealmQuery<data> query[]=new RealmQuery[counter];
                        RealmResults<data> result;
                        for(int tempo=0;tempo<counter;tempo++){
                            query[tempo]=realm.where(data.class).equalTo(Am.getId(),idam[tempo]);
                        }*/
                        realm.commitTransaction();



                       return namesam;


                } else {
                    //give a toast

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            realm.close();
            return null;

        }
        @Override

        protected void onPostExecute(String result[]) {
            super.onPostExecute(result);
            String da=" ";
            if(result!=null) {
                for (int i = 0; i < result.length; i++) {
                    da += result[i];
                }
                tv55.setText(da);  // here parsed center data (ammenities only right now) is displayed a single string
            }
            else
            {
                tv55.setText("data not parsed");
            }

        }
    }
}




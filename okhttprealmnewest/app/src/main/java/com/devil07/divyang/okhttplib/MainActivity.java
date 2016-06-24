/*
 this class is the starting class and is used to parse json data for storing and displaying recycler view
 */

package com.devil07.divyang.okhttplib;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;


import io.realm.Realm;
import io.realm.RealmConfiguration;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    Boolean isInternetPresent = false;
    Detection cd;
    private JSONObject jsn;
    private JSONObject jsnobj;
    String imgurls;
    TextView output;
    private List<data> dataList = new ArrayList<>();   //datalist to form a list for recycler view first this list will be set and then send to dataadaptor for forming recyclerview
    private RecyclerView recyclerView;
    private dataadaptor mAdapter;
    private TextView tv1;
    Realm realm;
    Boolean checkRealm;
  String[] amenities={"Music" , "Lockers" , "Parking" , "Air conditioning" , "Wifi" , "Changing room" , "Cafe"
            , "Shower" , "Drinking water" , "Television" ,"Soft floor"};  //as ammenities and corresponding id are set hence set them initially in a variable
    int [] amenID={1,2,3,4,5,6,7,8,9,10,11};
    ammenities Am[]=new ammenities[11];     //creating objects of ammenities class  11 as currently have only 11 ammenites
                                            //TODO: this is just rough we will be parsing and getting ammenities number and then setting it later


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialiseRealm();
      ammeninit();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        cd = new Detection(getApplicationContext());  //cd is object for netwrok connection (detrection class)
        output = (TextView) findViewById(R.id.tv1);
        mAdapter = new dataadaptor(this, dataList);


        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            // Internet Connection is Present
            realm = Realm.getDefaultInstance();
            RealmResults<data> Mydata = realm.where(data.class).findAll();
            realm.beginTransaction();
            Mydata.clear();
            realm.commitTransaction();
            realm.close();
            new GetDataTask().execute();

            //  mAdapter.notifyDataSetChanged();

        }
        else
        {
            realm = Realm.getDefaultInstance();
            RealmResults<data> Mydata = realm.where(data.class).findAll();
            for (int i =0;i<Mydata.size();i++)
            {
                String add1=Mydata.get(i).getAdress1();
                String add2=Mydata.get(i).getAdress2();
                byte [] img =Mydata.get(i).getImg();
                data x = new data(add1,add2,img);
                dataList.add(x);
            }
            realm.close();

        }

        //in recycler view the datalist is formed and then

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());// get Internet status
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());                                     // check for Internet status
        recyclerView.setAdapter(mAdapter);



    }

    private void ammeninit() {
        realm = Realm.getDefaultInstance();
        /*RealmResults<ammenities> Mydata = realm.where(ammenities.class).findAll();
        realm.beginTransaction();
        Mydata.clear();
        realm.commitTransaction();*/  //TODO: commented this because clearing ammenities which should be permanently set at every visit to this activity would give negative results
                                     // contd--> clearing would be done once the app starts in the starting activity which would be never revisited again
                                    // contd --> right now ammenities table would be created again and again


        /*setting ammenities here*/

        realm.beginTransaction();
        for(int ctr=0;ctr<11;ctr++){
            Am[ctr] = realm.createObject(ammenities.class);
            Am[ctr].setAmmen(amenities[ctr]);
            Am[ctr].setId(amenID[ctr]);
        }


        realm.commitTransaction();
        realm.close();
    }

    public class GetDataTask extends AsyncTask<Void, Integer, String> {
        ProgressDialog dialog;

        /*
        json parsing is a long and time consuming process hence needs to be done on the seperate thread rather than the main thread
         */

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /**
             * Progress Dialog for User Interaction
             */
            /*  dialog = new ProgressDialog(MainActivity.this);
               dialog.setTitle("Hey Wait Please...");
               dialog.setMessage("I am getting your JSON");
               dialog.show();*/  //TODO: this dialogue appears when json is being parsed and set in recycler view but its giving certain errors with realm will work on it
        }

        //  @Nullable
        @Override

        protected String doInBackground(Void... params) {
            realm = Realm.getDefaultInstance();
          /*
          Starting json parsing from here all the http request are done from JSONParser class
          -- okhttp singleton ie: feature of volley to make sure single prefetched queue not of any use here
           */

            JSONParser obj = new JSONParser();
            obj.setMAIN_URL("");         //this function is from JSONParser class because we need to change parsing url to center specific once center has been clicked
                                         //new url being url/centerid


          //only some of the data acquired is used as this is just a skeleton strucure any other data required would be obtained easily

            JSONObject jsonObject = obj.getDataFromWeb();
            jsn = jsonObject;
            try {


                if (jsn != null) {

                    JSONArray ja = jsn.getJSONArray("results");
                    for (int i = 0; i < ja.length(); i++) {


                        JSONObject jsonObj = ja.getJSONObject(i);
                        String name = jsonObj.getString("name");
                        String about = jsonObj.getString("about");


                        JSONArray jb = jsonObj.getJSONArray("amenities");
                        int idam[]=new int[jb.length()];
                        String namesam[]=new String[jb.length()];
                        int counter=jb.length();
                        for (int j = 0; j < jb.length(); j++) {
                            JSONObject jsonobj = jb.getJSONObject(j);
                            idam[j] = jsonobj.getInt("id");
                            namesam[j] = jsonobj.getString("name");

                        }

                        JSONArray jc = jsonObj.getJSONArray("categories");

                        for (int k = 0; k < jc.length(); k++) {
                            JSONObject jsonobj = jc.getJSONObject(k);
                            String id = jsonobj.getString("id");
                            String names = jsonobj.getString("name");

                        }

                        String address_1 = jsonObj.getString("address_1");

                        String address_2 = jsonObj.getString("address_2");

                        String city = jsonObj.getString("city");
                        String lat = jsonObj.getString("latitude");
                        String lng = jsonObj.getString("longitude");


                        JSONArray jd = jsonObj.getJSONArray("photos");

                        for (int l = 0; l < jd.length(); l++) {
                            JSONObject jsonobj = jd.getJSONObject(l);
                            String id = jsonobj.getString("id");
                            String names = jsonobj.getString("image");
                            imgurls = names;
                            String fea = jsonobj.getString("is_featured");
                        }
                    /*
                        WATCH OUT FOR--  some variables in json parsing would be repeated or never used as they are not stored yet will
                        work on that later
                         */

                        Bitmap theBitmap = Glide.              //loading image into bitmap using glide image library
                                with(MainActivity.this).
                                load(imgurls).
                                asBitmap().
                                into(100, 100). // Width and height
                                get();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();   // converting bitmap image into byte array as thats what only the realm stores as an array check the readme
                        theBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();

                        /*
                        setting realm data class which holds information needed to set the recycler view
                         */

                        // here we are adding to realm data base as well as using data class to load into recycler view without storing before as internet is on so it picks info
                        realm.beginTransaction();
                        data x = new data(address_1, address_2, byteArray);
                        data y=realm.createObject(data.class);
                        y.setAdress1(address_1);
                        y.setAdress2(address_2);
                        y.setImg(byteArray);
                        realm.commitTransaction();
                        dataList.add(x);  // adding data class object to datalist of recycler view
                    }
                    //   return data;


                } else {
                    // Toast.makeText(MainActivity.this, "Hellooooooo in Center", Toast.LENGTH_LONG).show();


                }


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            realm.close();

            return "result";

        }

        @Override

        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            mAdapter.notifyDataSetChanged();  //to notify change in datalist hence in mAdaptor

        }
    }
    /*
    here we are initializing realm
     */

    private void initialiseRealm() {
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this)
                .name(Realm.DEFAULT_REALM_NAME)  //name needs to be same in all activities to make sure we are accessing the same realm storage and not a new one
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()  //this is done to manage dynamic realm table size changes
                .build();
        Realm.setDefaultConfiguration(realmConfig);



    }
}
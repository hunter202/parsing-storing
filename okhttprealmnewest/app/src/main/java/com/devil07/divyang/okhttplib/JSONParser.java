package com.devil07.divyang.okhttplib;

import android.support.annotation.NonNull;
import android.util.Log;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;



public class JSONParser {
            /********
              * URLS
              *******/
                        private  String MAIN_URL;
            /**
              * TAGs Defined Here...
              */
                    public    String TAG = "TAG";
            /**
              * Key to Send
              */
                    private    String KEY_USER_ID = "user_id";
            /**
              * Response
              */
                    private    Response response;
            /**
              * Get Table Booking Charge
              *
              * @return JSON Object
              */
                    public  JSONObject getDataFromWeb() {
                    try {
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                            .url(MAIN_URL)
                                            .build();
                            response = client.newCall(request).execute();
                            return new JSONObject(response.body().string());
                        } catch (@NonNull IOException | JSONException e) {
                            Log.e(TAG, "" + e.getLocalizedMessage());
                        }
                    return null;
                }
    public void setMAIN_URL(String  MAIN_URL)
    {
        this.MAIN_URL="http://dev.fittect.com/api/centers/"+MAIN_URL;
    }


        }
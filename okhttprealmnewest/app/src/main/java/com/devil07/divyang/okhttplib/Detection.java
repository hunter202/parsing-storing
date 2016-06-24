/*
this class is used to detect internet connection as if internet connection is there data structure is reinitialized and if not then
informtion is picked from stored realm and presented to the user
 */

package com.devil07.divyang.okhttplib;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
public class Detection {

    private Context _context;

    public Detection(Context context) {
        this._context = context;
    }

    public boolean isConnectingToInternet()
    {
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;

    }
}

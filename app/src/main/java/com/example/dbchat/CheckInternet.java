package com.example.dbchat;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
//this class will verify if we are connected or not
public class CheckInternet {

    public static String getNetworkInfo(Context context) {
        String status = null;
        //getting the connectivity service
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        //after cheking it will return a variable status that will say if it's working or not
        if (networkInfo != null) {

            status = "connected";
            return status;
        } else {
            status = "disconnected";
            return status;
        }

    }
}


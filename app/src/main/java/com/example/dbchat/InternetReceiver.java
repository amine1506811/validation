package com.example.dbchat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
//preparing the intent receiver
public class InternetReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String status = CheckInternet.getNetworkInfo(context);
        if (status.equals("connected")){//after getting the value of status
            // from checkingInternet class showing toast
            Toast.makeText(context, "You are connected now", Toast.LENGTH_SHORT).show();
        }
        else if(status.equals("disconnected")){
            Toast.makeText(context, "You are disconnected", Toast.LENGTH_SHORT).show();
        }
    }
}

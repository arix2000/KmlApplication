package com.kml.supportClasses;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyBroadcastReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.d("ON_RECEIVE", "onReceive: Service Stop");
        context.startService(new Intent(context, TimerService.class));
    }
}

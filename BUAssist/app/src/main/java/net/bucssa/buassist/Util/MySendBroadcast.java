package net.bucssa.buassist.Util;

import android.content.Context;
import android.content.Intent;

/**
 * Created by JackieChen on 2017/3/13.
 */

public class MySendBroadcast {
    public static void sendBroadcastString(Context context, String s){
        Intent intent = new Intent();
        intent.setAction("DeviceList");
        intent.putExtra("DeviceList", s);
        context.sendBroadcast(intent);
    }
    public static void sendBroadcastRefresh(Context context){
        Intent intent = new Intent();
        intent.setAction("DeviceList");
        intent.putExtra("Refresh", "refresh");
        context.sendBroadcast(intent);
    }
}

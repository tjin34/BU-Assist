package net.bucssa.buassist.Util;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by Shin on 17/7/4.
 */

public class JSONUtils {


    public static JSONObject UserInfo2JSON(int uid, String realname, int gender,
                                    int year, int month, int day) {
        JSONObject json = new JSONObject();
        try {
            json.put("uid", uid);
            json.put("realname", realname);
            json.put("gender", gender);
            json.put("birthyear", year);
            json.put("birthmonth", month);
            json.put("birthday", day);
        } catch (Exception ex) {
            Log.e("Error:", ex.toString());
        }

        return json;
    }

}

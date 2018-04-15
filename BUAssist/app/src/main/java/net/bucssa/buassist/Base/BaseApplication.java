package net.bucssa.buassist.Base;

import android.app.Application;

import com.pgyersdk.crash.PgyCrashManager;


/**
 * Created by Shinji on 2018/4/15.
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        /* 蒲公英CrashLog */
        PgyCrashManager.register(this);
    }
}

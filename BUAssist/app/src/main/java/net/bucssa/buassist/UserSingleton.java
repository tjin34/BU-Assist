package net.bucssa.buassist;

import android.app.Activity;
import android.content.Context;

import net.bucssa.buassist.Bean.Login.UserInfo;
import net.bucssa.buassist.Util.Constant;
import net.bucssa.buassist.Util.SPUtils;

/**
 * Created by Shin on 2017-6-22
 */

public class UserSingleton {
    private Boolean isLogin;

    public volatile static UserInfo USERINFO;
    public static int tempEstateID;
    private volatile static UserSingleton instance = new UserSingleton();
    public static String bigAvatar = "";
    public static String midAvatar = "";
    public static String smallAvatar = "";

    public static UserSingleton getInstance() {
        return instance;
    }

    /**
     * 获取登录id
     * @param mContext
     * @return
     */
    public int getUid(Context mContext) {
        return (int) SPUtils.get(mContext, Constant.USERID, 0);
    }

    /**
     * 获取Token
     * @param mContext
     * @return
     */
    public String getUserToken(Context mContext) {
        return (String) SPUtils.get(mContext, Constant.TOKEN,"");
    }

    public boolean isLogin(Context mContext) {
        if (isLogin == null) {
            isLogin = (boolean) SPUtils.get(mContext, Constant.ISLOGIN, false);
        }
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    /**
     * 清空用户数据
     */
    public void clear(Context context){
        USERINFO = new UserInfo();
        SPUtils.clear(((Activity) context).getApplicationContext());
    }

    public static void getHttpAvatar() {
        bigAvatar = USERINFO.getBig();
        midAvatar = USERINFO.getMiddle();
        smallAvatar = USERINFO.getSmall();
    }
}

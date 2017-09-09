package net.bucssa.buassist.Bean.Request;

/**
 * Created by KimuraShin on 17/8/21.
 */

public class UploadAvatarReq {

    /**
     * uid : 0
     * imgBase64 :
     * token :
     */

    private int uid;
    private String imgBase64;
    private String token;

    public UploadAvatarReq(int uid, String imgBase64, String token) {
        setUid(uid);
        setImgBase64(imgBase64);
        setToken(token);
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getImgBase64() {
        return imgBase64;
    }

    public void setImgBase64(String imgBase64) {
        this.imgBase64 = imgBase64;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

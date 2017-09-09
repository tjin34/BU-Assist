package net.bucssa.buassist.Bean.Request;

/**
 * Created by KimuraShin on 17/8/21.
 */

public class EditInfoInt {

    /**
     * uid : 0
     * key :
     * value : 0
     * token :
     */

    private int uid;
    private String key;
    private int value;
    private String token;

    public EditInfoInt(int uid, String key, int value, String token) {
        setToken(token);
        setUid(uid);
        setKey(key);
        setValue(value);
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

package net.bucssa.buassist.Bean.Request;

/**
 * Created by KimuraShin on 17/8/21.
 */

public class EditInfoStr {

    /**
     * uid : 0
     * key :
     * value :
     * token :
     */

    private int uid;
    private String key;
    private String value;
    private String token;

    public EditInfoStr(int uid, String key, String value, String token) {
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

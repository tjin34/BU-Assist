package net.bucssa.buassist.Bean.Request;

/**
 * Created by KimuraShin on 17/8/21.
 */

public class DelChatReq {

    /**
     * uid : 0
     * plids : 3,5,20,40
     * types : 1,2,2,1
     * token :
     */

    private int uid;
    private String plids;
    private String types;
    private String token;


    public DelChatReq(int uid, String plids, String types, String token) {
        setUid(uid);
        setToken(token);
        setPlids(plids);
        setTypes(types);
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getPlids() {
        return plids;
    }

    public void setPlids(String plids) {
        this.plids = plids;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

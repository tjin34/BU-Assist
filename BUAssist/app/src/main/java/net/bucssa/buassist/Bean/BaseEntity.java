package net.bucssa.buassist.Bean;

import java.io.Serializable;

/**
 * Created by Administrator on 16-12-2.
 */
public class BaseEntity<T> implements Serializable {


    /**
     * code : 200
     * error :
     * success : true
     * datas : {}
     * total : 0
     * newNum : 0
     */



    private int code;
    private String error;
    private boolean success;
    private T datas;
    private int total;
    private int newNum;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getDatas() {
        return datas;
    }

    public void setDatas(T datas) {
        this.datas = datas;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getNewNum() {
        return newNum;
    }

    public void setNewNum(int newNum) {
        this.newNum = newNum;
    }
}

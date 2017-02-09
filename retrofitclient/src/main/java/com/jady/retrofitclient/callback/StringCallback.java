package com.jady.retrofitclient.callback;

/**
 * Created by jady on 2016/12/19.
 */
public class StringCallback {
    private String code;
    private String access_token;
    private String uptoken;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getUptoken() {
        return uptoken;
    }

    public void setUptoken(String uptoken) {
        this.uptoken = uptoken;
    }

    @Override
    public String toString() {
        return "StringCallback{" +
                "code='" + code + '\'' +
                ", access_token='" + access_token + '\'' +
                ", uptoken='" + uptoken + '\'' +
                '}';
    }
}

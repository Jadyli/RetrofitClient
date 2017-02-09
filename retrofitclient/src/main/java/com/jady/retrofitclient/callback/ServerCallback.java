package com.jady.retrofitclient.callback;

/**
 * Created by jady on 2016/12/7.
 */

public class ServerCallback<T> {
    private long server_time;
    private boolean success = false;
    private String err_code;
    private String message;
    private String name;
    private T data;

    public long getServer_time() {
        return server_time;
    }

    public void setServer_time(long server_time) {
        this.server_time = server_time;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErr_code() {
        return err_code;
    }

    public void setErr_code(String err_code) {
        this.err_code = err_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ServerCallback{" +
                "server_time=" + server_time +
                ", success=" + success +
                ", err_code='" + err_code + '\'' +
                ", message='" + message + '\'' +
                ", name='" + name + '\'' +
                ", data=" + data +
                '}';
    }
}

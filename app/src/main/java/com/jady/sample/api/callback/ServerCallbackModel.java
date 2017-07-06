package com.jady.sample.bean;

/**
 * Created by lipingfa on 2017/6/12.
 */
public class ServerCallbackModel<T> {
    private long server_time;
    private T data;
    private boolean success;
    private String err_code;
    private String message;

    public long getServer_time() {
        return server_time;
    }

    public void setServer_time(long server_time) {
        this.server_time = server_time;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
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

    @Override
    public String toString() {
        return "ServerCallbackModel{" +
                "server_time=" + server_time +
                ", data=" + data +
                ", success=" + success +
                ", err_code='" + err_code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}

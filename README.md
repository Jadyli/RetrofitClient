# RetrofitClient

RetrofitClient基于OKHttp和Retrofit封装了基本的GET、POST、PUT、DELET请求和文件上传下载方法，只需三步，快速集成。

<p><img src="https://github.com/Jadyli/RetrofitClient/blob/master/image/1.gif?raw=true" width="350" height="600"/>
<img src="https://github.com/Jadyli/RetrofitClient/blob/master/image/2.gif?raw=true" width="350" height="600"/>
<img src="https://github.com/Jadyli/RetrofitClient/blob/master/image/3.gif?raw=true" width="350" height="600"/>
<img src="https://github.com/Jadyli/RetrofitClient/blob/master/image/4.gif?raw=true" width="350" height="600"/></p>

## 1 在app或lib级别的build.gradle文件中添加依赖

```
compile 'com.jady:retrofitclient:0.2.6'
```

## 2 在MainApplication中初始化

```java
HttpManager.init(this, UrlConfig.BASE_URL);
HttpManager.setOnGetHeadersListener(new HttpManager.OnGetHeadersListener() {
      @Override
      public Map<String, String> getHeaders() {
          Map<String, String> headers = new HashMap<>();
          headers.put("access_token", "1234");
          return headers;
      }
});
```
init(Context context, String baseUrl)方法用来传项目的基础url，当然具体的请求也可以动态修改。
另一个是所有请求都需要添加的的请求头，比如`accessToken`，`deviceKey`这种。

## 3 自动义请求回调

参考：
1.[ServerCallback](https://github.com/Jadyli/RetrofitClient/blob/beta_v0.2/app/src/main/java/com/jady/sample/api/callback/ServerCallback.java)
2.[CommonCallback](https://github.com/Jadyli/RetrofitClient/blob/beta_v0.2/app/src/main/java/com/jady/sample/api/callback/CommonCallback.java)

具体封装过程参考我的另一篇文章[android使用gson和泛型解析服务器回调的封装](http://blog.csdn.net/u013005791/article/details/72956132)。


## 使用

参考:
1.[实例中的API类](https://github.com/Jadyli/RetrofitClient/blob/beta_v0.2/app/src/main/java/com/jady/sample/api/API.java)
2.[实例中的BaseRequestFragment类](https://github.com/Jadyli/RetrofitClient/blob/beta_v0.2/app/src/main/java/com/jady/sample/fragment/BaseRequestFragment.java)

>GET请求

```java
API.testGet(new CommonCallback<Feed>() {

     @Override
     public void onSuccess(Feed feed) {
           tvBaseRequestGet.setText(feed.getContent);
     }

     @Override
     public void onFailure(String error_code, String error_message) {

     }
});
```
不使用`baseUrl`:

```java
HttpManager.getFullPath(String fullUrl, Map<String, Object> parameters, HttpCallback callback)
```

>POST请求

```java
API.testPost(name, password, new ServerCallback<ServerCallbackModel<String>, String>() {

    @Override
    public void onSuccess(String accessToken) {
        showToast("accessToken:" + accessToken);
    }

    @Override
    public void onFailure(String error_code, String error_message) {
    }

    /**
    * 对单个请求允许Toast显示错误信息
    */
    @Override
    public boolean enableShowToast() {
        return true;
    }
});
```
不使用`baseUrl`:

```java
HttpManager.postFullPath(String fullUrl, Map<String, Object> parameters, HttpCallback callback)
```
post对象，以json传输

```java
HttpManager.postByBody(String url, T body, HttpCallback callback)
```

>PUT请求

```java
API.testPut(putContent, new ServerCallback<ServerCallbackModel<Object>, Object>() {

    @Override
    public void onSuccess(Object o) {
        showToast("更新成功");
    }

    @Override
    public void onFailure(String error_code, String error_message) {
        showToast("更新失败,请检查token");
    }
});
```

>DELETE请求

```java
API.testDelete(1,new ServerCallback<ServerCallbackModel<Object>, Object>() {
    @Override
    public void onSuccess(Object o) {
        showToast("删除成功");
    }

    @Override
    public void onFailure(String error_code, String error_message) {
        showToast("删除失败,请检查token");
    }
});
```

>文件上传
单文件上传：

```java
HttpManager.uploadFile(String url, String filePath, String fileDes, TransformProgressListener iProgress)
```
多文件上传：

```java
HttpManager.uploadFiles(String url, List<String> filePathList, TransformProgressListener iProgress)
```

如果需要临时改变`baseUrl`，只需要在发送请求之前调用

```java
HttpManager.setTmpBaseUrl("http://192.168.0.127:8080/retrofitclientserver/");
HttpManager.get(UrlConfig.USER_INFO, null, callback);
```

如果需要临时添加请求头，需要在发送请求前调用：

```java
HttpManager.addTmpHeaders(headers);
HttpManager.get(UrlConfig.USER_INFO, null, callback);
```

具体使用方法可以参考库中的demo。


服务器端代码参考：[RetrofitClientServer](https://github.com/Jadyli/RetrofitClientServer)
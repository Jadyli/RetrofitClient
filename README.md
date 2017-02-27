# RetrofitClient 0.1.5
upgrade description：
**1.DownloadFileListener**
> 1.make method named of onStart() not abstract
> 2.make method name of onError() abstract

## Usage：
### Step1
add the dependency on build.gradle.

```
 compile 'com.jady:retrofitclient:0.1.4'
```
### Step2
you should init RetrofitClient before you use the library.Usually you can call the method named of `HttpManager.init(Context context, String baseUrl)`as while as the application start.if you need to set the cache directory, you can call the method named of `HttpManager.initCache(String cacheDirPath, long maxSize)`.
**for example:**

```
HttpManager.init(this, baseUrl);
HttpManager.initCache(getCacheDir() + "/http", 10 * 1024 * 1024);
```
### Step3
Call the relevant method at the positon where you need to do operation such as `get` or `post`。for example:

```
String phone = etPhone.getText().toString().trim();
Map<String, Object> parameters = new HashMap<>();
parameters.put("mobilephone", phone);
//这个ServerCallback是针对我公司的服务的，不适应其他场景，
// 所以不需要加，一般直接传回字节流，用Gson转为你自己的就行。
//HttpManager.getInstance().post(suffix, parameters, new HttpCallback<ServerCallback<Object>>() {

HttpManager.getInstance().post(suffix, parameters, new HttpCallback() {

                @Override
                public void onSuccess(Object o) {

                }

                @Override
                public void onFailure(String err_code, String message) {

                }
            });
``` 



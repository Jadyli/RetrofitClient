package com.jady.retrofitclient.request;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * 通用的Retrofit请求。
 * 为什么用Map？通用性，不需要写很多请求方法.
 *
 * @see com.jady.retrofitclient.HttpManager 这里封装了通用的方法，请求的参数都在这里封装。
 * <p>
 * 为什么不用{@link retrofit2.Call}? Call已经使用过泛型了，如果这里使用Call必须填写具体的对象，所以使用okhttp3的Observable，
 * 在外面封装一层
 * Created by jady on 2016/12/7.
 */
public interface CommonRequest {

    @GET("{path}")
    Observable<ResponseBody> doGet(@Path(value = "path", encoded = true) String url, @QueryMap Map<String, Object> map);

    @GET("{path}")
    Observable<ResponseBody> doGet(@Path(value = "path", encoded = true) String url);

    /**
     * 参数含有@Field和@FieldMap的请求必须加@FormUrlEncoded
     * Post请求最好用@Field,@Query也行，只是参数会暴露在Url中
     *
     * @param url
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("{path}")
    Observable<ResponseBody> doPost(@Path(value = "path", encoded = true) String url, @FieldMap Map<String, Object> map);

    @POST("{path}")
    Observable<ResponseBody> doPost(@Path(value = "path", encoded = true) String url, @Body RequestBody body);

    @PUT("{path}")
    Observable<ResponseBody> doPut(@Path(value = "path", encoded = true) String url, @Body Map<String, Object> map);

    @HTTP(method = "DELETE",path = "{path}",hasBody = true)
    Observable<ResponseBody> doDelete(@Path(value = "path", encoded = true) String url, @Body Map<String, Object> map);

    @POST("{path}")
    Observable<ResponseBody> doPostNotEncoded(@Path(value = "path", encoded = true) String url, @Body Map<String, Object> map);

    /**
     * 完整路径
     *
     * @param url
     * @return
     */
    @GET
    Observable<ResponseBody> doGetFullPath(@Url String url);

    /**
     * 完整路径
     *
     * @param url
     * @param map
     * @return
     */
    @GET
    Observable<ResponseBody> doGetFullPath(@Url String url, @QueryMap Map<String, Object> map);

    /**
     * 参数含有@Field和@FieldMap的请求必须加@FormUrlEncoded
     * Post请求最好用@Field,@Query也行，只是参数会暴露在Url中
     *
     * @param url 完整路径
     * @return
     */
    @FormUrlEncoded
    @POST
    Observable<ResponseBody> doPostFullPath(@Url String url);

    /**
     * 参数含有@Field和@FieldMap的请求必须加@FormUrlEncoded
     * Post请求最好用@Field,@Query也行，只是参数会暴露在Url中
     *
     * @param url 完整路径
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST
    Observable<ResponseBody> doPostFullPath(@Url String url, @FieldMap Map<String, Object> map);

    @Multipart
    @POST("{path}")
    Observable<ResponseBody> uploadFile(
            @Path(value = "path", encoded = true) String url,
            @PartMap() Map<String, RequestBody> maps);

    //支持大文件
    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String fileUrl);
}

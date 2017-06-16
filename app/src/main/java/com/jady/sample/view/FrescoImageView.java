package com.jady.sample.view;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.jady.sample.api.UrlConfig;

import java.io.File;

/**
 * @Description: Created by lipingfa on 2016/9/6.
 */
public class FrescoImageView extends SimpleDraweeView {

    private static final String TAG = "FrescoImageView";

    public FrescoImageView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    public FrescoImageView(Context context) {
        super(context);
    }

    public FrescoImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FrescoImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public FrescoImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 设置圆角弧度
     *
     * @param radius 弧度,单位:px
     */
    public void setRoundRadius(int radius) {
        RoundingParams roundingParams = new RoundingParams();
        roundingParams.setCornersRadius(radius);
        getHierarchy().setRoundingParams(roundingParams);
    }

    public void setUrl(String url) {
        setUrl(url, 0, 0);
    }

    /**
     * 解析图片,支持网络
     *
     * @param url
     */
    public void setUrl(String url, final int width, final int height) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        String localImagePath = getLocalImagePath(url);
        if (!TextUtils.isEmpty(localImagePath)) {
            url = "file://" + localImagePath;
        }


        if (url.startsWith("/")) {
            url = UrlConfig.IMG_PREFIX + url;
            if (width > 0 && height > 0) {
                url += "_" + width + "x" + height;
            }
        }

        ImageRequestBuilder builder = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url));
        if (width != 0 && height != 0) {
            builder.setResizeOptions(new ResizeOptions(width, height));
        }
        ImageRequest request = builder.build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(getController())
                .build();
        setController(controller);
//        setOnClickListener(listener);
    }

    /**
     * 获取屏幕的宽度px
     *
     * @param context 上下文
     * @return 屏幕宽px
     */
    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();// 创建了一张白纸
        windowManager.getDefaultDisplay().getMetrics(outMetrics);// 给白纸设置宽高
        return outMetrics.widthPixels;
    }

    /**
     * 解析本地资源文件,如drawbale中的文件
     *
     * @param resId
     */
    public void setLocalRes(int resId) {
        setLocalRes(resId, 0, 0);
    }

    /**
     * 解析本地资源文件,如drawbale中的文件
     *
     * @param resId
     */
    public void setLocalRes(int resId, int width, int height) {
        if (resId == 0) {
            return;
        }
        String imageUrl = "res:///" + resId;
        ImageRequestBuilder builder = ImageRequestBuilder.newBuilderWithSource(Uri.parse(imageUrl));
        if (width != 0 && height != 0) {
            builder.setResizeOptions(new ResizeOptions(width, height));
        }
        ImageRequest request = builder.build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(getController())
                .build();
        setController(controller);
    }

    /**
     * 解析本地assets文件夹文件
     *
     * @param fileName
     */
    public void setLocalAsset(String fileName) {
        setLocalAsset(fileName, 0, 0);
    }

    /**
     * 解析本地assets文件夹文件
     *
     * @param fileName
     */
    public void setLocalAsset(String fileName, int width, int height) {
        if (TextUtils.isEmpty(fileName) || fileName.equals("null")) {
            return;
        }
        //注意:不能用assets
        String imageUrl = "asset:///" + fileName;
        ImageRequestBuilder builder = ImageRequestBuilder.newBuilderWithSource(Uri.parse(imageUrl));
        if (width != 0 && height != 0) {
            builder.setResizeOptions(new ResizeOptions(width, height));
        }
        ImageRequest request = builder.build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(getController())
                .build();
        setController(controller);
    }

    /**
     * 解析本地存储的文件
     *
     * @param path
     */
    public void setLocalFile(String path) {
        if (TextUtils.isEmpty(path) || path.equals("null")) {
            return;
        }
        String localImagePath = getLocalImagePath(path);
        if (TextUtils.isEmpty(localImagePath)) {
            return;
        }
        String imageUrl = "file://" + localImagePath;
        setImageURI(Uri.parse(imageUrl));
    }

    private String getLocalImagePath(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        if (new File(path).exists()) {
            return path;
        }
        return null;
    }

}

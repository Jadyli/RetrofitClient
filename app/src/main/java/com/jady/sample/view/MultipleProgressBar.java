package com.jady.sample.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.jady.sample.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static android.R.attr.path;

/**
 * Created by lipingfa on 2017/6/27.
 */
public class MultipleProgressBar extends View {

    private float strokeWidth;
    @ProgressBarType
    private int type;
    private int progressColor;
    private int bgColor;
    private int strokeColor;
    private float radius;
    private Paint mProgressPaint, mBgPaint, mStrokePaint;
    private int mProgress = 0;
    private int mMaxProgress = 100;

    @IntDef({ProgressBarType.ROUND, ProgressBarType.CIRCLE, ProgressBarType.RECT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ProgressBarType {
        int ROUND = 1;
        int CIRCLE = 2;
        int RECT = 3;
    }

    public MultipleProgressBar(Context context) {
        this(context, null);
    }

    public MultipleProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultipleProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MultipleProgressBar);
        type = array.getColor(R.styleable.MultipleProgressBar_type, ProgressBarType.RECT);
        progressColor = array.getColor(R.styleable.MultipleProgressBar_progress_color, Color.GREEN);
        bgColor = array.getColor(R.styleable.MultipleProgressBar_bg_color, Color.RED);
        strokeColor = array.getColor(R.styleable.MultipleProgressBar_stroke_color, Color.RED);
        radius = array.getDimension(R.styleable.MultipleProgressBar_radius, 10);
        strokeWidth = array.getDimension(R.styleable.MultipleProgressBar_stroke_width, 0);
        array.recycle();

        mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.LINEAR_TEXT_FLAG);
        mProgressPaint.setColor(progressColor);
        mProgressPaint.setStyle(Paint.Style.FILL);
        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.LINEAR_TEXT_FLAG);
        mBgPaint.setColor(bgColor);
        mBgPaint.setStyle(Paint.Style.FILL);
        mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.LINEAR_TEXT_FLAG);
        mBgPaint.setColor(strokeColor);
        mBgPaint.setStyle(Paint.Style.STROKE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        float progressLength = (float) mProgress / mMaxProgress * getWidth();
        switch (type) {
            case ProgressBarType.ROUND:
                drawRoundRectProgress(canvas);
                break;
            case ProgressBarType.CIRCLE:
                drawCircleProgress(canvas);
                break;
            case ProgressBarType.RECT:
                drawRectProgress(canvas);
                break;
        }
    }

    private void drawRoundRectProgress(Canvas canvas) {
        float progressLength = (float) mProgress / mMaxProgress * getWidth();
        RectF rectF = new RectF(getLeft(), getTop(), getRight(), getBottom());
        canvas.drawRoundRect(rectF, radius, radius, mBgPaint);
        if (strokeWidth > 0) {
            canvas.drawRoundRect(rectF, radius, radius, mStrokePaint);
        }
        //画圆角矩形
//        canvas.drawRoundRect(rect, radius, radius, mProgressPaint);
        Path path = new Path();
        path.moveTo(0, getTop() + radius);
        RectF rectLeftB = new RectF(getLeft(), getBottom() - radius * 2, radius * 2, getBottom());
        RectF rectLeftT = new RectF(getLeft(), getTop(), radius * 2, getTop() + radius * 2);
        RectF rectRightT = new RectF(getWidth() - 2 * radius, getTop(), getWidth(), getTop() + radius * 2);
        RectF rectRightB = new RectF(getWidth() - 2 * radius, getBottom() - radius * 2, getWidth(), getBottom());
        float startAngle, sweepAngle;
        if (progressLength <= radius) {
            //进度小于圆角半径

            //左上角弧度
            startAngle = 180;
            sweepAngle = 90 * (progressLength / radius);
            path.arcTo(rectLeftT, startAngle, sweepAngle);
            path.lineTo(progressLength, getBottom() - (radius - progressLength));

            //左下角弧度
            //90 + 90 * (1 - progressLength / radius)
            startAngle = 90 * (2 - progressLength / radius);
            sweepAngle = 180 - startAngle;
            path.arcTo(rectLeftB, startAngle, sweepAngle);
        } else if (progressLength > radius && progressLength < getWidth() - radius) {
            //左上角圆角
            path.arcTo(rectLeftT, 180, 90);

            path.lineTo(progressLength, getTop());
            path.lineTo(progressLength, getBottom());
            path.lineTo(radius, getBottom());

            //左下角圆角
            path.arcTo(rectLeftB, 90, 90);
        } else {
            //进度大于宽度-圆角半径
            //左上角圆角
            path.arcTo(rectLeftT, 180, 90);
            path.lineTo(getWidth() - radius, getTop());
            //右上角圆弧
            startAngle = 270;
            sweepAngle = 90 * (1 - (getWidth() - progressLength) / radius);
            path.arcTo(rectRightT, startAngle, sweepAngle);
            path.lineTo(progressLength, getBottom() - (getRight() - progressLength));

            //右下角圆弧
            startAngle = (getWidth() - progressLength) / radius * 90;
            sweepAngle = 90 - startAngle;
            path.arcTo(rectRightB, startAngle, sweepAngle);

            path.lineTo(radius, getBottom());

            //左下角圆角
            path.arcTo(rectLeftB, 90, 90);
        }
        path.close();
        canvas.drawPath(path, mProgressPaint);
    }

    private void drawCircleProgress(Canvas canvas) {

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        canvas.drawCircle(centerX, centerY, radius, mBgPaint);
        if (strokeWidth > 0) {
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, mStrokePaint);
        }
        Path path = new Path();
        path.moveTo(centerX, centerY);
        path.lineTo(centerX, getTop());
        RectF rectF = new RectF(getLeft(), getTop(), getRight(), getBottom());
        path.arcTo(rectF, 270, mProgress / mMaxProgress * 360);
        path.close();
        canvas.drawPath(path, mProgressPaint);
    }

    private void drawRectProgress(Canvas canvas) {
        RectF rectF = new RectF(getLeft(), getTop(), getRight(), getBottom());
        canvas.drawRect(rectF, mBgPaint);
        if (strokeWidth > 0) {
            canvas.drawRect(rectF, mStrokePaint);
        }
        float progressLength = (float) mProgress / mMaxProgress * getWidth();
        RectF progressRect = new RectF(getLeft(), getTop(), progressLength, getBottom());
        canvas.drawRect(progressRect, mProgressPaint);
    }

    public int getMaxProgress() {
        return mMaxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.mMaxProgress = maxProgress;
    }

    public void setProgress(int progress) {
        if (progress > mMaxProgress) {
            progress = mMaxProgress;
        }
        this.mProgress = Math.abs(progress);
        this.invalidate();
    }
}

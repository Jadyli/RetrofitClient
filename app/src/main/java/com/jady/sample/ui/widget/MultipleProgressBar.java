package com.jady.sample.ui.widget;

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

/**
 * Created by lipingfa on 2017/6/27.
 */
public class MultipleProgressBar extends View {

    private float strokeWidth;
    @ProgressBarType
    private int type;
    private int progressColor, bgColor, strokeColor;
    private float mRadius, mRectTop, mTextSize;
    private Paint mProgressPaint, mBgPaint, mStrokePaint;
    private float mProgress = 0, mMaxProgress = 100;

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
        bgColor = array.getColor(R.styleable.MultipleProgressBar_bg_color, Color.GRAY);
        strokeColor = array.getColor(R.styleable.MultipleProgressBar_stroke_color, Color.GRAY);
        mRadius = array.getDimension(R.styleable.MultipleProgressBar_radius, 10);
        strokeWidth = array.getDimension(R.styleable.MultipleProgressBar_stroke_width, 0);
        mRectTop = array.getDimension(R.styleable.MultipleProgressBar_rect_top, 0);
        mTextSize = array.getDimension(R.styleable.MultipleProgressBar_text_size, 24);
        array.recycle();

        mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.LINEAR_TEXT_FLAG);
        mProgressPaint.setColor(progressColor);
        mProgressPaint.setStyle(Paint.Style.FILL);
        mProgressPaint.setTextSize(mTextSize);
        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.LINEAR_TEXT_FLAG);
        mBgPaint.setColor(bgColor);
        mBgPaint.setStyle(Paint.Style.FILL);
        mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.LINEAR_TEXT_FLAG);
        mStrokePaint.setColor(strokeColor);
        mStrokePaint.setStyle(Paint.Style.STROKE);

    }

    @Override
    protected void onDraw(Canvas canvas) {

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
        RectF rectF = new RectF(0, mRectTop, getRight(), getBottom());
        canvas.drawRoundRect(rectF, mRadius, mRadius, mBgPaint);
        if (strokeWidth > 0) {
            canvas.drawRoundRect(rectF, mRadius, mRadius, mStrokePaint);
        }
        canvas.drawText((int) mProgress + "%", progressLength, mRectTop - mProgressPaint.descent(), mProgressPaint);
        //画圆角矩形
//        canvas.drawRoundRect(rect, mRadius, mRadius, mProgressPaint);
        Path path = new Path();
        path.moveTo(0, 0 + mRadius);
        RectF rectLeftB = new RectF(0, getBottom() - mRadius * 2, mRadius * 2, getBottom());
        RectF rectLeftT = new RectF(0, mRectTop, mRadius * 2, 0 + mRadius * 2);
        RectF rectRightT = new RectF(getWidth() - 2 * mRadius, mRectTop, getWidth(), 0 + mRadius * 2);
        RectF rectRightB = new RectF(getWidth() - 2 * mRadius, getBottom() - mRadius * 2, getWidth(), getBottom());
        float startAngle, sweepAngle;
        if (progressLength <= mRadius) {
            //进度小于圆角半径

            //左上角弧度
            startAngle = 180;
            sweepAngle = 90 * (progressLength / mRadius);
            path.arcTo(rectLeftT, startAngle, sweepAngle);
            path.lineTo(progressLength, getBottom() - (mRadius - progressLength));

            //左下角弧度
            //90 + 90 * (1 - progressLength / mRadius)
            startAngle = 90 * (2 - progressLength / mRadius);
            sweepAngle = 180 - startAngle;
            path.arcTo(rectLeftB, startAngle, sweepAngle);
        } else if (progressLength > mRadius && progressLength < getWidth() - mRadius) {
            //左上角圆角
            path.arcTo(rectLeftT, 180, 90);

            path.lineTo(progressLength, mRectTop);
            path.lineTo(progressLength, getBottom());
            path.lineTo(mRadius, getBottom());

            //左下角圆角
            path.arcTo(rectLeftB, 90, 90);
        } else {
            //进度大于宽度-圆角半径
            //左上角圆角
            path.arcTo(rectLeftT, 180, 90);
            path.lineTo(getWidth() - mRadius, mRectTop);
            //右上角圆弧
            startAngle = 270;
            sweepAngle = 90 * (1 - (getWidth() - progressLength) / mRadius);
            path.arcTo(rectRightT, startAngle, sweepAngle);
            path.lineTo(progressLength, getBottom() - (getRight() - progressLength));

            //右下角圆弧
            startAngle = (getWidth() - progressLength) / mRadius * 90;
            sweepAngle = 90 - startAngle;
            path.arcTo(rectRightB, startAngle, sweepAngle);

            path.lineTo(mRadius, getBottom());

            //左下角圆角
            path.arcTo(rectLeftB, 90, 90);
        }
        path.close();
        canvas.drawPath(path, mProgressPaint);
    }

    private void drawCircleProgress(Canvas canvas) {

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        canvas.drawCircle(centerX, centerY, mRadius, mBgPaint);
        if (strokeWidth > 0) {
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, mRadius, mStrokePaint);
        }
        Path path = new Path();
        path.moveTo(centerX, centerY);
        path.lineTo(centerX, 0);
        RectF rectF = new RectF(0, 0, getRight(), getBottom());
        path.arcTo(rectF, 270, mProgress / mMaxProgress * 360);
        path.close();
        canvas.drawPath(path, mProgressPaint);
    }

    private void drawRectProgress(Canvas canvas) {
        RectF rectF = new RectF(0, mRectTop, getRight(), getBottom());
        canvas.drawRect(rectF, mBgPaint);
        if (strokeWidth > 0) {
            canvas.drawRect(rectF, mStrokePaint);
        }
        float progressLength = (float) mProgress / mMaxProgress * getWidth();
        canvas.drawText((int) mProgress + "%", progressLength, mRectTop - mProgressPaint.descent(), mProgressPaint);
        RectF progressRect = new RectF(0, mRectTop, progressLength, getBottom());
        canvas.drawRect(progressRect, mProgressPaint);
    }

    public float getMaxProgress() {
        return mMaxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.mMaxProgress = maxProgress;
    }

    public void setProgress(float progress) {
        progress *= 100;
        if (progress > mMaxProgress) {
            progress = mMaxProgress;
        }
        this.mProgress = Math.abs(progress);
        invalidate();
    }
}

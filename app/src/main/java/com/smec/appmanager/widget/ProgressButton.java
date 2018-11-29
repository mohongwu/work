package com.smec.appmanager.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.smec.appmanager.R;
import com.smec.appmanager.api.SmecAppManagerApi;
import com.smec.appmanager.beans.DownloadInfo;
import com.smec.appmanager.utils.DownloadManager;

/**
 * Created by xupeizuo on 2018/6/29.
 */

public class ProgressButton extends View implements DownloadManager.DownloadObserver {

    private Paint.FontMetrics fm;
    private long progress = 0;
    private int textColor = Color.WHITE;
    private Paint paint;
    private float textSize = 10;
    private int foreground;
    private int backgroundColor;
    private String text;
    private long max = 100;
    private int corner = 6;// 圆角的弧度
    private OnProgressButtonClickListener buttonClickListener;

    public ProgressButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ProgressButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressButton);
        this.backgroundColor = typedArray.getInteger(R.styleable.ProgressButton_backgroundBtn, Color.parseColor("#C6C6C6"));
        this.foreground = typedArray.getInteger(R.styleable.ProgressButton_foreground,  Color.rgb(20,131,214));
        this.textColor = typedArray.getInteger(R.styleable.ProgressButton_textcolor, Color.WHITE);
        this.max = typedArray.getInteger(R.styleable.ProgressButton_maxBtn, 100);
        this.progress = typedArray.getInteger(R.styleable.ProgressButton_progressBtn, 0);
        this.text = typedArray.getString(R.styleable.ProgressButton_textBtn);
        this.textSize = typedArray.getDimension(R.styleable.ProgressButton_textSizeBtn, 20);
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(5);

        /**
         * 绘制背景
         */
        RectF oval = new RectF(0, 0, getWidth(), getHeight());

        paint.setColor(this.backgroundColor);
        canvas.drawRoundRect(oval, corner, corner, paint);

        /***
         * 绘制进度值
         */

        paint.setColor(foreground);
        if (progress <= corner) {
            oval = new RectF(0, corner - progress, getWidth() * this.progress / this.max, getHeight()
                    - corner + progress);
            canvas.drawRoundRect(oval, progress,progress, paint);
        } else {
            oval = new RectF(0, 0, getWidth() * this.progress / this.max, getHeight());
            canvas.drawRoundRect(oval, corner, corner, paint);
        }

        /***
         * 绘制文本
         */
        if ("".equals(text) || text == null) {
            return;
        }
        paint.setTextSize(this.textSize);
        fm = paint.getFontMetrics();
        paint.setColor(this.textColor);

        float textCenterVerticalBaselineY = getHeight() / 2 - fm.descent + (fm.descent - fm.ascent) / 2;
        canvas.drawText(this.text, (getMeasuredWidth() - paint.measureText(this.text)) / 2, textCenterVerticalBaselineY,
                paint);
    }

    /**
     * 设置最大值
     *
     * @param max
     */
    public void setMax(long max) {
        this.max = max;
    }

    /**
     * 设置文本提示信息
     *
     * @param text
     */
    public void setText(String text) {
        this.text = text;
        //设置进度之后，要求UI强制进行重绘
        postInvalidate();
    }

    public String getText() {
        return text;
    }

    /**
     * 设置进度条的颜色值
     *
     * @param color
     */
    public void setForeground(int color) {
        this.foreground = color;
    }

    /**
     * 设置进度条的背景色
     */
    public void setBackgroundColor(int color) {
        this.backgroundColor = color;
    }

    /***
     * 设置文本的大小
     */
    public void setTextSize(int size) {
        this.textSize = size;
    }

    /**
     * 设置文本的颜色值
     *
     * @param color
     */
    public void setTextColor(int color) {
        this.textColor = color;
    }

    /**
     * 设置进度值
     *
     * @param progress
     */
    public void setProgress(long progress) {
        if(progress>max){
            return;
        }
        this.progress=progress;
        //设置进度之后，要求UI强制进行重绘
        postInvalidate();
    }

    public long getMax(){
        return max;
    }

    public long getProgress(){
        return progress;
    }

    /*@SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (buttonClickListener != null){
                    buttonClickListener.onClickListener();
                }
                break;
            default:
                break;
        }
        return true;
    }*/

    public void setOnProgressButtonClickListener(OnProgressButtonClickListener clickListener){
        buttonClickListener = clickListener;
    }


    public interface OnProgressButtonClickListener{
        void onClickListener();
    }

    @Override
    public void onDownloadStateChanged(DownloadInfo info) {
        switch (info.currentState){
            case DownloadManager.STATE_WAITING:
                setText("等待");
                break;
            case DownloadManager.STATE_DOWNLOAD:
                setText("下载中");
                break;
            case DownloadManager.STATE_PAUSE:
                setText("暂停");
                //DownloadManager.getInstance().unregisterObserver(this);
                //ampApk.setStatus(SmecAppManagerApi.AppStatus.PASUE.getInfo());
                //DownloadManager.getInstance().pause(ampApk);
                break;
            case DownloadManager.STATE_INSTALL:
                setText("安装中");

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                DownloadManager.getInstance().install(info);
                break;

        }
    }

    @Override
    public void onDownloadProgressChanged(DownloadInfo info) {
        setMax( info.size);
        setProgress( info.currentPos);
    }

}

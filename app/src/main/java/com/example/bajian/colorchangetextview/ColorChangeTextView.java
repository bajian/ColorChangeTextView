package com.example.bajian.colorchangetextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

/**
 * 可以变色的textview
 * Created by bajian on 2015/11/2.
 * email 313066164@qq.com
 */
public class ColorChangeTextView extends View {

    private static final String TAG = "ColorChangeTextView";
    private int mTextWidth;
    private int mTextHeight;
    private Paint mPaint;

    private int mDirection=FROM_LEFT;
    private float mProgress=0;
    private int mTextChangeColor=0x00ff00;
    private int mTextOriginColor=0xff0000;
    private int mTextSize=sp2px(12);
    private String mText="bajian";
    private Rect mTextBound = new Rect();

    //变色方向
    public static final int FROM_LEFT=0;
    public static final int FROM_RIGHT=1;
    public static final int FROM_TOP=2;

    public static final int FROM_BOTTOM=3;

    private int mRealWidth;//除去padding的text宽度
    private int mTextStartX;//开始变色的位置


    public ColorChangeTextView(Context context) {
        super(context,null);
    }

    public ColorChangeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        Log.d(TAG, "ColorChangeTextView construct");
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ColorChangeTextView);
        mText=a.getString(R.styleable.ColorChangeTextView_text);
        mTextSize=a.getDimensionPixelSize(R.styleable.ColorChangeTextView_text_size, mTextSize);
        mTextOriginColor=a.getColor(R.styleable.ColorChangeTextView_text_origin_color, mTextOriginColor);
        mTextChangeColor=a.getColor(R.styleable.ColorChangeTextView_text_change_color, mTextChangeColor);
        mProgress=a.getFloat(R.styleable.ColorChangeTextView_progress, 0);
        mDirection=a.getInt(R.styleable.ColorChangeTextView_change_direction, FROM_LEFT);
        a.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(mTextSize);
    }


    public void setDirection(int mDirection) {
        this.mDirection = mDirection;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int r = (int) (mProgress* mTextWidth +mTextStartX );
        Log.d(TAG,"R="+r);
        if(mDirection == FROM_LEFT)
        {
            drawChangeLeft(canvas, r);//无先后顺序
            drawOriginLeft(canvas, r);
        }else
        {
            drawOriginRight(canvas, r);
            drawChangeRight(canvas, r);
        }

    }

    private void drawChangeRight(Canvas canvas, int r)
    {
        drawText(canvas, mTextChangeColor, (int) (mTextStartX + (1 - mProgress) * mTextWidth), (int) (mTextStartX + mTextWidth));
    }
    private void drawOriginRight(Canvas canvas, int r)
    {
        drawText(canvas, mTextOriginColor, mTextStartX, (int) (mTextStartX +(1-mProgress)*mTextWidth) );
    }

    private void drawChangeLeft(Canvas canvas, int r)
    {
        drawText(canvas, mTextChangeColor, mTextStartX, (int) (mTextStartX + mProgress * mTextWidth) );
    }

    private void drawOriginLeft(Canvas canvas, int r)
    {
        drawText(canvas, mTextOriginColor, (int) (mTextStartX + mProgress * mTextWidth), (int) (mTextStartX +mTextWidth));
    }

    private void drawText(Canvas canvas , int color , int startX , int endX)
    {
        mPaint.setColor(color);
        canvas.save(Canvas.CLIP_SAVE_FLAG);
        //int left, int top, int right, int bottom
        canvas.clipRect(startX, 0, endX, getMeasuredHeight());
        //String text, float x, float y, @NonNull Paint paint
        canvas.drawText(mText, mTextStartX, getMeasuredHeight() / 2
                + mTextBound.height() / 2, mPaint);//居中
        canvas.restore();
    }


    /**
     * 如果继承TextView，测量就不需要写了
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure");
        measureText();

        int width=measureWidth(widthMeasureSpec);
        int height=measureHeight(heightMeasureSpec);
        Log.d(TAG, "width=" + width + ",height="+height);
        setMeasuredDimension(width,height);

        mRealWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        mTextStartX =mRealWidth / 2 - mTextWidth / 2;

    }

    private void measureText() {

        mTextWidth = (int) mPaint.measureText(mText);
//mTextBound:Returns the unioned bounds of all the text. Must be allocated by the caller
        mPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
        mTextHeight = mTextBound.height();
    }

    private int measureWidth(int mWidthMeasureSpec) {
        int mode = MeasureSpec.getMode(mWidthMeasureSpec);
        int value = MeasureSpec.getSize(mWidthMeasureSpec);
        int width=0;
        switch (mode) {
            case MeasureSpec.EXACTLY:
                width=value;
                break;
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                width=mTextBound.width();
                width += getPaddingLeft() + getPaddingRight();
                break;
        }

        width = mode == MeasureSpec.AT_MOST ? Math.min(width, value) : width;
        return width + getPaddingLeft() + getPaddingRight();

    }

    private int measureHeight(int mHeightMeasureSpec) {
        int mode = MeasureSpec.getMode(mHeightMeasureSpec);
        int value = MeasureSpec.getSize(mHeightMeasureSpec);
        int height=0;
        switch (mode) {
            case MeasureSpec.EXACTLY:
                height=value;
                break;
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                height=mTextBound.height();//TODO MEASURE
                height += getPaddingTop() + getPaddingBottom();
                break;
        }

        height = mode == MeasureSpec.AT_MOST ? Math.min(height, value) : height;
        return height + getPaddingBottom() + getPaddingTop();

    }


    private int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    private int sp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                dpVal, getResources().getDisplayMetrics());
    }


    public float getTextWidth() {
        return mTextWidth;
    }

    public Paint getPaint() {
        return mPaint;
    }

    public float getProgress() {
        return mProgress;
    }

    public int getTextChangeColor() {
        return mTextChangeColor;
    }

    public int getTextOriginColor() {
        return mTextOriginColor;
    }

    public int getTextSize() {
        return mTextSize;
    }

    public String getText() {
        return mText;
    }

    public Rect getTextBound() {
        return mTextBound;
    }

    public int getTextStartX() {
        return mTextStartX;
    }

    public int getDirection() {
        return mDirection;
    }

    public void setTextStartX(int mTextStartX) {
        this.mTextStartX = mTextStartX;
    }

    public void setTextWidth(int mTextWidth) {
        this.mTextWidth = mTextWidth;
    }

    public void setPaint(Paint mPaint) {
        this.mPaint = mPaint;
    }

    public void setProgress(float progress) {
        this.mProgress = progress;
    }

    public void setTextChangeColor(int mTextChangeColor) {
        this.mTextChangeColor = mTextChangeColor;
    }

    public void setTextOriginColor(int mTextOriginColor) {
        this.mTextOriginColor = mTextOriginColor;
    }

    public void setTextSize(int mTextSize) {
        this.mTextSize = mTextSize;
    }

    public void setText(String mText) {
        this.mText = mText;
    }

    public void setTextBound(Rect mTextBound) {
        this.mTextBound = mTextBound;
    }
}

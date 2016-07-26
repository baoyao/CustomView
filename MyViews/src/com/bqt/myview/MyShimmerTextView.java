package com.bqt.myview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

public class MyShimmerTextView extends TextView {
	/**��Ⱦ����������ʾ�����е���ɫЧ��*/
	private LinearGradient mLinearGradient;
	/**��������ȷ����Ⱦ��Χ*/
	private Matrix mGradientMatrix;
	/**��Ⱦ����ʼλ��*/
	private int mViewWidth = 0;
	/**��Ⱦ����ֹ����*/
	private int mTranslate = 0;
	/**�Ƿ���������*/
	private boolean mAnimating = true;
	/**���ٺ���ˢ��һ��*/
	private int speed = 50;
	private Paint mPaint;

	public MyShimmerTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mPaint = getPaint();
		mGradientMatrix = new Matrix();
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mViewWidth = getMeasuredWidth();
		//���Գ���һ�£�ʹ�ò�ͬ��ģʽ���Եõ���ͬ��Ч��
		mLinearGradient = new LinearGradient(0, 0, mViewWidth, 0, new int[] { Color.BLACK, Color.WHITE, Color.BLACK }, null, TileMode.CLAMP);
		mPaint.setShader(mLinearGradient);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		Log.i("bqt", w + "--" + h);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mAnimating && mGradientMatrix != null) {
			mTranslate += mViewWidth / 10;//ÿ���ƶ���Ļ��1/10��
			if (mTranslate > 2 * mViewWidth) {
				mTranslate = -mViewWidth;
			}
			mGradientMatrix.setTranslate(mTranslate, 0);
			mLinearGradient.setLocalMatrix(mGradientMatrix);//��ָ����������Ⱦ
			postInvalidateDelayed(speed);
		}
	}
}
package com.bqt.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.view.View;

public class LinearGradientView extends View {
	private Paint mPaint = null;
	private Shader mLinearGradient11 = null;
	private Shader mLinearGradient12 = null;
	private Shader mLinearGradient13 = null;
	private Shader mLinearGradient21 = null;
	private Shader mLinearGradient22 = null;
	private Shader mLinearGradient23 = null;
	private Shader mLinearGradient31 = null;
	private Shader mLinearGradient32 = null;
	private Shader mLinearGradient33 = null;

	public LinearGradientView(Context context) {
		super(context);
		// ��һ�ڶ���������ʾ������㣬���������ڶԽǵ�����λ�ã��������ĸ�������ʾ�����յ㣻�����������ʾ������ɫ��
		// ��������������Ϊ�գ���ʾ����ֵΪ0-1 new float[] {0.25f, 0.5f, 0.75f, 1 }  ���Ϊ�գ���ɫ���ȷֲ������ݶ��ߡ�
		mLinearGradient11 = new LinearGradient(0, 0, 0, 200, new int[] { Color.RED, Color.GREEN, Color.BLUE, Color.BLACK }, null, TileMode.CLAMP);
		mLinearGradient12 = new LinearGradient(0, 0, 0, 300, new int[] { Color.RED, Color.GREEN, Color.BLUE, Color.BLACK }, new float[] { 0, 0.1f, 0.5f, 0.5f }, TileMode.CLAMP);
		mLinearGradient13 = new LinearGradient(0, 0, 0, 400, new int[] { Color.RED, Color.GREEN, Color.BLUE }, null, TileMode.CLAMP);

		mLinearGradient21 = new LinearGradient(0, 320, 0, 320 + 200, new int[] { Color.RED, Color.GREEN, Color.BLUE, Color.BLACK }, null, TileMode.MIRROR);
		mLinearGradient22 = new LinearGradient(0, 320, 0, 320 + 100, new int[] { Color.RED, Color.GREEN, Color.BLUE }, null, TileMode.MIRROR);
		mLinearGradient23 = new LinearGradient(0, 320, 0, 320 + 100, new int[] { Color.RED, Color.GREEN, Color.BLUE }, new float[] { 0, 0.1f, 0.5f }, TileMode.MIRROR);

		mLinearGradient31 = new LinearGradient(0, 640, 0, 640 + 200, new int[] { Color.RED, Color.GREEN, Color.BLUE, Color.BLACK }, null, TileMode.REPEAT);
		mLinearGradient32 = new LinearGradient(0, 640, 0, 640 + 100, new int[] { Color.RED, Color.GREEN, Color.BLUE }, null, TileMode.REPEAT);
		mLinearGradient33 = new LinearGradient(0, 640, 0, 640 + 100, new int[] { Color.RED, Color.GREEN, Color.BLUE }, new float[] { 0, 0.1f, 0.5f }, TileMode.REPEAT);
		mPaint = new Paint();
	}

	@Override
	protected void onDraw(Canvas canvas) {

		//�ظ����һ������
		mPaint.setStrokeWidth(2);
		mPaint.setShader(mLinearGradient11);
		canvas.drawRect(0, 0, 200, 300, mPaint);//200-300֮��ȫ�������ĺ�ɫ
		mPaint.setShader(mLinearGradient12);
		canvas.drawRect(210, 0, 410, 300, mPaint);//0.5֮��ȫ���Ǻ�ɫ��
		mPaint.setShader(mLinearGradient13);
		canvas.drawRect(420, 0, 620, 300, mPaint); // LinearGradient�ĸ߶�С��Ҫ���Ƶľ��εĸ߶�ʱ�Ż����ظ��������Ч��
		canvas.drawLine(0, 310, 720, 310, mPaint);

		//����Ч��
		mPaint.setShader(mLinearGradient21);
		canvas.drawRect(0, 320, 200, 620, mPaint);
		mPaint.setShader(mLinearGradient22);
		canvas.drawRect(210, 320, 410, 620, mPaint);
		mPaint.setShader(mLinearGradient23);
		canvas.drawRect(420, 320, 620, 620, mPaint);
		canvas.drawLine(0, 630, 720, 630, mPaint);

		//�ظ�Ч��
		mPaint.setShader(mLinearGradient31);
		canvas.drawRect(0, 640, 200, 940, mPaint);
		mPaint.setShader(mLinearGradient32);
		canvas.drawRect(210, 640, 410, 940, mPaint);
		mPaint.setShader(mLinearGradient33);
		canvas.drawRect(420, 640, 620, 940, mPaint);
	}
}
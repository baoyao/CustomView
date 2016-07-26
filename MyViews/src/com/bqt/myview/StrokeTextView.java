package com.bqt.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint.Style;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

/*
 * �����ּ���ߣ�ʵ�ַ���������TextView���ӣ�ֻ����ߵ�TextView���ڵײ���ʵ��TextView����������
 * ���˿ؼ���������ͬʱ������һ��TextView��������TextView��ΪSTROKE��ʽ
 * �ڲ���onMeasure������onDraw������onLayout����ʱ��ʹ��ͬ���Ĳ���ͬ���ķ�ʽ�����STROKE��ʽ��TextView
 */
public class StrokeTextView extends TextView {
	/**��ߵ�TextView*/
	private TextView strokeTextView = null;
	/**��ߵ�TextView����ɫ*/
	private int strokeColor = 0xffff0000;
	/**��ߵ�TextView�Ŀ��*/
	private float strokeWidth = 1.5f;

	/**
	 * ������ߵ���ɫ�Ϳ��
	 */
	public void setStrokeColorAndWidth(int strokeColor, float strokeWidth) {
		this.strokeColor = strokeColor;
		this.strokeWidth = strokeWidth;
		init();//�������µ��ó�ʼ������
	}

	public StrokeTextView(Context context) {
		this(context, null);
	}

	public StrokeTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public StrokeTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		strokeTextView = new TextView(context, attrs, defStyle);
		init();
	}

	public void init() {
		TextPaint textPaint = strokeTextView.getPaint();
		textPaint.setStrokeWidth(strokeWidth); //������߿��
		strokeTextView.setTextColor(strokeColor); //���������ɫ
		textPaint.setStyle(Style.STROKE); //������ֻ���
		invalidate();
	}

	//**************************************************************************************************************************
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		strokeTextView.setText(getText());
		strokeTextView.setGravity(getGravity());
		strokeTextView.measure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	//�Բۣ�Ϊɶ�����super���ں��棿
	protected void onDraw(Canvas canvas) {
		strokeTextView.draw(canvas);
		super.onDraw(canvas);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		strokeTextView.layout(left, top, right, bottom);
	}

	@Override
	public void setLayoutParams(ViewGroup.LayoutParams params) {
		super.setLayoutParams(params);
		strokeTextView.setLayoutParams(params);
	}

	@Override
	public void setBackgroundResource(int resid) {
		super.setBackgroundResource(resid);
		strokeTextView.setBackgroundResource(resid);
	}

	@Override
	public void setTextSize(float size) {
		super.setTextSize(size);
		strokeTextView.setTextSize(size);
	}

	@Override
	public void setTextSize(int unit, float size) {
		super.setTextSize(unit, size);
		strokeTextView.setTextSize(unit, size);
	}

	@Override
	public void setPadding(int left, int top, int right, int bottom) {
		super.setPadding(left, top, right, bottom);
		strokeTextView.setPadding(left, top, right, bottom);
	}
}
package com.bqt.myview;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class CustomProgressBar extends View {
	/**��һȦ����ɫ*/
	private int mFirstColor = Color.GREEN;
	/**�ڶ�Ȧ����ɫ*/
	private int mSecondColor = Color.RED;
	/**Ȧ�Ŀ��*/
	private int mCircleWidth = 40;
	/**��ǰ����*/
	private int mProgress = 0;
	/**�ٶȡ�ʵ�ʴ����������ʱ�䣬����ֻ��Ϊ�˷�����ʾ*/
	private int mSpeed = 30;
	/**���ȵİٷֱ���ʽ*/
	private String percent = "0%";
	/**����ʱ�ı����Ƶķ�Χ*/
	private Rect mRect = null;
	/**����ʱ�ı��Ĵ�С*/
	private int sTextSize = 40;
	/**����ʱ�ı�����ɫ*/
	private int sTextColor = Color.BLACK;
	/**����*/
	private Paint mPaint = null;
	/**�Ƿ�Ӧ�ÿ�ʼ��һ��*/
	private boolean isToNext = false;
	/**���ڶ���Բ������״�ʹ�С�Ľ���*/
	private RectF oval;
	private OnComompleteListener mListener;

	public CustomProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomProgressBar(Context context) {
		this(context, null);
	}

	public CustomProgressBar(final Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mRect = new Rect();
		mPaint = new Paint();
		oval = new RectF();
		TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomProgressBar, defStyle, 0);
		for (int i = 0; i < typedArray.getIndexCount(); i++) {
			int attr = typedArray.getIndex(i);
			switch (attr) {
			case R.styleable.CustomProgressBar_firstColor:
				mFirstColor = typedArray.getColor(attr, Color.GREEN);
				break;
			case R.styleable.CustomProgressBar_secondColor:
				mSecondColor = typedArray.getColor(attr, Color.RED);
				break;
			case R.styleable.CustomProgressBar_circleWidth:
				mCircleWidth = typedArray.getDimensionPixelSize(attr,
						(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 20, getResources().getDisplayMetrics()));
				break;
			case R.styleable.CustomProgressBar_speed:
				mSpeed = typedArray.getInt(attr, 20);
				break;
			}
		}
		typedArray.recycle();

		// ��ͼ�߳�
		new Thread() {
			public void run() {
				while (true) {
					mProgress++;
					if (mProgress == 360) {
						if (mListener != null && context instanceof Activity) {
							((Activity) context).runOnUiThread(new Runnable() {//Activity�õ��ص�ʱ������ֱ�������߳��и���UI
										@Override
										public void run() {
											mListener.onComplete(CustomProgressBar.this, isToNext);
										}
									});
						}
						mProgress = 0;
						percent = "100%";
						if (!isToNext) {//���������ת�Ļ���ֹͣ
							mPaint.getTextBounds(percent, 0, percent.length(), mRect);
							postInvalidate();//Ҫˢ��һ�£���������״̬����û������
							return;
						}
					} else if (mProgress % 10 == 0) {//ÿ10������ˢ��һ�Σ�Ŀ���Ƿ�ֹƵ���ĸ��½��ȡ�ʵ����Ŀ�в���Ҫ���ǣ�
						percent = mProgress * 100 / 360 + "%";
					}
					mPaint.getTextBounds(percent, 0, percent.length(), mRect);//����ʼ�ı��ı߽�ֵ��װ������mRect��
					postInvalidate();
					try {
						Thread.sleep(mSpeed);
					} catch (InterruptedException e) {
					}
				}
			};
		}.start();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		mPaint.setAntiAlias(true); // �������
		mPaint.setStyle(Paint.Style.STROKE); //����
		mPaint.setStrokeWidth(mCircleWidth); // ����Բ���Ŀ��
		int centre = getWidth() / 2; // ��ȡԲ�ĵ�x����
		int radius = centre - mCircleWidth / 2;// �뾶
		oval = new RectF(centre - radius, centre - radius, centre + radius, centre + radius); // ���ڶ����Բ������״�ʹ�С�Ľ���

		if (!isToNext) {// ��һ��ɫ��Ȧ�������ڶ���ɫ��
			mPaint.setColor(mFirstColor); // ����Բ������ɫ
			canvas.drawCircle(centre, centre, radius, mPaint); // ����Բ��
			mPaint.setColor(mSecondColor); // ����Բ������ɫ
			canvas.drawArc(oval, -90, mProgress, false, mPaint); // ���ݽ��Ȼ�Բ��
		} else {
			mPaint.setColor(mSecondColor); // ����Բ������ɫ
			canvas.drawCircle(centre, centre, radius, mPaint); // ����Բ��
			mPaint.setColor(mFirstColor); // ����Բ������ɫ
			canvas.drawArc(oval, -90, mProgress, false, mPaint); // ���ݽ��Ȼ�Բ��
		}
		//�ٻ���һ��֪ʶ��ǰ���ȵ����֣��Ȱ�װ���ֵķ�Χ���ٸ��ݴ˴�С�������ֵ�λ��
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setTextSize(sTextSize);
		mPaint.setColor(sTextColor);
		canvas.drawText(percent, centre - mRect.width() / 2, centre + mRect.height() / 2, mPaint);
	}

	public boolean isToNext() {
		return isToNext;
	}

	public void setToNext(boolean isToNext) {
		this.isToNext = isToNext;
	}

	//�Զ���Ļص�
	public interface OnComompleteListener {
		public void onComplete(CustomProgressBar pb, boolean isToNext);
	}

	public void setOnComompleteListener(OnComompleteListener listener) {
		mListener = listener;
	}
}
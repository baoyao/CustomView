package com.bqt.myview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;

public class CustomVolumControlBar extends View implements OnClickListener {
	/**��һȦ����ɫ����ɫ*/
	private int mFirstColor = Color.GRAY;
	/**�ڶ�Ȧ����ɫ���������������ɫ*/
	private int mSecondColor = Color.RED;
	/**Ȧ�ĺ��*/
	private int mCircleWidth = 10;
	/**���ĸ���*/
	private int mCount = 6;
	/**����ļ�϶*/
	private int mSplitSize = 20;
	/**��ǰ���ȣ���ʵӦ�����ж�mCount�Ƿ����mCurrentCount�ģ�*/
	private int mCurrentCount = 4;
	/**���Ŀ�ʼ��*/
	private int mStartCount = 0;
	/**�м��ͼƬ*/
	private Bitmap mImage = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
	private final Paint mPaint = new Paint();//����
	private final Rect mRect = new Rect();

	public CustomVolumControlBar(Context context) {
		this(context, null);
	}

	public CustomVolumControlBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomVolumControlBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setOnClickListener(this);//������Լ����õ���¼�
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomVolumControlBar, defStyle, 0);
		for (int i = 0; i < a.getIndexCount(); i++) {
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.CustomVolumControlBar_firstColor:
				mFirstColor = a.getColor(attr, Color.GRAY);
				break;
			case R.styleable.CustomVolumControlBar_secondColor:
				mSecondColor = a.getColor(attr, Color.RED);
				break;
			case R.styleable.CustomVolumControlBar_cb_bg:
				mImage = BitmapFactory.decodeResource(getResources(), a.getResourceId(attr, R.drawable.ic_launcher));
				break;
			case R.styleable.CustomVolumControlBar_circleWidth:
				mCircleWidth = a.getDimensionPixelSize(attr,
						(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 10, getResources().getDisplayMetrics()));
				break;
			case R.styleable.CustomVolumControlBar_dotCount:
				mCount = a.getInt(attr, 6);
				break;
			case R.styleable.CustomVolumControlBar_splitSize:
				mSplitSize = a.getInt(attr, 20);
				break;
			}
		}
		a.recycle();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		mPaint.setAntiAlias(true); // �������
		mPaint.setStrokeWidth(mCircleWidth); // ����Բ���Ŀ��
		mPaint.setStrokeCap(Paint.Cap.ROUND); // �����߶ζϵ㴦��״ΪԲͷ
		mPaint.setAntiAlias(true); // �������
		mPaint.setStyle(Paint.Style.STROKE); // ���ÿ���
		int centre = getWidth() / 2; // ��ȡԲ�ĵ�x����
		int radius = centre - mCircleWidth / 2;// �뾶
		//�����ȥ
		drawOval(canvas, centre, radius);
		//�������������ε�λ��
		int relRadius = radius - mCircleWidth / 2;// �����Բ�İ뾶
		// ���������εľ��붥�� = mCircleWidth + relRadius - ��2 / 2
		mRect.left = (int) (relRadius - Math.sqrt(2) * 1.0f / 2 * relRadius) + mCircleWidth;
		//���������εľ������ = mCircleWidth + relRadius - ��2 / 2
		mRect.top = (int) (relRadius - Math.sqrt(2) * 1.0f / 2 * relRadius) + mCircleWidth;
		mRect.bottom = (int) (mRect.left + Math.sqrt(2) * relRadius);
		mRect.right = (int) (mRect.left + Math.sqrt(2) * relRadius);
		// ���ͼƬ�Ƚ�С����ô����ͼƬ�ĳߴ���õ�������
		if (mImage.getWidth() < Math.sqrt(2) * relRadius) {
			mRect.left = (int) (mRect.left + Math.sqrt(2) * relRadius * 1.0f / 2 - mImage.getWidth() * 1.0f / 2);
			mRect.top = (int) (mRect.top + Math.sqrt(2) * relRadius * 1.0f / 2 - mImage.getHeight() * 1.0f / 2);
			mRect.right = (int) (mRect.left + mImage.getWidth());
			mRect.bottom = (int) (mRect.top + mImage.getHeight());
		}
		// ��ͼ
		canvas.drawBitmap(mImage, null, mRect, mPaint);
	}

	//���ݲ�������ÿ��С��
	private void drawOval(Canvas canvas, int centre, int radius) {
		float itemSize = (360 * 1.0f - mCount * mSplitSize) / mCount;//������Ҫ���ĸ����Լ���϶����ÿ�������ռ�ı���*360
		RectF oval = new RectF(centre - radius, centre - radius, centre + radius, centre + radius); // ���ڶ����Բ������״�ʹ�С�Ľ���
		mPaint.setColor(mFirstColor); // ����Բ������ɫ
		for (int i = 0; i < mCount; i++) {
			canvas.drawArc(oval, i * (itemSize + mSplitSize), itemSize, false, mPaint); // ���ݽ��Ȼ�Բ��
		}
		mPaint.setColor(mSecondColor); // ����Բ������ɫ
		for (int i = 0; i < mCurrentCount; i++) {
			canvas.drawArc(oval, (i - mStartCount) * (itemSize + mSplitSize), itemSize, false, mPaint); // ���ݽ��Ȼ�Բ��
		}
	}

	@Override
	public void onClick(View v) {
		if (mCurrentCount < mCount) mCurrentCount++;
		else mCurrentCount = 1;
		invalidate();
	}
}
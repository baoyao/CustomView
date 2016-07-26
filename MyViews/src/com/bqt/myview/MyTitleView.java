package com.bqt.myview;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class MyTitleView extends View implements OnClickListener {
	/**��ʼ�ı����ݣ��ڶ���ʱ����ֵ�����ڲ�����û�ж���ʱ������Ĭ��ֵ*/
	private String mTitleText = "8888";
	/**�ı�����ɫ*/
	private int mTitleTextColor = Color.RED;
	/**�ı������С*/
	private int mTitleTextSize;
	/**����ɫ*/
	private int mTitleBackgroundColor = Color.YELLOW;
	/**Բ�Ǵ�С*/
	private int mTitleRoundSize;
	/**����ʱ�ı����Ƶķ�Χ*/
	private Rect mRect;
	private Paint mPaint;//����
	private Context mContext;

	//�ڡ����롿���洴�������ʱ��ʹ�ô˹��췽��
	public MyTitleView(Context context) {
		this(context, null);
	}

	//�ڡ����֡��ļ���ʹ��ʱ��ϵͳĬ�ϻ�������������Ĺ��췽��
	public MyTitleView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MyTitleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		initAttrs(context, attrs, defStyle);
		initRect();
		mTitleRoundSize = dp2px(5);
		setOnClickListener(this);
	}

	//��ʼ�����Լ�
	private void initAttrs(Context context, AttributeSet attrs, int defStyle) {
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyTitleView, defStyle, 0);//��ȡ�����ڡ����Լ����С����塿���Զ������Եļ���
		int count = typedArray.getIndexCount();//��ȡ�����ڡ����֡��ļ��С����á����Զ������Եĸ�������û�������κ��Զ�������ԣ����ֵΪ0
		for (int i = 0; i < count; i++) {
			int attrIndex = typedArray.getIndex(i);//��ȡ�����Եı�ţ���ֵ����R�ļ��Զ����ɵģ��������Լ����ж�������Ե�λ�ã���һ�����Զ�Ӧ�ı��Ϊ0
			switch (attrIndex) {
			case R.styleable.MyTitleView_titleText://=0
				mTitleText = typedArray.getString(attrIndex);// ��ȡ�����֡��ļ������õ�ֵ
				break;
			case R.styleable.MyTitleView_titleTextColors://=1
				mTitleTextColor = typedArray.getColor(attrIndex, Color.RED);//defValue��Value to return if the attribute is not defined or not a resource 
				// ע�⣺������û���ô�����ʱ�������ִ�в���������Բ����ڴ�����Ĭ��ֵ��Ĭ��ֵ����ֱ���ڹ��췽���г�ʼ����
				break;
			case R.styleable.MyTitleView_titleBackground://=2
				mTitleBackgroundColor = typedArray.getColor(attrIndex, Color.YELLOW);
				break;
			case R.styleable.MyTitleView_titleTextSize://=3
				mTitleTextSize = typedArray.getDimensionPixelSize(attrIndex, 30);//��ʶ��������е�λ��sp(��dp)����px
				break;
			}
		}
		typedArray.recycle();//Give back a previously retrieved array, for later re-use.
	}

	//��ȡ�ı���Ҫռ�õĿռ��С
	private void initRect() {
		mPaint = new Paint();
		//������Զ����С�������Զ���ģ���������һ��Ĭ�ϵġ���Ϊ�����ԱʱContext�������ڣ����Է������︳��ֵ��
		if (mTitleTextSize == 0) mTitleTextSize = dp2px(30);
		mPaint.setTextSize(mTitleTextSize);//��λΪpx
		mRect = new Rect();
		mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mRect);//����ʼ�ı��ı߽�ֵ��װ������mRect��
	}

	@Override
	public void onClick(View v) {
		mTitleText = getRandomText();
		invalidate();//���ô˷�������ˢ��UI��If the view is visible, onDraw will be called at some point in the future��
	}

	@Override
	//����view�ߴ�ʱ�Ļص�����
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		//���ÿ��
		int width = 0;
		int width_size = MeasureSpec.getSize(widthMeasureSpec);//������ֵ
		int width_mode = MeasureSpec.getMode(widthMeasureSpec);//����ģʽ
		switch (width_mode) {
		case MeasureSpec.EXACTLY:// �����˾����ֵ�����ǡ�MATCH_PARENT��ʱ������ֱ��ʹ�ò�����ֵ
			width = getPaddingLeft() + getPaddingRight() + width_size;//Paddingֵ+����ֵ
			break;
		case MeasureSpec.AT_MOST:// ����������Ϊ��WARP_CONTENT��ʱ��������ֵʵ��Ϊ��MATCH_PARENT��������ʹ�ã������������¼���
			width = getPaddingLeft() + getPaddingRight() + mRect.width();//Paddingֵ+�ı�ʵ��ռ�ÿռ�
			break;
		}
		//���ø߶�
		int height = 0;
		int height_size = MeasureSpec.getSize(heightMeasureSpec);
		int height_mode = MeasureSpec.getMode(heightMeasureSpec);
		switch (height_mode) {
		case MeasureSpec.EXACTLY:
			height = getPaddingTop() + getPaddingBottom() + height_size;
			break;
		case MeasureSpec.AT_MOST:
			height = getPaddingTop() + getPaddingBottom() + mRect.height();
			break;
		}
		//���ÿؼ�ʵ�ʴ�С
		setMeasuredDimension(width, height);
	}

	@Override
	//��onDraw�л��ơ�Բ�Ǿ��Ρ��ı�����������ɵ����֡�ֻҪͼ�����иı䣬�˷����ھͻ����
	protected void onDraw(Canvas canvas) {
		Log.i("bqt", "getMeasuredWidth()=" + getMeasuredWidth() + "��getMeasuredHeight()=" + getMeasuredHeight());//���һ�ε���һ��
		mPaint.setColor(mTitleBackgroundColor);
		canvas.drawRoundRect(new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight()), mTitleRoundSize, mTitleRoundSize, mPaint);//���Ʊ���
		mPaint.setColor(mTitleTextColor);
		canvas.drawText(mTitleText, getWidth() / 2 - mRect.width() / 2, getHeight() / 2 + mRect.height() / 2, mPaint);//���л�������
	}

	/** 
	  * �����ֻ��ķֱ��ʴ� dp �ĵ�λ ת��Ϊ px(����) 
	  */
	public int dp2px(float dpValue) {
		final float scale = mContext.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * ��ȡһ����λ���ֵ������
	 */
	public String getRandomText() {
		Random random = new Random();
		Set<Integer> set = new HashSet<Integer>();
		while (set.size() < 4) {
			int randomInt = random.nextInt(10);
			set.add(randomInt);
		}
		StringBuffer sb = new StringBuffer();
		for (Integer i : set) {
			sb.append("" + i);
		}
		return sb.toString();
	}
}
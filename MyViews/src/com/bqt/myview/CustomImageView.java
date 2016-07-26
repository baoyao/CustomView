package com.bqt.myview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * �Զ���View������ͨͼ�ν�ȡ��Բ�ǣ�Բ�λ����ߵ�Ч��
 */
public class CustomImageView extends View {
	/**Բ��*/
	public static final int TYPE_CIRCLE = 0;
	/**����*/
	public static final int TYPE_RECTF = 1;
	/**����ߵ�Բ��*/
	public static final int TYPE_CIRCLE_WITH_STROKE = 2;
	/**���ͣ�Ĭ��ΪԲ��*/
	private int type = TYPE_CIRCLE;
	/**������ʹ����ָ����ߣ�������ȫ����*/
	private Bitmap mSrc = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
	/**Բ�ǵĴ�С*/
	private int mRadius;
	/**��ߵ���ɫ*/
	private int strokeColor = 0xffff0000;
	/**��ߵĿ��*/
	private int strokeWidth;
	/**�ؼ��Ŀ��*/
	private int mWidth;
	/**�ؼ��ĸ߶�*/
	private int mHeight;
	private Context mContext;

	public CustomImageView(Context context) {
		this(context, null);
	}

	public CustomImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		mRadius = dp2px(40);
		strokeWidth = dp2px(2);
		TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomImageView, defStyle, 0);
		for (int i = 0; i < typedArray.getIndexCount(); i++) {
			int attr = typedArray.getIndex(i);
			switch (attr) {
			case R.styleable.CustomImageView_src:
				mSrc = BitmapFactory.decodeResource(getResources(), typedArray.getResourceId(attr, 0));
				break;
			case R.styleable.CustomImageView_types://����
				type = typedArray.getInt(attr, 0);
				break;
			case R.styleable.CustomImageView_radius://Բ�Ǵ�С
				mRadius = typedArray.getDimensionPixelSize(attr, dp2px(5));
				break;
			}
		}
		typedArray.recycle();
	}

	//**************************************************************************************************************************
	// ����ؼ��ĸ߶ȺͿ��
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//���ÿ��
		int width_size = MeasureSpec.getSize(widthMeasureSpec);
		int width_mode = MeasureSpec.getMode(widthMeasureSpec);
		if (width_mode == MeasureSpec.EXACTLY) mWidth = width_size;
		else {
			// ��ͼƬ�����Ŀ�
			int desireByImg = getPaddingLeft() + getPaddingRight() + mSrc.getWidth();
			if (width_mode == MeasureSpec.AT_MOST) mWidth = Math.min(desireByImg, width_size);
			else mWidth = desireByImg;
		}
		//���ø߶�
		int height_size = MeasureSpec.getSize(heightMeasureSpec);
		int height_mode = MeasureSpec.getMode(heightMeasureSpec);
		if (height_mode == MeasureSpec.EXACTLY) mHeight = height_size;
		else {
			int desire = getPaddingTop() + getPaddingBottom() + mSrc.getHeight();
			if (height_mode == MeasureSpec.AT_MOST) mHeight = Math.min(desire, height_size);
			else mHeight = desire;
		}
		setMeasuredDimension(mWidth, mHeight);
	}

	@Override
	//����ͼ��
	protected void onDraw(Canvas canvas) {
		switch (type) {
		case TYPE_CIRCLE:
			int min = Math.min(mWidth, mHeight);//���������һ�£���С��ֵ����ѹ��
			mSrc = Bitmap.createScaledBitmap(mSrc, min, min, false);
			canvas.drawBitmap(getCircleImage(mSrc, min), 0, 0, null);
			break;
		case TYPE_RECTF:
			canvas.drawBitmap(getRoundConerImage(mSrc), 0, 0, null);
			break;
		case TYPE_CIRCLE_WITH_STROKE:
			int min2 = Math.min(mWidth, mHeight);
			mSrc = Bitmap.createScaledBitmap(mSrc, min2, min2, false);
			canvas.drawBitmap(getCircleImageWithStroke(mSrc, min2), 0, 0, null);
			break;
		}
	}

	//**************************************************************************************************************************
	/**
	 * ���ơ�Բ�Ρ�ͼƬ
	 * @param min Ҫ���ɵ�ͼ�δ�С
	 */
	private Bitmap getCircleImage(Bitmap source, int min) {
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		Bitmap target = Bitmap.createBitmap(min, min, Config.ARGB_8888);
		Canvas canvas = new Canvas(target);//����һ��ͬ����С�Ļ���
		//SRC_INģʽ��ȡ��������չ�֡�����
		canvas.drawCircle(min / 2, min / 2, min / 2, paint); //�Ȼ���Բ��
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));//ʹ��SRC_IN
		canvas.drawBitmap(source, 0, 0, paint);//�����ͼƬ
		return target;
	}

	/**
	 * ���ơ�Բ�Ǿ��Ρ�ͼƬ
	 */
	private Bitmap getRoundConerImage(Bitmap source) {
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		Bitmap target = Bitmap.createBitmap(mWidth, mHeight, Config.ARGB_8888);
		Canvas canvas = new Canvas(target);
		RectF rect = new RectF(0, 0, source.getWidth(), source.getHeight());//ָ��Բ�Ǿ��εı߽�
		//���ʣ�����DST_INģʽ��ȡ����չ��ǰͼ�����ı����˳��Ϊ�β��ԣ�
		canvas.drawRoundRect(rect, mRadius, mRadius, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(source, 0, 0, paint);
		return target;
	}

	/**
	 * ���ơ�����ߡ���Բ��ͼƬ
	 */
	private Bitmap getCircleImageWithStroke(Bitmap source, int min) {
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		Bitmap target = Bitmap.createBitmap(min, min, Config.ARGB_8888);
		Canvas canvas = new Canvas(target);
		//�Ȳü���һ��Բ��ͼƬ
		canvas.drawCircle(min / 2, min / 2, min / 2, paint); //�Ȼ���Բ��
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));//ʹ��SRC_IN
		canvas.drawBitmap(source, 0, 0, paint);//�����ͼƬ
		//�ٻ�һ������ɫ�Ŀ��ĵ�Ȧ
		paint.setColor(strokeColor);
		paint.setStrokeWidth(strokeWidth);
		paint.setStyle(Style.STROKE);
		//���ʣ�Ϊʲô���ǲ���DST_ATOPģʽ��ȡǰͼ�ǽ����������ͼ�������֣�
		canvas.drawCircle(min / 2, min / 2, min / 2, paint);
		return target;
	}

	/** 
	  * �����ֻ��ķֱ��ʴ� dp �ĵ�λ ת��Ϊ px(����) 
	  */
	public int dp2px(float dpValue) {
		final float scale = mContext.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
}
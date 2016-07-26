package com.bqt.myview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;

public class RoundImageView extends ImageView {
	public static final int TYPE_CIRCLE = 0;
	public static final int TYPE_ROUND = 1;

	/**ͼƬ�����ͣ�Բ��orԲ�ǣ�ֵ��ο�TYPE_CIRCLE��TYPE_ROUND*/
	private int viewType;
	/**Բ�ǵĴ�С*/
	private int roundRadiu;
	/**Բ�εİ뾶*/
	private int circleRadiu;

	/**��¼Բ�Ǿ��εı߽�*/
	private RectF mRoundRect;
	private Paint mBitmapPaint;

	public RoundImageView(Context context) {
		this(context, null);
	}

	public RoundImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mBitmapPaint = new Paint();
		mBitmapPaint.setAntiAlias(true);
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);
		roundRadiu = typedArray.getDimensionPixelSize(R.styleable.RoundImageView_borderRadius, dp2px(10));//Ĭ��Ϊ10dp
		viewType = typedArray.getInt(R.styleable.RoundImageView_type, TYPE_CIRCLE);//Ĭ��ΪԲ��
		//circleRadiu�Ĵ�С��Ҫ��onMeasure���ݲ������Ŀ��ȷ��
		typedArray.recycle();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		//��Ϊ������ֱ�Ӽ̳�ImageView(�����Ǽ̳���View)������ֱ�ӵ���super����������ȷ�õ�ʵ�ʵĿ��(��ImageViewĬ�ϵĹ���)
		if (viewType == TYPE_CIRCLE) {
			int mSize = Math.min(getMeasuredWidth(), getMeasuredHeight());
			circleRadiu = mSize / 2;
			setMeasuredDimension(mSize, mSize);//ʵ�ʵĴ�С
		}
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		Drawable drawable = getDrawable();
		if (drawable == null) return;

		//1������BitmapҪ�ü������ͣ��Լ�bitmap��view�Ŀ�ߣ�����Bitmap��Ҫ���ŵı���
		Bitmap bitmap = drawableToBitamp(drawable);
		float scale = calculateBitmapScale(bitmap);

		//2��ΪBitmapShader����һ���任����Matrix��ͨ��Matrix��Bitmap��������
		Matrix mMatrix = new Matrix();
		mMatrix.setScale(scale, scale);

		//3��ͨ��Matrix�����ź��Bitmap�ƶ���View������λ��
		float dx = getMeasuredWidth() - bitmap.getWidth() * scale;
		float dy = getMeasuredHeight() - bitmap.getHeight() * scale;
		mMatrix.postTranslate(dx / 2, dy / 2);//ע��ֻ����һ��set������������Ҫ��post��pre����

		//4��ͨ��BitmapShader��Bitmap������Ⱦ��
		BitmapShader bitmapShader = new BitmapShader(bitmap, TileMode.MIRROR, TileMode.REPEAT);//���������������ģʽ
		bitmapShader.setLocalMatrix(mMatrix);//���������Ѿ���Bitmap�Ŀ��һ�������ڡ�view�Ŀ�ߣ�������������ģʽ��û���κ�Ч���ġ�
		mBitmapPaint.setShader(bitmapShader);

		//5����󻭳�����Ҫ��ͼ��
		if (viewType == TYPE_ROUND) {
			// ��һ������������ͨ��getWidth��getHeight��ȡView�Ĵ�С
			mRoundRect = new RectF(0, 0, getWidth(), getHeight());
			canvas.drawRoundRect(mRoundRect, roundRadiu, roundRadiu, mBitmapPaint);//���ĸ�����������ƣ�Բ�Ǵ�С
		} else {
			canvas.drawCircle(circleRadiu, circleRadiu, circleRadiu, mBitmapPaint);//Բ�����꣬�뾶
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		// ������Ҳ����������ֱ��ʹ��ϵͳ�����ǲ�����׼ȷ��View�Ĵ�С
		//	if (viewType == TYPE_ROUND) mRoundRect = new RectF(0, 0, w, h);
		Log.i("bqt", "w=" + w + "--h=" + h);
	}

	/**drawableתbitmap*/
	private Bitmap drawableToBitamp(Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
			return bitmapDrawable.getBitmap();
		}
		int drawableWidth = drawable.getIntrinsicWidth();
		int drawableHeight = drawable.getIntrinsicHeight();
		Bitmap bitmap = Bitmap.createBitmap(drawableWidth, drawableHeight, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawableWidth, drawableHeight);
		drawable.draw(canvas);
		return bitmap;
	}

	/**����Bitmap��Ҫ���ŵı���*/
	private float calculateBitmapScale(Bitmap bitmap) {
		float scale = 1.0f;
		if (viewType == TYPE_CIRCLE) {
			int bSize = Math.min(bitmap.getWidth(), bitmap.getHeight());
			scale = circleRadiu * 2.0f / bSize;
		} else if (viewType == TYPE_ROUND) {
			// ���Bitmap�Ŀ���߸���view�Ŀ�߲�ƥ�䣬�������Ҫ���ŵı��������ź��Bitmap�Ŀ�ߣ�һ��Ҫ�����ڡ�view�Ŀ��
			if (bitmap.getWidth() != getWidth() || bitmap.getHeight() != getHeight()) {
				float scaleWidth = getWidth() * 1.0f / bitmap.getWidth();
				float scaleHeight = getHeight() * 1.0f / bitmap.getHeight();
				scale = Math.max(scaleWidth, scaleHeight);
			}
		}
		Log.i("bqt", "���ű���" + scale);
		return scale;
	}

	/**������Ļ��񣬽�dpֵתΪpxֵ*/
	public int dp2px(int dpVal) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, getResources().getDisplayMetrics());
	}

	//���⹫���������������ڶ�̬�޸�Բ�Ǵ�С��type
	public void setBorderRadius(int borderRadius) {
		int pxVal = dp2px(borderRadius);
		if (roundRadiu != pxVal) {
			roundRadiu = pxVal;
			invalidate();
		}
	}

	public void setType(int type) {
		if (type != TYPE_ROUND && type != TYPE_CIRCLE) {
			type = TYPE_CIRCLE;
		}
		if (viewType != type) {
			viewType = type;
			requestLayout();
		}
	}
}
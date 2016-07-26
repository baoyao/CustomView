package com.bqt.myview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

public class SwitchButton extends View implements OnClickListener, OnTouchListener {
	private Context mContext;
	private Bitmap mSwitchBottom, mSwitchThumb, mSwitchFrame, mSwitchMask;
	private float mCurrentX = 0;
	/**����״̬*/
	private boolean mSwitchOn = true;
	/**����ƶ�����*/
	private int mMoveLength;
	/**��һ�ΰ��µ���Ч����*/
	private float mLastX = 0;
	/**���Ƶ�Ŀ�������С*/
	private Rect mDest = null;
	/**��ȡԴͼƬ�Ĵ�С*/
	private Rect mSrc = null;
	/**�ƶ���ƫ����*/
	private int mDeltX = 0;
	private Paint mPaint = null;
	private OnChangeListener mListener = null;
	private boolean mFlag = false;

	public SwitchButton(Context context) {
		this(context, null);
	}

	public SwitchButton(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SwitchButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		mSwitchBottom = BitmapFactory.decodeResource(getResources(), R.drawable.switch_bottom);
		mSwitchThumb = BitmapFactory.decodeResource(getResources(), R.drawable.switch_btn_pressed);
		mSwitchFrame = BitmapFactory.decodeResource(getResources(), R.drawable.switch_frame);
		mSwitchMask = ((BitmapDrawable) getResources().getDrawable(R.drawable.switch_mask)).getBitmap();//���ַ�ʽ����

		mMoveLength = mSwitchBottom.getWidth() - mSwitchFrame.getWidth();
		mSrc = new Rect();
		mDest = new Rect(0, 0, mSwitchFrame.getWidth(), mSwitchFrame.getHeight());
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setAlpha(255);
		mPaint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		setOnClickListener(this);
		setOnTouchListener(this);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {//����Ĵ�С��д���ģ����Բ������κο�ߵ����ö�����Ч�ģ�
		setMeasuredDimension(mSwitchFrame.getWidth(), mSwitchFrame.getHeight());
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mDeltX > 0 || mDeltX == 0 && mSwitchOn) {
			if (mSrc != null) mSrc.set(mMoveLength - mDeltX, 0, mSwitchBottom.getWidth() - mDeltX, mSwitchFrame.getHeight());
		} else if (mDeltX < 0 || mDeltX == 0 && !mSwitchOn) {
			if (mSrc != null) mSrc.set(-mDeltX, 0, mSwitchFrame.getWidth() - mDeltX, mSwitchFrame.getHeight());
		}
		//������������壬�Լ��о�����˫������ư�
		int count = canvas.saveLayer(new RectF(mDest), null, Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
				| Canvas.FULL_COLOR_LAYER_SAVE_FLAG | Canvas.CLIP_TO_LAYER_SAVE_FLAG);
		canvas.drawBitmap(mSwitchBottom, mSrc, mDest, null);
		canvas.drawBitmap(mSwitchThumb, mSrc, mDest, null);
		canvas.drawBitmap(mSwitchFrame, 0, 0, null);
		canvas.drawBitmap(mSwitchMask, 0, 0, mPaint);
		canvas.restoreToCount(count);
	}

	@Override
	//onTouchListener��onTouch���ȼ���onTouchEvent�ߣ����ȴ�������onTouch����false����Ŵ���onTouchEvent����֮onTouchEvent�������ᱻ���ã�
	public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
		return false;//��������click�¼���ʵ�ֵȵȶ�����onTouchEvent������onTouch����true����Щ�¼������ᱻ����
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastX = event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			mCurrentX = event.getX();
			mDeltX = (int) (mCurrentX - mLastX);
			// ������ؿ������󻬶������߿��ع������һ�������ʱ���ǲ���Ҫ����ģ�
			if ((mSwitchOn && mDeltX < 0) || (!mSwitchOn && mDeltX > 0)) {
				mFlag = true;
				mDeltX = 0;
			}
			if (Math.abs(mDeltX) > mMoveLength) mDeltX = mDeltX > 0 ? mMoveLength : -mMoveLength;
			invalidate();
			return true;
		case MotionEvent.ACTION_UP:
			if (Math.abs(mDeltX) > 0 && Math.abs(mDeltX) < mMoveLength / 2) {
				mDeltX = 0;
				invalidate();
				return true;
			} else if (Math.abs(mDeltX) > mMoveLength / 2 && Math.abs(mDeltX) <= mMoveLength) {
				mDeltX = mDeltX > 0 ? mMoveLength : -mMoveLength;
				mSwitchOn = !mSwitchOn;
				if (mListener != null) mListener.onChange(this, mSwitchOn);
				invalidate();
				mDeltX = 0;
				return true;
			} else if (mDeltX == 0 && mFlag) {
				//��ʱ��õ����ǲ���Ҫ���д���ģ���Ϊ�Ѿ�move����
				mDeltX = 0;
				mFlag = false;
				return true;
			}
			return super.onTouchEvent(event);
		default:
			break;
		}
		invalidate();
		return super.onTouchEvent(event);
	}

	@Override
	public void onClick(View v) {
		mDeltX = mSwitchOn ? mMoveLength : -mMoveLength;
		mSwitchOn = !mSwitchOn;
		if (mListener != null) mListener.onChange(this, mSwitchOn);
		invalidate();
		mDeltX = 0;
	}

	//�Զ���Ļص�
	public interface OnChangeListener {
		public void onChange(SwitchButton sb, boolean state);
	}

	public void setOnChangeListener(OnChangeListener listener) {
		mListener = listener;
	}
}
package com.bqt.myview;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;

import com.bqt.myview.utils.LyrcUtil;
import com.bqt.myview.utils.bean.Lyrc;

/**
 * ��ʲ��ŵ��Զ���ؼ�
 */
public class LrcView extends TextView {
	private String filePath = "/a.lrc";//�ļ�·��
	private List<Lyrc> lyrcList;//�������
	private int current = 0;//��ǰ��
	private int lineSpacing = 70; //�м��
	//��ǰ���ڲ��ŵ���
	private Paint currentPaint;
	private int currentColor = Color.BLUE;//��ɫ
	private int currentSize = 55;//�����С
	private Typeface currentTypeface = Typeface.DEFAULT_BOLD;//���壬Ĭ�ϵ�����+����
	//������
	private Paint ortherPaint;
	private int ortherColor = Color.GREEN;
	private int ortherSize = 45;
	private Typeface ortherTypeface = Typeface.SERIF;//һ����������
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			invalidate();
		}
	};

	public LrcView(Context context) {
		this(context, null);
	}

	public LrcView(Context context, AttributeSet attrs) {
		super(context, attrs);
		lyrcList = LyrcUtil.readLRC(new File(Environment.getExternalStorageDirectory().getPath() + filePath));
		currentPaint = new Paint();
		currentPaint.setColor(currentColor);
		currentPaint.setTextSize(currentSize);
		currentPaint.setTextAlign(Align.CENTER);
		currentPaint.setTypeface(currentTypeface);
		ortherPaint = new Paint();
		ortherPaint.setColor(ortherColor);
		ortherPaint.setTextSize(ortherSize);
		ortherPaint.setTextAlign(Align.CENTER);
		ortherPaint.setTypeface(ortherTypeface);
	}

	public void setLrcType(String filePath, int lineSpacing, int currentColor, int currentSize, int ortherColor, int ortherSize) {
		lyrcList = LyrcUtil.readLRC(new File(filePath));
		this.lineSpacing = lineSpacing;

		currentPaint.setColor(currentColor);
		currentPaint.setTextSize(currentSize);
		ortherPaint.setColor(ortherColor);
		ortherPaint.setTextSize(ortherSize);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (current < lyrcList.size()) {
			if (lyrcList != null && lyrcList.size() > 0) {
				Lyrc lyrc = null;
				//��ǰ�������
				for (int i = current - 1; i >= 0; i--) {
					lyrc = lyrcList.get(i);
					canvas.drawText(lyrc.lrcString, getWidth() / 2, getHeight() / 2 + lineSpacing * (i - current), ortherPaint);
				}
				//����ǰ������
				lyrc = lyrcList.get(current);
				canvas.drawText(lyrc.lrcString, getWidth() / 2, getHeight() / 2, currentPaint);
				//�����������
				for (int i = current + 1; i < lyrcList.size(); i++) {
					lyrc = lyrcList.get(i);
					canvas.drawText(lyrc.lrcString, getWidth() / 2, getHeight() / 2 + lineSpacing * (i - current), ortherPaint);
				}
				//����handler��ǰ�е�ʱ��
				lyrc = lyrcList.get(current);
				handler.sendEmptyMessageDelayed(10, lyrc.sleepTime);
				//��ǰ��+1
				current++;
			} else canvas.drawText("δ�ҵ����", getWidth() / 2, getHeight() / 2, currentPaint);
		}
		super.onDraw(canvas);
	}
}
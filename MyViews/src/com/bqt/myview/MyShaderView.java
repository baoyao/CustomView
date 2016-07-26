package com.bqt.myview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.SweepGradient;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;

public class MyShaderView extends View {
	// Shader��Ⱦ����ר��������Ⱦͼ���Լ�һЩ����ͼ��
	private BitmapShader bitmapShader; //bitmap��Ⱦ������Ҫ������Ⱦͼ��
	private LinearGradient linearGradient; //������Ⱦ
	private RadialGradient radialGradient; //������Ⱦ
	private SweepGradient sweepGradient; //ɨ�轥�䣬Χ��һ�����ĵ�ɨ�轥������Ӱ�������״�ɨ�裬�����ݶ���Ⱦ
	private ComposeShader composeShader; //�����Ⱦ�������Ժ��������������������ʹ��
	private int width;
	private int height;
	private Paint paint;

	public MyShaderView(Context context) {
		super(context);

		Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.icon)).getBitmap();
		width = bitmap.getWidth();
		height = bitmap.getHeight();
		//  tileX tileY��The tiling mode for x/y to draw the bitmap in.   ��λͼ�� X/Y ���� �߹�/��ש/��ש ģʽ 
		//   CLAMP  �������Ⱦ������ԭʼ�߽緶Χ���Ḵ�Ʒ�Χ�ڡ���Ե��Ⱦɫ
		//   REPEA   �����������ġ��ظ�����Ⱦ��ͼƬ��ƽ��
		//   MIRROR�������������ظ���Ⱦ��ͼƬ�������REPEAT �ظ���ʽ��һ���������ԡ����񡿷�ʽƽ��
		bitmapShader = new BitmapShader(bitmap, TileMode.MIRROR, TileMode.MIRROR);

		// ǰ�ĸ�����:  ������ ����/�յ� ���� x/y λ�ã� colors:  ������ɫ���飻
		// positions:���Ҳ��һ�����飬����ָ����ɫ��������λ�ã����Ϊnull �����¶��߾��ȷֲ�
		linearGradient = new LinearGradient(50, 50, 1000, 1000, new int[] { Color.RED, Color.GREEN, Color.BLUE, Color.WHITE }, null, TileMode.REPEAT);

		// centerX  centerY  �뾶   ������ɫ����   position     ƽ�̷�ʽ  
		radialGradient = new RadialGradient(50, 200, 50, new int[] { Color.WHITE, Color.YELLOW, Color.GREEN, Color.RED }, null, TileMode.REPEAT);

		//�����Ⱦģʽ��16��  Shader shaderA, Shader shaderB, Mode mode
		composeShader = new ComposeShader(bitmapShader, linearGradient, PorterDuff.Mode.DARKEN);

		//�ݶ���Ⱦ��  float cx, float cy, int[] colors, float[] positions
		sweepGradient = new SweepGradient(30, 30, new int[] { Color.GREEN, Color.RED, Color.BLUE, Color.WHITE }, null);
		paint = new Paint();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawColor(Color.WHITE); //����ɫ  

		//��Bitmap��ȡΪԲ�Ρ���Բ��
		paint.setShader(bitmapShader);
		canvas.drawOval(new RectF(0, 0, width, height), paint);
		canvas.drawOval(new RectF(150, 0, 150 + width, height), paint);
		canvas.drawOval(new RectF(300, 0, 300 + 100 + width, height), paint);

		//�������Խ���ľ���  
		paint.setShader(linearGradient);
		canvas.drawRect(0, 200, 400, 400, paint);

		//���ƻ��ν����Բ
		paint.setShader(radialGradient);
		canvas.drawCircle(50, 500, 50, paint);
		canvas.drawCircle(50 + 100, 500, 50, paint);
		canvas.drawCircle(50 + 200, 500, 50, paint);
		canvas.drawCircle(50 + 300, 500, 50, paint);

		//���ƻ�Ͻ���(�����뻷�λ��)�ľ���  
		paint.setShader(composeShader);
		canvas.drawRect(0, 600, 600, 900, paint);

		//�������ν���ľ���  
		paint.setShader(sweepGradient);
		canvas.drawRect(0, 950, 400, 1100, paint);
	}
}
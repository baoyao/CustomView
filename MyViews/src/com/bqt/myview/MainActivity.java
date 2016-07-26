package com.bqt.myview;

import android.app.Activity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_custom_imageview);
//		setContentView(R.layout.activity_custom_progressbar);
		setContentView(R.layout.activity_flowlayout);//default
//		setContentView(R.layout.activity_mybutton);

		FlowLayout flow_layout = (FlowLayout) findViewById(R.id.flow_layout);
		//һ��Ҫע�⣬�����Զ����FlowLayout��ʹ�õ���MarginLayoutParams����������Ҳֻ����MarginLayoutParams����Ȼ��ClassCastException
		MarginLayoutParams marginLayoutParams = new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		int margins = (int) (2 * getResources().getDisplayMetrics().density + 0.5f);
		marginLayoutParams.setMargins(margins, margins, margins, margins);
		TextView tv1 = new TextView(new ContextThemeWrapper(this, R.style.text_style3), null, 0);//���Ǵ���������style�ķ�����
		TextView tv2 = new TextView(new ContextThemeWrapper(this, R.style.text_style2), null, 0);
		TextView tv3 = new TextView(new ContextThemeWrapper(this, R.style.text_style1), null, 0);
		TextView tv4 = new TextView(new ContextThemeWrapper(this, R.style.text_style2), null, 0);
		tv1.setText("���������View");
		tv2.setText("������style");
		tv3.setText("������margins");
		tv4.setText("���ͣ�http://www.cnblogs.com/baiqiantao/�����TextView�����ر𳤻�������Ч��");
		tv1.setLayoutParams(marginLayoutParams);
		tv2.setLayoutParams(marginLayoutParams);
		tv3.setLayoutParams(marginLayoutParams);
		tv4.setLayoutParams(marginLayoutParams);
		flow_layout.addView(tv1);
		flow_layout.addView(tv2);
		flow_layout.addView(tv3);
		flow_layout.addView(tv4);
	}
}
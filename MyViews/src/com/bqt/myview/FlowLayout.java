package com.bqt.myview;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class FlowLayout extends ViewGroup {
	/**�洢���е�View�����м�¼*/
	private List<List<View>> mAllViews = new ArrayList<List<View>>();
	/**��¼ÿһ�е����߶�*/
	private List<Integer> mLineHeight = new ArrayList<Integer>();

	/**���ֵĿ��*/
	private int width = 0, height = 0;
	/**ÿһ�еĿ�ȣ�width����ȡ�������Ŀ��*/
	private int lineWidth = 0;
	/**ÿһ�еĸ߶ȣ��ۼ���height*/
	private int lineHeight = 0;
	/**�����е��ӿؼ�*/
	private View child;
	/**�������ӿؼ����õ�LayoutParam*/
	private MarginLayoutParams layoutParams;

	public FlowLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected LayoutParams generateLayoutParams(LayoutParams p) {
		return new MarginLayoutParams(p);
	}

	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new MarginLayoutParams(getContext(), attrs);
	}

	@Override
	protected LayoutParams generateDefaultLayoutParams() {
		return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		//���������ӿؼ������Լ��Ŀ�͸�
		for (int i = 0; i < getChildCount(); i++) {
			child = getChildAt(i);
			// ��ϵͳȥ������ǰchild�Ŀ��
			measureChild(child, widthMeasureSpec, heightMeasureSpec);
			// ��ȡ��ǰchildʵ��ռ�ݵĿ��
			layoutParams = (MarginLayoutParams) child.getLayoutParams();
			int childWidth = child.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
			int childHeight = child.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
			//������뵱ǰchild�󳬳���������ȣ���Ŀǰ����ȸ�width���ۼ�height��Ȼ��������
			if (lineWidth + childWidth > MeasureSpec.getSize(widthMeasureSpec)) {
				width = Math.max(lineWidth, childWidth);// �Աȵõ������
				// �������У�����ǰ�еĿ����Ϊ��ǰchild�Ŀ��
				lineWidth = childWidth;
				lineHeight = childHeight;
				// �ۼ��и�
				height += lineHeight;
			} else {
				// ���򣨲����У��ۼ��п�lineHeightȡ���߶�
				lineWidth += childWidth;
				lineHeight = Math.max(lineHeight, childHeight);
			}
			// ��������һ�����򽫵�ǰ��¼������Ⱥ͵�ǰlineWidth���Ƚϣ����ۼ��и�
			if (i == getChildCount() - 1) {
				width = Math.max(width, lineWidth);
				height += lineHeight;
			}
		}
		//����ǲ��������õ���wrap_content������Ϊ���Ǽ����ֵ������ֱ������Ϊ������������ֵ��
		width = (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) ? MeasureSpec.getSize(widthMeasureSpec) : width;
		height = (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) ? MeasureSpec.getSize(heightMeasureSpec) : height;
		setMeasuredDimension(width, height);
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		mAllViews.clear();
		mLineHeight.clear();
		lineWidth = 0;
		lineHeight = 0;
		// �洢ÿһ�����е�childView
		List<View> lineViews = new ArrayList<View>();
		for (int i = 0; i < getChildCount(); i++) {
			child = getChildAt(i);
			layoutParams = (MarginLayoutParams) child.getLayoutParams();
			int childWidth = child.getMeasuredWidth();
			int childHeight = child.getMeasuredHeight();
			// �����Ҫ����
			if (childWidth + layoutParams.leftMargin + layoutParams.rightMargin + lineWidth > width) {
				// ��¼��һ�����е�View�е����߶�
				mLineHeight.add(lineHeight);
				// ����ǰ�е�childView���棬Ȼ�����µ�ArrayList������һ�е�childView
				mAllViews.add(lineViews);
				lineWidth = 0;// �����п�
				lineViews = new ArrayList<View>();
			}
			//�������Ҫ���У����ۼ�
			lineWidth += childWidth + layoutParams.leftMargin + layoutParams.rightMargin;
			lineHeight = Math.max(lineHeight, childHeight + layoutParams.topMargin + layoutParams.bottomMargin);
			lineViews.add(child);
		}
		// ��¼���һ��
		mLineHeight.add(lineHeight);
		mAllViews.add(lineViews);
		//��¼��ǰchild���ǰһ��child������λ��
		int left = 0;
		int top = 0;
		// һ��һ�еı���
		for (int i = 0; i < mAllViews.size(); i++) {
			// ����ÿһ��
			lineViews = mAllViews.get(i);
			for (int j = 0; j < lineViews.size(); j++) {
				child = lineViews.get(j);
				if (child.getVisibility() == View.GONE) continue;
				layoutParams = (MarginLayoutParams) child.getLayoutParams();
				//����child������
				int leftPosition = left + layoutParams.leftMargin;
				int topPosition = top + layoutParams.topMargin;
				int rightPosition = leftPosition + child.getMeasuredWidth();
				int bottomPosition = topPosition + child.getMeasuredHeight();
				//��child���в���
				child.layout(leftPosition, topPosition, rightPosition, bottomPosition);
				//���λ������
				left = rightPosition + layoutParams.rightMargin;
			}
			//���λ�ô������ͷ��ʼ��������
			left = 0;
			top += mLineHeight.get(i);
		}
	}
}
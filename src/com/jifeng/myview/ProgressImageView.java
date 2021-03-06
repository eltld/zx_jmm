/*
 * @author Jason
 * @email jason@gfeng.com.cn
 * @company 机锋科技
 * @version 1.0
 * @website:www.dnet168.com
 */
package com.jifeng.myview;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @description 帧动画播放的imageviews
 */
public class ProgressImageView extends ImageView {

	public ProgressImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public ProgressImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
		Drawable draw = this.getDrawable();
		if (draw instanceof AnimationDrawable) {
			((AnimationDrawable) draw).start();
		}
	}
}

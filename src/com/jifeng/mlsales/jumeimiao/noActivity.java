package com.jifeng.mlsales.jumeimiao;

import com.jifeng.mlsales.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class noActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		initData();
		findView();
		register();
	}

	// ���ҿؼ�
	private void findView() {

	}

	// ע���¼�
	private void register() {

	}

	/*
	 * ��ʼ������
	 */
	private void initData() {

	}

	// //xmlע�����¼���ʵ��
	public void doclick(View view) {
		switch (view.getId()) {
		case R.id.setting_back:// ����
			finish();
			break;
		default:
			break;
		}
	}
}

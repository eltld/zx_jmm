package com.jifeng.mlsales.jumeimiao;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.sharesdk.framework.ShareSDK;

import com.jifeng.image.ImageLoader;
import com.jifeng.mlsales.R;
import com.jifeng.myview.LoadingDialog;
import com.jifeng.tools.MyTools;
import com.jifeng.url.AllStaticMessage;
import com.jifeng.url.HttpUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DaiShouHuoActivity extends Activity {
	private Intent mIntent;
	private GridView mGridView;
	private MyGridViewAdapter mAdapter;
	private List<JSONObject> mData;
	private LoadingDialog dialog;

	private ImageView iv_no;
	private TextView tv_no;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.activity_daishouhuo);
		} catch (OutOfMemoryError e) {
			com.nostra13.universalimageloader.core.ImageLoader.getInstance()
					.clearMemoryCache();
		}
		dialog = new LoadingDialog(this);
		dialog.loading();
		mData = new ArrayList<JSONObject>();
		initData();
		findView();
		register();

		getData("1");

	}

	@Override
	protected void onDestroy() {
		ShareSDK.stopSDK(this);
		setContentView(R.layout.view_null);
		super.onDestroy();
		dialog = null;
		mIntent = null;
		mGridView = null;
		mAdapter = null;
		mData = null;
		this.finish();
		System.gc();
	}

	// 查找控件
	private void findView() {
		mGridView = (GridView) findViewById(R.id.daishouhuo_gridview);
		mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));// 设置点击是背景透明

		iv_no = (ImageView) findViewById(R.id.iv_no);
		tv_no = (TextView) findViewById(R.id.tv_no);
	}

	// 注册事件
	private void register() {

	}

	/*
	 * 初始化数据
	 */
	private void initData() {

	}

	// //xml注册点击事件的实现
	public void doclick(View view) {
		switch (view.getId()) {
		case R.id.daishouhuo_back:// 返回
			finish();
			break;
		default:
			break;
		}
	}

	@SuppressLint("ShowToast")
	private void getData(String page) {
		String url = AllStaticMessage.URL_Order_List + AllStaticMessage.User_Id
				+ "&orderState=3" + "&page=" + page;
		HttpUtil.get(url, DaiShouHuoActivity.this, dialog,
				new JsonHttpResponseHandler() {
					@SuppressLint("ShowToast")
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						super.onSuccess(statusCode, headers, response);
						try {
							if (response.getString("Status").equals("true")) {
								JSONArray array = response
										.getJSONArray("Results");
								if (array.length() > 0) {
									for (int i = 0; i < array.length(); i++) {
										mData.add(array.getJSONObject(i));
									}
									mAdapter = new MyGridViewAdapter(mData);
									mGridView.setAdapter(mAdapter);
								} else {
									tv_no.setText("暂无待收货订单");
									mGridView.setVisibility(View.GONE);
									iv_no.setVisibility(View.VISIBLE);
									tv_no.setVisibility(View.VISIBLE);
								}
							} else {
								tv_no.setText("暂无待收货订单");
								mGridView.setVisibility(View.GONE);
								iv_no.setVisibility(View.VISIBLE);
								tv_no.setVisibility(View.VISIBLE);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (dialog != null) {
							dialog.stop();
						}

					}

					@Override
					public void onStart() {
						super.onStart();
						// 请求开始
					}

					@Override
					public void onFinish() {
						super.onFinish();
						// 请求结束
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// TODO Auto-generated method stub
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
						// 错误返回JSONObject
						if (dialog != null) {
							dialog.stop();
						}
					}
				});
	}

	private class MyGridViewAdapter extends BaseAdapter {
		AppItem appItem;
		List<JSONObject> mListData;
		ImageLoader imageLoader;

		public MyGridViewAdapter(List<JSONObject> listData) {
			mListData = new ArrayList<JSONObject>();
			this.mListData = listData;
			imageLoader = new ImageLoader(DaiShouHuoActivity.this, "");
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mListData.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				View v = LayoutInflater.from(DaiShouHuoActivity.this).inflate(
						R.layout.item_daishouhuo_listview, null);
				appItem = new AppItem();
				appItem.AppText_time = (TextView) v
						.findViewById(R.id.item_order_time);
				appItem.AppText_id = (TextView) v
						.findViewById(R.id.item_order_id);
				appItem.AppText_status = (TextView) v
						.findViewById(R.id.item_order_status);
				appItem.AppText_price = (TextView) v
						.findViewById(R.id.item_order_price);
				appItem.AppImg = (ImageView) v
						.findViewById(R.id.item_order_img);
				// appItem.AppBtn_WuLiu = (Button)
				// v.findViewById(R.id.btn_wuliu);
				// appItem.AppBtn_QueRen = (Button)
				// v.findViewById(R.id.btn_shouhuo);
				v.setTag(appItem);
				convertView = v;
			} else {
				appItem = (AppItem) convertView.getTag();
			}
			try {
				appItem.AppText_time.setText(mListData.get(position)
						.getString("AddTime").toString());
				appItem.AppText_id.setText(mListData.get(position)
						.getString("OrderId").toString());
				appItem.AppText_price
						.setText("￥"
								+ mListData.get(position).getString("total")
										.toString());
				String imgUrl = AllStaticMessage.URL_GBase + "/UsersData/"
						+ mData.get(position).getString("Account").toString()
						+ "/" + mData.get(position).getString("Img").toString()
						+ "/5.jpg";
				imageLoader.DisplayImage(imgUrl, appItem.AppImg);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// appItem.AppBtn_WuLiu.setOnClickListener(new
			// onItemClick(appItem,mData.get(position),"wuliu"));
			// appItem.AppBtn_QueRen.setOnClickListener(new
			// onItemClick(appItem,mData.get(position),"shouhuo"));

			/* edit by jason */

			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// mIntent=new
					// Intent(DaiShouHuoActivity.this,DaiShouHuo_Detail_Activity.class);
					// startActivity(mIntent);
					try {
						mIntent = new Intent(DaiShouHuoActivity.this,
								OrderDetailActivity.class);
						mIntent.putExtra("id",
								mData.get(position).getString("OrderId")
										.toString());
						startActivity(mIntent);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			return convertView;
		}

	}

	class AppItem {
		TextView AppText_time;
		TextView AppText_id;
		TextView AppText_status;
		TextView AppText_price;
		ImageView AppImg;
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}

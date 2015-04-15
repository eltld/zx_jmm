package com.jifeng.mlsales.jumeimiao;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.sharesdk.framework.ShareSDK;

import com.jifeng.mlsales.R;
import com.jifeng.myview.LoadingDialog;
import com.jifeng.tools.MyTools;
import com.jifeng.url.AllStaticMessage;
import com.jifeng.url.HttpUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DaiZhiFuActivity extends Activity {
	private Intent mIntent;
	private GridView mGridView;
	private MyGridViewAdapter mAdapter;
	private TextView mText_Title;
	String orderState;// 1 ������ 2������
	private List<JSONObject> mData;
	private LoadingDialog dialog;

	private String no = "";
	private ImageView iv_no;
	private TextView tv_no;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_daizhifu);
		dialog = new LoadingDialog(this);
		dialog.loading();
		mData = new ArrayList<JSONObject>();
		initData();
		findView();
		register();

	}

	@Override
	protected void onDestroy() {
		ShareSDK.stopSDK(this);
		super.onDestroy();
		// setContentView(R.layout.view_null);
		//
		// dialog = null;
		// mText_Title = null;
		// mIntent = null;
		// mGridView = null;
		// mAdapter = null;
		// mData = null;
		// orderState = null;
		// this.finish();
		// System.gc();
	}

	// ���ҿؼ�
	private void findView() {
		mGridView = (GridView) findViewById(R.id.daizhifu_gridview);
		mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));// ���õ���Ǳ���͸��
		mText_Title = (TextView) findViewById(R.id.textview_title);
		iv_no = (ImageView) findViewById(R.id.iv_no);
		tv_no = (TextView) findViewById(R.id.tv_no);

		mText_Title.setText(getIntent().getStringExtra("title").toString());
		if (getIntent().getStringExtra("title").toString().equals("�������")) {
			orderState = "1";
			no = "���޴������";
		} else {
			orderState = "2";
			no = "���޴���������";
		}
	}

	// ע���¼�
	private void register() {

	}

	// ����ʵ��

	/*
	 * ��ʼ������
	 */
	private void initData() {

	}

	// //xmlע�����¼���ʵ��
	public void doclick(View view) {
		switch (view.getId()) {
		case R.id.daizhifu_back:// ����
			finish();
			break;
		default:
			break;
		}
	}

	private void getData(String page) {
		String url = AllStaticMessage.URL_Order_List + AllStaticMessage.User_Id
				+ "&orderState=" + orderState + "&page=" + page;
		HttpUtil.get(url, DaiZhiFuActivity.this, dialog,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						// TODO Auto-generated method stub
						super.onSuccess(statusCode, headers, response);
						// �ɹ�����JSONObject
						try {
							if (response.getString("Status").equals("true")) {
								JSONArray array = response
										.getJSONArray("Results");
								// Log.i("11111", array.toString());
								if (mData != null) {
									mData.clear();
								} else {
									mData = new ArrayList<JSONObject>();
								}
								if (array.length() > 0) {
									for (int i = 0; i < array.length(); i++) {
										mData.add(array.getJSONObject(i));
									}
									mAdapter = new MyGridViewAdapter(mData);
									mGridView.setAdapter(mAdapter);
								} else {
									tv_no.setText(no);
									mGridView.setVisibility(View.GONE);
									iv_no.setVisibility(View.VISIBLE);
									tv_no.setVisibility(View.VISIBLE);
								}
							} else {
								tv_no.setText(no);
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
						// ����ʼ
					}

					@Override
					public void onFinish() {
						super.onFinish();
						// �������
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// TODO Auto-generated method stub
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
						// ���󷵻�JSONObject
						if (dialog != null) {
							dialog.stop();
						}
					}
				});
	}

	private class MyGridViewAdapter extends BaseAdapter {
		AppItem appItem;
		List<JSONObject> mListData;

		DisplayImageOptions options;

		public MyGridViewAdapter(List<JSONObject> listData) {
			mListData = new ArrayList<JSONObject>();
			this.mListData = listData;
			// imageLoader = new ImageLoader(DaiZhiFuActivity.this, "");
			// MyTools.initImageLoader(DaiZhiFuActivity.this);
			options = MyTools.createOptions(R.drawable.img);
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
				View v = LayoutInflater.from(DaiZhiFuActivity.this).inflate(
						R.layout.item_daizhifu_gridview, null);

				appItem = new AppItem();
				appItem.AppText_time = (TextView) v
						.findViewById(R.id.item_order_id);
				appItem.AppText_status = (TextView) v
						.findViewById(R.id.item_order_status);
				appItem.AppText_price = (TextView) v
						.findViewById(R.id.item_order_price);
				appItem.AppImg = (ImageView) v
						.findViewById(R.id.item_order_img);
				appItem.AppImg_zhifu = (ImageView) v
						.findViewById(R.id.img_zhifu);
				appItem.AppmLayout = (RelativeLayout) v
						.findViewById(R.id.rel_time_zhifu);
				v.setTag(appItem);
				convertView = v;
			} else {
				appItem = (AppItem) convertView.getTag();
			}
			// appItem.mAppIcon.setImageDrawable(getResources().getDrawable(img[position]));
			// appItem.AppImg.setImageResource(imgId[position]);
			// appItem.AppText.setText(titles[position]);
			// convertView.setBackgroundResource(R.drawable.doclick);
			try {
				appItem.AppText_time.setText(mListData.get(position)
						.get("AddTime").toString());
				appItem.AppText_price.setText("��"
						+ mListData.get(position).get("total").toString());
				if (orderState.equals("1")) {
					appItem.AppText_status.setText("������");
					appItem.AppmLayout.setVisibility(View.VISIBLE);
					appItem.AppImg_zhifu.setOnClickListener(new onItemClick(
							appItem, mListData.get(position)));
				} else {
					appItem.AppText_status.setText("������");
					appItem.AppmLayout.setVisibility(View.GONE);
				}
				// ����ͼƬ
				String imgUrl = AllStaticMessage.URL_GBase + "/UsersData/"
						+ mData.get(position).getString("Account").toString()
						+ "/" + mData.get(position).getString("Img").toString()
						+ "/5.jpg";
				// imageLoader.DisplayImage(imgUrl, appItem.AppImg);
				ImageLoader.getInstance().displayImage(imgUrl, appItem.AppImg,
						options);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					try {
						mIntent = new Intent(DaiZhiFuActivity.this,
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
		RelativeLayout AppmLayout;
		TextView AppText_time;
		TextView AppText_status;
		TextView AppText_price;
		ImageView AppImg, AppImg_zhifu;
	}

	class onItemClick implements OnClickListener {
		AppItem appItem;
		JSONObject jsonObject;

		public onItemClick(AppItem appIte, JSONObject paywa) {
			this.appItem = appIte;
			this.jsonObject = paywa;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			// ����֧��
			try {
				mIntent = new Intent(DaiZhiFuActivity.this, MyPayActivity.class);
				mIntent.putExtra("allprice", jsonObject.getString("total"));
				if (jsonObject.getString("PayWay").contains("֧����")) {
					mIntent.putExtra("payway", "zfb");
				} else {
					mIntent.putExtra("payway", "wx");
				}
				mIntent.putExtra("orderid", jsonObject.getString("OrderId")
						.toString());
				startActivity(mIntent);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		getData("1");
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}

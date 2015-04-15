package com.jifeng.mlsales.jumeimiao;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.sharesdk.framework.ShareSDK;

import com.jifeng.mlsales.R;
import com.jifeng.myview.LoadingDialog;
import com.jifeng.tools.AutoLoadListener;
import com.jifeng.tools.MyTools;
import com.jifeng.tools.AutoLoadListener.AutoLoadCallBack;
import com.jifeng.url.AllStaticMessage;
import com.jifeng.url.HttpUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

public class OrderActivity extends Activity {
	private Intent mIntent;
	private GridView mGridView;
	private MyGridViewAdapter mAdapter;
	private List<JSONObject> mListData;
	private LoadingDialog dialog;
	private LinearLayout mLayout;// ���ظ���
	private int pageno = 1;
	private String AllPage = "1";

	private ImageView iv_no;
	private TextView tv_no;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0x01:
				getData(String.valueOf(pageno));
				break;

			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.activity_order);
		} catch (OutOfMemoryError e) {
			com.nostra13.universalimageloader.core.ImageLoader.getInstance()
					.clearMemoryCache();
		}
		dialog = new LoadingDialog(this);
		mListData = new ArrayList<JSONObject>();

		findView();

		handler.sendEmptyMessage(0x01);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		ShareSDK.stopSDK(this);
		super.onDestroy();
		// setContentView(R.layout.view_null);
		//
		// mIntent = null;
		// dialog = null;
		// mGridView = null;
		// mAdapter = null;
		// mListData = null;
		// mLayout = null;
		// AllPage = null;
		// this.finish();
		// System.gc();
	}

	// ���ҿؼ�
	private void findView() {
		mGridView = (GridView) findViewById(R.id.order_gridview);
		mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));// ���õ���Ǳ���͸��
		mLayout = (LinearLayout) findViewById(R.id.layout_loading);

		iv_no = (ImageView) findViewById(R.id.iv_no);
		tv_no = (TextView) findViewById(R.id.tv_no);
	}

	// //xmlע�����¼���ʵ��
	public void doclick(View view) {
		switch (view.getId()) {
		case R.id.order_back:// ����
			finish();
			break;
		default:
			break;
		}
	}

	// @Override
	// public void onWindowFocusChanged(boolean hasFocus) {
	// // TODO Auto-generated method stub
	// super.onWindowFocusChanged(hasFocus);
	//
	// }
	@Override
	protected void onResume() {
		super.onResume();
		if (AllStaticMessage.OrderFormFlag) {
			AllStaticMessage.OrderFormFlag = false;
			if (mListData != null) {
				mListData.clear();
			}
			if (mAdapter != null) {
				mAdapter.notifyDataSetChanged();
			}
			pageno = 1;
			getData(String.valueOf(pageno));
		}
		MobclickAgent.onResume(this);
	}

	// ��ȡ�����б�
	private void getData(String page) {
		if (mLayout.getVisibility() == View.GONE) {
			dialog.loading();
		}
		String url = AllStaticMessage.URL_Order_List + AllStaticMessage.User_Id
				+ "&orderState=0" + "&page=" + page;
		HttpUtil.get(url, OrderActivity.this, dialog,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						super.onSuccess(statusCode, headers, response);
						// �ɹ�����JSONObject
						try {
							if (response.getString("Status").equals("true")) {
								JSONArray mArray = response
										.getJSONArray("Results");
								if (mArray.length() > 0) {
									for (int i = 0; i < mArray.length(); i++) {
										mListData.add(mArray.getJSONObject(i));
									}
									if (mAdapter == null) {
										mAdapter = new MyGridViewAdapter(
												mListData);
										mGridView.setAdapter(mAdapter);
										AutoLoadListener autoLoadListener = new AutoLoadListener(
												callBack);
										if (mListData.size() > 0) {
											mGridView
													.setOnScrollListener(autoLoadListener);
										}
									} else if (mAdapter != null) {
										mAdapter.notifyDataSetChanged();
										mLayout.setVisibility(View.GONE);
									}
								} else {
									tv_no.setText("���޶���");
									mGridView.setVisibility(View.GONE);
									iv_no.setVisibility(View.VISIBLE);
									tv_no.setVisibility(View.VISIBLE);
								}
							} else {
								tv_no.setText("���޶���");
								mGridView.setVisibility(View.GONE);
								iv_no.setVisibility(View.VISIBLE);
								tv_no.setVisibility(View.VISIBLE);

							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						mLayout.setVisibility(View.GONE);
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
						if (mLayout != null) {
							mLayout.setVisibility(View.GONE);
						}
						if (dialog != null) {
							dialog.stop();
						}
					}
				});
	}

	AutoLoadCallBack callBack = new AutoLoadCallBack() {
		public void execute(String url) {
			if (mLayout.getVisibility() == View.GONE) {
				if (pageno < Integer.parseInt(AllPage)) {
					pageno++;
					mLayout.setVisibility(View.VISIBLE);
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							getData(String.valueOf(pageno));
							mAdapter.notifyDataSetChanged();
						}
					}, 1500);

				} else {
					Toast.makeText(OrderActivity.this, "����ȫ���������", 100).show();
				}
			}

		}
	};

	private class MyGridViewAdapter extends BaseAdapter {
		AppItem appItem;
		List<JSONObject> mData = new ArrayList<JSONObject>();
		DisplayImageOptions options;

		public MyGridViewAdapter(List<JSONObject> array) {
			this.mData = array;
			// imageLoader=new ImageLoader(OrderActivity.this,"");
			options = MyTools.createOptions(R.drawable.img);
			// MyTools.initImageLoader(OrderActivity.this);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mData.size();
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
				View v = LayoutInflater.from(OrderActivity.this).inflate(
						R.layout.item_order_gridview, null);

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
				appItem.AppLayout = (LinearLayout) v
						.findViewById(R.id.liner_order_list);
				// appItem.AppBtn_ok=(Button) v.findViewById(R.id.btn_shouhuo);
				// appItem.AppBtn_wuliu=(Button) v.findViewById(R.id.btn_wuliu);
				appItem.AppBtn_lijizhifu = (Button) v
						.findViewById(R.id.btn_lijizhifu);
				v.setTag(appItem);
				convertView = v;
			} else {
				appItem = (AppItem) convertView.getTag();
			}
			try {
				appItem.AppText_time.setText(mData.get(position)
						.getString("AddTime").toString());
				appItem.AppText_id.setText(mData.get(position)
						.getString("OrderId").toString());
				appItem.AppLayout.setVisibility(View.VISIBLE);
				// appItem.AppBtn_ok.setVisibility(View.GONE);
				// appItem.AppBtn_wuliu.setVisibility(View.GONE);
				appItem.AppBtn_lijizhifu.setVisibility(View.GONE);

				switch (Integer.parseInt(mData.get(position)
						.getString("Status").toString())) {
				case 0:
					appItem.AppBtn_lijizhifu.setVisibility(View.VISIBLE);
					appItem.AppBtn_lijizhifu
							.setOnClickListener(new onItemClick(appItem, mData
									.get(position)));
					break;
				case 1:

				case 2:
					// appItem.AppBtn_ok.setVisibility(View.VISIBLE);
					// appItem.AppBtn_wuliu.setVisibility(View.VISIBLE);
					break;
				case 3:
				case 4:
				case 5:
					appItem.AppLayout.setVisibility(View.GONE);
					break;
				default:
					break;
				}
				if (mData.get(position).getString("Status").toString()
						.equals("1")) {
					appItem.AppText_status.setText("֧��"
							+ AllStaticMessage.zhifu[Integer.parseInt(mData
									.get(position).getString("Status")
									.toString())]);
				} else {
					appItem.AppText_status
							.setText(AllStaticMessage.zhifu[Integer
									.parseInt(mData.get(position)
											.getString("Status").toString())]);
				}

				appItem.AppText_price.setText("��"
						+ mData.get(position).getString("total").toString());
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
						mIntent = new Intent(OrderActivity.this,
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
		LinearLayout AppLayout;
		Button AppBtn_lijizhifu;// AppBtn_ok,AppBtn_wuliu,
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
				mIntent = new Intent(OrderActivity.this, MyPayActivity.class);
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
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}

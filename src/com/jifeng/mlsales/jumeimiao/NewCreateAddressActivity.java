package com.jifeng.mlsales.jumeimiao;

import java.util.ArrayList;
import java.util.List;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.sharesdk.framework.ShareSDK;

import com.jifeng.city.MyListItem;
import com.jifeng.mlsales.R;
import com.jifeng.myview.LoadingDialog;
import com.jifeng.tools.MyTools;
import com.jifeng.url.AllStaticMessage;
import com.jifeng.url.HttpUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class NewCreateAddressActivity extends BaseActivity implements
		OnWheelChangedListener {
	private EditText mText_Name, mText_Phone, mText_Detail;
	// private Button mBtn_Ok;//mBtn_Time,
	private ImageView mImageView;
	// private String[] time;
	private String province = null, city = null, district = null, name = null,
			phonenum = null, detail = null, isDefault = "0", sanjiId = "";// addtime
																			// =
																			// null,
	private int themeCheckedId = 0;
	private boolean imgFlag = true;
	private LoadingDialog dialog;
	private String id;
	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;
	private Button mBtnConfirm;
	String[] provicedata, citydata, districtdata;
	List<MyListItem> listProvice, listCity, listDistrict;
	private Button mBtn_Provice, mBtn_City, mBtn_District;
	View mView;
	AlertDialog alertDialog;
	boolean firstIn = true;

	private PopupWindow popupWindow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_address);
		// time = getResources().getStringArray(R.array.shouhuoTime);
		dialog = new LoadingDialog(this);
		findView();
		initData();
	}

	// ���ҿؼ�
	private void findView() {
		mText_Name = (EditText) findViewById(R.id.create_address_name);
		mText_Phone = (EditText) findViewById(R.id.create_address_phone);
		mText_Detail = (EditText) findViewById(R.id.create_address_detail);
		// mBtn_Time = (Button) findViewById(R.id.create_address_time);
		// mBtn_Ok = (Button) findViewById(R.id.create_address_ok);
		mImageView = (ImageView) findViewById(R.id.create_address_isdefault);
		mBtn_Provice = (Button) findViewById(R.id.create_address_province);
		mBtn_City = (Button) findViewById(R.id.create_address_city);
		mBtn_District = (Button) findViewById(R.id.create_address_country);
		mView = LayoutInflater.from(NewCreateAddressActivity.this).inflate(
				R.layout.address_select, null);

		mViewProvince = (WheelView) mView.findViewById(R.id.id_province);
		mViewCity = (WheelView) mView.findViewById(R.id.id_city);
		mViewDistrict = (WheelView) mView.findViewById(R.id.id_district);
		mBtnConfirm = (Button) mView.findViewById(R.id.wv_ok);
		setUpListener();
		setUpData();
		mBtnConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// alertDialog.cancel();
				popupWindow.dismiss();
				// province = listProvice.get(mViewProvince.getCurrentItem())
				// .getName().toString().trim();
				// city = listCity.get(mViewCity.getCurrentItem()).getName()
				// .toString().trim();
				// // if(listDistrict!=null &&
				// // listDistrict.size()>mViewDistrict.getCurrentItem()){
				// district = listDistrict.get(mViewDistrict.getCurrentItem())
				// .getName().toString().trim();
				// sanjiId = listDistrict.get(mViewDistrict.getCurrentItem())
				// .getPcode().toString().trim();
				// }else{
				// district="";
				// }
				mBtn_Provice.setText(mCurrentProviceName + " "
						+ mCurrentCityName + " " + mCurrentDistrictName);
				// mBtn_City.setText(city);
				// mBtn_District.setText(district);
			}
		});

	}

	private void setUpListener() {
		// ���change�¼�
		mViewProvince.addChangingListener(this);
		// ���change�¼�
		mViewCity.addChangingListener(this);
		// ���change�¼�
		mViewDistrict.addChangingListener(this);

	}

	private void setUpData() {
		initProvinceDatas();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(
				NewCreateAddressActivity.this, mProvinceDatas));
		// ���ÿɼ���Ŀ����
		mViewProvince.setVisibleItems(7);
		mViewCity.setVisibleItems(7);
		mViewDistrict.setVisibleItems(7);
		updateCities();
		updateAreas();
	}

	/*
	 * ��ʼ�����ݣ��޸�ʱ��
	 */
	@SuppressWarnings("deprecation")
	private void initData() {
		if (!getIntent().getStringExtra("data").equals("")) {
			try {
				JSONObject jsonObject = new JSONObject(getIntent()
						.getStringExtra("data").toString());
				mText_Name.setText(jsonObject.getString("TrueName"));
				mText_Phone.setText(jsonObject.getString("PhoneTel"));
				mText_Detail.setText(jsonObject.getString("DetailAddress"));
				// mBtn_Time.setText(jsonObject.getString("GoodsTime"));
				// addtime = jsonObject.getString("GoodsTime");
				province = jsonObject.getString("Province");
				city = jsonObject.getString("City");
				district = jsonObject.getString("Country");
				sanjiId = jsonObject.getString("CountryCode");
				mBtn_Provice.setText(province + " " + city + " " + district);
				mBtn_City.setText(city);
				mBtn_District.setText(district);

				if (jsonObject.getString("IsDefault").toString().equals("1")) {
					isDefault = "1";
					imgFlag = true;
					mImageView.setImageDrawable(getResources().getDrawable(
							R.drawable.register_select_2));
				} else {
					isDefault = "0";
					imgFlag = false;
					mImageView.setImageDrawable(getResources().getDrawable(
							R.drawable.register_select_1));
				}
				id = jsonObject.getString("Id");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	// //xmlע�����¼���ʵ��
	@SuppressWarnings("deprecation")
	public void doclick(View view) {
		switch (view.getId()) {
		case R.id.add_address_back:// ����
			finish();
			break;
		case R.id.create_address_province:

		case R.id.create_address_city:

		case R.id.create_address_country:
			popupWindowShow();
			// if (firstIn) {
			// AlertDialog.Builder builder = new AlertDialog.Builder(
			// NewCreateAddressActivity.this);
			// alertDialog = builder.create();
			// alertDialog.show();
			// WindowManager manager = getWindowManager();
			// int height = manager.getDefaultDisplay().getHeight();
			// int width = manager.getDefaultDisplay().getWidth();
			// alertDialog.getWindow().setLayout((width - 60), height / 2);
			// alertDialog.getWindow().setContentView(mView);
			// firstIn = false;
			// } else {
			// alertDialog.show();
			// }
			break;

		case R.id.create_address_isdefault:
			if (imgFlag) {
				isDefault = "0";
				imgFlag = false;
				mImageView.setImageDrawable(getResources().getDrawable(
						R.drawable.register_select_2));
			} else {
				imgFlag = true;
				isDefault = "1";
				mImageView.setImageDrawable(getResources().getDrawable(
						R.drawable.register_select_1));
			}
			break;
		case R.id.create_address_ok_2:
		case R.id.create_address_ok:

			name = mText_Name.getText().toString().trim();
			phonenum = mText_Phone.getText().toString().trim();
			detail = mText_Detail.getText().toString().trim();
			if (name == null || name.equals("")) {
				Toast.makeText(NewCreateAddressActivity.this, "�������ջ�������", 500)
						.show();
				return;
			}
			if (phonenum == null || phonenum.equals("")) {
				Toast.makeText(NewCreateAddressActivity.this, "�������ջ�����ϵ�绰", 500)
						.show();
				return;
			}
			if (phonenum.length() < 7 || phonenum.length() > 11) {
				Toast.makeText(NewCreateAddressActivity.this, "�ջ�����ϵ�绰��ʽ����",
						500).show();
				return;
			}
			// if (addtime == null || addtime.equals("")) {
			// Toast.makeText(NewCreateAddressActivity.this, "��ѡ���ջ�ʱ��", 500)
			// .show();
			// return;
			// }
			// if (province == null || province.equals("")) {
			// Toast.makeText(NewCreateAddressActivity.this, "��ѡ��ʡ��", 500)
			// .show();
			// return;
			// }
			if (mCurrentProviceName == null || mCurrentProviceName.equals("")) {
				Toast.makeText(NewCreateAddressActivity.this, "��ѡ��ʡ��", 500)
						.show();
				return;
			}
			// if (city == null || city.equals("")) {
			// Toast.makeText(NewCreateAddressActivity.this, "��ѡ�����", 500)
			// .show();
			// return;
			// }
			// if (district == null || district.equals("")) {
			// Toast.makeText(NewCreateAddressActivity.this, "��ѡ������", 500)
			// .show();
			// return;
			// }
			if (detail == null || detail.equals("")) {
				Toast.makeText(NewCreateAddressActivity.this, "��������ϸ��ַ", 500)
						.show();
				return;
			}
			if (getIntent().getStringExtra("data").equals("")) {
				getData("");
			} else {
				getData(id);
			}

			break;
		default:
			break;
		}
	}

	private void getData(String id) {
		dialog.loading();
		// if (province.equals("�Ϻ���")) {
		// city = "�Ϻ���";
		// }
		String url = AllStaticMessage.URL_New_CreateAddress
				+ AllStaticMessage.User_Id + "&userName=" + name + "&tel="
				+ phonenum + "&accept=" + "" + "&province="
				+ mCurrentProviceName + "&city=" + mCurrentCityName
				+ "&country=" + mCurrentDistrictName + "&detailAddress="
				+ detail + "&IsDefault=" + isDefault + "&addressId=" + id
				+ "&Region=" + "&zoneCode=" + mCurrentZipCode;

		HttpUtil.get(url, NewCreateAddressActivity.this, dialog,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode,
							@SuppressWarnings("deprecation") Header[] headers,
							JSONObject response) {
						super.onSuccess(statusCode, headers, response);
						// �ɹ�����JSONObject
						try {
							if (response.getString("Status").equals("true")) {
								Toast.makeText(
										NewCreateAddressActivity.this,
										response.getString("Results")
												.toString(), 500).show();
								AllStaticMessage.AddressListFlag = true;
								finish();
							} else {
								Toast.makeText(
										NewCreateAddressActivity.this,
										response.getString("Results")
												.toString(), 500).show();
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
					public void onFailure(int statusCode,
							@SuppressWarnings("deprecation") Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
						// ���󷵻�JSONObject
						if (dialog != null) {
							dialog.stop();
						}
					}
				});
	}

	/**
	 * ���ݵ�ǰ��ʡ��������WheelView����Ϣ
	 */
	private void updateCities() {
		int pCurrent = mViewProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[] { "" };
		}
		mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
		mViewCity.setCurrentItem(0);
		updateAreas();
	}

	/**
	 * ���ݵ�ǰ���У�������WheelView����Ϣ
	 */
	private void updateAreas() {
		int pCurrent = mViewCity.getCurrentItem();
		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
		String[] areas = mDistrictDatasMap.get(mCurrentCityName);

		if (areas == null) {
			areas = new String[] { "" };
		}
		mViewDistrict
				.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
		mViewDistrict.setCurrentItem(0);
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		if (wheel == mViewProvince) {
			updateCities();
		} else if (wheel == mViewCity) {
			updateAreas();
		} else if (wheel == mViewDistrict) {
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
			mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
		}
	}

	private void popupWindowShow() {
		if (firstIn) {
			popupWindow = new PopupWindow(mView);
			popupWindow.setWidth(LayoutParams.MATCH_PARENT);
			popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
			popupWindow.setAnimationStyle(R.style.AnimBottomPopup);
			ColorDrawable dw = new ColorDrawable(0xb0000000);
			popupWindow.setBackgroundDrawable(dw);
			popupWindow.setOutsideTouchable(true);

			popupWindow.showAtLocation(findViewById(R.id.parent),
					Gravity.BOTTOM, 0, 0);
			firstIn = false;
		} else {
			popupWindow.showAtLocation(findViewById(R.id.parent),
					Gravity.BOTTOM, 0, 0);
		}
	}

	@Override
	protected void onDestroy() {
		ShareSDK.stopSDK(this);
		super.onDestroy();
		dialog = null;
		mText_Name = null;
		mText_Phone = null;
		mText_Detail = null;
		// mBtn_Time= null;
		// mBtn_Ok= null;
		mImageView = null;
		// time= null;
		province = null;
		city = null;
		district = null;
		name = null;
		phonenum = null;
		// addtime =null;
		detail = null;
		isDefault = null;
		id = null;
		mViewProvince = null;
		mViewCity = null;
		mViewDistrict = null;
		mBtnConfirm = null;
		provicedata = null;
		citydata = null;
		districtdata = null;
		listProvice = null;
		listCity = null;
		listDistrict = null;
		mBtn_Provice = null;
		mBtn_City = null;
		mBtn_District = null;
		mView = null;
		// alertDialog = null;
		setContentView(R.layout.view_null);
		this.finish();
		System.gc();
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

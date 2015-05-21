package com.jifeng.mlsales.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.jifeng.adapter.MainAdapter;
import com.jifeng.mlsales.R;
import com.jifeng.mlsales.jumeimiao.GoodsListActivity;
import com.jifeng.mlsales.jumeimiao.LoginActivity;
import com.jifeng.myview.LoadingDialog;
import com.jifeng.myview.My_ViewPager;
import com.jifeng.myview.My_ViewPager.OnSingleTouchListener;
import com.jifeng.tools.MyTools;
import com.jifeng.url.AllStaticMessage;
import com.jifeng.url.HttpUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class OneFragment extends BaseFragment implements
		OnHeaderRefreshListener, OnFooterLoadListener {
	private int height, width;
	private JSONArray mArray_ad;
	private ImageView[] imgs;
	private ImageView[] mImageViews;
	private MainAdapter adapter = null;
	private List<JSONObject> mData;

	private AbPullToRefreshView mAbPullToRefreshView = null;

	private ListView mListView;
	private ImageView goodslist_zhiding;
	private RelativeLayout rl_progress;

	private LoadingDialog dialog;
	/** ��־λ����־�Ѿ���ʼ����� */
	private boolean isPrepared;
	/** �Ƿ��ѱ����ع�һ�Σ��ڶ��ξͲ���ȥ���������� */
	private boolean mHasLoadedOnce;

	private RelativeLayout rl_zhiding;
	private TextView tv_number, tv_cont;

	private int cont;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WindowManager manager = getActivity().getWindowManager();
		height = manager.getDefaultDisplay().getHeight();
		width = manager.getDefaultDisplay().getWidth();

		mData = new ArrayList<JSONObject>();

		dialog = new LoadingDialog(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.loading_item_second_one,
				container, false);
		mAbPullToRefreshView = (AbPullToRefreshView) rootView
				.findViewById(R.id.mPullRefreshView);
		mListView = (ListView) rootView.findViewById(R.id.main_first_list);
		goodslist_zhiding = (ImageView) rootView
				.findViewById(R.id.goodslist_zhiding);
		rl_progress = (RelativeLayout) rootView.findViewById(R.id.rl_progress);
		rl_zhiding = (RelativeLayout) rootView.findViewById(R.id.rl_zhiding);
		tv_number = (TextView) rootView.findViewById(R.id.tv_number);
		tv_cont = (TextView) rootView.findViewById(R.id.tv_cont);

		View headerView = LayoutInflater.from(getActivity()).inflate(
				R.layout.sticky_listview_header_item, null);

		RelativeLayout relativeLayout = (RelativeLayout) headerView
				.findViewById(R.id.liner_second);
		MyTools.getHight(relativeLayout, width, height, getActivity());

		mAbPullToRefreshView.setOnHeaderRefreshListener(this);
		mAbPullToRefreshView.setOnFooterLoadListener(this);

		mListView.setVerticalScrollBarEnabled(false);
		mListView.setSmoothScrollbarEnabled(true);
		mListView.setFooterDividersEnabled(false);
		mListView.setDividerHeight(0);
		mListView.setOnScrollListener(new PauseOnScrollListener(ImageLoader
				.getInstance(), true, true));
		mListView.setOnScrollListener(new AbsListView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				switch (arg1) {
				// ����Ļֹͣ���� SCROLL_STATE_IDLE=0
				case SCROLL_STATE_IDLE:
					if (cont >= 10) {
						rl_zhiding.setVisibility(View.GONE);
						goodslist_zhiding.setVisibility(View.VISIBLE);
					} else {
						goodslist_zhiding.setVisibility(View.GONE);
						rl_zhiding.setVisibility(View.GONE);
					}
					break;
				// ����Ļ�������û�ʹ�õĴ�������ָ������Ļ��ʱΪSCROLL_STATE_TOUCH_SCROLL = 1��
				// �����û��Ĳ�������Ļ�������Ի���ʱΪSCROLL_STATE_FLING = 2
				case SCROLL_STATE_TOUCH_SCROLL:
				case SCROLL_STATE_FLING:
					if (cont >= 10) {
						rl_zhiding.setVisibility(View.VISIBLE);
						goodslist_zhiding.setVisibility(View.GONE);
					} else {
						goodslist_zhiding.setVisibility(View.GONE);
						rl_zhiding.setVisibility(View.GONE);
					}
					break;
				}
			}

			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
				tv_number.setText(arg1 + arg2 - 1 + "");
				tv_cont.setText(arg3 - 1 + "");
				cont = arg1 + arg2;
			}
		});

		mListView.addHeaderView(headerView);
		final My_ViewPager my_ViewPager = (My_ViewPager) headerView
				.findViewById(R.id.pic_viewPager);
		LinearLayout layout_dian = (LinearLayout) headerView
				.findViewById(R.id.yuandian);

		getImgUrl(layout_dian, my_ViewPager);

		// getListData(mListView, "0");
		my_ViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				for (int i = 0; i < imgs.length; i++) {
					if (i == arg0 % mImageViews.length) {
						imgs[i].setBackgroundResource(R.drawable.second_view6);
					} else {
						imgs[i].setBackgroundResource(R.drawable.second_view5);
					}
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		// ���ֵ���¼�
		my_ViewPager.setOnSingleTouchListener(new OnSingleTouchListener() {
			@Override
			public void onSingleTouch() {
				try {
					if (mArray_ad.getJSONObject(my_ViewPager.getCurrentItem())
							.getString("LinkUrl").contains("Find")) {
						dialog.loading();
						AllStaticMessage.Back_to_Find = true;
						dialog.stop();
					} else if (mArray_ad
							.getJSONObject(my_ViewPager.getCurrentItem())
							.getString("LinkUrl").contains("Reg")) {
						Intent intent = new Intent(getActivity(),
								LoginActivity.class);
						startActivity(intent);
					} else if (mArray_ad
							.getJSONObject(my_ViewPager.getCurrentItem())
							.getString("AdType").equals("1")
							&& mArray_ad
									.getJSONObject(
											my_ViewPager.getCurrentItem())
									.getString("LinkUrl").equals("")) {
						String activeId = mArray_ad.getJSONObject(
								my_ViewPager.getCurrentItem()).getString(
								"Account");
						Intent intent = new Intent(getActivity(),
								GoodsListActivity.class);
						intent.putExtra("active", "1");
						intent.putExtra("activeId", activeId);
						startActivity(intent);
					}
					// else if (mArray_ad
					// .getJSONObject(my_ViewPager.getCurrentItem())
					// .getString("AdType").equals("3")
					// && mArray_ad
					// .getJSONObject(
					// my_ViewPager.getCurrentItem())
					// .getString("LinkUrl").equals("")) {
					// Intent intent = new Intent(getActivity(),
					// GoodsDetailActivity.class);
					// intent.putExtra(
					// "pid",
					// mArray_ad
					// .getJSONObject(
					// my_ViewPager.getCurrentItem())
					// .getString("Account").toString());// �id
					//
					// intent.putExtra(
					// "guigeid",
					// mArray_ad
					// .getJSONObject(
					// my_ViewPager.getCurrentItem())
					// .getString("Id").toString());// ���
					// intent.putExtra(
					// "goodsid",
					// mArray_ad
					// .getJSONObject(
					// my_ViewPager.getCurrentItem())
					// .getString("GoodsId").toString());// ��Ʒid
					// intent.putExtra(
					// "imgurl",
					// AllStaticMessage.URL_GBase
					// + mArray_ad
					// .getJSONObject(
					// my_ViewPager
					// .getCurrentItem())
					// .getString("ImgUrl").toString());
					// startActivity(intent);
					// }
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		});
		goodslist_zhiding.setOnClickListener(new View.OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View arg0) {
				mListView.setSelection(0);
				mListView.smoothScrollBy(0, 1);
				goodslist_zhiding.setVisibility(View.GONE);
				rl_zhiding.setVisibility(View.GONE);

			}
		});
		isPrepared = true;
		lazyLoad();
		// ViewGroup parent = (ViewGroup) rootView.getParent();
		// if (parent != null) {
		// parent.removeView(rootView);
		// }

		return rootView;
	}

	private void getImgUrl(final LinearLayout layout_dian,
			final My_ViewPager my_ViewPager) {
		String url = AllStaticMessage.URL_Shouye_DiBu;
		HttpUtil.get(url, getActivity(), null, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				// �ɹ�����JSONObject
				try {
					if (response.getString("Status").toString().equals("true")) {
						mArray_ad = response.getJSONArray("Results");
						imgs = new ImageView[mArray_ad.length()];
						for (int i = 0; i < imgs.length; i++) {
							ImageView imageView = new ImageView(getActivity());// ����ͼƬ��
							// imageView.setLayoutParams(new
							// LayoutParams(20,20));
							// �����·�ͼƬ����
							imageView.setPadding(10, 0, 10, 5);// �ڱ߾�
							if (i == 0) {
								imageView
										.setBackgroundResource(R.drawable.second_view6);
								// imageView.setBackground(MyTools.getDrawableImg(MainActivity.this,
								// R.drawable.second_view6));
							} else {
								imageView
										.setBackgroundResource(R.drawable.second_view5);
								// imageView.setBackground(MyTools.getDrawableImg(MainActivity.this,
								// R.drawable.second_view5));
							}
							imgs[i] = imageView;
							layout_dian.addView(imageView);
						}
						mImageViews = new ImageView[imgs.length];
						inImg();
						MyPicAdapter adapter = new MyPicAdapter();
						my_ViewPager.setAdapter(adapter);
					} else {
						Toast.makeText(getActivity(),
								response.getString("Results").toString(), 500)
								.show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
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
				super.onFailure(statusCode, headers, throwable, errorResponse);
				// ���󷵻�JSONObject
				rl_progress.setVisibility(View.GONE);
			}
		});
	}

	private void inImg() {
		DisplayImageOptions options = MyTools
				.createOptions(R.drawable.loading_01);
		for (int i = 0; i < mArray_ad.length(); i++) {
			// appItem.mAppIcon = (ImageView) v.findViewById(R.id.imgbig);
			ImageView imageView = new ImageView(getActivity());
			imageView.setScaleType(ScaleType.FIT_XY);
			// �첽����ͼƬ
			try {
				String infUrl = AllStaticMessage.URL_GBase
						+ mArray_ad.getJSONObject(i).getString("ImgUrl")
								.toString();
				// final String aid =
				// mArray_ad.getJSONObject(i).getString("Id").toString();
				// DisplayImageOptions
				// options=MyTools.createOptions(R.drawable.loading_01);
				ImageLoader.getInstance().displayImage(infUrl, imageView,
						options);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			mImageViews[i] = imageView;
		}
	}

	public class MyPicAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			if (mArray_ad.length() > 0) {
				return mArray_ad.length();
			} else {
				return 0;
			}
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			if (mArray_ad.length() > 3) {
				((ViewPager) container).removeView(mImageViews[position
						% mImageViews.length]);
			}
		}

		/**
		 * ����ͼƬ��ȥ���õ�ǰ��position ���� ͼƬ���鳤��ȡ�����ǹؼ�
		 */
		@Override
		public Object instantiateItem(View container, final int position) {

			try {
				if (mImageViews.length == 1) {
					((ViewPager) container).addView(mImageViews[0], 0);//
				} else {
					((ViewPager) container).addView(mImageViews[position
							% mImageViews.length], 0);//
				}

			} catch (Exception e) {
			}
			// �����ת��һҳ
			int num = position % mImageViews.length;
			if (mImageViews.length == 1) {
				return mImageViews[0];
			} else {
				return mImageViews[position % mImageViews.length];
			}
		}
	}  

	private void getListData(final ListView mListView, String id) {
		String url_1 = AllStaticMessage.URL_Shouye_1 + id;
		HttpUtil.get(url_1, getActivity(), null, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					if (response.getString("Status").equals("true")) {
						JSONArray mArray = response.getJSONArray("Results");
						if (mData != null) {
							mData.clear();
						}
						for (int i = 0; i < mArray.length(); i++) {
							mData.add(mArray.getJSONObject(i));
						}

						adapter = new MainAdapter(getActivity(), height, width,
								mData, mListView);
						mListView.setAdapter(adapter);
						mAbPullToRefreshView.onHeaderRefreshFinish();
						mHasLoadedOnce = true;
						rl_progress.setVisibility(View.GONE);
					} else {
						mAbPullToRefreshView.onHeaderRefreshFinish();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				rl_progress.setVisibility(View.GONE);
			}

			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onFinish() {
				super.onFinish();
			}
		});
	}

	@Override
	public void onFooterLoad(AbPullToRefreshView view) {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				mAbPullToRefreshView.onFooterLoadFinish();
				Toast.makeText(getActivity(), "û�и�����", 0).show();
			}
		}, 1000);

	}

	@Override
	public void onHeaderRefresh(AbPullToRefreshView view) {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				getListData(mListView, "0");
			}
		}, 1000);

	}

	@Override
	protected void lazyLoad() {
		if (!isPrepared || !isVisible || mHasLoadedOnce) {
			return;
		} else {
			getListData(mListView, "0");
		}

	}

}

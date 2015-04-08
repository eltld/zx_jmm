package com.jifeng.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jifeng.image.AsyncImageLoader;
import com.jifeng.image.AsyncImageLoader.ImageCallback;
import com.jifeng.mlsales.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.ECLAIR_MR1)
public class MyTools {
	public static AsyncImageLoader image = new AsyncImageLoader();
	/**
	 * �����ֻ��ķֱ��ʴ� dp �ĵ�λ ת��Ϊ px(����)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
	
	public static String getRandom()
	{
		Random random = new Random();

        int str = random.nextInt(9999)%(9999-1000+1) + 1000;
        
        return String.valueOf(str) ;
	}

	/**
	 * ����Ƿ����SDCard
	 * 
	 * @return
	 */
	public static boolean hasSdcard() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	// �ж��ļ��Ƿ����
	public static boolean hasFile(String path) {
		try {
			File f = new File(path);
			if (!f.exists()) {
				return false;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}

	// �ж��ֻ���ʽ�Ƿ���ȷ
	public static boolean isMobileNO(String mobiles) {
		if (mobiles != null && !mobiles.equals("")) {
			Pattern p = Pattern
					.compile("^((13[0-9])|(15[^4,\\D])|(17[0-9])|(18[0-9]))\\d{8}$");//|(14[0-9])
			Matcher m = p.matcher(mobiles);

			return m.matches();
		} else {
			return false;
		}
	}

	// �ж�������������
	public static boolean checkNetWorkStatus(Context context) {
		boolean netSataus = false;
		ConnectivityManager cwjManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		cwjManager.getActiveNetworkInfo();
		if (cwjManager.getActiveNetworkInfo() != null) {
			netSataus = cwjManager.getActiveNetworkInfo().isAvailable();
		}
		return netSataus;
	}

	// ����webview��һЩ����
	public static void webviewSetting(WebView mWebView) {
		// ����֧��JavaScript�ű�
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		// ֻ���ڻ���/�����û���
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);// LOAD_CACHE_ONLY,LOAD_NO_CACHE
		// ���ÿ��Է����ļ�
		webSettings.setAllowFileAccess(true);
		// �Զ���Ӧ��Ļ
		webSettings.setUseWideViewPort(true);
		webSettings.setLoadWithOverviewMode(true);
	}

	// // ����item����listview�ĸ߶�
	// public static void setListViewHeightBasedOnChildren(ListView listView,
	// ListAdapter listAdapter) {
	// if (listAdapter == null) {
	// // pre-condition
	// return;
	// }
	//
	// int totalHeight = 0;
	// for (int i = 0; i < listAdapter.getCount(); i++) {
	// View listItem = listAdapter.getView(i, null, listView);
	// listItem.measure(0, 0);
	// totalHeight += listItem.getMeasuredHeight();
	// }
	// ViewGroup.LayoutParams params = listView.getLayoutParams();
	// params.height = totalHeight
	// + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
	// listView.setLayoutParams(params);
	// }

	/**
	 * ����ͷ��
	 ***/
	public static Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// ����ѹ������������100��ʾ��ѹ������ѹ��������ݴ�ŵ�baos��
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // ѭ���ж����ѹ����ͼƬ�Ƿ����100kb,���ڼ���ѹ��
			baos.reset();// ����baos�����baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// ����ѹ��options%����ѹ��������ݴ�ŵ�baos��
			options -= 10;// ÿ�ζ�����10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// ��ѹ���������baos��ŵ�ByteArrayInputStream��
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// ��ByteArrayInputStream��������ͼƬ
		return bitmap;
	}

	public static String getFileName(String IMAGE_FILE_NAME) {
		String saveDir = Environment.getExternalStorageDirectory()
				+ "/JuMeiMiao/pic";
		File dir = new File(saveDir);
		if (!dir.exists()) {
			dir.mkdirs(); // �����ļ���
		}
		String fileName = saveDir + "/" + IMAGE_FILE_NAME;
		return fileName;
	}

	public static File saveMyBitmap(Bitmap mBitmap, String IMAGE_FILE_NAME) {
		mBitmap = compressImage(mBitmap);
		String filename = getFileName(IMAGE_FILE_NAME);
		File f = new File(filename);
		try {
			f.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return f;
	}

	// ͷ��Բ��
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();
		// ��֤�Ƿ��Σ����Ҵ����Ļ�
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int w;
		int deltaX = 0;
		int deltaY = 0;
		if (width <= height) {
			w = width;
			deltaY = height - w;
		} else {
			w = height;
			deltaX = width - w;
		}
		final Rect rect = new Rect(deltaX, deltaY, w, w);
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		// Բ�Σ�����ֻ��һ��

		int radius = (int) (Math.sqrt(w * w * 2.0d) / 2);
		canvas.drawRoundRect(rectF, radius, radius, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	/*
	 * ʱ�����(��������)
	 */

	public static String getTime() {
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy/MM/dd   HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// ��ȡ��ǰʱ��
		String str = formatter.format(curDate);
		return str;
	}

	public static int creayTime(String endtime, String currenttime) {// String
																		// endtime,
																		// String
																		// currenttime
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		int between = 0;
		try {
			Date end = formatter.parse(endtime);
			Date curret = formatter.parse(currenttime);
			between = (int) ((end.getTime() - curret.getTime()) / (1000 * 60 * 60 * 24));
			return between;
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return between;
	}

	/**
	 * ��ȡAndroid �ֻ�Ψһ��ʶ��
	 */
	public static String getAndroidID(Context context) {
		TelephonyManager TelephonyMgr = (TelephonyManager) context
				.getSystemService(context.TELEPHONY_SERVICE);
		String szImei = TelephonyMgr.getDeviceId();
		return szImei;
	}

	/**
	 * ��ֵ
	 */
	public static void setText(TextView textView, String value, Context mContext) {
		textView.setText(value);
		// textView.setTextColor(mContext.getResources().getColor(R.color.white));//������ɫ
		// textView.setTextSize(15);//���������С
	}

	// ��������listview�ĸ߶ȣ�����Ƕ����listview��scrollview��
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	// ��ͼƬ
	public static void downImg(String imgurl, ImageView mImageView) {
		Drawable cachedImage = image.loadDrawable(imgurl, mImageView,
				new ImageCallback() {
					@Override
					public void imageLoaded(Drawable imageDrawable,
							ImageView imageView, String imageUrl) {
						if (imageDrawable != null) {
							imageView.setImageDrawable(imageDrawable);
						}
					}
				});
		if (cachedImage != null) {
			mImageView.setImageDrawable(cachedImage);
		}
	}

	//
	// ��̬���ÿؼ��߶�(��)
	public static void getHight(RelativeLayout layout, int width, int height,
			Context mContext) {
		LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) layout
				.getLayoutParams(); // ȡ�ؼ�mLayout��ǰ�Ĳ��ֲ���

		if (width == 800 && height == 1280) {
			linearParams.height = MyTools.dip2px(mContext, 150);// �ֻ�
			layout.setLayoutParams(linearParams); // ʹ���úõĲ��ֲ���Ӧ�õ��ؼ�
		} else if (width == 480 && height == 800) {
			linearParams.height = MyTools.dip2px(mContext, 120);
			layout.setLayoutParams(linearParams);
		} else if (width == 480 && height == 854) {
			linearParams.height = MyTools.dip2px(mContext, 126);
			layout.setLayoutParams(linearParams);
		} else if (width == 800 && height == 1232) {
			linearParams.height = MyTools.dip2px(mContext, 280);// ƽ��
			layout.setLayoutParams(linearParams); // ʹ���úõĲ��ֲ���Ӧ�õ��ؼ�
		} else if (width == 720 && height == 1280) {
			linearParams.height = MyTools.dip2px(mContext, 135);
			layout.setLayoutParams(linearParams);
		} else if (width == 640 && height == 960) {
			linearParams.height = MyTools.dip2px(mContext, 120);
			layout.setLayoutParams(linearParams);
		} else if (width == 1600 && height == 2560) {
			linearParams.height = MyTools.dip2px(mContext, 265);
			layout.setLayoutParams(linearParams);
		}else if (width == 1080 && height == 1776) {
			linearParams.height = MyTools.dip2px(mContext, 135);
			layout.setLayoutParams(linearParams);
		}else if (width == 1080 && height == 1812) {
			linearParams.height = MyTools.dip2px(mContext, 135);
			layout.setLayoutParams(linearParams);
		} else if (width == 1080 && height == 1920) {
			linearParams.height = MyTools.dip2px(mContext, 136);
			layout.setLayoutParams(linearParams);
		} else if (width == 1440 && height == 2392) {
			linearParams.height = MyTools.dip2px(mContext, 160);
			layout.setLayoutParams(linearParams);
		} else if (width == 768 && height == 1184) {
			linearParams.height = MyTools.dip2px(mContext, 145);
			layout.setLayoutParams(linearParams);
		} else if (width == 1440 && height == 2560) {
			linearParams.height = MyTools.dip2px(mContext, 175);
			layout.setLayoutParams(linearParams);
		} else if (width == 1200 && height == 1824) {
			linearParams.height = MyTools.dip2px(mContext, 175);
			layout.setLayoutParams(linearParams);
		}
	}

	/**
	 * �õ��Զ����progressDialog
	 */
//	public static Dialog createLoadingDialog(Context context, String msg) {
//		LayoutInflater inflater = LayoutInflater.from(context);
//		View v = inflater.inflate(R.layout.all_dialog, null);// �õ�����view
//		RelativeLayout layout = (RelativeLayout) v
//				.findViewById(R.id.dialog_view);// ���ز���
//		// main.xml�е�ImageView
//		ImageView spaceshipImage = (ImageView) v
//				.findViewById(R.id.loadingImageView);
//		TextView tipTextView = (TextView) v.findViewById(R.id.id_tv_loadingmsg);// ��ʾ����
//		// ���ض�����ʾ
//		// Animation hyperspaceJumpAnimation =
//		// AnimationUtils.loadAnimation(context, R.anim.loading_animation);
//		// ʹ��ImageView��ʾ����
//		// spaceshipImage.startAnimation(hyperspaceJumpAnimation);
//
//		// �ֲ�ͼƬ��ʾ
//		spaceshipImage.setBackgroundResource(R.anim.progress_round);
//		AnimationDrawable animationDrawable = (AnimationDrawable) spaceshipImage
//				.getBackground();
//		animationDrawable.start();
//
//		tipTextView.setText(msg);// ���ü�����Ϣ
//
//		Dialog loadingDialog = new Dialog(context, R.style.CustomDialog);// �����Զ�����ʽdialog
//
//		// loadingDialog.setCancelable(false);// �������á����ؼ���ȡ��
//		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.WRAP_CONTENT,
//				LinearLayout.LayoutParams.WRAP_CONTENT));// ���ò���
//		return loadingDialog;
//	}

	// ��̬���ÿؼ��߶ȣ�����
	public static void setHight(RelativeLayout layout, int width, int height,
			Context mContext) {
		LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) layout
				.getLayoutParams(); // ȡ�ؼ�mLayout��ǰ�Ĳ��ֲ���
		if (width == 800 && height == 1280) {
			linearParams.height = MyTools.dip2px(mContext, 265);// �ֻ�
			layout.setLayoutParams(linearParams); // ʹ���úõĲ��ֲ���Ӧ�õ��ؼ�
		} else if (width == 480 && height == 800) {
			linearParams.height = MyTools.dip2px(mContext, 230);
			layout.setLayoutParams(linearParams);
		} else if (width == 480 && height == 854) {
			linearParams.height = MyTools.dip2px(mContext, 235);
			layout.setLayoutParams(linearParams);
		} else if (width == 800 && height == 1232) {
			linearParams.height = MyTools.dip2px(mContext, 442);// ƽ��
			layout.setLayoutParams(linearParams); // ʹ���úõĲ��ֲ���Ӧ�õ��ؼ�
		} else if (width == 720 && height == 1280) {
			linearParams.height = MyTools.dip2px(mContext, 245);//257
			layout.setLayoutParams(linearParams);
		} else if (width == 1080 && height == 1812) {
			linearParams.height = MyTools.dip2px(mContext, 245);
			layout.setLayoutParams(linearParams);
		} else if (width == 1080 && height == 1920) {
			linearParams.height = MyTools.dip2px(mContext, 245);
			layout.setLayoutParams(linearParams);
		} else if (width == 1440 && height == 2392) {
			linearParams.height = MyTools.dip2px(mContext, 271);
			layout.setLayoutParams(linearParams);
		} else if (width == 768 && height == 1184) {
			linearParams.height = MyTools.dip2px(mContext, 251);
			layout.setLayoutParams(linearParams);
		} else if (width == 1440 && height == 2560) {
			linearParams.height = MyTools.dip2px(mContext, 285);
			layout.setLayoutParams(linearParams);
		} else if (width == 1200 && height == 1824) {
			linearParams.height = MyTools.dip2px(mContext, 305);
			layout.setLayoutParams(linearParams);
		}
	}
	
	public static DisplayImageOptions createOptions(int imgId) {
		@SuppressWarnings("deprecation")
		DisplayImageOptions options = new DisplayImageOptions.Builder()
        .showImageOnLoading(imgId)
        .showImageForEmptyUri(imgId)
        .showImageOnFail(imgId)
        .cacheInMemory(true)//�������ص�ͼƬ�Ƿ񻺴����ڴ���  
        .cacheOnDisc(true)//�������ص�ͼƬ�Ƿ񻺴���SD����  
        //.considerExifParams(true)  //�Ƿ���JPEGͼ��EXIF��������ת����ת��
        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
        /*����ͼƬ����εı��뷽ʽ��ʾmageScaleType��ѡ��ֵ:
              EXACTLY :ͼ����ȫ��������С��Ŀ���С
              EXACTLY_STRETCHED:ͼƬ�����ŵ�Ŀ���С��ȫ
              IN_SAMPLE_INT:ͼ�񽫱����β�����������
              IN_SAMPLE_POWER_OF_2:ͼƬ������2����ֱ����һ���ٲ��裬ʹͼ���С��Ŀ���С
              NONE:ͼƬ�������*/
        .bitmapConfig(Bitmap.Config.RGB_565)//����ͼƬ�Ľ�������
        .delayBeforeLoading(30)//int delayInMillisΪ�����õ�����ǰ���ӳ�ʱ��
        //��ͼƬ���д���
        .resetViewBeforeLoading(true)//����ͼƬ������ǰ�Ƿ����ã���λ  
        //.displayer(new RoundedBitmapDisplayer(20))//���Ƽ��ã��������Ƿ�����ΪԲ�ǣ�����Ϊ����  
        //.displayer(new FadeInBitmapDisplayer(100))//�Ƿ�ͼƬ���غú���Ķ���ʱ�䣬���ܻ��������
        .build();
		return options;
	}
	

//	/**
//	 * Layout����
//	 * 
//	 * @return
//	 */
//	public static LayoutAnimationController getAnimationController() {
//		int duration = 300;
//		AnimationSet set = new AnimationSet(true);
//
//		Animation animation = new AlphaAnimation(0.0f, 1.0f);
//		animation.setDuration(duration);
//		set.addAnimation(animation);
//
//		animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
//				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
//				-1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
//		animation.setDuration(duration);
//		set.addAnimation(animation);
//
//		LayoutAnimationController controller = new LayoutAnimationController(
//				set, 0.5f);
//		controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
//		return controller;
//	}
}
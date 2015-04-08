package com.jifeng.mlsales;

import java.io.File;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;

import com.jifeng.url.AllStaticMessage;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;


public class UILApplication extends Application {
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	public void onCreate() {
		
		super.onCreate();
		//��������
		if (AllStaticMessage.DEVELOPER_MODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
		}
		initImageLoader(getApplicationContext());
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	public static void initImageLoader(Context context) {
		 File cacheDir = StorageUtils.getCacheDirectory(context);
		/*File sdcardDir =Environment.getExternalStorageDirectory();
		String path=sdcardDir.getPath()+"/cardImages";
		File cacheDir=new File(path);;
		     if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
		            // ����һ���ļ��ж��󣬸�ֵΪ�ⲿ�洢����Ŀ¼
		             
		           //�õ�һ��·����������sdcard���ļ���·��������
		            if (!cacheDir.exists()) {
		             //�������ڣ�����Ŀ¼��������Ӧ��������ʱ�򴴽�
		            	cacheDir.mkdirs();
		           }
		     }
		    
		     ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
		        .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
		        .diskCacheExtraOptions(480, 800, null)
		        .taskExecutor(null)
		        .taskExecutorForCachedImages(null)
		        .threadPoolSize(3) // default
		        .threadPriority(Thread.NORM_PRIORITY - 1) // default
		        .tasksProcessingOrder(QueueProcessingType.FIFO) // default
		        .denyCacheImageMultipleSizesInMemory()
		        .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
		        .memoryCacheSize(2 * 1024 * 1024)
		        .memoryCacheSizePercentage(13) // default
		        .diskCache(new UnlimitedDiscCache(cacheDir)) // default
		        .diskCacheSize(50 * 1024 * 1024)
		        .diskCacheFileCount(100)
		        .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
		        .imageDownloader(new BaseImageDownloader(context)) // default
		        .imageDecoder(new BaseImageDecoder(false)) // default
		        .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
		        .writeDebugLogs()
		        .build();
		     ImageLoader.getInstance().init(config);
		     */
		
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
		config.threadPriority(Thread.NORM_PRIORITY - 2);
		//config.denyCacheImageMultipleSizesInMemory();//��ͬһ��Uri��ȡ��ͬ��С��ͼƬ�����浽�ڴ�ʱ��ֻ����һ����Ĭ�ϻỺ������ͬ�Ĵ�С����ͬͼƬ
		//config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
		config.diskCacheFileNameGenerator(new HashCodeFileNameGenerator());
		config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
		config.diskCacheFileCount(300);
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
		//config.writeDebugLogs(); // Remove for release app
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config.build());
		
		
	}
}
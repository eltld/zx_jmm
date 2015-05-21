package com.jifeng.url;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.jifeng.mlsales.R;
import com.jifeng.tools.MyTools;
import com.jifeng.tools.ShrefUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

/**
 * ����httpURLConnection���ӷ�ʽ��������������
 * 
 * @author Administrator
 * 
 */
@SuppressLint("ShowToast")
public class HttpUtil {

	public static ShrefUtil userName;

	/**
	 * ����HttpClient����ʽ��GET��
	 */
	public static byte[] httpClientGet(String path, Context context) {
		InputStream inputStream = null;
		byte[] buffer = null;
		HttpURLConnection conn = null;
		try {
			URL url = new URL(path);
			// ͨ��URL�����ӣ���ȡ����
			conn = (HttpURLConnection) url.openConnection();
			// ����ʽ GET/POST
			// ��ʱʱ��(ms),����ʱ�����ӶϿ�
			conn.setConnectTimeout(10000);
			// conn.setReadTimeout(2000);
			conn.setDoInput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "text/html");
			conn.setRequestProperty("Accept-Charset", "utf-8");
			conn.setRequestProperty("contentType", "utf-8");
			// conn.setRequestProperty("Cookie", Constans.key_name + "="+
			// Constans.key_value);
			// ͨ�����ӻ�ȡ����(InputStream)
			inputStream = conn.getInputStream();
			if (conn.getResponseCode() == 200) {
				// buffer= convertStreamToString(inputStream).getBytes();
				buffer = new byte[1024];
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				int len;
				while ((len = inputStream.read(buffer)) != -1) {
					out.write(buffer, 0, len);
				}
				buffer = out.toByteArray();
				return buffer;
			}
		} catch (ConnectTimeoutException e) {// ��ʱ���������ӳ���
			// buffer = "�ף����ݼ���ʧ����,����һ����������,���¼��ذ�".getBytes();
			Toast.makeText(context, "�ף����ݼ���ʧ����,����һ����������,���¼��ذ�", 1000).show();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			Log.i("123", "url����ȷ");
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
				if (conn != null) {
					conn.disconnect();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return buffer;
	}

	/**
	 * ����HttpClient����ʽ��GET��
	 */
	public static byte[] httpClientGetData(String path, Context context) {
		byte[] data = null;

		try {
			// ����ʵ��httpClientGet����DefaultHttpClient
			// ��ȡ����
			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(path);
			get.setHeader("authValidate", AllStaticMessage.authValidate);
			// ִ�����ӣ�get����
			// ���ط�װ��HttpResponse
			HttpResponse response = client.execute(get);
			// ��÷�����(���磺code�룬=200��ʾ�ɹ�����)ͷЭ�飬����
			if (response.getStatusLine().getStatusCode() == 200) {
				response.getAllHeaders();
				HttpEntity entity = response.getEntity();
				data = EntityUtils.toByteArray(entity);

			} else {
				// ��ͬ�ķ����Լ����崦��
				// Log.i("Code", "http code"
				// + response.getStatusLine().getStatusCode());
			}

		} catch (ConnectTimeoutException e) {// ��ʱ���������ӳ���
			// data = "�ף����ݼ���ʧ����,����һ����������,���¼��ذ�".getBytes();
			Toast.makeText(context, "�ף����ݼ���ʧ����,����һ����������,���¼��ذ�", 1000).show();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}

	// ��ȡ�ɹ���cookie
	@SuppressWarnings("unused")
	public static byte[] getRequest(String url) throws Exception {
		DefaultHttpClient client = new DefaultHttpClient();
		byte[] data = null;
		int statusCode = 0;
		HttpGet getMethod = new HttpGet(url);
		try {
			HttpResponse httpResponse = client.execute(getMethod);
			statusCode = httpResponse.getStatusLine().getStatusCode();
			// �����ص�httpResponse��Ϣ
			if (statusCode == 200) {
				HttpEntity entity = httpResponse.getEntity();
				data = EntityUtils.toByteArray(entity);

				Cookie cookie;
				String cookname = null, cookvalue = null;
				List<Cookie> cookies = client.getCookieStore().getCookies();
				if (cookies.isEmpty()) {
				} else {
					for (int i = 0; i < cookies.size(); i++) {
						// ����cookie
						cookie = cookies.get(i);
						// Log.i("11111", cookie.toString());
						cookname = cookie.getName().trim();
						cookvalue = cookie.getValue().trim();
						// if (cookname.equals(Constans.key_name)) {
						// Constans.key_value = cookvalue;
						// }
					}
				}
			} else {
			}
			// result = "networkerror";
		} catch (ConnectTimeoutException e) {// ��ʱ���������ӳ���
			// result = "timeouterror";
		} catch (ClientProtocolException e) {
			// result = "networkerror";
		} catch (Exception e) {
			// result = "readerror";
			throw new Exception(e);

		}

		finally {
			getMethod.abort();
		}

		return data;

	}

	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return sb.toString();
	}

	public static void saveImg(Bitmap bitmap) {
		if (bitmap != null) {
			FileOutputStream fos = null;
			try {

				String path = android.os.Environment
						.getExternalStorageDirectory() + "/jumeimiao/pic/";
				File file = new File(path);
				if (!file.exists()) {
					file.mkdir();
				}
				File file2 = new File(path + "faceImage");
				if (!file2.exists()) {
					fos = new FileOutputStream(file2);
					if (null != fos) {
						bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
						fos.flush();
						fos.close();
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	// �¿��
	private static AsyncHttpClient client = new AsyncHttpClient(); // ʵ��������
	static {
		client.setTimeout(15000); // �������ӳ�ʱ����������ã�Ĭ��Ϊ10s

	}

	public static void get(String urlString, Context context, Dialog dialog,
			JsonHttpResponseHandler res) // ������������ȡjson�����������
	{
		if (MyTools.checkNetWorkStatus(context)) {
			client.get(urlString, res);
		} else {
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}
			Toast.makeText(context, context.getString(R.string.net_error), 500)
					.show();
		}
	}

	public static void get(String urlString, Context context,
			JsonHttpResponseHandler res) // ������������ȡjson�����������
	{
		if (MyTools.checkNetWorkStatus(context)) {
			client.get(urlString, res);
		} else {

			Toast.makeText(context, context.getString(R.string.net_error), 500)
					.show();
		}
	}

	public static AsyncHttpClient getClient() {
		return client;
	}
}

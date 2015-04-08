package com.jifeng.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * �Եײ�SharedPreference���־ò����ݲ����Ĺ��߰���
 * ���ߣ�������в���
 *
 */
@SuppressLint({ "WorldReadableFiles", "WorldWriteableFiles" })
public class ShrefUtil {
	
	//�ļ�������Ҫ��.xml��׺
	
	public static final String fileName="data";
	private SharedPreferences shref=null;
	/**
	 * ����.xml�ļ�
	 * ���췽��ֱ�Ӵ���
	 * �����־ò��xml,�ļ������ݿ���Ҫ�����Ļ���
	 */
	@SuppressWarnings("deprecation")
	public ShrefUtil(Context context,String fileName){
		/*
		 * ����ļ����ǲ����ڵľͻ�create�����µģ����һ��editor�༭�����ļ�
		 * ����ļ�����ֱ�ӷ��ر༭���������ļ�
		 * 1.�������ߴ򿪵��ļ���
		 * 2.ģʽ(��ȫ�Կ���)
		 */
		
		shref=context.getSharedPreferences(fileName, Context.MODE_PRIVATE+Context.MODE_WORLD_READABLE
												+Context.MODE_WORLD_WRITEABLE);
	}
	/**
	 * ������д���ļ�
	 * @param key String����
	 * @param value String����
	 */
	public void write(String key,String value){
		//1.�ֻ�ȡ�༭��editor
		//ͨ��edit()������ñ༭���ӿ�
		SharedPreferences.Editor editor=shref.edit();
		
		//2.д������
		editor.putString(key, value);
		
		//3.�ύ����(������д���ļ�)
		editor.commit();
	}
	//��������
	/**
	 * @param key String����
	 * @param value String����
	 */
	public void write(String key,int value){
		//1.�ֻ�ȡ�༭��editor
		//ͨ��edit()������ñ༭���ӿ�
		SharedPreferences.Editor editor=shref.edit();
		
		//2.д������
		editor.putInt(key, value);
		
		//3.�ύ����(������д���ļ�)
		editor.commit();
	}
	
	
	/**
	 * �����ݴ��ļ�ȡ��
	 * @param key String����    ����key��ȡvalue
	 * @return 
	 * 
	 * @return String����
	 */
	public String readString(String key){
		//ֱ��ͨ��shref��ȡ�����û��key���صڶ���������Ĭ��ֵ��
		return shref.getString(key, null);
	}
	//����
	public int readInt(String key){
		//ֱ��ͨ��shref��ȡ�����û��key���صڶ���������Ĭ��ֵ��
		return shref.getInt(key, -1);
	}
}

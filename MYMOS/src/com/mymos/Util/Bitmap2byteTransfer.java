package com.mymos.Util;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Bitmap2byteTransfer {

	/**
	 * 构造函数
	 */
	public Bitmap2byteTransfer() {

	}

	/**
	 * 
	 * 将Bitmap先转换成ByteArray再转换成String
	 * 
	 * @param bitmap
	 * @return
	 */
	public String bitmap2byte2String(Bitmap bitmap) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

		Log.i("byte[]---", baos.toByteArray().toString());
		Log.i("byteArrayOutputStream---", baos.toString());
		
		return byte2String(baos.toByteArray());
	}

	/**
	 * 
	 * 将String先转换成byte数组再将byte数组转换成Bitmap
	 * 
	 * @param byteStr
	 * @return
	 */
	public Bitmap string2Bye2Bitmap(String byteStr) {
		
		if (!(byteStr.isEmpty())){
			byte[] bytes=string2Byte(byteStr);
			return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		}
		return null;
	}
  
	/**
	 * 将byte转换成String
	 * @param bytes
	 * @return
	 */
	private String byte2String(byte[] bytes) {
		String byteStr = new String(bytes);
		return byteStr;
	}
    
	
	/**
	 * 将String转换成byte
	 * @param byteStr
	 * @return
	 */
	private byte[] string2Byte(String byteStr){
		return byteStr.getBytes();
	}
	
}

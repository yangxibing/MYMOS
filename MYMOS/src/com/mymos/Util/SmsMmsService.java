package com.mymos.Util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;

import com.mymos.Util.SData;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;

public class SmsMmsService {

//	private static final String TAG = "SmsMmsService";
	/**
	
	 * @param context
	 * @param uri
	 * @param protocol
	 * @return
	 */
	public static List<Map<String, Object>> getConversation(Context context,
			String uri, int protocol) {

		List<Map<String, Object>> list = null;
		String colValue[] = null;
		String keyValue[] = null;
		switch (protocol) {
		case SData.PROTOCOL_BOTH:
			colValue = new String[] {  SData.COL_ADDRESS,
					SData.COL_DATE, SData.COL_THREAD_ID, SData.COL_BODY,
					SData.COL_MSG_COUNT };
			keyValue = new String[] {SData.KEY_NAME,
					SData.KEY_DATE, SData.KEY_THREAD_ID, SData.KEY_BODY,
					SData.KEY_MSG_COUNT };
			break;
		case SData.PROTOCOL_SMS:
			// ע��һ��Ҫһһ��Ӧ����
			colValue = new String[] { SData.COL_ID, SData.COL_ADDRESS,
					SData.COL_DATE, SData.COL_THREAD_ID, SData.COL_BODY,
					SData.COL_MSG_COUNT };
			keyValue = new String[] { SData.KEY_ID, SData.KEY_NAME,
					SData.KEY_DATE, SData.KEY_THREAD_ID, SData.KEY_BODY,
					SData.KEY_MSG_COUNT };
			
			break;
		case SData.PROTOCOL_MMS:
			colValue = new String[] { SData.COL_ID, SData.COL_DATE,
					SData.COL_THREAD_ID, SData.COL_SUB, SData.COL_MSG_COUNT };
			keyValue = new String[] { SData.KEY_NAME, SData.KEY_DATE,
					SData.KEY_THREAD_ID, SData.KEY_SUB, SData.KEY_MSG_COUNT };
			break;
		}
		
		list = getSmsConversation(context, uri, colValue, keyValue,
				protocol);
		
		return list;
	}

	private static List<Map<String, Object>> getSmsConversation(
			Context context, String uri, String[] colValue, String[] keyValue,
			int protocol) {

		List<Map<String, Object>> list = null;

		ContentResolver cr = context.getContentResolver();
		String[] projection = new String[colValue.length];
		for (int i = 0; i < projection.length; i++)
			if (colValue[i].equals(SData.COL_MSG_COUNT))
				projection[i] = new String("count(thread_id) as  "
						+ colValue[i]);
			else
				projection[i] = new String(colValue[i]);

		// ע��selection�ĸ�ʽ����Ϊ�����Ǳ�where()��ס�ģ�������β����Ҫ����
		String selection = "0==0) group by (thread_id";
		Uri u = Uri.parse(uri);
		Cursor cursor = cr.query(u, projection, selection, null, "date desc");
		if (cursor.getCount() > 0) {
			list = new ArrayList<Map<String, Object>>();
			Map<String, Object> map = null;
			while (cursor.moveToNext()) {
				map = new HashMap<String, Object>();
				for (int i = 0; i < colValue.length; i++)
					if (protocol == SData.PROTOCOL_SMS
							&& colValue[i].equals(SData.COL_ADDRESS))
						map.put(keyValue[i],
								getContactsNameByAddress(context, cursor
										.getString(cursor
												.getColumnIndex(colValue[i]))));
					else if (protocol == SData.PROTOCOL_MMS
							&& colValue[i].equals(SData.COL_ID)) {
						map.put(keyValue[i],
								getContactsNameByMMsID(context, cursor
										.getInt(cursor
												.getColumnIndex(colValue[i]))));
					} else if (colValue[i].equals(SData.COL_DATE)){
						if(protocol == SData.PROTOCOL_SMS)
						map.put(keyValue[i], getDate(cursor.getLong(cursor
								.getColumnIndex(colValue[i]))));
						else if(protocol == SData.PROTOCOL_MMS)
							map.put(keyValue[i], getDate(cursor.getLong(cursor
									.getColumnIndex(colValue[i]))*1000));
					}
					else if(colValue[i].equals(SData.COL_MSG_COUNT))
						map.put(keyValue[i], "("+cursor.getString(cursor
								.getColumnIndex(colValue[i]))+")");
					else if(colValue[i].equals(SData.COL_ID))
						map.put(keyValue[i], cursor.getInt(cursor
								.getColumnIndex(colValue[i])));
					else if(colValue[i].equals(SData.COL_THREAD_ID))
						map.put(keyValue[i], cursor.getInt(cursor
								.getColumnIndex(colValue[i])));
					else
						map.put(keyValue[i], cursor.getString(cursor
								.getColumnIndex(colValue[i])));

				list.add(map);
			}
		}
		cursor.close();
		return list;
	}

	private static String getDate(long date){
		SimpleDateFormat dateFormat = new SimpleDateFormat(    
                "yyyy-MM-dd HH:mm:ss");//ע�⣬һ��Ҫ�Ѵ�Сд��ԣ�HH��hh�ǲ�ͬ�ģ�    
        Date d = new Date(date);    
        return dateFormat.format(d);  //�õ�����
	}
	
	/**
	 * ��ݲ��ŵ�id�õ�������ϵ���б��е�����
	 * ����id��Ӧ addr���msg_id
	 * ??contact_id??��ϵ���б��е�id��
	 * @param context
	 * @param id
	 * @return
	 */
	private static String getContactsNameByMMsID(Context context, int id) {
		String name = "";
		Uri uri = Uri.parse("content://mms/" + id + "/addr");
		Cursor c = context.getContentResolver().query(uri,
				new String[] { "address" }, " msg_id = ?",
				new String[] { id + "" }, null);
		if (c.getCount() > 0) {
			if(c.moveToFirst())
				name = c.getString(c.getColumnIndex("address"));
		}
		c.close();
		return getContactsNameByAddress(context, name);
	}

	/**
	 * ���address���绰����õ���Ӧ�ĺ�������ϵ���б��е���ƣ���û�����õ绰�������
	 * 
	 * @param context
	 * @param address
	 * @return
	 */
	private static String getContactsNameByAddress(Context context,
			String address) {
		String name = address;
		String pro[] = new String[] { Phone.DISPLAY_NAME, Phone.NUMBER };
		Uri up = Uri.withAppendedPath(Phone.CONTENT_FILTER_URI, address);
		Cursor c = context.getContentResolver()
				.query(up, pro, null, null, null);
		if (c.moveToFirst())
			name = (c.getString(c.getColumnIndex(Phone.DISPLAY_NAME)));
		else {
			if (address.contains("+86")) {
				up = Uri.withAppendedPath(Phone.CONTENT_FILTER_URI,
						address.replace("+86", ""));
			} else if (!address.contains("+86")) {
				up = Uri.withAppendedPath(Phone.CONTENT_FILTER_URI, "+86"
						+ address);
			}
			c = context.getContentResolver().query(up, pro, null, null, null);
			if (c.moveToFirst())
				name = (c.getString(c.getColumnIndex(Phone.DISPLAY_NAME)));
		}
		c.close();
		return name;
	}
	
	
	/**
	 * ���thread_id�õ��Ի�����������
	 * @param context
	 * @param uri
	 * @param thread_id
	 * @param protocol
	 * @return
	 */
	public static List<Map<String,Object>> getAllSmsByThread_id(Context context,
			String uri,int thread_id,int protocol){
		List<Map<String,Object>>list = null;
		
		switch(protocol){
		case SData.PROTOCOL_SMS:
			list = getSMSByThread_id(context,uri,thread_id);
			break;
		case SData.PROTOCOL_MMS:
			list = getMMSByThread_id(context,uri,thread_id);
			break;
		case SData.PROTOCOL_BOTH:
			break;
		}
			
		return list;
	}
	
	public static List<Map<String,Object>> getSMSByThread_id(Context context,
			String uri,int thread_id){
		List<Map<String,Object>>list = null;
		
		String pro [] = new String[]{SData.COL_DATE,SData.COL_BODY,SData.COL_ID};
		Cursor c = context.getContentResolver().query(Uri.parse(uri), pro,
				"thread_id = ?", new String[]{thread_id+""}, "date asc");
		
		if(c.getCount()>0){
			Map<String,Object>map = null;
			list = new ArrayList<Map<String,Object>>();
			while(c.moveToNext()){
				map = new HashMap<String,Object>();
				map.put(SData.KEY_ID, c.getInt(c.getColumnIndex(SData.COL_ID)));
				map.put(SData.KEY_BODY, c.getString(c.getColumnIndex(SData.COL_BODY)));
				map.put(SData.KEY_DATE, 
						getDate(c.getLong(c.getColumnIndex(SData.COL_DATE))));
				list.add(map);
			}
		}
		c.close();
		return list;
	}
	
	private static List<Map<String,Object>> getMMSByThread_id(Context context,
			String uri,int thread_id){
		List<Map<String,Object>>list = null;
		
		String pro [] = new String[]{SData.COL_DATE,SData.COL_ID,
				SData.COL_SUB};
		Cursor c = context.getContentResolver().query(Uri.parse(uri), pro,
				"thread_id = ?", new String[]{thread_id+""}, "date desc");
		
		if(c.getCount()>0){
			Map<String,Object>map = null;
			list = new ArrayList<Map<String,Object>>();
			while(c.moveToNext()){
				map = new HashMap<String,Object>();
				map.put(SData.KEY_ID, c.getInt(c.getColumnIndex(SData.COL_ID)));
				
				map.put(SData.KEY_SUB, c.getString(c.getColumnIndex(SData.COL_SUB)));
				//ע�����ʱ��Ҫ����1000	
				map.put(SData.KEY_DATE, 
						getDate(c.getLong(c.getColumnIndex(SData.COL_DATE))*1000));
				
				map.put(SData.KEY_DATA, getAllMmsData(c.getInt(c.getColumnIndex(SData.COL_ID)),context));
				/*
				map.put(SData.KEY_ANNEX_TEXT, 
						getMMSTextData(c.getInt(c.getColumnIndex(SData.COL_ID)),context));
				map.put(SData.KEY_ANNEX_DATA,
						getMMSData(c.getInt(c.getColumnIndex(SData.COL_ID)),context));
				*/
//				map.put(SData.KEY_ANNEX_DATA,
//						getMMSDataImg(c.getInt(c.getColumnIndex(SData.COL_ID)),context));
				map.put(SData.KEY_NAME, name/*�˾�һ��Ҫ����getMMsData����ִ��֮��*/);
				name = null;
				list.add(map);
			}
		}
		c.close();
		return list;
	}
	
	private static String name = null;
	
	
	/**
	 * ��ȡpdu_id��Ӧ��part���е�������ݣ�����ͼƬ���ı�
	 * @param pdu_id
	 * @param context
	 * @return
	 */
	private static Map<String,Object>getAllMmsData(int pdu_id,Context context){
		Map<String,Object>map = new HashMap<String,Object>();
		
		String selection = new String("mid=?");
		String selectionArgs[] = new String[] { pdu_id + "" };
		Cursor cur = context.getContentResolver().query(
				Uri.parse("content://mms/part"),
				new String[] { SData.COL_ID, SData.COL_CT ,SData.COL_CL, SData.COL_TEXT},
				// null,
				selection, selectionArgs, "cl asc");//��cl����
//		int textCount = 0;
//		int imgCount = 0;
		int index = 0;
		ByteArrayOutputStream baos = null;
			
		if(cur.getCount()>0){
			
			while(cur.moveToNext()){
				
				String ct = cur.getString(cur.getColumnIndex(SData.COL_CT));
//				Log.i(TAG, "the ct is : "+ct);
				if(ct.equals("image/jpeg") || ct.equals("image/jpg")
						|| ct.equals("image/gif") || ct.equals("image/png")){
					name = cur.getString(cur.getColumnIndex(SData.COL_CL));
					int _partID = cur.getInt(cur.getColumnIndex(SData.COL_ID));
					map.put(SData.KEY_DATA+index,
							getImgData(cur,_partID,baos,context));
					index++;
				}else if(cur.getString(cur.getColumnIndex(SData.COL_CT)).equals("text/plain")){
					map.put(SData.KEY_DATA+index,
							getTextData(cur,context));
					index++;
				}
				
			}
			cur.close();
		}
		
		return map;
	}
	
	/**
	 * �õ�ͼ�����
	 * @param cur
	 * @param _partID
	 * @param baos
	 * @param context
	 * @return
	 */
	private static byte[] getImgData(Cursor cur,int _partID,ByteArrayOutputStream baos,Context context){
		Uri partURI = Uri.parse("content://mms/part/"
				+ String.valueOf(_partID));
		baos = new ByteArrayOutputStream();
		InputStream is = null;
		try {
			// Log.i("SmsMmsService", "can reach here");
			is = context.getContentResolver().openInputStream(
					partURI);
			// Log.i("SmsMmsService", "can not reach here?");
			byte[] buffer = new byte[1024];
			int len = is.read(buffer);
			while (len != -1) {
				// Log.i("SmsMmsService", "in Reading...");
				baos.write(buffer, 0, len);
				len = is.read(buffer);
			}
			byte[] result = baos.toByteArray();
			is.close();
			baos.close();
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			// Toast.makeText(context, "Read Uri faild", 1).show();
		}
		return null;
	}
	
	/**
	 * �õ��ı����
	 * @param cur
	 * @param context
	 * @return
	 */
	private static String getTextData(Cursor cur,Context context){
		String result = cur.getString(cur.getColumnIndex(SData.COL_TEXT));
		if (result != null) {//Ŀǰ�汾
			return result;
		} else {//��ǰ�汾
			int _partID = cur.getInt(cur
					.getColumnIndex(SData.COL_ID));
			String partID = String.valueOf(_partID);
			Uri partURI = Uri.parse("content://mms/part/" + partID);
			InputStream is = null;
			try {
				is = context.getContentResolver().openInputStream(
						partURI);
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "UTF-8"));
				String line = "";
				StringBuilder data = new StringBuilder();
				while ((line = reader.readLine()) != null) {
					data.append(line + "\n");
				}
				return data.toString();
			} catch (IOException e) {
				return null;
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
					}
				}
			}
		}
	}
	
	/**
	 * ���id��ѯ���ŵķ��ı���ݣ�ע�����pdu_id��Ӧ��part���е�mid,����_id
	 * @param _id
	 * @param context
	 * @return
	 */
	private static byte[] getMMSData(int pdu_id, Context context) {
		String selection = new String("mid=?");
		String selectionArgs[] = new String[] { pdu_id + "" };
		Cursor cur = context.getContentResolver().query(
				Uri.parse("content://mms/part"),
				new String[] { SData.COL_ID, SData.COL_CT ,SData.COL_NAME},
				// null,
				selection, selectionArgs, null);
//		Log.i("SmsMmsService", "Before Reading...");
		// if(cur.getCount()>0)
		// while(cur.moveToNext()){
		// String[]cn = cur.getColumnNames();
		// for(String c:cn)
		// Log.i("SmsMmsService", c+" = "+cur.getString(cur.getColumnIndex(c)));
		// }

		ByteArrayOutputStream baos = null;
		// ע�⣬�˴�һ��ֻ����while��������Ϊpart����ͬһ��mid��Ӧ�źü��� _id,���е�һ����
		// application/smil���͵ģ�Ϊsmil.xml�ļ����ݣ��������text��img���͵ģ�Ҫ����ȡ����
		// ��ֻ����while
		if (cur.getCount() > 0)
			while (cur.moveToNext()) {
				String ct = cur.getString(cur.getColumnIndex(SData.COL_CT));
				//ֻ��ȡ��һ��ͼƬ
				if (ct.equals("image/jpeg") || ct.equals("image/jpg")
						|| ct.equals("image/gif") || ct.equals("image/png")) {
					int _partID = cur.getInt(cur.getColumnIndex(SData.COL_ID));
					Uri partURI = Uri.parse("content://mms/part/"
							+ String.valueOf(_partID));
					baos = new ByteArrayOutputStream();
					InputStream is = null;
					try {
						// Log.i("SmsMmsService", "can reach here");
						is = context.getContentResolver().openInputStream(
								partURI);
						// Log.i("SmsMmsService", "can not reach here?");
						byte[] buffer = new byte[1024];
						int len = is.read(buffer);
						while (len != -1) {
							// Log.i("SmsMmsService", "in Reading...");
							baos.write(buffer, 0, len);
							len = is.read(buffer);
						}
						name = cur.getString(cur.getColumnIndex(SData.COL_NAME));
						byte[] result = baos.toByteArray();
						is.close();
						baos.close();
						return result;
					} catch (IOException e) {
						e.printStackTrace();
						// Toast.makeText(context, "Read Uri faild", 1).show();
					}
				}

			}
		return null;
	}

	/**
	 * ���id��ѯ���ŵĵ�һ��ͼƬ��ע�����pdu_id��Ӧ��part���е�mid,����_id
	 * @param key
	 * @param context
	 * @return
	 */
	private static Bitmap getMMSDataImg(int pdu_id, Context context) {
		String selection = new String("mid=?");
		String selectionArgs[] = new String[] { pdu_id + "" };
		Cursor cur = context.getContentResolver().query(
				Uri.parse("content://mms/part"),
				new String[] { SData.COL_ID, SData.COL_CT },
				// null,
				selection, selectionArgs, null);
		Bitmap bitmap = null;
		if(cur.getCount()>0)
		while (cur.moveToNext()) {
			String ct = cur.getString(cur.getColumnIndex(SData.COL_CT));
			//ֻ��ȡ��һ��ͼƬ
			if (ct.equals("image/jpeg") || ct.equals("image/jpg")
					|| ct.equals("image/gif") || ct.equals("image/png")){
				
			int _partID = cur.getInt(cur.getColumnIndex("_id"));
			String partID = String.valueOf(_partID);
			Uri partURI = Uri.parse("content://mms/part/" + partID);
			
			InputStream is = null;
			try {
				is = context.getContentResolver().openInputStream(partURI);
				
				bitmap = BitmapFactory.decodeStream(is);
				
				cur.close();
				
				return bitmap;
			} catch (IOException e) {

			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
					}
				}
			}
			}
		}
		return bitmap;
		
	}
	
	/**
	 * �˺�����Ϊ�汾���ݶ�д��
	 * ���id��ѯ���ŵ��ı���ݣ�ע�����pdu_id��Ӧ��part���е�mid,����_id
	 * �˺����а汾�����һ��2.x�汾����ִ�д˺������ֱ�Ӷ�ȡ��text���ֶ����ݣ�ֻ����
	 * ��text���ֶ�����Ϊnullʱ�Ŷ�ȡ��������ִ�д˺���
	 * @param pdu_id
	 * @param context
	 * @return
	 */
	private static String getMMSTextData(int pdu_id, Context context) {
		StringBuilder data = new StringBuilder();
		String result = null;
		String selection = new String("mid=?");
		String selectionArgs[] = new String[] { pdu_id + "" };// ���key����pdu�����_id
		Cursor cur = context.getContentResolver().query(
				Uri.parse("content://mms/part"),
				new String[] { SData.COL_ID, SData.COL_CT, SData.COL_TEXT },
				selection, selectionArgs, SData.COL_CT + " desc");
		if (cur.getCount() > 0) {
			while (cur.moveToNext()) {
				if (cur.getString(cur.getColumnIndex(SData.COL_CT)).equals(
						"text/plain")) {
					result = cur.getString(cur.getColumnIndex(SData.COL_TEXT));
					if (result != null) {//Ŀǰ�汾
						cur.close();
						return result;
					} else {//��ǰ�汾
						int _partID = cur.getInt(cur
								.getColumnIndex(SData.COL_ID));
						String partID = String.valueOf(_partID);
						Uri partURI = Uri.parse("content://mms/part/" + partID);
						InputStream is = null;
						try {
							is = context.getContentResolver().openInputStream(
									partURI);
							BufferedReader reader = new BufferedReader(
									new InputStreamReader(is, "UTF-8"));
							String line = "";
							while ((line = reader.readLine()) != null) {
								data.append(line + "\n");
							}
							return data.toString();
						} catch (IOException e) {
							return null;
						} finally {
							if (is != null) {
								try {
									is.close();
								} catch (IOException e) {
								}
							}
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * ���thread_idɾ�����лỰ
	 * @param context
	 * @param thread_id
	 * @param type
	 * @param protocol
	 */
	public static void deleteByThread_id(Context context,int thread_id,int type,int protocol){
		delete(context,SData.COL_THREAD_ID,thread_id,type,protocol);
	}
	
	/**
	 * ���_idɾ��������
	 * @param context
	 * @param _id ����id
	 * @param type �������ͣ��ռ��䣬�����䣬�ݸ���
	 * @param protocol ����Э�飬���š�����
	 */
	public static void deleteById(Context context,int _id,int type,int protocol){
		delete(context,SData.COL_ID,_id,type,protocol);
	}
	
	/**
	 * �������ֵɾ��ĳһ����¼
	 * @param context
	 * @param col_name
	 * @param col_value
	 * @param type
	 * @param protocol
	 */
	public static void delete(Context context,String col_name,int col_value,int type,
			int protocol){
		String uri = "";
		String colType = "";
		switch(protocol){
		case SData.PROTOCOL_SMS:
			uri = SData.URI_SMS_ALL;
			colType = SData.COL_TYPE;
			break;
		case SData.PROTOCOL_MMS:
			uri = SData.URI_MMS_ALL;
			colType = SData.COL_MSG_BOX;
			break;
		}
		context.getContentResolver().delete(
				Uri.parse(uri),
				col_name+" = ?  and  "+colType+" = ?",
				new String[]{String.valueOf(col_value),String.valueOf(type)});
	}
}

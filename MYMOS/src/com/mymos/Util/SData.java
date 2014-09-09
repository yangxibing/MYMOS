package com.mymos.Util;

/**

 * @author THINK
 *
 */
public final class SData {

	/*

	 */
	public static final int PROTOCOL_SMS = 0;
	public static final int PROTOCOL_MMS = 1;
	public static final int PROTOCOL_BOTH = 2;
	
	/*
	 * ����Ϊ��Map����ȡ�������õ�Key
	 */
	
	// ��ȡ����/����������һ������
	public static final String KEY_MSG_BOX = "key_msg_box";
	
	// ��ȡ����
	public static final String KEY_TITLE = "key_title";

	// ��ȡ��������
	public static final String KEY_PROTOCOL = "key_protocol";

	// ��ȡUri
	public static final String KEY_URI = "key_uri";

	// ��ȡthread_id
	public static final String KEY_THREAD_ID = "key_thread_id";

	// ��ȡ _id
	public static final String KEY_ID = "key_id";

	// ��ȡ��������
	public static final String KEY_MSG_COUNT = "key_msg_count";

	// ��ȡ��������
	public static final String KEY_BODY = "key_body";

	// ��ȡ���ű���
	public static final String KEY_SUBJECT = "key_subject";
	
	// ��ȡ���ű���
	public static final String KEY_SUB = "key_sub";

	// ��ȡ��������
	public static final String KEY_DATE = "key_date";

	// ��ȡ���ŷ���������
	public static final String KEY_NAME = "key_name";

	// ��ȡ���¶�������
	public static final String KEY_SNIPPET = "key_snippet";

	// ��ȡ���ŷ��ı���������
	public static final String KEY_ANNEX_DATA = "key_annext_data";

	// ��ȡ�����ı���������
	public static final String KEY_ANNEX_TEXT = "key_annext_text";
	
	// ��ȡ��������
	public static final String KEY_DATA = "key_all_data";
		
	/*
	 * ����Ϊ���������������
	 */

	public static final String COL_TYPE = "type";
	
	public static final String COL_MSG_BOX = "msg_box";
	
	public static final String COL_DATE = "date";

	public static final String COL_THREAD_ID = "thread_id";

	public static final String COL_ID = "_id";

	public static final String COL_CT = "ct";
	
	public static final String COL_CL = "cl";
	
	public static final String COL_BODY = "body";

	public static final String COL_SUB = "sub";
	
	public static final String COL_SUBJECT = "subject";

	public static final String COL_SNIPPET = "snippet";

	public static final String COL_ADDRESS = "address";

	public static final String COL_MSG_COUNT = "msg_count";
	
	public static final String COL_TEXT = "text";
	
	public static final String COL_NAME = "name";

	/*
	 * ����Ϊ����Uri
	 */

	// ����
	public static final String URI_SMS_ALL = "content://sms/";
	public static final String URI_SMS_INBOX = "content://sms/inbox";
	public static final String URI_SMS_SENT = "content://sms/sent";
	public static final String URI_SMS_DRAFT = "content://sms/draft";
	public static final String[]URI_SMS_ARRAY = 
			new String[]{URI_SMS_INBOX,URI_SMS_SENT,URI_SMS_DRAFT};
	// ����
	public static final String URI_MMS_ALL = "content://mms/";
	public static final String URI_MMS_INBOX = "content://mms/inbox";
	public static final String URI_MMS_SENT = "content://mms/sent";
	public static final String URI_MMS_DRAFT = "content://mms/draft";
	public static final String[]URI_MMS_ARRAY =
			new String[]{URI_MMS_INBOX,URI_MMS_SENT,URI_MMS_DRAFT};
	// ���źͲ���
	public static final String URI_MMSSMS_CONVERSATION ="content://mms-sms/conversations";
	
	
	/*
	 * 短信类型
	 */
	
	public static final int TYPE_ALL = 0;
	
	public static final int TYPE_SMS = 0x77;
	public static final int TYPE_SMS_INBOX = 1;
	public static final int TYPE_SMS_SENT = 2;	
	public static final int TYPE_SMS_DRAFT = 3;
	public static final int[]TYPE_SMS_ARRAY = 
			new int[]{TYPE_SMS_INBOX,TYPE_SMS_SENT,TYPE_SMS_DRAFT};
	
	public static final int TYPE_MMS = 0x78;
	public static final int TYPE_MMS_INBOX = 1;
	public static final int TYPE_MMS_SENT = 2;
	public static final int TYPE_MMS_DRAFT = 3;
	public static final int[]TYPE_MMS_ARRAY = 
			new int[]{TYPE_MMS_INBOX,TYPE_MMS_SENT,TYPE_MMS_DRAFT};
	

}

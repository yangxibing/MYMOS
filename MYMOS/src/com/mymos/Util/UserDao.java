package com.mymos.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mymos.Entity.User;
import com.mymos.UI.IndexActivity;

public class UserDao extends Activity {
	private List<User> mUserList;
	private DatabaseAccessTool dbAT;

	public UserDao(Context context) {
		dbAT = new DatabaseAccessTool(context, "User.db3", null, 1);
	}

	/**
	 * 返回指定id的用户
	 * 
	 * @param id
	 * @return
	 */
	public User queryById(String id) {
		SQLiteDatabase db = dbAT.getWritableDatabase();
		Log.i("yt", "shh");
		Cursor cs = db.query("User", new String[] { "birthday", "city",
				"email", "userId", "firstName", "lastName", "password",
				"phoneNumber", "priority" }, "userId = ?", new String[] { id },
				null, null, null);

		String uId = null, uEmail = null, uCity = null, uFirstN = null, uLastN = null, uPass = null, uPhone = null;
		int uPri = 0;
		Date uBirthday = null;

		for (cs.moveToFirst(); !(cs.isAfterLast()); cs.moveToNext()) {
			uId = cs.getString(cs.getColumnIndex("userId"));
			String birthday = cs.getString(cs.getColumnIndex("birthday"));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				uBirthday = sdf.parse(birthday);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			uEmail = cs.getString(cs.getColumnIndex("email"));
			uCity = cs.getString(cs.getColumnIndex("city"));
			uFirstN = cs.getString(cs.getColumnIndex("firstName"));
			uLastN = cs.getString(cs.getColumnIndex("lastName"));
			uPass = cs.getString(cs.getColumnIndex("password"));
			uPhone = cs.getString(cs.getColumnIndex("phoneNumber"));
			uPri = cs.getInt(cs.getColumnIndex("priority"));
		}
		db.close();

		return new User(uId, uLastN, uFirstN, uPass, uPhone, uEmail, uPri,
				uBirthday, uCity);
	}

	/**
	 * 查询所有用户
	 * 
	 * @return
	 */
	public List<User> queryAll() {

		mUserList = new ArrayList<User>();

		SQLiteDatabase db = dbAT.getReadableDatabase();

		Cursor cs = db.query("User", new String[] { "birthday", "city",
				"email", "userId", "firstName", "lastName", "password",
				"phoneNumber", "priority" }, null, null, null, null, null);

		String uId = null, uEmail = null, uCity = null, uFirstN = null, uLastN = null, uPass = null, uPhone = null;
		int uPri = 0;
		Date uBirthday = null;

		for (cs.moveToFirst(); !(cs.isAfterLast()); cs.moveToNext()) {
			uId = cs.getString(cs.getColumnIndex("userId"));
			String birthday = cs.getString(cs.getColumnIndex("birthday"));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				uBirthday = sdf.parse(birthday);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			uEmail = cs.getString(cs.getColumnIndex("email"));
			uCity = cs.getString(cs.getColumnIndex("city"));
			uFirstN = cs.getString(cs.getColumnIndex("firstName"));
			uLastN = cs.getString(cs.getColumnIndex("lastName"));
			uPass = cs.getString(cs.getColumnIndex("password"));
			uPhone = cs.getString(cs.getColumnIndex("phoneNumber"));
			uPri = cs.getInt(cs.getColumnIndex("priority"));

			User u = new User(uId, uLastN, uFirstN, uPass, uPhone, uEmail,
					uPri, uBirthday, uCity);
			mUserList.add(u);
		}
		db.close();
		return mUserList;
	}
}

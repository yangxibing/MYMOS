package com.mymos.Util;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.TabWidget;

public class DatabaseAccessTool extends SQLiteOpenHelper {

	public DatabaseAccessTool(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String createTableUser= "CREATE TABLE User (birthday Date, city VARCHAR(20), email VARCHAR(20)," +
				"firstName VARCHAR(20), userId VARCHAR(20) primary key, lastName VARCHAR(10), password VARCHAR(20)," +
				"phoneNumber VARCHAR(20), priority Integer)";
	
		db.execSQL(createTableUser);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		onCreate(db);
	}
	
}

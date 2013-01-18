package com.supinfo.miniprojet.supreader.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqlLite extends SQLiteOpenHelper{
	
	public SqlLite(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}
	
	private static final String DATABASE_NAME = "SUPREADER";
	private static final int DATABASE_VERSION = 2;
	private static final String TABLE_NAME = "FEEDS";
	private static final String TABLE_CREATE = 
		"CREATE TABLE " + TABLE_NAME + " (" +
		"id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
		"name TEXT NOT NULL, " +
		"link TEXT NOT NULL);";
	
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CREATE);
		
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
		onCreate(db);
		
	} 	

}

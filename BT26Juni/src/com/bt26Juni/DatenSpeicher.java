package com.bt26Juni;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DatenSpeicher extends SQLiteOpenHelper implements BaseColumns{
	
	private static final String DATABASE_NAME = "Daten aus Graphic";
	private static final int DATABASE_VERSION = 1;
	public final String DATABASE_TABLE = "Daten";
	public final String ZEIT_COLUMN = "Zeit";
	public final String ZAHL_COLUMN = "Zahl";
	public final String COLUMN_ID = "_id";	
	private final String DATABASE_CREATE_SCRIPT = "create table "
			+ DATABASE_TABLE + " (" + COLUMN_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + ZEIT_COLUMN
			+ " TEXT, " + ZAHL_COLUMN + " INTEGER );";
	
	
	

	public DatenSpeicher(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE_SCRIPT);
		
	}
 

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}



package com.bluetoothtest2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class IloveHAC extends SQLiteOpenHelper implements BaseColumns {
	
	
	
	
	private static final String DATABASE_NAME = "Population in SAO";
	private static final int DATABASE_VERSION = 1;
	public final String DATABASE_TABLE = "Population";
	public final String LAND_COLUMN = "Land";
	public final String POPULATION_COLUMN = "PopulationZahl";
	public final String COLUMN_ID = "_id";	
	private final String DATABASE_CREATE_SCRIPT = "create table "
			+ DATABASE_TABLE + " (" + COLUMN_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + LAND_COLUMN
			+ " TEXT, " + POPULATION_COLUMN + " INTEGER );";
	
	private SQLiteDatabase mSqLiteDatabase;
	

	public IloveHAC(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE_SCRIPT);
		insertPopulation(db, "Vietnam", 80);
		insertPopulation(db, "Malaysia", 120);
		insertPopulation(db, "Indonesia", 250);
	
		
	}
 

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
	private void insertPopulation(SQLiteDatabase db, String land, int population){
			ContentValues values = new ContentValues();
			values.put(LAND_COLUMN, land);
			values.put(POPULATION_COLUMN, population);
			db.insert("Population", null, values);
		}
	
	
	
	

}


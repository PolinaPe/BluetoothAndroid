package com.bt26Juni;


import java.util.concurrent.BlockingQueue;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Consumer implements Runnable {
	private DatenSpeicher mDatabaseHelper;
	private SQLiteDatabase mSqLiteDatabase;
	protected BlockingQueue<QueueObjekt> queue = null;
	 
	
	Consumer(DatenSpeicher mDatabaseHelper, SQLiteDatabase mSqLiteDatabase,  BlockingQueue<QueueObjekt> queue){
		this.mDatabaseHelper = mDatabaseHelper;
		this.mSqLiteDatabase = mSqLiteDatabase;
		this.queue = queue;
	}

		

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try{ 
				while(true){
				
			    mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
			    ContentValues cv = new ContentValues();
			    QueueObjekt qo = queue.take();
			    cv.put(mDatabaseHelper.ZAHL_COLUMN,(String) qo.get_a());
			    cv.put(mDatabaseHelper.ZEIT_COLUMN,(String) qo.get_b());
			    
			    
			    mSqLiteDatabase.insert("Daten",null,cv);}}
				catch(InterruptedException e)
			{Log.e("Neee", "", e);}
			
		}
	
}

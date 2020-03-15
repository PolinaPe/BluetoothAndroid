package com.bt26Juni;



import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class DatenbankTabelle extends Activity{
	
	
	
	DatenSpeicher helper;
	SQLiteDatabase db;
	ListView listView;
	Cursor cursor;

	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	 super.onCreate(savedInstanceState);
	 setContentView(R.layout.activity_datenbank_tabelle);
	 TextView textView1 = (TextView) findViewById(R.id.textView3);
	 TextView textView2 = (TextView) findViewById(R.id.textView4);
	 listView = (ListView) findViewById(R.id.listView1);
	 helper = new DatenSpeicher(this);
	 db = helper.getReadableDatabase();
	 cursor = db.query("Daten", new String[]{"Zahl", "_id", "Zeit"}, null, null, null, null, null);
	 CursorAdapter adapter = new SimpleCursorAdapter(
	         this, 
	         R.layout.column_row,
	         cursor, 
	         new String[]{"Zahl", "Zeit"}, 
	         new int[] {R.id.textView1,R.id.textView2}, 0);
	 listView.setAdapter(adapter);

	 
	 }}

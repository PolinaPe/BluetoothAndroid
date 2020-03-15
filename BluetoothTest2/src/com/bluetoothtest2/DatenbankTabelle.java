package com.bluetoothtest2;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;



public class DatenbankTabelle extends ListActivity {
	private SQLiteDatabase db;
	private Cursor cursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_datenbank_tabelle);
	    ListView list = getListView();
	    
	    
	    
	    	IloveHAC hac = new IloveHAC(this);
	    	db = hac.getReadableDatabase();
	    	
	    	cursor = db.query("Population", new String[]{"_id","Land", "PopulationZahl"}, null, null, null, null, null);
	    	
	    	CursorAdapter listAdapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item, cursor, new String[] {"Land","PopulationZahl"}, new int[]{android.R.id.text1, android.R.id.text2}, 0);
	    	list.setAdapter(listAdapter); 
	}        
	}

package com.bt26Juni;



import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity
{
	//BT Adapter bekommen
	private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter();
    private static final int REQUEST_DISCOVERABLE = 0x2;// int=10

	/* Hier wird Aktivity gestartet*/
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}


	/* BT anschalten */
	public void onEnableButtonClicked(View view)
	{
		
		_bluetooth.enable();
		
		Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE); // Make device discoverable
		startActivityForResult(enabler, REQUEST_DISCOVERABLE); // Make device discoverable
	}


	/* BT ausschalten*/
	public void onDisableButtonClicked(View view)
	{
		_bluetooth.disable();
	}
	/* Zur DB Tabelle übergehen */
	public void onDatenbankButtonClicked(View view)
	{

		Intent enabler = new Intent(this, DatenbankTabelle.class);
		startActivity(enabler);
	}


	/* Als Client BT öffnen */
	public void onOpenClientSocketButtonClicked(View view)
	{

		Intent enabler = new Intent(this, ClientSocketActivity.class);
		startActivity(enabler);
	}


	/* Als Server BT öffnen */
	public void onOpenServerSocketButtonClicked(View view)
	{

		Intent enabler = new Intent(this, ServerSocketActivity.class);
		startActivity(enabler);
	}


}



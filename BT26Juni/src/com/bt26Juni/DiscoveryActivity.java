package com.bt26Juni;

import java.util.ArrayList;
import java.util.List;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DiscoveryActivity  extends ListActivity
{
	
	// Adapter bekommen 
	private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter();
	// List f�r BT devices 
	private List<BluetoothDevice> _devices = new ArrayList<BluetoothDevice>();

	
	
	
	
	
	// Reciever f�r Daten �ber Devices bekommen
	private BroadcastReceiver _foundReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			// inf o device bekommen 
			BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			// zu Liste hinzuf�gen
			_devices.add(device);
			//anzeigen
			showDevices();
		}
	};
	
	// Reciever um Reciever _foundReceiver dynamically abzumelden
	private BroadcastReceiver _discoveryReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent)  
		{	
			unregisterReceiver(_foundReceiver);
			unregisterReceiver(this);
			
		}
	};
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_discovery);
		
		if (!_bluetooth.isEnabled())
		{
			
			finish();
			return;
		}
		
		_bluetooth.startDiscovery();	
		
		// Registrierung des Abmeldungsreciever
		IntentFilter discoveryFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		registerReceiver(_discoveryReceiver, discoveryFilter);
		
		// Regiestrierung des Reciever f�r Ger�te
		IntentFilter foundFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(_foundReceiver, foundFilter);
		
	}
	

	/* Show devices list */
	protected void showDevices()
	{
		List<String> list = new ArrayList<String>();
		for (int i = 0, size = _devices.size(); i < size; ++i)
		{
			StringBuilder b = new StringBuilder();
			BluetoothDevice d = _devices.get(i);
			b.append(d.getAddress());
			b.append('\n');
			b.append(d.getName());
			String s = b.toString();
			list.add(s);
		}
		
		
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
		setListAdapter(adapter);
		
	}
	
	// das Ger�t aus der Liste ausw�hlen und in ClientActivity zur�ckgehen
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
	
		Intent result = new Intent();
		result.putExtra(BluetoothDevice.EXTRA_DEVICE, _devices.get(position));
		setResult(RESULT_OK, result);
		finish();
	}
	 @Override
	  protected void onDestroy() {
	    super.onDestroy();
	    // Nachpr�fen ob wir noch suchen
	    if (_bluetooth != null) {
	      _bluetooth.cancelDiscovery();}
	   
	  }
}
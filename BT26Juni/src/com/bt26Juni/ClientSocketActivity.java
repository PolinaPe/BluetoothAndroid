package com.bt26Juni;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ClientSocketActivity  extends Activity
{
	
	private static final int codeRequest = 0x1; 
	private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter(); // Adapter bekommen
	private BluetoothSocket socket = null; // Client BT auf null
	
	/*Activity startet. Prüft ob BT angeschaltet ist. Wenn ja, startet Frgment "DiscoveryActivity" */
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client_socket);
		if (!_bluetooth.isEnabled()) {
			finish();
			return;
		}
		
		Intent intent = new Intent(this, DiscoveryActivity.class);
		
		//Handlungsnachricht 
		Toast.makeText(this, "select device to connect", Toast.LENGTH_SHORT).show();
		
		//starten DiscoveryActivity
		startActivityForResult(intent, codeRequest);
	}
	
	// Information über das Gerät bekommen
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode != codeRequest) {
			return;
		}
		if (resultCode != RESULT_OK) {
			return;
		}
		final BluetoothDevice device = data.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE); // Device aus Intent bekommen
		new Thread() {
			public void run() {
				connect(device);
			};
		}.start();
	}
	
	// Verbinden das Gerät als Client 
	 
	protected void connect(BluetoothDevice device) {
		
		try {
			// schaffen Rfcomm BluetoothSocket
			socket = device.createRfcommSocketToServiceRecord(UUID.fromString("a60f35f0-b93a-11de-8a39-08002009c666"));
			// Verbindung von Socket initiate
			socket.connect();															
			
			
			
		} catch (IOException e) {
			Log.e("Connection failed","", e);
		} 
}
	 /* Die Daten eintippen und absenden */
	public void onSendButtonClicked(View view) {
		try{
			EditText editText = (EditText) findViewById(R.id.editText); // EditText java mit XML verbinden
			byte[] b = editText.getText().toString().getBytes(); // Daten in byte Collection
		    OutputStream outputStream = socket.getOutputStream(); // OutputStream für CleintSocket erzeugen
		    outputStream.write(b); // Daten reinschreiben
			editText.setText("");} // EditText leeren
		catch (IOException e) {
			Log.e("Can't send Date", "", e);
	}
					
	}
}

package com.bluetoothtest2;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.bluetoothtest2.IloveHAC;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class ServerDatenbank extends ListActivity
{
	/*Some constants, representing the name of the server */
	public static final String PROTOCOL_SCHEME_L2CAP = "btl2cap";
	public static final String PROTOCOL_SCHEME_RFCOMM = "btspp";
	public static final String PROTOCOL_SCHEME_BT_OBEX = "btgoep";
	public static final String PROTOCOL_SCHEME_TCP_OBEX = "tcpobex";
	
	
	private static final String TAG = ServerSocketActivity.class.getSimpleName();
	private Handler _handler = new Handler();
	
	/* GetDefaultAdapter */
	private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter();
	
	/* Server */
	private BluetoothServerSocket _serverSocket;
	private IloveHAC mDatabaseHelper;
	private SQLiteDatabase mSqLiteDatabase;
	
	/* Thread-Listen */
	private Thread _serverWorker = new Thread() {
		public void run() {
			listen();
		};
	};
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
				WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		setContentView(R.layout.activity_server_datenbank);
		if (!_bluetooth.isEnabled()) {
			finish();
			return;
		}
		/* Start listen */
		_serverWorker.start();
	}
	protected void onDestroy() {
		super.onDestroy();
		shutdownServer();
	}
	protected void finalize() throws Throwable {
		super.finalize();
		shutdownServer();
	}
	/* Stop server */
	private void shutdownServer() {
		new Thread() {
			public void run() {
				_serverWorker.interrupt();
				if (_serverSocket != null) {
					try {
						/* close sever */
						_serverSocket.close();
					} catch (IOException e) {
						Log.e("EF-BTBee", "", e);
					}
					_serverSocket = null;
				}
			};
		}.start();
	}
	public void onButtonClicked(View view) {
		shutdownServer();
	}
	protected void listen() {
		try {
			/* Create BT service
			 * param£ºserver name¡¢UUID
			 */
	
			_serverSocket = _bluetooth.listenUsingRfcommWithServiceRecord(PROTOCOL_SCHEME_RFCOMM,
					UUID.fromString("a60f35f0-b93a-11de-8a39-08002009c666"));
			
			/* Client devices*/
			final List<String> lines = new ArrayList<String>();
			_handler.post(new Runnable() {
				public void run() {
					lines.add("Rfcomm server started...");
					
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							ServerDatenbank.this,
							android.R.layout.simple_list_item_1, lines);
					setListAdapter(adapter);
					
				}
			});
			/* accept client request */
			BluetoothSocket socket = _serverSocket.accept();
			Log.d("EF-BTBee", ">>Accept Client Request");
			
			/* Processing the request content*/
			if (socket != null) {
				InputStream inputStream = socket.getInputStream();
				int read = -1;
				final byte[] bytes = new byte[2048];
				for (; (read = inputStream.read(bytes)) > -1;) {
					 final int count = read;
					_handler.post(new Runnable() {
						public void run() {
							
							String s = new String(bytes, 0, count);
							lines.add(s);
							ArrayAdapter<String> adapter = new ArrayAdapter<String>(
									ServerDatenbank.this,
									android.R.layout.simple_list_item_1, lines);
							setListAdapter(adapter);
						}
					});
					String s = new String(bytes, 0, read);
					IloveHAC mDatabaseHelper = new IloveHAC(this);
			        SQLiteDatabase mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
			        ContentValues cv = new ContentValues();
			        cv.put(mDatabaseHelper.POPULATION_COLUMN,Integer.parseInt(s));
			        mSqLiteDatabase.insert("Population",null,cv);
				}
				
				Log.d("EF-BTBee", ">>Server is over!!");
			}
			} catch (IOException e) {
				Log.e("EF-BTBee", "", e);
			} finally {
		}
	}
		public void onDatenbankButtonClicked(View view)
		{

			Intent enabler = new Intent(this, DatenbankTabelle.class);
			startActivity(enabler);
		}
	}




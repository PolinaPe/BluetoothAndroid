package com.bt26Juni;

import java.io.InputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.achartengine.GraphicalView;
import org.achartengine.model.XYSeries;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class Producer implements Runnable {
	
	private BluetoothServerSocket _serverSocket;
	private BluetoothAdapter _bluetooth;
	private GraphicalView mChart;
	private XYSeries visitsSeries;
	private BlockingQueue<QueueObjekt> bq = null;
	
    
	
	Producer(BluetoothServerSocket _serverSocket,BluetoothAdapter _bluetooth, GraphicalView mChart, XYSeries visitsSeries, BlockingQueue<QueueObjekt> queue){
		
		this._serverSocket = _serverSocket;
		this._bluetooth = _bluetooth;
		this.mChart = mChart;
		this.visitsSeries = visitsSeries;
		this.bq = queue;
	}
	
	public void run(){
		
		
		try {
			/* Create BT service
			 * param£ºserver name¡¢UUID
			 */
	
			_serverSocket = _bluetooth.listenUsingRfcommWithServiceRecord("BT26Juni",
					UUID.fromString("a60f35f0-b93a-11de-8a39-08002009c666"));
			
		
			// ClientSocket zustimmen
			BluetoothSocket socket = _serverSocket.accept();
			Log.d("EF-BTBee", ">>Accept Client Request");
			
			// Daten aus Client
			if (socket != null) {
				Log.d("EF-BTBee", ">>Funktioniert");
				InputStream inputStream = socket.getInputStream();
				final byte[] bytes = new byte[2048];	
				int read = -1;
				
				for (int i = 1; (read = inputStream.read(bytes)) > -1; i++) {
					String s = new String(bytes, 0, read);	
					String [] values = new String[2];
			        values[0] = Integer.toString(i);
                    values[1] = s;
                    visitsSeries.add(Integer.parseInt(values[0]), Integer.parseInt(values[1]));
                    mChart.repaint();
                    String time = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(new Date());
                    bq.put(new QueueObjekt(s, time));
                   
                    
                    }
			}
			} catch (Exception e) {
				Log.e("EF-BTBee", "", e);
			} 
		}   

	}



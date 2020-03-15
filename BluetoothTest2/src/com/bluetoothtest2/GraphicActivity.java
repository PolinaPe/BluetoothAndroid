package com.bluetoothtest2;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import android.app.Activity;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

public class GraphicActivity extends Activity
{
	   private GraphicalView mChart;
	   
	    private XYSeries visitsSeries ;
	    private XYMultipleSeriesDataset dataset;
	 
	    private XYSeriesRenderer visitsRenderer;
	    private XYMultipleSeriesRenderer multiRenderer;
	    
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
		setContentView(R.layout.activity_graphic);
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
			 // Creating an  XYSeries for Visits
	        visitsSeries = new XYSeries("Unique Visitors");
	        visitsSeries.add(15, 39);
	        // Creating a dataset to hold each series
	        dataset = new XYMultipleSeriesDataset();
	        // Adding Visits Series to the dataset
	        dataset.addSeries(visitsSeries);
	        
	 
	        // Creating XYSeriesRenderer to customize visitsSeries
	        visitsRenderer = new XYSeriesRenderer();
	        visitsRenderer.setColor(Color.BLUE);
	        visitsRenderer.setPointStyle(PointStyle.CIRCLE);
	        visitsRenderer.setFillPoints(true);
	        visitsRenderer.setLineWidth(20);
	        visitsRenderer.setDisplayChartValues(true);
	 
	        // Creating a XYMultipleSeriesRenderer to customize the whole chart
	        multiRenderer = new XYMultipleSeriesRenderer();
	 
	        multiRenderer.setChartTitle("Visits Chart");
	        multiRenderer.setXTitle("Seconds");
	        multiRenderer.setYTitle("Count");
	        multiRenderer.setZoomButtonsVisible(true);
	 
	        multiRenderer.setXAxisMin(0);
	        multiRenderer.setXAxisMax(10);
	 
	        multiRenderer.setYAxisMin(0);
	        multiRenderer.setYAxisMax(300);
	 
	        multiRenderer.setBarSpacing(2);
	 
	        // Adding visitsRenderer to multipleRenderer
	        // Note: The order of adding dataseries to dataset and renderers to multipleRenderer
	        // should be same
	        multiRenderer.addSeriesRenderer(visitsRenderer);
	 
	        // Getting a reference to LinearLayout of the MainActivity Layout
	        LinearLayout chartContainer = (LinearLayout) findViewById(R.id.chart_container);
	 
	        mChart = (GraphicalView) ChartFactory.getLineChartView(getBaseContext(), dataset, multiRenderer);
	 
	        // Adding the Line Chart to the LinearLayout
	        chartContainer.addView(mChart);
	    

	
				Log.d("EF-BTBee", ">>Server is over!!");
			
			} catch (IOException e) {
				Log.e("EF-BTBee", "", e);
			} finally {
		}
	}
}

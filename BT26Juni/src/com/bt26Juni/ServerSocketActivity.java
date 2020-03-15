package com.bt26Juni;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.bt26Juni.DatenSpeicher;
import com.bt26Juni.Producer;
import com.bt26Juni.Consumer;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;


public class ServerSocketActivity extends Activity
{

	
	
	private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter();// BluetoothAdpter erzeugen
	private BluetoothServerSocket _serverSocket;     // ServerSocket
	private DatenSpeicher mDatabaseHelper; //SQLiteOpenHelper
	private SQLiteDatabase mSqLiteDatabase; // SqliteDatabase
    private GraphicalView mChart; // Graphic
    
    private XYSeries visitsSeries; // Behälter für 2 XYValues
    private XYMultipleSeriesDataset dataset; // Behälter für alle XY Values
 
    private XYSeriesRenderer visitsRenderer; // Aussehen von Paar XYValues
    private XYMultipleSeriesRenderer multiRenderer; // Aussehen von allen XYValues
	
	
	
    
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server_socket);
		LinearLayout layout = (LinearLayout) findViewById(R.id.chart_container);
		if (!_bluetooth.isEnabled()) {
			finish();
			return;
		}
		
		mDatabaseHelper = new DatenSpeicher(this);
		visitsSeries = new XYSeries("Unique Visitors"); // erzeugen ein Paar XY und der Name der GraphicLinie eingeben
        dataset = new XYMultipleSeriesDataset(); 
        dataset.addSeries(visitsSeries);
 
        // Erzeugen XYSeriesRenderer 
        visitsRenderer = new XYSeriesRenderer();
        visitsRenderer.setColor(Color.BLUE); // LinieFarbe
        visitsRenderer.setFillPoints(true); // Aussehen von dem ValuePunkt
        visitsRenderer.setLineWidth(2); // die Breite von der Linie
       
 
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
        multiRenderer.addSeriesRenderer(visitsRenderer);
        // mChart erzeugen
        mChart = (GraphicalView) ChartFactory.getLineChartView(getBaseContext(), dataset, multiRenderer);
		 layout.addView(mChart);
		// 2 Threads erzeugen und starten 
	     BlockingQueue<QueueObjekt> bq = new ArrayBlockingQueue<QueueObjekt>(1000);
	        
	        Producer producer = new Producer(_serverSocket, _bluetooth, mChart, visitsSeries, bq);
	        Consumer consumer = new Consumer( mDatabaseHelper, mSqLiteDatabase, bq);

	        new Thread(producer).start();
	        new Thread(consumer).start();
		}
	

	protected void onDestroy() {
		super.onDestroy();
		if (_serverSocket != null) {
			try {
				// Server schliessen
				_serverSocket.close();
			} catch (IOException e) {
				Toast.makeText(this, "Server kann nicht geschlossen werden", Toast.LENGTH_SHORT).show();
			}
	}
}
}
	
	
	

	
		

	
	
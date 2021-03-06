package com.example.audiosender;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.annotation.TargetApi;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.NoiseSuppressor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends ActionBarActivity {
	
	public static Button startButton,stopButton;
	public static EditText ip_field;

	public byte[] buffer;
	public static DatagramSocket socket;
	private int port=50005;
	AudioRecord recorder;

	private int sampleRate = 16000;
	@SuppressWarnings("deprecation")
	private int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;    
	private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;       
	int minBufSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
	private boolean status = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

	    //minBufSize += 1024;
	    System.out.println("minBufSize: " + minBufSize);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}

	}
	    public void onStop(View arg0) {
	    	if (status) {
	                status = false;
	                recorder.stop();
	                recorder.release();
	                Log.d("VS","Recorder released");
	    	}
	    }
	
	    public void onStart(View arg0) {
	    	if (status == false) {
	    		ip_field = (EditText) findViewById(R.id.ip_field);
	                status = true;
	                startStreaming();   
	    	}
	    }
	    

	public void startStreaming() {


	    Thread streamThread = new Thread(new Runnable() {

	    	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	    	public void SupressAudio(AudioRecord ar) {

	            int id = ar.getAudioSessionId();
	            NoiseSuppressor.create(id);
	            AcousticEchoCanceler.create(id);
	    	}
	    	
	        @Override
	        public void run() {
	            try {

	                DatagramSocket socket = new DatagramSocket();
	                Log.d("VS", "Socket Created");

	                byte[] buffer = new byte[minBufSize];

	                Log.d("VS","Buffer created of size " + minBufSize);
	                DatagramPacket packet;

	                final InetAddress destination = InetAddress.getByName(ip_field.getText().toString());
	                Log.d("VS", "Address retrieved");


	                recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,sampleRate,channelConfig,audioFormat,minBufSize*2);
	                Log.d("VS", "Recorder initialized");
	                
	                SupressAudio(recorder);

	                recorder.startRecording();


	                while(status == true) {


	                    //reading data from MIC into buffer
	                    minBufSize = recorder.read(buffer, 64, buffer.length - 64);

	                    //putting buffer in the packet
	                    packet = new DatagramPacket (buffer,buffer.length,destination,port);

	                    Thread senderPacket = new Thread(new PacketSender(socket, packet));
	                    senderPacket.start();
	                    System.out.println("BufferSize: " +minBufSize);


	                }

	                socket.disconnect();
	                socket.close();
	            } catch(UnknownHostException e) {
	                Log.e("VS", "UnknownHostException");
	            } catch (IOException e) {
	                e.printStackTrace();
	                Log.e("VS", "IOException");
	            } 
	        }

	    });
	    streamThread.start();
	 }
	
	public static class PacketSender implements Runnable {
		
		DatagramSocket sock;
		DatagramPacket pack;
		
		public PacketSender(DatagramSocket sock, DatagramPacket pack) {
			this.sock = sock;
			this.pack = pack;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				sock.send(pack);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
                Log.e("VS", "IOException");
			}
		}
		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			
			return rootView;
		}
	}

}

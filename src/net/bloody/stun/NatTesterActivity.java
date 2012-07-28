package net.bloody.stun;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;


import net.bloody.stun.R;
import net.bloody.stun.client.StunClientInstaller;
import net.bloody.stun.tester.StunTester;
import net.bloody.system.SystemChecker;
import net.bloody.wifi.WifiConfigurationHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NatTesterActivity extends Activity {
    /** Called when the activity is first created. */
	private static final String tag = "bloody";
	private static Context context;
	
	private TextView tvSummary;
	private TextView tvResult;
	private Button btStartStop;
	private Button btSendMail;
	private boolean isRunning = false;
	final String serverIpAddress = "112.220.75.19";
	private String result = "";
	private Thread testThread;
	private String resultDirName;
	private final String resultFileName = "result.txt";
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nat_tester);
        
        context = this.getBaseContext();
         
        tvSummary = (TextView) this.findViewById(R.id.nat_tester_summary);
        tvResult = (TextView) this.findViewById(R.id.nat_tester_result);
        
        
        
        Log.i(tag, "start-stop : " + this.findViewById(R.id.nat_tester_start_stop).toString());
     
        
        btStartStop = (Button) this.findViewById(R.id.nat_tester_start_stop);
        btSendMail = (Button) this.findViewById(R.id.nat_tester_send_mail);
        
        tvSummary.setText("server ip : " + serverIpAddress);
       
        Log.w(tag, "package dir : " + this.getPackageResourcePath());
        Log.w(tag, "codepath : " + this.getPackageCodePath());
        
		
        btSendMail.setVisibility(View.GONE);
        
    }
    
    
    
	public void onStartStop(View v) {
		
		if (isRunning) {
			stopTest();
		}
		
		else {
			
			startTest();
		
		}
	}
	
	public void onSendMail(View v) {
		
		
		sendMail(net.bloody.system.Environment.getInstance(context).getSystemChecker(), resultFileName);
		
	
	}

	private void stopTest() {
		
		if (testThread != null) {
			testThread.stop();
			testThread = null;
			isRunning = false;
		}
		
		btStartStop.setText("Start Test");
	}
	
	
	private void startTest() {
		
	    
		btSendMail.setVisibility(View.GONE);
		tvResult.setText("");
		
		result = "";
		
		
        resultDirName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/stundata/";
        
        {
			File dir = new File(resultDirName);
		
			dir.mkdir();
		
		}
        final String workingDirName = context.getFilesDir().getAbsolutePath() + "/stundata/";
        
        {
			File dir = new File(workingDirName);
		
			dir.mkdir();
		
		}
        
        
        Log.i(tag, "external storage : " + resultDirName);
        
     	
        
        
        final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if(msg.what == StunTester.COMPLETE_TEST) {
					isRunning = false;
					testThread = null;
					btStartStop.setText("Start Test");
					btSendMail.setVisibility(View.VISIBLE);
					
				} else if(msg.what == StunTester.PROGRESS_TEST) {
					String testCaseResult = (String) msg.obj;
					
					result+= testCaseResult + "\r\n";
					
					tvResult.setText(result);
					
				} else if (msg.what == StunTester.FAIL_TEST) {
					
				}
				
			}
		};
      	
		
		testThread = new Thread()
		{
			@Override
			public void run()
			{
		
				(new StunClientInstaller(context)).install();
				
				
				
				
				// wifi  는 꺼버리고
				try {
					WifiConfigurationHelper.setWifEnabledAndWait(context, false);
				} catch (InterruptedException e) {
					Log.e(tag, "wifi diable failed", e);
				}
				
				FileOutputStream fos = null;
				try {
					fos = new FileOutputStream(workingDirName + resultFileName);
				} catch (FileNotFoundException e) {
					Log.e(tag, "get NOT make output file : ", e);
				}
				
				
				(new StunTester(context, handler, fos, serverIpAddress)).testAll();

				try {
					fos.close();
				} catch (IOException e) {
					Log.e(tag, "fos.close error :", e);
				}
				
				unix2Dos(workingDirName + resultFileName, resultDirName + resultFileName);
				
				logcatDumpFile(resultDirName + resultFileName);
				
				
				handler.sendEmptyMessage(StunTester.COMPLETE_TEST);

			}
			
			
		};

		testThread.start();
		
		isRunning = true;
		btStartStop.setText("Stop Test");
	}
 
	
	private void unix2Dos(String sourceFileName, String targetFilename) {
		File relFile = new File(sourceFileName);
		BufferedReader read;
		try {
			read = new BufferedReader(new FileReader(relFile));

			File targetFile = new File(targetFilename);
			FileWriter fwri = new FileWriter(targetFile);
			String line;
			while ((line = read.readLine()) != null) {
				fwri.write(line + "\r\n");
			}
			
			fwri.flush();
			fwri.close();

		} catch (FileNotFoundException e) {
			Log.e(tag, "unix2Dos : ", e);
		} catch (IOException e) {
			Log.e(tag, "unix2Dos : ", e);
		}
	}
	

	
	
	private void logcatDumpFile(String filename) {

		FileInputStream fis;
		try {
			fis = new FileInputStream(filename);

			BufferedReader br = null;
			InputStreamReader isr = null;

			isr = new InputStreamReader(fis);

			br = new BufferedReader(isr);

			String thisLine;

			while ((thisLine = br.readLine()) != null) {

				Log.v(tag, thisLine);

			}

			fis.close();

		} catch (Exception e) {
			Log.e(tag, "dump error : ", e);
		} 

	}
    
	private void sendMail(SystemChecker systemChecker, String filename) {
		Uri uri = Uri.parse("file:///sdcard/stundata/" + filename);
		Log.i(tag, "sendfile: " + filename);

		Intent sendIntent = new Intent(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_EMAIL,
				new String[] { "webmaster@netmanias.com" });
		sendIntent.putExtra(Intent.EXTRA_CC, 
				new String[] { "joe.joey@gmail.com", "cmyoo@netmanias.com" });
		
		sendIntent.putExtra(Intent.EXTRA_SUBJECT, "NatTester result");
		sendIntent.setType("plain/text");
		sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
		sendIntent.putExtra(Intent.EXTRA_TEXT, "summary \r\n" + systemChecker);
		startActivity(Intent.createChooser(sendIntent, "send result"));

	}
    
  
}
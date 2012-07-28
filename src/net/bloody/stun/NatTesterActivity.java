package net.bloody.stun;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;

import net.bloody.stun.R;
import net.bloody.stun.client.StunClient;
import net.bloody.stun.client.StunClientInstaller;
import net.bloody.system.SystemChecker;
import net.bloody.system.SystemCheckerImpl;
import net.bloody.wifi.WifiConfigurationHelper;




import de.javawi.jstun.attribute.MessageAttributeException;
import de.javawi.jstun.attribute.MessageAttributeParsingException;
import de.javawi.jstun.header.MessageHeaderParsingException;
import de.javawi.jstun.test.DiscoveryInfo;
import de.javawi.jstun.test.DiscoveryTest;
import de.javawi.jstun.util.UtilityException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

public class NatTesterActivity extends Activity {
    /** Called when the activity is first created. */
	private static final String tag = "bloody";
	private static Context context;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        context = this.getBaseContext();
       
        Log.w(tag, "dir : " + this.getPackageResourcePath());
        Log.w(tag, "codepath : " + this.getPackageCodePath());
        
        
        final String dirName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/stundata/";
        {
			File dir = new File(dirName);
		
			dir.mkdir();
		
		}
        
        final SystemChecker systemChecker = new SystemCheckerImpl(context);
		
			
		
		Thread t = new Thread()
		{
			@Override
			public void run()
			{
		
				(new StunClientInstaller(context)).install();
				
				try {
					WifiConfigurationHelper.setWifEnabledAndWait(context, false);
				} catch (InterruptedException e) {
					Log.e(tag, "wifi diable failed", e);
				}
				
				InetAddress inetAddress =  null;
				try {
					inetAddress = InetAddress.getByName(getLocalAddress());
				} catch (UnknownHostException e) {
					Log.e(tag, "inetaddress error : ", e);
				}
				
				Log.i(tag, "inetaddress : " + inetAddress);
				
				DiscoveryTest test = new DiscoveryTest(inetAddress, "112.220.75.19", 3478);

				try {
					DiscoveryInfo discoveryInfo = test.test();
					Log.i(tag, "result : " + discoveryInfo);
				} catch (SocketException e) {
					// TODO Auto-generated catch block
					Log.e(tag, "test error : ", e);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					Log.e(tag, "test error : ", e);
				} catch (MessageAttributeParsingException e) {
					// TODO Auto-generated catch block
					Log.e(tag, "test error : ", e);
				} catch (MessageHeaderParsingException e) {
					// TODO Auto-generated catch block
					Log.e(tag, "test error : ", e);
				} catch (UtilityException e) {
					// TODO Auto-generated catch block
					Log.e(tag, "test error : ", e);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Log.e(tag, "test error : ", e);
				} catch (MessageAttributeException e) {
					// TODO Auto-generated catch block
					Log.e(tag, "test error : ", e);
				}

				FileOutputStream fos = null;
				try {
					fos = new FileOutputStream(dirName + "result.txt");
				} catch (FileNotFoundException e) {
					Log.e(tag, "get NOT make output file : ", e);
				}

				{
					StunClient stunClient = new StunClient(context, "full",
							"tcp", "112.220.75.19", "3478");

					stunClient.execute();

					try {
						writeOutput(fos, stunClient.getOutput());
						writeOutput(fos, systemChecker.toString());
					} catch (IOException e) {
						Log.e(tag, "writeOutput error : ", e);
					}

				}

				try {
					fos.close();
				} catch (IOException e) {
					Log.e(tag, "fos.close error :", e);
				}

			}
		};

		t.start();
        
    }
    
    private String getLocalAddress() {
    	queryNetworkInterfaces();
    	String localAddress = "127.0.0.1";
    	try {
    	    Socket socket = new Socket("www.google.com", 80);
    	    Log.i(tag, socket.getLocalAddress().toString());
    	    localAddress = socket.getLocalAddress().toString().replaceAll("/", "");
    	} catch (Exception e) {
    	    Log.e(tag, "can NOT get localaddress", e);
    	}
    	
    	return localAddress;
    }
    
    public void queryNetworkInterfaces() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        //return inetAddress.getHostAddress().toString();
                    	
                    	Log.i(tag, String.format("interface %s - address %s", intf.getDisplayName(), inetAddress.getHostAddress()));
                    }
                }
            }
        } catch (SocketException e) {
            Log.e(tag, "can Not get interface status : ", e);
        }
        
        return;
    }
    
	private void writeOutput(FileOutputStream fos, String output)
			throws IOException {

		final long length = output.getBytes().length;
		fos.write(output.getBytes(), 0, (int) length);

	}
    
	private void sendMail() {
		String filename = "result.txt";
		Uri uri = Uri.parse("file:///sdcard/download/stundata/" + filename);
		Log.i(tag, "sendfile: " + filename);

		Intent sendIntent = new Intent(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_EMAIL,
				new String[] { "joe.joey@gmail.com", "joey.joe@samsung.com" });
		sendIntent.putExtra(Intent.EXTRA_SUBJECT, "result");
		sendIntent.setType("plain/text");
		sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
		sendIntent.putExtra(Intent.EXTRA_TEXT, "log from : ");
		startActivity(Intent.createChooser(sendIntent, "send u-Ready dump log"));

	}
    
  
}
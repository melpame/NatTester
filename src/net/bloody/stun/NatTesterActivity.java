package net.bloody.stun;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.zip.CRC32;

import net.bloody.stun.R;




import de.javawi.jstun.attribute.MessageAttributeException;
import de.javawi.jstun.attribute.MessageAttributeParsingException;
import de.javawi.jstun.header.MessageHeaderParsingException;
import de.javawi.jstun.test.DiscoveryInfo;
import de.javawi.jstun.test.DiscoveryTest;
import de.javawi.jstun.test.demo.DiscoveryTestDemo;
import de.javawi.jstun.util.UtilityException;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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
        
        
		
		Thread t = new Thread()
		{
			@Override
			public void run()
			{
		
				installNativeBin();
				
				InetAddress inetAddress =  null;
				try {
					inetAddress = InetAddress.getByName("33.233.223.57");
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
        	// InetAddress inetAddress = InetAddress.getLocalHost();
        
        	//DiscoveryTest test = new DiscoveryTest(iaddress, stunServer, port);
        	
        	
        	{
        		final ArrayList<String> commandLine = new ArrayList<String>();
        		commandLine.add(applicationRoot + "/bin/stunclient");
        		commandLine.add("112.220.75.19");
        		commandLine.add("--mode");
        		commandLine.add("full");
        		commandLine.add("--protocol");
        		commandLine.add("tcp");
        	
			

        		Log.i(tag, "command : " + commandLine);

        		try {
					Process process = Runtime.getRuntime().exec(
						commandLine.toArray(new String[0]));
					
					writeLogcat(process.getInputStream());
				} catch (IOException e) {
					Log.e(tag, "test error : ", e);
				}
			
        	}
        	
				
			}
		};
		
		t.start();
        
    }
    
    private void writeLogcat(InputStream fis) throws IOException {

    	BufferedReader br = null;
    	InputStreamReader isr = null;

    	isr = new InputStreamReader(fis);

    	br = new BufferedReader(isr);

    	String thisLine;

    	while ((thisLine = br.readLine()) != null) {

	
    		Log.w(tag, "result : " + thisLine);
    	}


    }
    
    private static String applicationRoot;
    
    
    private void installNativeBin() {
    	applicationRoot = context.getFilesDir().getParent();
    	
   
    	Log.d(tag, "Current directory is "+ applicationRoot);
    	
    	checkDirs();
    	
    	copyFile(applicationRoot + "/bin/stunclient", "0777", R.raw.stunclient);
    	
    	
    }
    
    
    private String copyFile(String filename, String permission, int ressource) {
    	String result = copyFile(filename, ressource);
    	if (result != null) {
    		return result;
    	}
    	try {
 
    		
    		{
        		final ArrayList<String> commandLine = new ArrayList<String>();
        		commandLine.add("chmod");
        		commandLine.add(permission);
        		commandLine.add(filename);
        		
    
        		Log.i(tag, "command : " + commandLine);

        		try {
					Process process = Runtime.getRuntime().exec(
						commandLine.toArray(new String[0]));
				} catch (IOException e) {
					Log.e(tag, "copyFile error : ", e);
				}
			
        	}
    	} catch(Exception ignore) {}
    	
    	return result;
    }
    
    
    private String copyFile(String filename, int ressource) {
    	File outFile = new File(filename);
    	Log.d(tag, "Copying file '"+filename+"' ...");
    	InputStream is = context.getResources().openRawResource(ressource);
    	byte buf[] = new byte[1024];
        int len;
        try {
        	OutputStream out = new FileOutputStream(outFile);
        	while((len = is.read(buf))>0) {
				out.write(buf,0,len);
			}
        	out.close();
        	is.close();
		} catch (IOException e) {
			return "Couldn't install file - "+filename+"!";
		}
		return null;
    }
    
    private void checkDirs() {
    	File dir = new File(applicationRoot);
    	if (dir.exists() == false) {
    			Log.e(tag, "Application data-dir does not exist!");
    	}
    	else {
    		String[] dirs = { "/bin", "/var", "/conf" };
    		for (String dirname : dirs) {
    			dir = new File(applicationRoot + dirname);
    	    	if (dir.exists() == false) {
    	    		if (!dir.mkdir()) {
    	    			Log.e(tag, "Couldn't create " + dirname + " directory!");
    	    		}
    	    	}
    	    	else {
    	    		Log.d(tag, "Directory '"+dir.getAbsolutePath()+"' already exists!");
    	    	}
    		}
    	}
    }
}
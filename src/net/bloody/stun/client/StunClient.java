package net.bloody.stun.client;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;


/*
 *                                             
./stunclient 112.220.75.19 --mode full --protocol udp 
./stunclient 112.220.75.19 --mode full --protocol udp --localport 2000
./stunclient 112.220.75.19 --mode full --protocol udp --localport 20000
./stunclient 112.220.75.19 --mode full --protocol udp --localport 40000
./stunclient 112.220.75.19 --mode full --protocol udp --localport 50000
./stunclient 112.220.75.19 --mode full --protocol udp --localport 60000
./stunclient 112.220.75.19 --mode full --protocol tcp --localport 2000
./stunclient 112.220.75.19 --mode full --protocol tcp --localport 20000
./stunclient 112.220.75.19 --mode full --protocol tcp --localport 40000
./stunclient 112.220.75.19 --mode full --protocol tcp --localport 50000
./stunclient 112.220.75.19 --mode full --protocol tcp --localport 60000

java -cp jstun.jar:slf4j-api-1.5.6.jar:slf4j-jdk14-1.5.6.jar de.javawi.jstun.test.demo.DiscoveryTestDemo

 */
public class StunClient {
	private static final String tag = "bloody";
	
	public StunClient(Context context, String mode, String protocol, String serverIpAddress, String port) {
		this.context = context;
		this.mode = mode;
		this.protocol = protocol;
		this.serverIpAddress = serverIpAddress;
		this.port = port;
	}
	
	
	
	public boolean execute() {
		
		boolean result = false;
		final String applicationRoot = context.getFilesDir().getParent();
  
		{
    		final ArrayList<String> commandLine = new ArrayList<String>();
    		commandLine.add(applicationRoot + "/bin/stunclient");
    		commandLine.add(serverIpAddress);
    		commandLine.add("--mode");
    		commandLine.add(mode);
    		commandLine.add("--protocol");
    		commandLine.add(protocol);
    		commandLine.add("--localport");
    		commandLine.add(port);
    	
		

    		Log.i(tag, "command : " + commandLine);

    		try {
				Process process = Runtime.getRuntime().exec(
					commandLine.toArray(new String[0]));
				
				saveProcessOutput(process.getInputStream());
				
				
				result = true;
				
			} catch (IOException e) {
				Log.e(tag, "test error : ", e);
			}
		
    	}
		
		return result;
	}

	private void saveProcessOutput(InputStream fis) throws IOException {

		BufferedReader br = null;
		InputStreamReader isr = null;

		isr = new InputStreamReader(fis);

		br = new BufferedReader(isr);

		String thisLine;

		while ((thisLine = br.readLine()) != null) {

			output += thisLine;

			Log.v(tag, "result : " + thisLine);

		}

	}
	
	public String getOutput() {
		return output;
	}
	
	
	private final Context context;
	private final String mode;
	private final String protocol;
	private final String serverIpAddress;
	private final String port;
	private String output;

}

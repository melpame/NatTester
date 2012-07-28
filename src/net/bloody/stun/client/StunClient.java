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

	public enum StunMode {
		Full,
		;
		
		@Override
		public
		String toString() {
			return this.name().toLowerCase();
		}
	}
	
	
	public enum StunProtocol {
		Tcp,
		Udp,
		;
		
		@Override
		public
		String toString() {
			return this.name().toLowerCase();
		}
	}
	
	public StunClient(Context context, StunMode mode, StunProtocol protocol, String serverIpAddress, String port) {
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
    		commandLine.add(mode.toString());
    		commandLine.add("--protocol");
    		commandLine.add(protocol.toString());
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
				output = "test error : " + e.getMessage();
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

			output += thisLine + "\r\n";

			Log.v(tag, "result : " + thisLine);

		}

	}
	
	public String getOutput() {
		return output;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("mode : ");
		sb.append(this.mode.toString());
		sb.append(", ");
		sb.append("protocol : ");
		sb.append(this.protocol.toString());
		sb.append(", ");
		sb.append("server : ");
		sb.append(this.serverIpAddress + ":" + port);
		
		
		return sb.toString();
	}
	
	
	private final Context context;
	private final StunMode mode;
	private final StunProtocol protocol;
	private final String serverIpAddress;
	private final String port;
	private String output;

}

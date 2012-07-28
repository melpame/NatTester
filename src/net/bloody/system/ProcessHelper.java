package net.bloody.system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Hashtable;

import net.bloody.utils.Logger;

public class ProcessHelper {
	private Hashtable<String,String> runningProcesses = new Hashtable<String,String>();
	public boolean isProcessRunning(String processName) throws Exception {

		boolean processIsRunning = false;
		Hashtable<String, String> tmpRunningProcesses = new Hashtable<String, String>();
		File procDir = new File("/proc");
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				try {
					Integer.parseInt(name);
				} catch (NumberFormatException ex) {
					return false;
				}
				return true;
			}
		};
		File[] processes = procDir.listFiles(filter);
		for (File process : processes) {
			String cmdLine = "";
			// Checking if this is a already known process
			if (this.runningProcesses.containsKey(process.getAbsoluteFile()
					.toString())) {
				cmdLine = this.runningProcesses.get(process.getAbsoluteFile()
						.toString());
			} else {
				ArrayList<String> cmdlineContent = this
						.readLinesFromFile(process.getAbsoluteFile()
								+ "/cmdline");
				if (cmdlineContent != null && cmdlineContent.size() > 0) {
					cmdLine = cmdlineContent.get(0);
				}
			}
			// Adding to tmp-Hashtable
			tmpRunningProcesses.put(process.getAbsoluteFile().toString(),
					cmdLine);

			// Checking if processName matches
			if (cmdLine.contains(processName)) {
				processIsRunning = true;
			}
		}
		// Overwriting runningProcesses
		this.runningProcesses = tmpRunningProcesses;
		return processIsRunning;
	}

	public boolean hasRootPermission() {
		boolean rooted = true;
		try {
			File su = new File("/system/bin/su");
			if (su.exists() == false) {
				su = new File("/system/xbin/su");
				if (su.exists() == false) {
					rooted = false;
				}
			}
		} catch (Exception e) {
			Logger.d("Can't obtain root - Here is what I know: "
					+ e.getMessage());
			rooted = false;
		}
		return rooted;
	}
	
	  public ArrayList<String> readLinesFromFile(String filename) {
	    	String line = null;
	    	BufferedReader br = null;
	    	InputStream ins = null;
	    	ArrayList<String> lines = new ArrayList<String>();
	    	File file = new File(filename);
	    	if (file.canRead() == false)
	    		return lines;
	    	try {
	    		ins = new FileInputStream(file);
	    		br = new BufferedReader(new InputStreamReader(ins), 8192);
	    		while((line = br.readLine())!=null) {
	    			lines.add(line.trim());
	    		}
	    	} catch (Exception e) {
	    		Logger.d("Unexpected error - Here is what I know: "+e.getMessage());
	    	}
	    	finally {
	    		try {
	    			ins.close();
	    			br.close();
	    		} catch (Exception e) {
	    			// Nothing.
	    		}
	    	}
	    	return lines;
	    }
	    
	    public boolean writeLinesToFile(String filename, String lines) {
			OutputStream out = null;
			boolean returnStatus = false;
			Logger.d("Writing " + lines.length() + " bytes to file: " + filename);
			try {
				out = new FileOutputStream(filename);
	        	out.write(lines.getBytes());
	        	out.flush();
			} catch (Exception e) {
				Logger.d("Unexpected error - Here is what I know: "+e.getMessage());
			}
			finally {
	        	try {
	        		if (out != null)
	        			out.close();
	        		returnStatus = true;
				} catch (IOException e) {
					returnStatus = false;
				}
			}
			return returnStatus;
	    }
}

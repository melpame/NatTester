package net.bloody.system;

import android.util.Log;

public class NativeTask {
    
	public static final String tag= "uready";

	static {
        try {
            Log.i(tag, "Trying to load libnativetask.so");
            System.loadLibrary("nativetask");
        }
        catch (UnsatisfiedLinkError ule) {
            Log.e(tag, "Could not load libnativetask.so");
        }
    }
    public static native String getProp(String name);
    public static native int runCommand(String command);
}

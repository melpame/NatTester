package net.bloody.system;

import android.content.Context;

public class Environment {
	private static Environment instance;
	private final SystemChecker systemChecker;
	private final Context context;
	
	private Environment(Context context) {
		this.context = context;
		this.systemChecker = new SystemCheckerImpl(context);
	}
	
	public static Environment getInstance(Context context) {
		
		if (instance == null) {
			instance = new Environment(context);
		}
		
		return instance;
	}
	
	public SystemChecker getSystemChecker() {
		return systemChecker;
	}
	
}

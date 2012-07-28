package net.bloody.utils;

import android.util.Log;

public class Logger {
	private static final String tag = "bloody";
	private static boolean isVerboseMode = false;
	

	private static String getStackTraceInfo()
	{
		StackTraceElement[] trace = new Exception().getStackTrace();
		String stackTraceInfo = null;
		
		if(trace != null && trace.length > 0) {
			StackTraceElement traceElement = null;

			for(int callStack = trace.length - 1; callStack > 0; callStack--)
			{
				traceElement = trace[callStack];
				if(traceElement.getClassName().startsWith("net.bloody.utils.Logger")) {
					StackTraceElement tempTraceElement = trace[callStack+1];
					if(tempTraceElement.getClassName().startsWith("net.bloody.Util"))
					{
						traceElement = trace[callStack + 2];
					}
					else
					{
						traceElement = trace[callStack + 1];
					}
					break;
				}
			}
			
			String[] tempStringArray = traceElement.getClassName().split("\\.");
			String className = tempStringArray[tempStringArray.length - 1];
			String methodName = traceElement.getMethodName();
			String lineNumber = String.format("%d", traceElement.getLineNumber());
			stackTraceInfo = new String("(" + className + "." + methodName + ") (" + lineNumber + ") ");
			return stackTraceInfo;
		}
		else
		{
			stackTraceInfo = new String("");
			return stackTraceInfo;
		}
	}
	
	public static void v(final String log) {
		if (isVerboseMode) {
			String stackTraceInfo = getStackTraceInfo();
			
			Log.v(tag, stackTraceInfo + log);
		}
	}

	public static void d(final String log) {
		String stackTraceInfo = getStackTraceInfo();
		
		Log.d(tag, stackTraceInfo + log);
	}
	
	public static void i(final String log) {
		String stackTraceInfo = getStackTraceInfo();

		Log.i(tag, stackTraceInfo + log);
	}

	public static void i(final String log, Throwable t) {
		String stackTraceInfo = getStackTraceInfo();

		Log.i(tag, stackTraceInfo + log, t);
	}
	
	public static void vi(final String log) {
		if (isVerboseMode) {
			String stackTraceInfo = getStackTraceInfo();

			Log.i(tag, stackTraceInfo + log);
		}
	}

	public static void e(final String log) {
		String stackTraceInfo = getStackTraceInfo();

		Log.e(tag, stackTraceInfo + log);
	}

	public static void w(final String log) {
		String stackTraceInfo = getStackTraceInfo();

		Log.w(tag, stackTraceInfo + log);
	}

	public static void e(String log, Throwable t) {
		String stackTraceInfo = getStackTraceInfo();

		Log.e(tag, stackTraceInfo + log, t);
	}
}

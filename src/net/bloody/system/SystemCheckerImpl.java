package net.bloody.system;

import java.io.File;
import java.util.List;

import net.bloody.utils.Logger;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;

public class SystemCheckerImpl implements SystemChecker {
	private final Context context;
	private final String model;
	private final String version_incremental;
	private final int sdk_int;
	private final String manufacturer;
	private final String code_name;
	
	public SystemCheckerImpl(Context context) {
		this.context = context;
		this.model = Build.MODEL;
		this.code_name = Build.VERSION.CODENAME;
		this.version_incremental = Build.VERSION.INCREMENTAL;
		this.sdk_int = android.os.Build.VERSION.SDK_INT;
		this.manufacturer = android.os.Build.MANUFACTURER;
		
		
	
		
		//Logger.i("info : " + (new android.os.Build()))
		
		Logger.vi("Build info Version RELEASE: " + android.os.Build.VERSION.RELEASE);
		checkVersion();
	}
	
	// release date
	private int year;
	private int month;
	private int day;
	
	private void checkVersion() {
		
		
		try {
			Logger.vi("Build info Version CODENAME: "
					+ code_name + " : "
					+ sdk_int);
			String version = version_incremental;
			
		} catch (Exception ignore) {
		}
		
	}
	
	private void checkNetwork() {
		
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		
		Logger.i("networkInfo : " + networkInfo);
		
		
	}
	
	
	private void checkTelephony() {
		
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		
		String line1Number = telephonyManager.getLine1Number();
		
		Logger.i("line1Number : " + line1Number);
		
		Logger.i("countryIos : " + telephonyManager.getNetworkCountryIso());

		Logger.i("operator : " + telephonyManager.getNetworkOperator());
		
		Logger.i("operatatorName : " + telephonyManager.getNetworkOperatorName());
		
		int networkType = telephonyManager.getNetworkType();
		
		Logger.i("netowrk Type : " + networkType);
		
		
		
		
	}
	
	public boolean checkProcess() {
		
		ActivityManager activityManager = (ActivityManager)
		context.getSystemService( Context.ACTIVITY_SERVICE );
		List<RunningAppProcessInfo> runningAppProcessInfos = activityManager.getRunningAppProcesses();
		
		for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcessInfos) {
			Logger.i("running process : " + runningAppProcessInfo.processName);
		}
		
		return true;
	}
	@Override
	public boolean isEmulator() {
		Logger.vi("isEmulator : " + Build.MODEL);
		if (model.contains("sdk")) return true;
		return false;
	}

	
		
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(model);
		sb.append(", ");
		sb.append(sdk_int);
		sb.append(", ");
		sb.append(code_name);
		sb.append(", ");
		sb.append(version_incremental);
		
		checkNetwork();
		checkTelephony();
		
		return sb.toString();
	}

}

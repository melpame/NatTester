package net.bloody.system;

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
	private final String networkOperatorName;
	private final String connectivityTypeName;
	private final String networkTypeName;
	private boolean hasDataNetwork = false;
	
	public SystemCheckerImpl(Context context) {
		this.context = context;
		this.model = Build.MODEL;
		this.code_name = Build.VERSION.CODENAME;
		this.version_incremental = Build.VERSION.INCREMENTAL;
		this.sdk_int = android.os.Build.VERSION.SDK_INT;
		this.manufacturer = android.os.Build.MANUFACTURER;
	
		
		Logger.vi("Build info Version RELEASE: " + android.os.Build.VERSION.RELEASE);
		checkVersion();
		{
			
			ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			
			NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
			
			Logger.i("networkInfo : " + networkInfo);
			
			if (networkInfo != null) {
			
				this.hasDataNetwork = true;
				this.connectivityTypeName = networkInfo.getTypeName();
			}
			else {
				this.connectivityTypeName = "no-data-network";
			}
			
		}
		
		{
			
			TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			
			String line1Number = telephonyManager.getLine1Number();
			
			Logger.i("line1Number : " + line1Number);
			
			Logger.i("countryIos : " + telephonyManager.getNetworkCountryIso());

			Logger.i("operator : " + telephonyManager.getNetworkOperator());
			
			Logger.i("operatatorName : " + telephonyManager.getNetworkOperatorName());
			
			int networkType = telephonyManager.getNetworkType();
			
			Logger.i("netowrk Type : " + networkType);
			
			if (networkType == TelephonyManager.NETWORK_TYPE_GPRS) {
				this.networkTypeName = "GPRS";
			}
			else if (networkType == TelephonyManager.NETWORK_TYPE_HSDPA) {
				this.networkTypeName = "HSDPA";
			}
			else if (networkType == TelephonyManager.NETWORK_TYPE_HSPA) {
				this.networkTypeName = "HSPA";
			}
			else if (networkType == TelephonyManager.NETWORK_TYPE_HSUPA) {
				this.networkTypeName = "HSUPA";
			}
			else if (networkType == TelephonyManager.NETWORK_TYPE_UMTS) {
				this.networkTypeName = "UMTS";
			}
			else if (networkType == TelephonyManager.NETWORK_TYPE_CDMA) {
				this.networkTypeName = "CDMA";
			}
			else if (networkType == 13) {
				this.networkTypeName = "LTE"; // NETWORK_TYPE_LTE
			}
			else if (networkType == 15) {
				this.networkTypeName = "HSPAP"; // NETWORK_TYPE_HSPAP
			}
			else {
				this.networkTypeName = "unknown : " + networkType;
			}
			
			this.networkOperatorName = telephonyManager.getNetworkOperatorName();
			
		}
	}
	
	private void checkVersion() {
		
		
		try {
			Logger.vi("Build info Version CODENAME: "
					+ code_name + " : "
					+ sdk_int);
			String version = version_incremental;
			
		} catch (Exception ignore) {
		}
		
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
		
		sb.append(this.manufacturer);
		sb.append(", ");
		sb.append(this.model);
		sb.append(", ");
		sb.append(this.sdk_int);
		sb.append(", ");
		sb.append(this.code_name);
		sb.append(", ");
		sb.append(this.version_incremental);
		sb.append(", ");
		sb.append(this.connectivityTypeName);
		sb.append(", ");
		sb.append(this.networkOperatorName);
		sb.append(", ");
		sb.append(this.networkTypeName);
		
		return sb.toString();
	}

}

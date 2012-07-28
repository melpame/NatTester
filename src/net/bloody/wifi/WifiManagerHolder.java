package net.bloody.wifi;

import android.content.Context;
import android.util.Log;


public class WifiManagerHolder {
	private static final String tag = "bloody";
	private static IWifiManager    wifiManagerHolderInstace;
	
	public static IWifiManager getInstance(Context context)
	{
		if (wifiManagerHolderInstace == null)
		{
			
			wifiManagerHolderInstace = new WifiAdapter(context);
			
			Log.i(tag, "create WifiAdapter : " + wifiManagerHolderInstace);
		}

		return wifiManagerHolderInstace;
	}
	
	public static void setInstance(Context context, IWifiManager wifiManager) {
		wifiManagerHolderInstace = wifiManager;
	}
}

package net.bloody.wifi;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.bloody.utils.Logger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;


public class WifiConfigurationHelper {
	
	public static Object getEnterpriseValue(Context context, WifiConfiguration wifiConfiguration,
	        String fieldName)
	{
		Object value = null;

		try
		{
			Field field = WifiConfiguration.class.getDeclaredField(fieldName);
			Object fieldInstance = field.get(wifiConfiguration);

			Method varNameMethod = fieldInstance.getClass().getMethod("value");

			value = varNameMethod.invoke(fieldInstance);
		} catch (Exception e)
		{
			Logger.e(e.getMessage(), e);
		}

		return value;
	}

	@SuppressWarnings("unchecked")
	public static void setEnterpriseValue(Context context, WifiConfiguration wifiConfiguration,
	        String fieldName, String value)
	{
		try
		{
			Field field = WifiConfiguration.class.getDeclaredField(fieldName);
			Object fieldInstance = field.get(wifiConfiguration);

			final Class[] parameterTypes = new Class[] { String.class };

			Method varNameMethod = fieldInstance.getClass().getMethod("setValue", parameterTypes);

			varNameMethod.invoke(fieldInstance, value);
		} catch (Exception e)
		{
			Logger.e( e.getMessage(), e);
		}
	}
	
	private static class WifiWaitSync {
        boolean isDone = false;
	};
	
	private static final int TIMEOUT_MSEC = 20000;
	private static final int WAIT_MSEC = 500;

	
	public static boolean setWifEnabledAndWait(Context context, boolean enable) throws InterruptedException {
		
		final WifiWaitSync wifiEnableSync = new WifiWaitSync();
		
		final IWifiManager wifiManager = new WifiAdapter(context);
	
		if (wifiManager.isWifiEnabled() == enable) return enable;
		
		final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				final String action = intent.getAction();
				if (action.equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {
					final boolean isConnected = intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false);
					synchronized (wifiEnableSync) {
						wifiEnableSync.isDone = true;
						wifiEnableSync.notifyAll();
					}
				}
			}
		};
		
		
		final IntentFilter intentFilter = new IntentFilter();
        
		intentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);


		context.registerReceiver(broadcastReceiver, intentFilter);
		
		
	
		synchronized (wifiEnableSync) {
			wifiEnableSync.isDone = false;
			wifiManager.setWifiEnabled(enable);
			long timeout = System.currentTimeMillis() + TIMEOUT_MSEC;
			while (System.currentTimeMillis() < timeout
					&& !wifiEnableSync.isDone)
				wifiEnableSync.wait(WAIT_MSEC);
		}
		
		context.unregisterReceiver(broadcastReceiver);
		
		return wifiManager.isWifiEnabled();
	}
	
	public static void resetWifAndWait(Context context) throws InterruptedException {
		
		WifiConfigurationHelper.setWifEnabledAndWait(context, false);
		
		WifiConfigurationHelper.setWifEnabledAndWait(context, true);
	}
	
	/*
	 * scan 결과가 얻어질때까지 waiting for TIMEOUT_MSEC
	 */
	public static List<ScanResult> getScanResult(final Context context) {
		final WifiWaitSync scanResultSync = new WifiWaitSync();
		final List<ScanResult> scanResults = new ArrayList<ScanResult>();
		final IWifiManager wifiManager = WifiManagerHolder.getInstance(context);
		
		final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
			
	        @Override
	        public void onReceive(Context context, Intent intent) {
	            final String action = intent.getAction();
	            if (action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
	            	synchronized (scanResultSync) {
						if (wifiManager.getScanResults() != null) {
							if (wifiManager.getScanResults().size() > 0) {
								scanResults.addAll(wifiManager.getScanResults());
								scanResultSync.isDone = true;
								scanResultSync.notify();
							}
							
						}
					}
	            }
	        }
	    };
	    
	    final IntentFilter intentFilter = new IntentFilter();
        
		intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);


		context.registerReceiver(broadcastReceiver, intentFilter);
	    
	    synchronized (scanResultSync) {
	    	scanResultSync.isDone = false;
			wifiManager.startScan();
			long timeout = System.currentTimeMillis() + TIMEOUT_MSEC;
			while (System.currentTimeMillis() < timeout
					&& !scanResultSync.isDone) {
				try {
					scanResultSync.wait(3000);
				} catch (InterruptedException ignore) {
				}
				
				wifiManager.startScan();
			}
		}
	    
	    context.unregisterReceiver(broadcastReceiver);
	    
	    return scanResults;
	    
	}
	
	
	
	public static void setRoamBand(final Context context, final int roamBandValue) {
		final IWifiManager wifiManager = new WifiAdapter(context);
		
		wifiManager.setRoamBand(roamBandValue);
	}
	
}

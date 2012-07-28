package net.bloody.wifi;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

class WifiAdapter implements IWifiManager {
	
	private static final String tag = "bloody";
	private final Context context;
	private WifiManager wifiManager;
	private static boolean isSupplicantConnected;
	private long sessionKey;
	private boolean isConnected;

	public WifiAdapter(Context context) {
		this.context = context;
		if(initWifiManager() != null) {
			isSupplicantConnected = wifiManager.isWifiEnabled();
		}
		
	}
	
	private WifiManager initWifiManager() {
		if (wifiManager != null) return wifiManager;
		wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		return wifiManager;
	}
	
	private void waitForApiDelay() {
		try {
			Thread.sleep(10);
		} catch (InterruptedException ignore) {
			
		}
	}
	@Override
	public int addNetwork(WifiConfiguration config) {
		Log.i(tag, String.format("wifi: addNetwork : %d connected : %s", config.networkId, isSupplicantConnected));
		if (initWifiManager() == null) return -1;
		return wifiManager.addNetwork(config);
	}
	
	@Override
	public int updateNetwork(WifiConfiguration config) {
		Log.i(tag, String.format("wifi: updateNetwork : %d connected : %s", config.networkId, isSupplicantConnected));
		if (initWifiManager() == null) return -1;
		return wifiManager.updateNetwork(config);
	}

	@Override
	public boolean disableNetwork(int networkId) {
		Log.i(tag, String.format("wifi: updateNetwork : %d connected : %s", networkId, isSupplicantConnected));
		if (initWifiManager() == null) return false;
		return wifiManager.disableNetwork(networkId);
	}

	@Override
	public boolean disconnect() {
		Log.i(tag, "wifi: disconnect connected : " + isSupplicantConnected);
		if (initWifiManager() == null) return false;
		return wifiManager.disconnect();
	}

	@Override
	public boolean enableNetwork(int netId, boolean disableOthers) {
		Log.i(tag, String.format("wifi: enableNetwork %d : disableOthers %s : isConnected: %s", netId, disableOthers, isSupplicantConnected));
		if (initWifiManager() == null) return false;
		return wifiManager.enableNetwork(netId, disableOthers);
	}

	@Override
	public List<WifiConfiguration> getConfiguredNetworks() {
		Log.i(tag, "wifi: getConfigNetworks : isConnected: " + isSupplicantConnected);
		if (initWifiManager() == null) return new ArrayList<WifiConfiguration>();
		
		List<WifiConfiguration> wifiConfigurations = wifiManager.getConfiguredNetworks();
		
		if (wifiConfigurations == null) return new ArrayList<WifiConfiguration>();
		return wifiConfigurations;
	}

	@Override
	public WifiInfo getConnectionInfo() {
		Log.i(tag, "wifi: getConnectionInfo");
		if (initWifiManager() == null) return null;
		return wifiManager.getConnectionInfo();
	}

	@Override
	public DhcpInfo getDhcpInfo() {
		
		if (initWifiManager() == null) return null;
		final DhcpInfo dhcpInfo =  wifiManager.getDhcpInfo();
		
		Log.i(tag, "wifi: getDhcpInfo : " + dhcpInfo);
		
		return dhcpInfo;
	}


	@Override
	public List<ScanResult> getScanResults() {
		Log.i(tag, "wifi: getScanResults");
		if (initWifiManager() == null) return null;
		return wifiManager.getScanResults();
	}

	@Override
	public boolean pingSupplicant() {
		if (initWifiManager() == null) return false;
		final boolean isReachable = wifiManager.pingSupplicant();
		if (!isReachable) {
			Log.e(tag, "supplicant is not reachable");
		}
		return isReachable;
	}

	@Override
	public boolean reassociate() {
		if (initWifiManager() == null) return false;
		Log.i(tag, "wifi: reassociate");
		return wifiManager.reassociate();
	}

	@Override
	public boolean reconnect(String reason) {
		if (initWifiManager() == null) return false;
		Log.i(tag, "wifi: reconnect : " + reason);
		return wifiManager.reconnect();
	}

	@Override
	public boolean removeNetwork(int netId) {
		if (initWifiManager() == null) return false;
		Log.i(tag, "wifi: removeNetwork");
		return wifiManager.removeNetwork(netId);
	}

	@Override
	public boolean saveConfiguration() {
		//ULog.i("wifi: saveConfiguration");
		if (initWifiManager() == null) return false;
		return wifiManager.saveConfiguration();
	}


	@Override
	public boolean setWifiEnabled(boolean enable) {
		Log.i(tag, "wifi: setWifiEnable : " + enable);
		if (initWifiManager() == null) return false;
		return wifiManager.setWifiEnabled(enable);
	}

	@Override
	public boolean startScan() {
		if (initWifiManager() == null) return false;
		return wifiManager.startScan();
	}
	
	@Override
	public boolean isWifiEnabled() {
		if (initWifiManager() == null) return false;
		boolean enable = wifiManager.isWifiEnabled();
		Log.i(tag, "wifi: isWifiEnabled : " + enable + " connected :" + isSupplicantConnected);
		return enable;
	}
	
	public static final int apiRetryCountMax = 2;
	
	
	@Override
	public boolean isSupplicantConnnected() {
		return WifiAdapter.isSupplicantConnected;
	}
	@Override
	public void setSupplicantConnected(boolean isConnected) {
		WifiAdapter.isSupplicantConnected = isConnected;
	}
	@Override
	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
		this.sessionKey = System.currentTimeMillis();
	}
	
	@Override
	public boolean hasRoamBandApi() {
		if (initWifiManager() == null) return false;
		
		try {

			Method method = wifiManager.getClass().getMethod("setRoamBand", new Class[] { Integer.TYPE });
			if (method == null) return false;
			return true;

		} catch (Exception e) {
			Log.e(tag, "hasRoamBandApi : " + e);
		}
		return false;
	}
	
	private boolean setRoamBandNative(int roamBand) {
		if (initWifiManager() == null) return false;

		try {
		    Log.d(tag, "Assume this is ICS so call setFrequencyBand first!");
            Method method = wifiManager.getClass().getMethod("setFrequencyBand", new Class[] { Integer.TYPE , Boolean.TYPE});
            if (method == null) return false;
            method.invoke(wifiManager, roamBand, false);
            
            Log.i(tag, "setFrequencyBand : " + roamBand);
            
            return true;
    

        } catch (Exception e){
            Log.e(tag, "setFrequencyBand : " + e);
        }

		return false;

	}

	@Override
	public boolean setRoamBand(int roamBand) {
	    //return true;
		
		for (int retry = 0; retry < apiRetryCountMax; retry++) {
			if (setRoamBandNative(roamBand)) return true;
			
			waitForApiDelay();
		}

		return false;
		
	}
	
	@Override
	public long getSessionKey() {
		return this.sessionKey;
	}
	@Override
	public boolean isConnected() {
		return this.isConnected;
	}

	
	
}

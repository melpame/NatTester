package net.bloody.wifi;

import java.util.List;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.ScanResult;
import android.net.DhcpInfo;

/**
 * Interface that allows controlling and querying Wi-Fi connectivity.
 *
 */
public interface IWifiManager
{
    List<WifiConfiguration> getConfiguredNetworks();

    boolean removeNetwork(int netId);

    boolean enableNetwork(int netId, boolean disableOthers);

    boolean disableNetwork(int netId);

    boolean pingSupplicant();

    List<ScanResult> getScanResults();

    boolean disconnect();

    boolean reconnect(String reason);

    boolean reassociate();

    WifiInfo getConnectionInfo();

    boolean setWifiEnabled(boolean enable);
                                                
    boolean saveConfiguration();

    DhcpInfo getDhcpInfo();
                             
	int addNetwork(WifiConfiguration config);
	
	int updateNetwork(WifiConfiguration config);                                                              

	boolean startScan();

	boolean isWifiEnabled();
	
	boolean hasRoamBandApi();
	
	
	
	static final int ROAM_BAND_AUTO = 0;
	static final int ROAM_BAND_5G_ONLY = 1;
	static final int ROAM_BAND_2G_ONLY = 2;
	static final int ROAM_BAND_DEFAULT_FACTORY = 3;
	static final int ROAM_BAND_DEFAULT = ROAM_BAND_AUTO;
	// 0: AUTO
	// 1: 5G Only
	// 2: 2G Only
	boolean setRoamBand(int roamBandVale);
	
	
	boolean isSupplicantConnnected();
	void setSupplicantConnected(boolean isConnected);
	void setConnected(boolean isConnected);
	
	boolean isConnected();
	
	long getSessionKey();
}

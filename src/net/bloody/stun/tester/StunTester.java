package net.bloody.stun.tester;

import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import de.javawi.jstun.test.DiscoveryInfo;
import de.javawi.jstun.test.DiscoveryTest;

import net.bloody.stun.client.StunClient;
import net.bloody.stun.client.StunClient.StunMode;
import net.bloody.stun.client.StunClient.StunProtocol;
import net.bloody.system.SystemChecker;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/*
 * ./stunclient 112.220.75.19 --mode full --protocol udp 3478
 ./stunclient 112.220.75.19 --mode full --protocol udp --localport 2000
 ./stunclient 112.220.75.19 --mode full --protocol udp --localport 20000
 ./stunclient 112.220.75.19 --mode full --protocol udp --localport 40000
 ./stunclient 112.220.75.19 --mode full --protocol udp --localport 50000
 ./stunclient 112.220.75.19 --mode full --protocol udp --localport 60000
 ./stunclient 112.220.75.19 --mode full --protocol tcp 3478
 ./stunclient 112.220.75.19 --mode full --protocol tcp --localport 2000
 ./stunclient 112.220.75.19 --mode full --protocol tcp --localport 20000
 ./stunclient 112.220.75.19 --mode full --protocol tcp --localport 40000
 ./stunclient 112.220.75.19 --mode full --protocol tcp --localport 50000
 ./stunclient 112.220.75.19 --mode full --protocol tcp --localport 60000
 */
public class StunTester {
	private static final String tag = "bloody";
	private final Context context;
	private final Handler handler;
	private final OutputStream outputStream;
	private final String serverIpAddress;
	private final String defaultStunPort = "3478";
	private final List<StunClient> testCases = new ArrayList<StunClient>();
	
	
	public static int START_TEST = 1;
	public static int PROGRESS_TEST = 2;
	public static int COMPLETE_TEST = 3;
	public static int FAIL_TEST = 4;

	public StunTester(Context context, Handler handler, OutputStream os, String serverIpAddress) {
		this.context = context;
		this.handler = handler;
		this.outputStream = os;
		this.serverIpAddress = serverIpAddress;

		testCases.add(new StunClient(context, StunMode.Full, StunProtocol.Udp,
				this.serverIpAddress, "3478"));
		testCases.add(new StunClient(context, StunMode.Full, StunProtocol.Udp,
				this.serverIpAddress, "2000"));
		testCases.add(new StunClient(context, StunMode.Full, StunProtocol.Udp,
				this.serverIpAddress, "20000"));
		testCases.add(new StunClient(context, StunMode.Full, StunProtocol.Udp,
				this.serverIpAddress, "40000"));
		testCases.add(new StunClient(context, StunMode.Full, StunProtocol.Udp,
				this.serverIpAddress, "50000"));
		testCases.add(new StunClient(context, StunMode.Full, StunProtocol.Udp,
				this.serverIpAddress, "60000"));

		testCases.add(new StunClient(context, StunMode.Full, StunProtocol.Tcp,
				this.serverIpAddress, "3478"));
		testCases.add(new StunClient(context, StunMode.Full, StunProtocol.Tcp,
				this.serverIpAddress, "2000"));
		testCases.add(new StunClient(context, StunMode.Full, StunProtocol.Tcp,
				this.serverIpAddress, "20000"));
		testCases.add(new StunClient(context, StunMode.Full, StunProtocol.Tcp,
				this.serverIpAddress, "40000"));
		testCases.add(new StunClient(context, StunMode.Full, StunProtocol.Tcp,
				this.serverIpAddress, "50000"));
		testCases.add(new StunClient(context, StunMode.Full, StunProtocol.Tcp,
				this.serverIpAddress, "60000"));

	}

	public void testAll() {

		int failCount = 0;
		int testCount = 0;

		final SystemChecker systemChecker = net.bloody.system.Environment
				.getInstance(context).getSystemChecker();

		writeOutput(outputStream, systemChecker.toString() + "\r\n");

		
		writeOutput(outputStream, "\r\nJSTUN Test\r\n");
		
		testCount++;
		boolean isSuccess = testJstun();
		
		if (!isSuccess) failCount++;
		
		handler.sendMessage(Message.obtain(handler, PROGRESS_TEST, String.format("%d : jstun test", testCount)));


		writeOutput(outputStream, "\r\nSTUNTMAN Test\r\n\r\n");

		for (StunClient stunClient : testCases) {
			boolean testResult = stunClient.execute();

		
			testCount++;
			writeLine();
			
			writeOutput(outputStream, stunClient.toString() + "\r\n");

			writeOutput(outputStream, stunClient.getOutput() + "\r\n");

			writeOutput(outputStream, "\r\n");

			if (!testResult)
				failCount++;
			
			
			handler.sendMessage(Message.obtain(handler, PROGRESS_TEST, String.format("%d : %s", testCount, stunClient.toString())));

			Log.i(tag, "testCase : " + stunClient + " result : "
					+ (testResult ? "success" : "fail"));
		}
		
		writeLine();

		final String resultSummary = String.format(
				"Total Test : %d, Success : %d, Fail : %d\r\n",
				testCases.size(), testCases.size() - failCount, failCount);
		
		writeOutput(outputStream, resultSummary);
		
		
		handler.sendMessage(Message.obtain(handler, PROGRESS_TEST, resultSummary));


	}
	
	private void writeLine() {
	
		writeOutput(outputStream,
		"----------------------------------------------------------------\r\n");

	}

	private boolean testJstun() {
		
		boolean isSuccess = false;

		// JSTUN 으로 일단 기본 테스트 하고
		InetAddress inetAddress = null;
		try {
			inetAddress = InetAddress.getByName(getLocalAddress());
		} catch (UnknownHostException e) {
			Log.e(tag, "inetaddress error : ", e);
		}

		writeLine();
		Log.i(tag, "inetaddress : " + inetAddress);

		DiscoveryTest test = new DiscoveryTest(inetAddress, serverIpAddress,
				Integer.parseInt(defaultStunPort));

		try {
			DiscoveryInfo discoveryInfo = test.test();
			Log.i(tag, "result : " + discoveryInfo);

			writeOutput(outputStream, discoveryInfo.toString());
			
			isSuccess = true;

		} catch (Exception e) {

			writeOutput(outputStream, "Fail : " + e.getMessage());

			Log.e(tag, "test error : ", e);
		}
		
		return isSuccess;
	}

	private String getLocalAddress() {
		queryNetworkInterfaces();
		String localAddress = "127.0.0.1";
		try {
			Socket socket = new Socket("www.google.com", 80);
			Log.i(tag, socket.getLocalAddress().toString());
			localAddress = socket.getLocalAddress().toString()
					.replaceAll("/", "");
		} catch (Exception e) {
			Log.e(tag, "can NOT get localaddress", e);
		}

		return localAddress;
	}

	public void queryNetworkInterfaces() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {

						Log.i(tag,
								String.format("interface %s - address %s",
										intf.getDisplayName(),
										inetAddress.getHostAddress()));

						writeOutput(
								outputStream,
								String.format("interface %s - address %s\r\n",
										intf.getDisplayName(),
										inetAddress.getHostAddress()));
					}
				}
			}
		} catch (SocketException e) {
			Log.e(tag, "can Not get interface status : ", e);
		}

		return;
	}

	private void writeOutput(OutputStream os, String output) {

		try {

			final long length = output.getBytes().length;
			os.write(output.getBytes(), 0, (int) length);
		} catch (Exception e) {
			Log.e(tag, "writeOutput error : ", e);
		}

	}

}

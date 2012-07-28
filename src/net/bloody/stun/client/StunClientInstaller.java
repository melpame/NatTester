package net.bloody.stun.client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import net.bloody.stun.R;
import android.content.Context;
import android.util.Log;

public class StunClientInstaller {
	private static final String tag = "bloody";

	public StunClientInstaller(Context context) {
		this.context = context;
	}
	
	
	public void install() {
		installNativeBin();
	}

	private final Context context;

	private static String applicationRoot;

	private void installNativeBin() {
		applicationRoot = context.getFilesDir().getParent();

		Log.d(tag, "Current directory is " + applicationRoot);

		checkDirs();

		copyFile(applicationRoot + "/bin/stunclient", "0777", R.raw.stunclient);

	}

	private String copyFile(String filename, String permission, int ressource) {
		String result = copyFile(filename, ressource);
		if (result != null) {
			return result;
		}
		try {

			{
				final ArrayList<String> commandLine = new ArrayList<String>();
				commandLine.add("chmod");
				commandLine.add(permission);
				commandLine.add(filename);

				Log.i(tag, "command : " + commandLine);

				try {
					Process process = Runtime.getRuntime().exec(
							commandLine.toArray(new String[0]));
				} catch (IOException e) {
					Log.e(tag, "copyFile error : ", e);
				}

			}
		} catch (Exception ignore) {
		}

		return result;
	}

	private String copyFile(String filename, int ressource) {
		File outFile = new File(filename);
		Log.d(tag, "Copying file '" + filename + "' ...");
		InputStream is = context.getResources().openRawResource(ressource);
		byte buf[] = new byte[1024];
		int len;
		try {
			OutputStream out = new FileOutputStream(outFile);
			while ((len = is.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.close();
			is.close();
		} catch (IOException e) {
			return "Couldn't install file - " + filename + "!";
		}
		return null;
	}

	private void checkDirs() {
		File dir = new File(applicationRoot);
		if (dir.exists() == false) {
			Log.e(tag, "Application data-dir does not exist!");
		} else {
			String[] dirs = { "/bin", "/var", "/conf" };
			for (String dirname : dirs) {
				dir = new File(applicationRoot + dirname);
				if (dir.exists() == false) {
					if (!dir.mkdir()) {
						Log.e(tag, "Couldn't create " + dirname + " directory!");
					}
				} else {
					Log.d(tag, "Directory '" + dir.getAbsolutePath()
							+ "' already exists!");
				}
			}
		}
	}
}

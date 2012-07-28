package net.bloody.utils;

import java.io.FileOutputStream;
import java.io.IOException;

public class CsvExporter {
	public static void writeToFile(String output, String outputFilename) throws IOException {

		FileOutputStream fos = new FileOutputStream(outputFilename);

		final long length = output.getBytes().length;
		
		fos.write(output.getBytes(), 0, (int) length);

		fos.close();

	}

}

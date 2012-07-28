package net.bloody.utils;

public class BinaryUtils {
	public static String bytesToHex(final byte[] bytes) {
        final StringBuilder buf = new StringBuilder(bytes.length * 2);
        for (final byte b : bytes) {
            final String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                buf.append("0");
            }
            buf.append(hex);
        }
        return buf.toString();
    }

    public static byte[] hexToBytes(final String hex) {
        final byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(i * 2, i * 2 + 2), 16);
        }
        return bytes;
    }
    
    public static byte[] padBuffer(final byte[] buffer, final int padding) {
		if (buffer.length % padding != 0) {
			byte[] newBuffer =  new byte[((buffer.length + padding)/padding) * padding];
			int paddingLength = newBuffer.length - buffer.length;
			
			for (int i = 0; i < buffer.length; i++) {
				newBuffer[i] = buffer[i];
			}
			
			return newBuffer;
		}
		
		return buffer;
    }
}

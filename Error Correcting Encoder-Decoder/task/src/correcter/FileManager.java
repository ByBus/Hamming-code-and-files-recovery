package correcter;

import java.io.*;

public class FileManager {
    final static String SEND_FILE = "send.txt";
    final static String RECEIVED_FILE = "received.txt";
    final static String ENCODED_FILE = "encoded.txt";
    final static String DECODED_FILE = "decoded.txt";

    public static byte[] readBytesFromFile(String filepath) throws IOException {
        //System.out.println(System.getProperty("user.dir"));
        try (FileInputStream inputStream = new FileInputStream(filepath)) {
            return inputStream.readAllBytes();
        }
    }

    public static void saveBytesToFile(byte[] data, String filepath) throws IOException {
        OutputStream outputStream = new FileOutputStream(filepath, false);
        outputStream.write(data);
        outputStream.close();
    }

    public static String convertBytesToString(byte[] data) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : data) {
            stringBuilder.append(String
                    .format("%8s", Integer.toBinaryString(b & 0xFF))
                    .replace(' ', '0'));
        }
        return stringBuilder.toString();
    }

    public static byte[] convertStringToBytes(String lineOfBits) throws IOException {
        int byteSize = 8;
        try (ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream()) {
            for (int i = 0; i < lineOfBits.length(); i += byteSize) {
                String bitsOfByte = lineOfBits.substring(i, i + byteSize);
                int byte_ = Integer.parseInt(bitsOfByte, 2);
                byteOutputStream.write(byte_);
            }
            return byteOutputStream.toByteArray();
        }
    }
}

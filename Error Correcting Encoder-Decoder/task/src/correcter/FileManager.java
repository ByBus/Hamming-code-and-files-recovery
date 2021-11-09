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
}

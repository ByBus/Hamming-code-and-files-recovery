package correcter;

import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        menu();
    }

    public static void menu() throws IOException {
        Corrupter corrupter = new Corrupter();
        Corrector corrector = new Corrector();
        Scanner input = new Scanner(System.in);
        switch (input.next()) {
            case "encode":
                prepareFile(corrector);
                break;
            case "send":
                addDamageAndSend(corrupter);
                break;
            case "decode":
                recoverFile(corrector);
                break;
            default:
                System.out.println("Incorrect command");
        }
    }

    private static void recoverFile(Corrector corrector) throws IOException {
        byte[] bytesOfDamagedFile = FileManager.readBytesFromFile(FileManager.RECEIVED_FILE);
        byte[] recoveredBytes = corrector.recoverBytes(bytesOfDamagedFile);
        FileManager.saveBytesToFile(recoveredBytes, FileManager.DECODED_FILE);
    }

    private static void addDamageAndSend(Corrupter corrupter) throws IOException {
        byte[] bytesOfEncodedFile = FileManager.readBytesFromFile(FileManager.ENCODED_FILE);
        byte[] damagedBytes = corrupter.corruptEveryByteOfData(bytesOfEncodedFile);
        FileManager.saveBytesToFile(damagedBytes, FileManager.RECEIVED_FILE);
    }

    private static void prepareFile(Corrector corrector) throws IOException {
        byte[] bytesFromText = FileManager.readBytesFromFile(FileManager.SEND_FILE);
        byte[] parityBytes = corrector.convertToHammingBytes(bytesFromText);
        FileManager.saveBytesToFile(parityBytes, FileManager.ENCODED_FILE);
    }
}
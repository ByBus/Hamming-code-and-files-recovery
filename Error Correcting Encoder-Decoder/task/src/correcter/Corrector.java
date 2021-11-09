package correcter;

import java.io.*;

public class Corrector {

    private final static int[] P1_CURSORS = {32, 8, 2};
    private final static int[] P2_CURSORS = {32, 4, 2};
    private final static int[] P4_CURSORS = {8, 4, 2};
    private final static int[][] CURSORS = {P1_CURSORS, P2_CURSORS, P4_CURSORS};
    private final static int[] PARITY_BIT_POSITIONS = {128, 64, 16};

    protected byte[] convertToHammingBytes(byte[] bytes) throws IOException {
        try (ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream()) {
            for (byte b : bytes) {
                byte rightHalfByte = (byte) (b & 0xF);
                byte leftHalfByte = (byte) (b >>> 4);
                byteOutputStream.write(addParityBits(leftHalfByte));
                byteOutputStream.write(addParityBits(rightHalfByte));
            }
            return byteOutputStream.toByteArray();
        }
    }

    private int addParityBits(byte halfByte) {
        // 0b00001111 -> 0bXX1X1110 -> 0b11111110;
        byte hammingByte = (byte) (halfByte << 1);
        byte fifthBitLSH = (byte) ((hammingByte & (1 << 4)) << 1);
        hammingByte = (byte) (hammingByte & 0xF | fifthBitLSH);
        for (int i = 0; i < CURSORS.length; i++) {
            if (calculateParityValue(hammingByte, CURSORS[i]) == 1) {
                hammingByte |= PARITY_BIT_POSITIONS[i];
            }
        }
        return hammingByte;
    }

    private int calculateParityValue(byte hammingByte, int[] cursorsPositions) {
        int counter = 0;
        for (int position : cursorsPositions) {
            if ((hammingByte & position) != 0) {
                counter++;
            }
        }
        return counter % 2;
    }

    private static byte recombineByte(byte hammingByte) {
        int separatedThirdBit = (hammingByte & 32) >>> 2; // 01101010 & 100000 -> 100000 -> 1000
        return (byte) (hammingByte >>> 1 & 7 ^ separatedThirdBit); // 01101010 -> 00110101 -> 00000101 -> 00001101
    }

    protected byte[] recoverBytes(byte[] bytes) throws IOException {
        try (ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream()) {
            for (int i = 0; i < bytes.length - 1; i += 2) {
                byte leftByte = recombineByte(recoverByte(bytes[i]));
                byte rightByte = recombineByte(recoverByte(bytes[i + 1]));
                leftByte = (byte) (leftByte << 4);
                byteOutputStream.write(leftByte | rightByte);
            }
            return byteOutputStream.toByteArray();
        }
    }

    private byte recoverByte(byte hammingByte) {
        int[] parityValues = {1, 2, 4};
        int damagedBytePosition = 0;
        for (int i = 0; i < CURSORS.length; i++) {
            int parity = getParityValue(hammingByte, PARITY_BIT_POSITIONS[i]);
            if (calculateParityValue(hammingByte, CURSORS[i]) != parity) {
                damagedBytePosition += parityValues[i];
            }
        }
        if (damagedBytePosition != 0) {
            int cursor = 128 >>> --damagedBytePosition;
            hammingByte = (byte) (hammingByte ^ cursor);
        }
        return hammingByte;
    }

    private int getParityValue(byte hammingByte, int parityPosition) {
        return (hammingByte & parityPosition) != 0 ? 1 : 0;
    }
}

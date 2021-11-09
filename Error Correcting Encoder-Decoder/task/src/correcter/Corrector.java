package correcter;

import java.io.*;
import java.util.Arrays;

public class Corrector {
    protected String recoverMessage(String message) {
        StringBuilder recoveredMessage = new StringBuilder();
        for (int i = 0; i < message.length() - 2; i += 3) {
            int j = 0;
            while (true) {
                int indexChar1 = i + j % 3;
                int indexChar2 = i + (j + 1) % 3;
                char char1 = message.charAt(indexChar1);
                char char2 = message.charAt(indexChar2);
                if (char1 == char2) {
                    recoveredMessage.append(char1);
                    break;
                }
                if (j > 3) {
                    recoveredMessage.append("*"); // Letter not recovered
                    break;
                }
                j++;
            }
        }
        return  recoveredMessage.toString();
    }

    protected String repeatLettersOfMessageTimes(String message, int times) {
        StringBuilder tripledMessage = new StringBuilder();
        for (char letter : message.toCharArray()) {
            tripledMessage.append(String.valueOf(letter).repeat(times));
        }
        return tripledMessage.toString();
    }

    protected byte[] createBytesWithParityBit(String lineOfBits) throws IOException {
        StringBuilder buffer = new StringBuilder(lineOfBits);
        try (ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream()) {
            int chunk = 3;
            int length = buffer.length();
            if (length % chunk != 0) { // Correction if not divisible by 3
                int additionalZeroes = chunk - length % chunk;
                buffer.append("0".repeat(additionalZeroes));
            }

            lineOfBits = buffer.toString();
            for (int i = 0; i < lineOfBits.length(); i += chunk) {
                String bits = lineOfBits.substring(i, i + chunk);
                String bitsWithParity = addParityBit(bits);
                String doubledBits = repeatLettersOfMessageTimes(bitsWithParity, 2);
                byteOutputStream.write(Integer.parseInt(doubledBits, 2));
            }
            return byteOutputStream.toByteArray();
        }
    }

    private String addParityBit(String stringOfBits) {
        String[] bits = stringOfBits.split("");
        int length = bits.length;
        int[] separatedBits = new int[length + 1];
        for (int i = 0; i < length; i++) {
            separatedBits[i] = Integer.parseInt(bits[i]);
        }
        int parityBit = separatedBits[0] ^ separatedBits[1] ^ separatedBits[2];
        separatedBits[length] = parityBit;
        return Arrays.toString(separatedBits).replaceAll("[\\[\\] ,]", "");
    }

    protected String recoverOriginalBits(String lineOfBits) {
        StringBuilder buffer = new StringBuilder();
        int byteSize = 8;
        int chunk = 2;

        for (int i = 0; i < lineOfBits.length(); i += byteSize) {
            String bitsOfByte = lineOfBits.substring(i, i + byteSize);
            String[] pairs = bitsOfByte.split("(?<=\\G.{"+ chunk +"})"); // ["00", "11", "01", "11"]
            String tripleOfBits = recoverTripleOfBits(pairs);
            buffer.append(tripleOfBits);
        }

        int bufferSize = buffer.length();
        if (bufferSize % byteSize != 0) { // correction for too long sequence
           int correctLength = bufferSize - bufferSize % byteSize;
           buffer.setLength(correctLength);
        }
        return buffer.toString();
    }

    private String recoverTripleOfBits(String[] pairs) {
        int[] ints = markDamagedPair(pairs); // parityBit is the last one
        int indexOfParity = ints.length - 1;
        StringBuilder buffer = new StringBuilder();
        // if Parity bit is damaged
        int parityBit = ints[indexOfParity];
        if (parityBit == -1) {
            for (int i = 0; i < indexOfParity; i++) {
                buffer.append(ints[i]);
            }
            return buffer.toString();
        }
        int damagedBitIndex = 0;
        int recoveredBit = parityBit;
        for (int j = 0; j < indexOfParity; j++) {
            if (ints[j] == -1) {
                damagedBitIndex = j;
                continue;
            }
            recoveredBit ^= ints[j];
        }
        ints[damagedBitIndex] = recoveredBit;
        for (int k = 0; k < indexOfParity; k++) {
            buffer.append(ints[k]);
        }
        return buffer.toString();
    }

    /**
     * Create array of single ints
     * @param pairs array of paired bits ["00", "11", "01", "11"]
     * @return array of ints, damaged pair replaced with -1 [0, 1, -1, 1]
     */
    private int[] markDamagedPair(String[] pairs) {
        int[] ints = new int[pairs.length];
        for (int j = 0; j < pairs.length; j++) {
            if (pairs[j].charAt(0) == pairs[j].charAt(1)) {
                ints[j] = Integer.parseInt("" + pairs[j].charAt(0));
            } else {
                ints[j] = -1;
            }
        }
        return ints;
    }
}

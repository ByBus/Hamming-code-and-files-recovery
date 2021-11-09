package correcter;

import java.util.Random;

/**
 * I know the class name is worth working on.
 */
public class Corrupter {

    protected byte[] corruptEveryByteOfData(byte[] data) {
        byte[] bytes = data.clone();
        Random random = new Random();
        for (int i = 0; i < bytes.length; i++) {
            int shift = random.nextInt(8);
            byte cursor = (byte) (1 << shift);
            bytes[i] = (byte) (bytes[i] ^ cursor);
        }
        return bytes;
    }
}

package correcter;

import java.util.Random;

/**
 * I know the class name is worth working on.
 */
public class Corrupter {

    protected String addErrorsToMessage(String message) {
        char currentLetter;
        Random random = new Random();
        StringBuilder damagedMessage = new StringBuilder();
        for (int i = 0; i < message.length(); i += 3) {
            int subIndex = i + random.nextInt(3);
            for (int j = i; j < i + 3 && j < message.length(); j++) {
                currentLetter = j == subIndex ? getRandomLetter(message.charAt(j)) : message.charAt(j);
                damagedMessage.append(currentLetter);
            }
        }
        return damagedMessage.toString();
    }

    private char getRandomLetter(char excludeChar) {
        char letter;
        do {
            letter = randomLetter();
        } while (excludeChar == letter);
        return letter;
    }

    private char randomLetter() {
        Random random = new Random();
        char letter = (char) (random.nextInt('Z' - 'A' + 1) + 'A');
        double lowerCaseProbability = random.nextDouble();
        return lowerCaseProbability < 0.5 ? Character.toLowerCase(letter) : letter;
    }

    protected byte[] corruptEveryBiteOfData(byte[] data) {
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

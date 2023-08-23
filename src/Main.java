import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {

    public static final BlockingQueue<String> searchA = new ArrayBlockingQueue<>(100);
    public static final BlockingQueue<String> searchB = new ArrayBlockingQueue<>(100);
    public static final BlockingQueue<String> searchC = new ArrayBlockingQueue<>(100);

    public static void main(String[] args) {
        new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                String text = generateText("abc", 100_000);
                try {
                    searchA.put(text);
                    searchB.put(text);
                    searchC.put(text);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        new Thread(() -> {
            counter('a', searchA);
        }).start();

        new Thread(() -> {
            counter('b', searchB);
        }).start();

        new Thread(() -> {
            counter('c', searchC);
        }).start();
    }

    private static void counter(char charA, BlockingQueue<String> queue) {
        int count = 0;
        int number = 0;

        for (int i = 0; i < 10_000; i++) {
            int countLine = 0;
            String text;
            try {
                text = queue.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            for (int j = 0; j < text.length(); j++) {
                if (text.charAt(j) == charA) {
                    countLine++;
                }
            }
            if (countLine > count) {
                count = countLine;
                number = i;
            }
        }
        System.out.format("Строка %d, количество " + charA + " - %d\n", number, count);
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}
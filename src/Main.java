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
            int count = 0;
            int number = 0;


            char charA = 'a';

            for (int i = 0; i < 10_000; i++) {
                int countLine = 0;
                String text;
                try {
                    text = searchA.take();
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
            System.out.format("Строка %d, количество A - %d\n", number, count);
        }).start();

        new Thread(() -> {
            int count = 0;
            int number = 0;

            char charA = 'b';

            for (int i = 0; i < 10_000; i++) {
                int countLine = 0;
                String text;
                try {
                    text = searchB.take();
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
            System.out.format("Строка %d, количество B - %d\n", number, count);
        }).start();

        new Thread(() -> {
            int count = 0;
            int number = 0;

            char charA = 'c';

            for (int i = 0; i < 10_000; i++) {
                int countLine = 0;
                String text;
                try {
                    text = searchC.take();
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
            System.out.format("Строка %d, количество C - %d\n", number, count);
        }).start();
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
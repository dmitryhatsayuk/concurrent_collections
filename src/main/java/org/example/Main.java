package org.example;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static int textsSize = 10000;
    public static int queueCapacity = 100;
    public static BlockingQueue<String> queueA = new ArrayBlockingQueue<>(queueCapacity);
    public static BlockingQueue<String> queueB = new ArrayBlockingQueue<>(queueCapacity);
    public static BlockingQueue<String> queueC = new ArrayBlockingQueue<>(queueCapacity);

    public static void main(String[] args) {
        //Запуск потока на создание текстов и раздачу их в очереди
        ExecutorService executor = Executors.newFixedThreadPool(4);
            executor.submit(() -> {

                for (int i = 0; i < textsSize; i++) {
                    try {
                        queueA.put(generateText("abc", 100000));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        queueB.put(generateText("abc", 100000));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        queueC.put(generateText("abc", 100000));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

            });
//Запуск потоков разбирающих очереди

            executor.submit(() -> {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    maxCharCounter('a', queueA);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            executor.submit(() -> {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    maxCharCounter('b', queueB);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            executor.submit(() -> {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    maxCharCounter('c', queueC);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });



    }

    public static Integer targetCharCounter(String string, char target) {
        char[] chars = string.toCharArray();
        int counter = 0;
        for (char aChar : chars) {
            if (aChar == target) {
                counter++;
            }
        }
        return counter;
    }

    public static void maxCharCounter(char target, BlockingQueue<String> queue) throws InterruptedException {
        int counter = 0;
        String maxString = null;
        while (!queue.isEmpty()) {
            String string = queue.take();
            if (targetCharCounter(string, target) > counter) {
                counter = targetCharCounter(string, target);
                maxString = string;
            }
        }
        System.out.println("Строка с максимальным количеством " + target + "- " + counter + "шт. :");
        System.out.println(maxString);
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
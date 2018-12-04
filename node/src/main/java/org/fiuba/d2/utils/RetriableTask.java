package org.fiuba.d2.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.System.exit;
import static java.lang.Thread.sleep;

public class RetriableTask {

    private static final ExecutorService executor = Executors.newFixedThreadPool(10);


    public static void submit(Runnable task) {
        executor.submit(() -> {
            while (true) {
                try {
                    task.run();
                    return;
                } catch (Exception e) {
                    sleepOneSecond();
                }
            }
        });
    }

    public static void sleepOneSecond() {
        try {
            sleep(1000);
        } catch (InterruptedException e1) {
            exit(1);
        }
    }

    public static void runAsync(Runnable runnable) {
        executor.submit(runnable);
    }


}

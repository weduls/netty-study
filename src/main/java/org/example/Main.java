package org.example;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();
        sleep1().get();
        sleep2().get();
        long endTime1 = System.currentTimeMillis();
        System.out.printf("[end - 3] %d", endTime1 - startTime);
        CompletableFuture<String> stringCompletableFuture = sleep1();
        CompletableFuture<String> stringCompletableFuture1 = sleep2();
        CompletableFuture.allOf(stringCompletableFuture, stringCompletableFuture1)
                .thenAccept(d -> {
                    try {
                        System.out.println(Thread.currentThread().getName() + stringCompletableFuture.get());
                        System.out.println(Thread.currentThread().getName() + stringCompletableFuture1.get());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                }).get();
        long endTime2 = System.currentTimeMillis();
        System.out.printf("[end - 3] %d", endTime2 - endTime1);
        CompletableFuture<String> stringCompletableFuture3 = sleep1();
        CompletableFuture<String> stringCompletableFuture4 = sleep2();
        CompletableFuture.allOf(stringCompletableFuture3, stringCompletableFuture4)
                .thenAcceptAsync(d -> {
                    try {
                        System.out.println(Thread.currentThread().getName() + stringCompletableFuture3.get());
                        System.out.println(Thread.currentThread().getName() + stringCompletableFuture4.get());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                }).get();
        System.out.printf("[end - 3] %d", System.currentTimeMillis() - endTime2);
    }

    public static CompletableFuture<String> sleep1() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("123");
            return "string1";
        });
    }

    public static CompletableFuture<String> sleep2() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("234");
            return "string2";
        });
    }
}
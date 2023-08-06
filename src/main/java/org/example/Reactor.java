package org.example;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Reactor {
    public static void main(String[] args) {
        ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(4);

        for (int i = 0; i < 3; i++) {
            // 요청이 여러개에서 스레드풀내에 스레드를 잘 사용하지만 하나의 subscribe에 대해서는 스레드풀 내에서 하나의 스레드만 사용함
            Flux.fromIterable(List.of(1,2,3))
                    .doOnNext(d -> {
                        System.out.println(Thread.currentThread() + " " + d);
                    })
                    .subscribeOn(Schedulers.fromExecutorService(threadPoolExecutor))
                    .doOnComplete(() -> System.out.println("complete"))
                    .subscribe();
        }
        threadPoolExecutor.shutdown();
    }
}

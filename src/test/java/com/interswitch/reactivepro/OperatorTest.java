package com.interswitch.reactivepro;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;

import java.time.Duration;

@SpringBootTest
@RunWith(SpringRunner.class)
public class OperatorTest {

    @Test
    public void map(){
        Flux.range(1, 5)
                .map(i -> i*10)
                .subscribe(System.out::println);
    }

    @Test
    public void flatMap(){
        Flux.range(1, 5)
                .flatMap(i -> Flux.range(i*10, 2))
                .subscribe(System.out::println);
    }

    @Test
    public void flatMapMany(){
        Flux.range(1, 5)
                .flatMapSequential(i -> Flux.range(1, 2))
                .subscribe(System.out::println);
    }

    @Test
    public void concat() throws InterruptedException {
        Flux<Integer> oneToFive = Flux.range(1, 5)
                .delayElements(Duration.ofMillis(200));

        Flux<Integer> sixToTen = Flux.range(6, 10)
                .delayElements(Duration.ofMillis(200));

        Flux.concat(oneToFive, sixToTen)
                .subscribe(System.out::println);

//        oneToFive.concatWith(sixToTen)
//                .subscribe(System.out::println);
        Thread.sleep(4000);
    }

    //combine the publisher not in a sequential way like concat.
    @Test
    public void merge() throws InterruptedException {
        Flux<Integer> oneToFive = Flux.range(1, 5)
                .delayElements(Duration.ofMillis(200));

        Flux<Integer> sixToTen = Flux.range(6, 10)
                .delayElements(Duration.ofMillis(200));

        Flux.merge(oneToFive, sixToTen)
                .subscribe(System.out::println);

        oneToFive.mergeWith(sixToTen)
                .subscribe(System.out::println);
        Thread.sleep(4000);
    }

    @Test
    public void zip() {
        Flux<Integer> oneToFive = Flux.range(1, 5);

        Flux<Integer> sixToTen = Flux.range(6, 10);

        Flux.zip(oneToFive, sixToTen,
                (item1, item2) -> item1+ " , "+item2)
                .subscribe(System.out::println);
//        oneToFive.zipWith(sixToTen)
//                .subscribe(System.out::println);

    }
}

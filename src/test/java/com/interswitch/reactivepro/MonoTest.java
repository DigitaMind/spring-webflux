package com.interswitch.reactivepro;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MonoTest {

    @Test
    public void monoItem(){
        Mono.just("A")
                .log();
    }

    @Test
    public void monoWithConsumer(){
        Mono.just("A")
        .log()
        .subscribe(s -> System.out.println(s));
    }

    @Test
    public void monoWithDoOn(){
        Mono.just("B")
                .log()
                .doOnSubscribe(s -> System.out.println("Subscribe: "+s))
                .doOnRequest(r -> System.out.println("Request: "+r))
                .doOnSuccess(s -> System.out.println("Success: "+s))
                .subscribe(System.out::println);

    }

    @Test
    public void emptyMono(){
        Mono.empty()
                .log()
                .subscribe(System.out::println);
    }
}

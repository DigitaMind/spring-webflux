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

    @Test
    public void emptyConsumerMono(){
        Mono.empty()
                .log()
                .subscribe(System.out::println,
                        null,
                        () -> System.out.println("Done"));
    }

    @Test
    public void errorRuntimeExceptionMono(){
        Mono.error(new RuntimeException())
                .log()
                .subscribe();
    }

    @Test
    public void errorExceptionMono(){
        Mono.error(new Exception())
                .log()
                .subscribe(System.out::println,
                        e -> System.out.println("Error "+e));
    }

    @Test
    public void errorDoOnErrorMono(){
        Mono.error(new Exception())
                .doOnError(e -> System.out.println("Error: "+e))
                .log()
                .subscribe();
    }

    @Test
    public void errorOnErrorResumeMono(){
        Mono.error(new Exception())
                .onErrorResume(e -> {System.out.println("Caught: "+e);
                                        return Mono.just("B");})
                .log()
                .subscribe();
    }

    @Test
    public void errorOnErrorReturnMono(){
        Mono.error(new Exception())
                .onErrorReturn("B")
                .log()
                .subscribe();
    }
}

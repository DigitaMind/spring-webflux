package com.interswitch.reactivepro;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.reactivestreams.Subscription;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.Random;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FluxTest {
    @Test
    public void justFlux(){
        Flux.just("A", "B", "C")
                .log()
                .subscribe();
    }

    //Pass all objects in one just onNext method
    @Test
    public void fluxFromList(){
        Flux.just(Arrays.asList("A", "B", "C"))
                .log()
                .subscribe();
    }

    @Test
    public void fluxFromIterable(){
        Flux.fromIterable(Arrays.asList("A", "B", "C"))
                .log()
                .subscribe();
    }

    @Test
    public void fluxFromRange(){
        Flux.range(10, 20)
                .log()
                .subscribe(s -> System.out.println("Received: "+s));
    }

    @Test
    public void fluxFromIntervalSleepingThread() throws Exception{
        Flux.interval(Duration.ofSeconds(1))
                .log()
                .take(2)
                .subscribe();
        //Thread.sleep(5000);
    }

    @Test
    public void fluxFromInterval(){
        Flux.interval(Duration.ofSeconds(2))
                .log()
                .subscribe();
    }

    @Test
    public void fluxRequest(){
        Flux.range(1, 5 )
                .log()
                .subscribe(null, null, null,
                        s -> s.request(3));
    }

    @Test
    public void customSubscriber(){
        Flux.range(1, 5 )
                .log()
                .subscribe(new BaseSubscriber<Integer>() {
                    int elementsToProcess = 3;
                    int counter = 0;
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        System.out.println("Subscribed!");
                        request(elementsToProcess);
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        counter++;
                        if(counter == elementsToProcess){
                            counter = 0;

                            Random r = new Random();
                            elementsToProcess = r.ints(1, 4)
                                                    .findFirst()
                                                    .getAsInt();
                            request(elementsToProcess);
                        }
                    }
                });
    }

    @Test
    public void fluxLimitRate(){
        Flux.range(1, 5 )
                .log()
                .limitRate(3)
                .subscribe(null, null, null,
                        s -> s.request(3));
    }


}

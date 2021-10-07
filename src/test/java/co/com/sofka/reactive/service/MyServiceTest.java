package co.com.sofka.reactive.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

@SpringBootTest
class MyServiceTest {

    @Autowired
    MyService myService;

    @Test
    void testOne() {
        Mono<String> one = myService.searchOne();
        StepVerifier.create(one).expectNext("Pedro").verifyComplete();
    }
    @Test
    void testSome() {
        Flux<String> some = myService.searchAll();
        StepVerifier
                .create(some)
                .expectNext("Pedro")
                .expectNext("Maria")
                .expectNext("Jesus")
                .expectNext("Carmen")
                .verifyComplete();
    }

    @Test
    void testSomeSlow(){
        Flux<String> some = myService.searchAllSlow();
        StepVerifier.create(some)
                .expectNext("Pedro")
                .thenAwait(Duration.ofSeconds(5))
                .expectNext("Maria")
                .thenAwait(Duration.ofSeconds(5))
                .expectNext("Jesus")
                .thenAwait(Duration.ofSeconds(5))
                .expectNext("Carmen")
                .thenAwait(Duration.ofSeconds(5)).verifyComplete();
    }

    @Test
    void testSearchAllWithFilter(){
        Flux<String> someFiltered = myService.searchAllWithFilter();
        StepVerifier
                .create(someFiltered)
                .expectNext("JOHN")
                .expectNextMatches(name -> name.startsWith("MA"))
                .expectNext("CLOE", "CATE")
                .expectComplete()
                .verify();
    }
}

package co.com.sofka.reactive.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
    class MyServiceTest {
        @Autowired
        MyService myService;

    @Test
        void testMono() {
            Mono<String> one = myService.searchOne();
            StepVerifier.create(one).expectNext("Pedro").verifyComplete();
        }
        @Test
        void testVarios() {
            Flux<String> some = myService.searchAll();
            StepVerifier
                    .create(some)
                    .expectNext("Pedro")
                    .expectNext("Maria")
                    .expectNext("Jesus")
                    .expectNext("Carmen")
                    .verifyComplete();
        }
    }
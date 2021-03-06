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

    @Test
    void testSearchAllWithError() {
        Flux<String> sourceWithError = myService.searchAllWithError();
        StepVerifier
                .create(sourceWithError)
                .expectNextCount(4)
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("Message for the error")
                ).verify();
    }

    //Solo podemos usar un m??todo para verificar las excepciones. La se??al OnError notifica al suscriptor que el editor est?? cerrado con un estado de error. Por lo tanto, no podemos agregar m??s expectativas despu??s.
    //
    //Si no es necesario verificar el tipo y el mensaje de la excepci??n a la vez, podemos usar uno de los m??todos dedicados:
    //
    //expectError() - esperar cualquier tipo de error
    //expectError(Class<? extends Throwable> class) - espera un error de un tipo espec??fico
    //expectErrorMessage(String errorMessage) - se espera un error con un mensaje espec??fico
    //expectErrorMatches(Predicate<Throwable> predicate) - se espera un error que coincida con un predicado dado
    //expectErrorSatisfies(Consumer<Throwable> assertionConsumer) - consume un Throwable para hacer una aserci??n personalizada

    @Test
    void testWithVirtualTime(){
        StepVerifier.withVirtualTime(() -> Flux.interval(Duration.ofSeconds(1)).take(2))
                .expectSubscription()
                .expectNoEvent(Duration.ofSeconds(1)) //m??todo de expectativa que se ocupa del tiempo
                .expectNext(0L)
                .thenAwait(Duration.ofSeconds(1)) //m??todo de expectativa que se ocupa del tiempo
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    void testEmitAssertsAfterExecution(){
        Flux<Integer> source = myService.emitAssertsAfterExecution();
        StepVerifier.create(source)
                .expectNext(2)
                .expectComplete()
                .verifyThenAssertThat()
                .hasDropped(4)
                .tookLessThan(Duration.ofMillis(1050));
    }


}

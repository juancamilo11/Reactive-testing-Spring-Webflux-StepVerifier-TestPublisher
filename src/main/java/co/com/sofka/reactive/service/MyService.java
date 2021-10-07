package co.com.sofka.reactive.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class MyService {

    public Mono<String> searchOne() {
        return Mono.just("Pedro");
    }

    public Flux<String> searchAll() {
        return Flux.just("Pedro", "Maria", "Jesus", "Carmen");
    }

    public Flux<String> searchAllSlow() {
        return Flux.just("Pedro", "Maria", "Jesus", "Carmen")
                .delaySequence(Duration.ofSeconds(20));
    }

    public Flux<String> searchAllWithFilter() {
        Flux<String> source = Flux.just("John", "Monica", "Mark", "Cloe", "Frank", "Casper", "Olivia", "Emily", "Cate")
                .filter(name -> name.length() == 4)
                .map(String::toUpperCase);
        return source;
    }
}

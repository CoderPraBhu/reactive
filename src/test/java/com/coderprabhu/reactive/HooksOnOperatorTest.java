package com.coderprabhu.reactive;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;

@SpringBootTest
public class HooksOnOperatorTest {

	
    @Test
    void hooksOnOperatorTest() {
        Hooks.onOperatorDebug();

        Flux<String> flux = Flux.just("A","B","C","D")
            .map(String::toLowerCase)
            .flatMapSequential(letter -> {
            	return Flux.just(letter);
            });

    }
}
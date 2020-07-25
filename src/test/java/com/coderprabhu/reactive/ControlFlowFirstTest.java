package com.coderprabhu.reactive;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
class ControlFlowFirstTest {

	@Test
	void first() throws InterruptedException {
		Flux<Integer> one = Flux.just(1, 2, 3).delayElements(Duration.ofMillis(150));
		Flux<Integer> two = Flux.just(4, 5, 6).delayElements(Duration.ofMillis(100));
		Flux<Integer> first = Flux.first(one, two);
		StepVerifier.create(first).expectNext(4,5,6).verifyComplete();
		StepVerifier.create(one).expectNext(1,2,3).verifyComplete();
	}
}	
package com.coderprabhu.reactive;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
class ControlFlowMergeTest {

	@Test
	void merge() throws InterruptedException {
		Flux<Integer> fastest = Flux.just(1,2);
		Flux<Integer> better = Flux.just(3,4).delayElements(Duration.ofMillis(20));
		Flux<Integer> delayElements = Flux.just(5,6).delayElements(Duration.ofMillis(40));
		Flux<Flux<Integer>> nested = Flux.just(fastest,better,delayElements);
		Flux<Integer> merged = Flux.merge(nested);
		StepVerifier.create(merged).expectNext(1,2,3,5,4,6).verifyComplete();
	}
}
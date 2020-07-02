package com.coderprabhu.reactive;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
class TakeTest {
	
	@Test
	void take() {
		var data = range().take(5);
		StepVerifier.create(data).expectNext(1,2,3,4,5).verifyComplete();
	}
	
	@Test
	void takeUntil() {
		var data = range().takeUntil(i -> i==6);
		StepVerifier.create(data).expectNext(1,2,3,4,5,6).verifyComplete();
	}


	private Flux<Integer> range() {
		return Flux.range(1,1000);
	}
}
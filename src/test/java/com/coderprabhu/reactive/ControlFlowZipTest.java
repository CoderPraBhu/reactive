package com.coderprabhu.reactive;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
class ControlFlowZipTest {

	@Test
	void zip() throws InterruptedException {
		Flux<Integer> one = Flux.just(1, 2, 3);
		Flux<String> two = Flux.just("a", "b", "c");
		Flux<String> zip = Flux.zip(one, two).map(tuple -> this.from(tuple.getT1(), tuple.getT2()));
		StepVerifier.create(zip).expectNext("1:a", "2:b", "3:c").verifyComplete();
	}

	public String from(Integer one, String two) {
		return one + ":" + two;
	}
}
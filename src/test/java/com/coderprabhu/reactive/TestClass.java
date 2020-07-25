package com.coderprabhu.reactive;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
public class TestClass {

	@Test
	void test() {
		Flux<String> theFlux = 
				Flux.just("A", "B", "C", "D")
					.map(String::toLowerCase)
					.flatMapSequential(letter -> {
						return Flux.just(letter);
					});
		StepVerifier.create(theFlux)
					.expectNext("a", "b", "c", "d")
					.verifyComplete();
	}
}
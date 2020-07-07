package com.coderprabhu.reactive;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
class OnErrorReturnTest {
	/**
	 * Use the onErrorReturn operator to produce a single value to be 
	 * emitted starting from the place where the error was encountered. 
	*/
	@Test
	void context() throws InterruptedException {
		Flux<Integer> resultsInError = Flux.just(1,2,3)
			.flatMap(counter -> {
				if(counter == 2){
					return Flux.error(new IllegalArgumentException("2 is bad!"));
				} else {
					return Flux.just(counter);
				}
			});

			Flux<Integer> iFlux = resultsInError
				.onErrorReturn(0);

			StepVerifier.create(iFlux)
				.expectNext(1,0)
				.verifyComplete();
	}
}

/*
*/
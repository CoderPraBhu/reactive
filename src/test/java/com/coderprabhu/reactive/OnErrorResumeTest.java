package com.coderprabhu.reactive;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
class OnErrorResumeTest {
	/**
	 * Use the onErrorResume operator to produce a Publisher that should be emitted 
	 * starting from the place where the error was encountered. 
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
				.onErrorResume(IllegalArgumentException.class, e -> Flux.just(3,2,1));

			StepVerifier.create(iFlux)
				.expectNext(1,3,2,1)
				.verifyComplete();
	}
}

/*
*/
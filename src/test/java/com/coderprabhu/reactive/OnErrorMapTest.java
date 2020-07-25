package com.coderprabhu.reactive;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
class OnErrorMapTest {
	/**
	 * Use onErrorMap if you want to normalize errors or, for some reason, 
	 * map one error to another. You can use it with other operators to filter 
	 * particular errors, then canonicalize them, then route to a shared error 
	 * handler. 
	*/
	@Test
	void context() throws InterruptedException {
		Flux<Integer> resultsInError = Flux.error(new IllegalArgumentException("error"));
		var isError = new AtomicBoolean();
		Flux<Integer> errorHandler = resultsInError
			.onErrorMap(IllegalArgumentException.class, ex -> new GenericException())
			.doOnError(GenericException.class, ge -> isError.set(true));

		StepVerifier.create(errorHandler)
			.expectError()
			.verify();
	}
	class GenericException extends RuntimeException{

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;
			
	}
}

/*
*/
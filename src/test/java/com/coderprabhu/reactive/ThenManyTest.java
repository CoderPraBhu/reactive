package com.coderprabhu.reactive;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
class ThenManyTest {

	@Test
	void contextLoads() {
		var letters = new AtomicInteger();
		var numbers = new AtomicInteger();
		Flux<String> lettersPublisher = Flux.just("A", "B", "C")
				.doOnNext(value -> letters.incrementAndGet());
		Flux<Integer> numbersPublisher = Flux.just(1,2,3)
				.doOnNext(number -> numbers.incrementAndGet());
		Flux<Integer> thisBeforeThat = lettersPublisher.thenMany(numbersPublisher);
		StepVerifier.create(thisBeforeThat).expectNext(1,2,3).verifyComplete();
		Assertions.assertEquals(letters.get(), 3 );
		Assertions.assertEquals(numbers.get(), 3 );
	}
}

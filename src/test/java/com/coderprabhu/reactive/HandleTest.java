package com.coderprabhu.reactive;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
@SpringBootTest
class HandleTest {
	
	@Test
	void test() {
		StepVerifier
			.create(this.handle(5,4))
			.expectNext(0,1,2,3)
			.expectError(IllegalArgumentException.class)
			.verify();	
		StepVerifier
			.create(this.handle(3,3))
			.expectNext(0,1,2)
			.verifyComplete();	
	}

	private Flux<Integer> handle(int max, int numberToError) {
		return Flux
				.range(0, max)
				.handle((value, sink)->{
					var upto = Stream
								.iterate(0, i -> i < numberToError, i -> i + 1 )
								.collect(Collectors.toList());
					if(upto.contains(value)) {
						sink.next(value);
						return;
					}
					if (value == numberToError){
						sink.error(new IllegalArgumentException("Reached 4"));
						return;
					}
					sink.complete();
				});
	}
}
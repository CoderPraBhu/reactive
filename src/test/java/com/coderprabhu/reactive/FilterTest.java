package com.coderprabhu.reactive;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
class FilterTest {
	
	@Test
	void test() {
		var data = range().filter( i -> i%2==0);
		StepVerifier.create(data).expectNext(2,4,6,8).verifyComplete();
	}
	
	private Flux<Integer> range() {
		return Flux.range(1,8);
	}
}
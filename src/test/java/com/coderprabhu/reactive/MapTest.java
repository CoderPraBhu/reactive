package com.coderprabhu.reactive;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
class MapTest {

	@Test
	void contextLoads() {
		var data = Flux.just("a","b", "c").map(String::toUpperCase);
		StepVerifier.create(data).expectNext("A","B","C").verifyComplete();
	}
}

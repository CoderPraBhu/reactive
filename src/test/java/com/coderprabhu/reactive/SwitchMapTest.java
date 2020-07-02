package com.coderprabhu.reactive;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
class SwitchMapTest {
	
	@Test
	void contextLoads() {
		var data = Flux.just("re","rea","reac","react","reacti","reactive")
				.delayElements(Duration.ofMillis(100))
				.switchMap(this::lookup);
		StepVerifier.create(data).expectNext("reactive-> reactive" ).verifyComplete();
	}

	private Flux<String> lookup(String word) {
		return Flux.just(word + "-> reactive").delayElements(Duration.ofMillis(500));
	}
}
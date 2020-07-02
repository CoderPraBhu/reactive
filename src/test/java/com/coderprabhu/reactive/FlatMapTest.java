package com.coderprabhu.reactive;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
class FlatMapTest {
	
	@Test
	void contextLoads() {
		var data = Flux.just(new Pair(1, 300), new Pair(2,200), new Pair(3,100))
				.flatMap(pair -> this.delayReplayFor(pair));
		StepVerifier.create(data).expectNext(3,2,1).verifyComplete();
	}

	private Flux<Integer> delayReplayFor(Pair pair) {
		return Flux.just(pair.id).delayElements(Duration.ofMillis(pair.delay));
	}
}

@AllArgsConstructor
class Pair {

	int id;
	int delay;
}
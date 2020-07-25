package com.coderprabhu.reactive;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
class ControlFlowTimeoutTest {

	@Test
	void timeout() throws InterruptedException {
		Flux<Integer> one = Flux.just(1, 2, 3).delayElements(Duration.ofSeconds(1)).timeout(Duration.ofMillis(500))
				.onErrorResume(this::onError);
		StepVerifier.create(one).expectNext(0).verifyComplete();
	}

	public Flux<Integer> onError(Throwable t) {
		Assertions.assertTrue(t instanceof TimeoutException, "not equal to" + TimeoutException.class.getName());
		return Flux.just(0);
	}
}
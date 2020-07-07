package com.coderprabhu.reactive;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.util.retry.Retry;

@SpringBootTest
@Log4j2
class ControlFlowRetryTest {
	@Test
	void retry() throws InterruptedException {
		var errored = new AtomicBoolean();
		Flux<String> producer = Flux.create(sink -> {
			if (!errored.get()){
				errored.set(true);
				sink.error(new RuntimeException("Let's assume the error"));
				log.info("Returning " + RuntimeException.class.getName());
			} else {
				log.info("We've already errored. So now let's return the value");
				sink.next("Hello");
			}
			sink.complete();
		});
		Flux<String> fluxThatRetriesOnError = producer.retryWhen(
			Retry.backoff(1, Duration.ofMillis(100)));
		StepVerifier.create(fluxThatRetriesOnError)
			.expectNext("Hello")
			.verifyComplete();
	}
}
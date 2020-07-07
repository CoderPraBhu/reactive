package com.coderprabhu.reactive;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

@Log4j2
@SpringBootTest
class SchedulersHookTest {

	@Test
	void hookTest() {
		var counter = new AtomicInteger();
		Schedulers.onScheduleHook("my hook", runnable -> () -> {
			counter.incrementAndGet();
			log.info("before execution " + Thread.currentThread().getName());
			runnable.run();
			log.info("after execution " + Thread.currentThread().getName());
		});
		Flux<Integer> flux = Flux.just(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15)
			.delayElements(Duration.ofMillis(5000))
			.subscribeOn(Schedulers.immediate());
		StepVerifier.create(flux).expectNext(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15).verifyComplete();
		Assertions.assertEquals(15, counter.get());
	}
}
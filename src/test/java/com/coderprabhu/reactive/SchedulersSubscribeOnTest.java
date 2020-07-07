package com.coderprabhu.reactive;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

@Log4j2
@SpringBootTest
class SchedulersSubscribeOnTest {

	@Test
	void subscribeOnTest() {
		var threadName= SchedulersSubscribeOnTest.class.getName();
		var map = new ConcurrentHashMap<String, AtomicInteger>();
		var executor = Executors.newFixedThreadPool(5, runnable -> {
			Runnable wrapper = () -> {
				var key = Thread.currentThread().getName();
				log.info("executing on " + key);
				var result = map.computeIfAbsent(key, s -> new AtomicInteger() );
				result.incrementAndGet();
				runnable.run();
			};
			return new Thread(wrapper, threadName);
		});
		Scheduler scheduler = Schedulers.fromExecutor(executor);
		Flux<Integer> flux = Flux.just(1,2)
			.subscribeOn(scheduler)
			.doFinally(signal -> map.forEach((k, v ) -> log.info(k + " 1= " + v )));
		Flux<Integer> flux2Flux = Flux.just(1,2)
			.subscribeOn(scheduler)
			.doFinally(signal -> map.forEach((k, v ) -> log.info(k + " 2= " + v )));
		StepVerifier.create(flux).expectNextCount(2).verifyComplete();
		StepVerifier.create(flux2Flux).expectNextCount(2).verifyComplete();

		var atomicInteger = map.get(threadName);
		Assertions.assertEquals(2, atomicInteger.get());
	}
}
package com.coderprabhu.reactive;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.test.StepVerifier;

@SpringBootTest
@Log4j2
class FluxCreateTest {

	ExecutorService executor = Executors.newFixedThreadPool(1);
	
	@Test
	void contextLoads() {
		Flux<Integer> flux= Flux.create(emitter -> this.launch(emitter,5));
		StepVerifier.create(flux.doFinally(onFinally -> this.executor.shutdown()))
					.expectNextCount(5)
					.verifyComplete();
	}

	private void launch(FluxSink<Integer> emitter, int count) {
	
		executor.submit(() -> {
			var integer = new AtomicInteger();
			while(integer.get()< 5 ) {
				double random = Math.random();
				emitter.next(integer.incrementAndGet());
				try {
					Thread.sleep( (long) (1000 * random));
				} catch (InterruptedException e) {
					log.error(e);
				}
			}
			emitter.complete();
		});
	}

}

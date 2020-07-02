package com.coderprabhu.reactive;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
@Log4j2
class TransformTest {

	@Test
	void contextLoads() {
		var finished = new AtomicBoolean();
		var letters = Flux.just("A", "B", "C")
				.transform(stringFlux -> stringFlux.doFinally(onFinally -> finished.set(true)));
		StepVerifier.create(letters).expectNextCount(3).verifyComplete();
		Assertions.assertTrue(finished.get());
	}
}

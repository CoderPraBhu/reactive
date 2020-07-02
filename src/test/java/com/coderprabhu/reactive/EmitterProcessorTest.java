package com.coderprabhu.reactive;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.FluxSink;
import reactor.test.StepVerifier;

@SpringBootTest
class EmitterProcessorTest {

	@Test
	void contextLoads() {
		EmitterProcessor<String> processor = EmitterProcessor.create();
		produce(processor.sink());
		consume(processor);
	}

	private void produce(FluxSink<String> sink) {
		sink.next("1");
		sink.next("2");
		sink.next("3");
		sink.complete();
	}

	private void consume(EmitterProcessor<String> processor) {
		StepVerifier.create(processor)
			.expectNext("1")
			.expectNext("2")
			.expectNext("3")
			.verifyComplete();
	}

}

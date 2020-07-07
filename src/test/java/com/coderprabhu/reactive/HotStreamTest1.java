package com.coderprabhu.reactive;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.FluxSink;

@SpringBootTest
class HotStreamTest1 {

	@Test
	void hotStreamTest() {
		var first = new ArrayList<Integer>();
		var second = new ArrayList<Integer>();
		EmitterProcessor<Integer> emitter = EmitterProcessor.create(2);
		FluxSink<Integer> sink = emitter.sink();
		emitter.subscribe(first::add);
		sink.next(4);
		sink.next(5);
		emitter.subscribe(second::add);
		sink.next(6);
		sink.complete();
		assertEquals(Arrays.asList(Arrays.array(4, 5, 6)), first);
		assertEquals(Arrays.asList(Arrays.array(6)), second);
	}
}
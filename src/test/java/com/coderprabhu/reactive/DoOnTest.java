package com.coderprabhu.reactive;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Signal;
import reactor.core.publisher.SignalType;
import reactor.test.StepVerifier;
@Log4j2
@SpringBootTest
class DoOnTest {
	
	@Test
	void test() {
		
		var signals = new ArrayList<Signal<Integer>>();
		var nextValues = new ArrayList<Integer>();
		var subscriptions = new ArrayList<Subscription>();
		var exceptions = new ArrayList<Throwable>();
		var finallySignals = new ArrayList<SignalType>();

		Flux<Integer> on = Flux.<Integer>create(sink -> {
								sink.next(1);
								sink.next(2);
								sink.next(3);
								sink.error(new IllegalArgumentException("oops"));
								sink.complete();
							})
							.doOnNext(nextValues::add)
							.doOnEach(signals::add)
							.doOnSubscribe(subscriptions::add)
							.doOnError(IllegalArgumentException.class, exceptions::add)
							.doFinally(finallySignals::add);
		StepVerifier.create(on)
				.expectNext(1,2,3)
				.expectError(IllegalArgumentException.class)
				.verify();
		signals.stream().map(val -> "signal: " + val).forEach(log::info);
		assertEquals(4, signals.size());
		finallySignals.stream().map(val -> "finallySignal: " + val).forEach(log::info);
		assertEquals(1, finallySignals.size());
		subscriptions.stream().map(val -> "subscriptions: " + val).forEach(log::info);
		assertEquals(1, subscriptions.size());
		exceptions.stream().map(val -> "exceptions: " + val).forEach(log::info);
		assertEquals(1, exceptions.size());
		assertTrue(exceptions.get(0) instanceof IllegalArgumentException);
		nextValues.stream().map(val -> "nextValues: " + val).forEach(log::info);
		assertEquals(3, nextValues.size());
		assertEquals(Arrays.asList(Arrays.array(1,2,3)), nextValues);		
	}
}
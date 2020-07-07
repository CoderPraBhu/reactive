package com.coderprabhu.reactive;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Signal;
import reactor.core.publisher.SignalType;
import reactor.util.context.Context;

@SpringBootTest
@Log4j2
class ContextTest {
	@Test
	void context() throws InterruptedException {
		var max = 3;
		var contextVals = new ConcurrentHashMap<String, AtomicInteger>(); 
		var key = "key1";
		var cdl = new CountDownLatch(max);
		Context context = Context.of(key, "value1");
		Flux<Integer> just = Flux.range(0,max)
			.delayElements(Duration.ofMillis(2000))
			.doOnEach((Signal<Integer> integerSignal) -> {
				Context localContext = integerSignal.getContext();
				if(integerSignal.getType() == SignalType.ON_NEXT){
					String currentKeyValue = localContext.get(key);
					Assertions.assertNotNull(currentKeyValue);
					Assertions.assertEquals(currentKeyValue, "value1");
					contextVals
						.computeIfAbsent("key1", k -> new AtomicInteger(0))
						.incrementAndGet();
				}
			}).subscriberContext(context);
		just.subscribe(integer -> {
			log.info("Integer: " + integer);
			cdl.countDown();
		});
		cdl.await();
		assertEquals(max, contextVals.get(key).get());
	}
}

/*
2020-07-06 17:24:47.667  INFO 89259 --- [     parallel-1] com.coderprabhu.reactive.ContextTest     : Integer: 0
2020-07-06 17:24:49.674  INFO 89259 --- [     parallel-2] com.coderprabhu.reactive.ContextTest     : Integer: 1
2020-07-06 17:24:51.675  INFO 89259 --- [     parallel-3] com.coderprabhu.reactive.ContextTest     : Integer: 2
*/
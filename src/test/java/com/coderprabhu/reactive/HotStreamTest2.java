package com.coderprabhu.reactive;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;

@SpringBootTest
@Log4j2
class HotStreamTest2 {

	@Test
	/** forces convergence of both asynchronous subscribers with a CountDownLatch */
	void hotStreamTest() throws InterruptedException {
		int factor = 1000;
		log.info("start");
		var cdl = new CountDownLatch(2);
		Flux<Integer> live = Flux.range(0, 10).delayElements(Duration.ofMillis(factor)).share();
		var one = new ArrayList<Integer>();
		var two = new ArrayList<Integer>();
		live.doFinally(signalTypeConsumer(cdl)).subscribe(collect(one, "first"));
		Thread.sleep(factor * 4);
		log.info("sleeping : " + factor * 4);
		live.doFinally(signalTypeConsumer(cdl)).subscribe(collect(two, "second"));
		log.info("waiting for latch to come down to 0");
		cdl.await(20, TimeUnit.SECONDS);
		log.info("wait for latch to come down to 0 complete");
		assertTrue(one.size() > two.size());
		log.info("one" + one + " two " + two);
		log.info("stop");
	}

	private Consumer<? super Integer> collect(ArrayList<Integer> lst, String name) {
		// log.info("list so far " + lst);
		return elm -> { 
			lst.add(elm);
			log.info(" adding " + elm + " "+ name +" list so far " + lst );
		};
	}

	private Consumer<SignalType> signalTypeConsumer(CountDownLatch cdl) {
		return signal -> {
			if(signal.equals(SignalType.ON_COMPLETE)){
				cdl.countDown();
				log.info("reducing countdown... ");
			} else {
				log.info("signal... " + signal);
			}
		};
	}
}

/*

2020-07-04 19:21:02.024  INFO 63827 --- [           main] com.coderprabhu.reactive.HotStreamTest2  : start
2020-07-04 19:21:03.046  INFO 63827 --- [     parallel-1] com.coderprabhu.reactive.HotStreamTest2  :  adding 0 first list so far [0]
2020-07-04 19:21:04.052  INFO 63827 --- [     parallel-2] com.coderprabhu.reactive.HotStreamTest2  :  adding 1 first list so far [0, 1]
2020-07-04 19:21:05.053  INFO 63827 --- [     parallel-3] com.coderprabhu.reactive.HotStreamTest2  :  adding 2 first list so far [0, 1, 2]
2020-07-04 19:21:06.045  INFO 63827 --- [           main] com.coderprabhu.reactive.HotStreamTest2  : sleeping : 4000
2020-07-04 19:21:06.045  INFO 63827 --- [           main] com.coderprabhu.reactive.HotStreamTest2  : waiting for latch to come down to 0
2020-07-04 19:21:06.058  INFO 63827 --- [     parallel-4] com.coderprabhu.reactive.HotStreamTest2  :  adding 3 first list so far [0, 1, 2, 3]
2020-07-04 19:21:06.059  INFO 63827 --- [     parallel-4] com.coderprabhu.reactive.HotStreamTest2  :  adding 3 second list so far [3]
2020-07-04 19:21:07.063  INFO 63827 --- [     parallel-5] com.coderprabhu.reactive.HotStreamTest2  :  adding 4 first list so far [0, 1, 2, 3, 4]
2020-07-04 19:21:07.063  INFO 63827 --- [     parallel-5] com.coderprabhu.reactive.HotStreamTest2  :  adding 4 second list so far [3, 4]
2020-07-04 19:21:08.066  INFO 63827 --- [     parallel-6] com.coderprabhu.reactive.HotStreamTest2  :  adding 5 first list so far [0, 1, 2, 3, 4, 5]
2020-07-04 19:21:08.066  INFO 63827 --- [     parallel-6] com.coderprabhu.reactive.HotStreamTest2  :  adding 5 second list so far [3, 4, 5]
2020-07-04 19:21:09.070  INFO 63827 --- [     parallel-7] com.coderprabhu.reactive.HotStreamTest2  :  adding 6 first list so far [0, 1, 2, 3, 4, 5, 6]
2020-07-04 19:21:09.070  INFO 63827 --- [     parallel-7] com.coderprabhu.reactive.HotStreamTest2  :  adding 6 second list so far [3, 4, 5, 6]
2020-07-04 19:21:10.073  INFO 63827 --- [     parallel-8] com.coderprabhu.reactive.HotStreamTest2  :  adding 7 first list so far [0, 1, 2, 3, 4, 5, 6, 7]
2020-07-04 19:21:10.074  INFO 63827 --- [     parallel-8] com.coderprabhu.reactive.HotStreamTest2  :  adding 7 second list so far [3, 4, 5, 6, 7]
2020-07-04 19:21:11.077  INFO 63827 --- [     parallel-9] com.coderprabhu.reactive.HotStreamTest2  :  adding 8 first list so far [0, 1, 2, 3, 4, 5, 6, 7, 8]
2020-07-04 19:21:11.078  INFO 63827 --- [     parallel-9] com.coderprabhu.reactive.HotStreamTest2  :  adding 8 second list so far [3, 4, 5, 6, 7, 8]
2020-07-04 19:21:12.078  INFO 63827 --- [    parallel-10] com.coderprabhu.reactive.HotStreamTest2  :  adding 9 first list so far [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]

*/
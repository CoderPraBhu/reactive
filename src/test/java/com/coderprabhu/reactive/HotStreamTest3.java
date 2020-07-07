package com.coderprabhu.reactive;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@SpringBootTest
@Log4j2
class HotStreamTest3 {

	private List<Integer> one = new ArrayList<>();
	private List<Integer> two = new ArrayList<>();
	private List<Integer> three = new ArrayList<>();

	private Consumer<Integer> subscribe(List<Integer> list, String name ){
		return elm -> { 
			list.add(elm);
			log.info(" adding " + elm + " "+ name +" list so far " + list );
		};
	}
	@Test
	/** use the publish operator to create a Publisher that lets you "pile on" subscribers until a limit is reached. 
	 * Then, all subscribers may observe the results.  */
	void hotStreamTest() throws InterruptedException {
		Flux<Integer> pileOn = Flux.just(1,2,3)
			.publish()
			.autoConnect(3)
			.subscribeOn(Schedulers.immediate()); // same thread
		pileOn.subscribe(subscribe(one, "first"));
		assertEquals(0,this.one.size());
		pileOn.subscribe(subscribe(two, "second"));
		assertEquals(0,this.two.size());
		pileOn.subscribe(subscribe(three, "third"));
		assertEquals(3, this.one.size());
		assertEquals(3,this.two.size());
		assertEquals(3, this.three.size());
	}
}

/*

2020-07-04 19:37:23.196  INFO 64333 --- [           main] com.coderprabhu.reactive.HotStreamTest3  : Started HotStreamTest3 in 0.994 seconds (JVM running for 1.631)
2020-07-04 19:37:23.462  INFO 64333 --- [           main] com.coderprabhu.reactive.HotStreamTest3  :  adding 1 first list so far [1]
2020-07-04 19:37:23.463  INFO 64333 --- [           main] com.coderprabhu.reactive.HotStreamTest3  :  adding 1 second list so far [1]
2020-07-04 19:37:23.463  INFO 64333 --- [           main] com.coderprabhu.reactive.HotStreamTest3  :  adding 1 third list so far [1]
2020-07-04 19:37:23.463  INFO 64333 --- [           main] com.coderprabhu.reactive.HotStreamTest3  :  adding 2 first list so far [1, 2]
2020-07-04 19:37:23.463  INFO 64333 --- [           main] com.coderprabhu.reactive.HotStreamTest3  :  adding 2 second list so far [1, 2]
2020-07-04 19:37:23.464  INFO 64333 --- [           main] com.coderprabhu.reactive.HotStreamTest3  :  adding 2 third list so far [1, 2]
2020-07-04 19:37:23.464  INFO 64333 --- [           main] com.coderprabhu.reactive.HotStreamTest3  :  adding 3 first list so far [1, 2, 3]
2020-07-04 19:37:23.464  INFO 64333 --- [           main] com.coderprabhu.reactive.HotStreamTest3  :  adding 3 second list so far [1, 2, 3]
2020-07-04 19:37:23.464  INFO 64333 --- [           main] com.coderprabhu.reactive.HotStreamTest3  :  adding 3 third list so far [1, 2, 3]
*/
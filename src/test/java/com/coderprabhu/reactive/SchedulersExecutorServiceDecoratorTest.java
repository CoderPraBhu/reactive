package com.coderprabhu.reactive;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import org.aopalliance.intercept.MethodInterceptor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

@Log4j2
@SpringBootTest
class SchedulersExecutorServiceDecoratorTest {

	private final AtomicInteger methodInvocationCount = new AtomicInteger();

	private String rsb = "rsb";

	@BeforeEach
	public void before() {
		Schedulers.resetFactory();
		Schedulers.addExecutorServiceDecorator(this.rsb,
				(scheduler, scheduledExecutorService) -> this.decorate(scheduledExecutorService));
	}

	@AfterEach
	public void after() {
		Schedulers.resetFactory();
		Schedulers.removeExecutorServiceDecorator(this.rsb);
	}

	@Test
	void changeDefaultDecorator() {
		Flux<Integer> flux = Flux.just(1).delayElements(Duration.ofMillis(1));
		StepVerifier.create(flux).thenAwait(Duration.ofMillis(10)).expectNextCount(1).verifyComplete();
		Assertions.assertEquals(1, this.methodInvocationCount.get());
	}

	private ScheduledExecutorService decorate(ScheduledExecutorService scheduledExecutorService) {
		var pfb = new ProxyFactoryBean();
		try {
			pfb.setProxyInterfaces(new Class[] { ScheduledExecutorService.class });
			pfb.addAdvice((MethodInterceptor)methodInvocation -> {
				var methodName = methodInvocation.getMethod().getName().toLowerCase();
				this.methodInvocationCount.incrementAndGet();
				log.info("methodName: " + methodName + " incrementing.. " );
				return methodInvocation.proceed();
			});
			pfb.setSingleton(true);
			pfb.setTarget(scheduledExecutorService);
			return (ScheduledExecutorService) pfb.getObject();
		} catch (ClassNotFoundException e) {
			log.error(e);
		}
		return null;
	}
}
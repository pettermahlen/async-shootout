package com.spotify.asynctest;

import rx.Scheduler;
import rx.schedulers.Schedulers;

import java.util.concurrent.Executors;

public class RxJavaThingTest extends ThingTester {
  @Override
  protected AsyncThing createThing(Services services) {
    Scheduler scheduler = Schedulers.from(Executors.newSingleThreadExecutor());

    // see RxServices for wrapping rationale
    return new RxJavaThing(new RxServices(services, scheduler));
  }
}

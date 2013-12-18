package com.spotify.asynctest;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * TODO: document!
 */
public class TrickleThingTest extends ThingTester {
  private final Executor executor = Executors.newSingleThreadExecutor();

  @Override
  protected AsyncThing createThing(Services services) {
    return new TrickleThing(services, executor);
  }
}

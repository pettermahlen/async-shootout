package com.spotify.asynctest;

/**
 * TODO: document!
 */
public class ListenableFutureThingTest extends ThingTester {
  @Override
  protected AsyncThing createThing(Services services) {
    return new ListenableFutureThing(services);
  }
}

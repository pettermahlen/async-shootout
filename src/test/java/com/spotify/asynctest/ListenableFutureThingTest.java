package com.spotify.asynctest;

public class ListenableFutureThingTest extends ThingTester {
  @Override
  protected AsyncThing createThing(Services services) {
    return new ListenableFutureThing(services);
  }
}

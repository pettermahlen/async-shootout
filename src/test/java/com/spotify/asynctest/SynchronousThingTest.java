package com.spotify.asynctest;

public class SynchronousThingTest extends ThingTester {
  @Override
  protected AsyncThing createThing(Services services) {
    return new SynchronousThing(services);
  }
}

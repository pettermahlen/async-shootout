package com.spotify.asynctest;

public class TrickleThingTest extends ThingTester {
  @Override
  protected AsyncThing createThing(Services services) {
    return new TrickleThing(services);
  }
}

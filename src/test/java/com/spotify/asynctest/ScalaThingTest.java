/*
 * Copyright (c) 2013 Spotify AB
 */

package com.spotify.asynctest;

public class ScalaThingTest extends ThingTester {
  @Override
  protected AsyncThing createThing(Services services) {
    final ScalaServices scalaServices = new ScalaServicesBridge(services);
    final ScalaFutureThing scalaFutureThing = new ScalaFutureThing(scalaServices);
    return new ScalaThingToJavaThing(scalaFutureThing);
  }
}

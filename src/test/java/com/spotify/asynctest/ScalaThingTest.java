/*
 * Copyright (c) 2013 Spotify AB
 */

package com.spotify.asynctest;

public class ScalaThingTest extends ThingTester {
  @Override
  protected AsyncThing createThing(Services services) {
    final ScalaServices scalaServices = new ScalaServicesBridge(services);
    final ScalaThing scalaThing = new ScalaThing(scalaServices);
    return new ScalaThingToJavaThing(scalaThing);
  }
}

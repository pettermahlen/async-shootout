package com.spotify.asynctest;

import com.google.common.util.concurrent.ListenableFuture;

/**
 *  Implementation of {@link AsyncThing} using Trickle. See https://ghe.spotify.net/step/trickle
 *  for information about how to use Trickle.
 */
public class TrickleThing implements AsyncThing {
  public TrickleThing(final Services services) {
  }

  @Override
  public ListenableFuture<String> call(int number, String userName) {
    throw new UnsupportedOperationException();
  }
}

package com.spotify.asynctest;

import com.google.common.util.concurrent.ListenableFuture;

/**
 * TODO: document!
 */
public class TrickleThing implements AsyncThing {
  @Override
  public ListenableFuture<String> call(int number, String userName) {
    throw new UnsupportedOperationException();
  }
}

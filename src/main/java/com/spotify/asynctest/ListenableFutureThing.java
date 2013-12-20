package com.spotify.asynctest;

import com.google.common.util.concurrent.ListenableFuture;

/**
 * Implementation of {@link AsyncThing} using ListenableFutures. See
 * https://code.google.com/p/guava-libraries/wiki/ListenableFutureExplained and
 * https://blogit.spotify.net/2013/08/15/asynchronous-calls-in-java/ for some more information
 * on how to work with ListenableFutures. Also see
 * https://git.spotify.net/cgit.cgi/java/futures-extra.git/tree/src/main/java/com/spotify/futures/FuturesExtra.java
 * for some utility functions that may be useful.
 */
public class ListenableFutureThing implements AsyncThing {
  private final Services services;

  public ListenableFutureThing(Services services) {
    this.services = services;
  }

  @Override
  public ListenableFuture<String> call(int number, final String userName) {
    throw new UnsupportedOperationException();
  }

}

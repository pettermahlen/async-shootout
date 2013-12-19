package com.spotify.asynctest;

import com.google.common.util.concurrent.ListenableFuture;

/**
 * Implementation of {@link AsyncThing} using ListenableFutures. See
 * https://code.google.com/p/guava-libraries/wiki/ListenableFutureExplained and
 * https://blogit.spotify.net/2013/08/15/asynchronous-calls-in-java/ for some more information
 * on how to work with ListenableFutures.
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

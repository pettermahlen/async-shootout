package com.spotify.asynctest;

import com.google.common.util.concurrent.ListenableFuture;

/**
 * Interface to implement.
 */
public interface AsyncThing {
  ListenableFuture<String> call(int number, String userName);
}

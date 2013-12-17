package com.spotify.asynctest;

import com.google.common.util.concurrent.ListenableFuture;

/**
 * TODO: document!
 */
public interface AsyncThing {
  ListenableFuture<String> call(int number, String userName);
}

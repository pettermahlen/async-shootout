package com.spotify.asynctest;

import com.google.common.util.concurrent.ListenableFuture;

/**
 * Interface defining the downstream service calls.
 */
public interface Services {
  ListenableFuture<LookupResult> lookup(int number, String userName);

  ListenableFuture<Void> log(String userName, LookupResult result);

  ListenableFuture<DecorationResult> decorateVersionA(LookupResult result);

  ListenableFuture<DecorationResult> decorateVersionB(LookupResult result);
}

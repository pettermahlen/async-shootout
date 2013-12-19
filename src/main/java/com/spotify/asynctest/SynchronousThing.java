package com.spotify.asynctest;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Synchronous implementation of the graph, shown as an example. This class doesn't pass all the
 * tests.
 */
public class SynchronousThing implements AsyncThing {
  private static final Logger LOG = LoggerFactory.getLogger(SynchronousThing.class);

  private final Services services;

  public SynchronousThing(Services services) {
    this.services = services;
  }

  @Override
  public ListenableFuture<String> call(int number, String userName) {
    try {
      LookupResult lookupResult = services.lookup(number, userName).get(100, TimeUnit.MILLISECONDS);

      DecorationResult decorationResult = getDecorationResult(lookupResult);

      services.log(userName, lookupResult).get(100, TimeUnit.MILLISECONDS);

      return Futures.immediateFuture(decorationResult.getDecorationResult());
    }
    catch (Exception e) {
      return Futures.immediateFailedFuture(e);
    }
  }

  private DecorationResult getDecorationResult(LookupResult lookupResult) {
    DecorationResult decorationResult;

    try {
      if (lookupResult.getVersion() == Version.A) {
        decorationResult = services.decorateVersionA(lookupResult).get(100, TimeUnit.MILLISECONDS);
      }
      else {
        decorationResult = services.decorateVersionB(lookupResult).get(100, TimeUnit.MILLISECONDS);
      }
    }
    catch (Exception e) {
      LOG.error("Unable to decorate", e);
      decorationResult = DecorationResult.DEFAULT;
    }

    return decorationResult;
  }
}

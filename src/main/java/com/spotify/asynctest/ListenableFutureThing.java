package com.spotify.asynctest;

import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.FutureFallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

import static com.google.common.util.concurrent.Futures.immediateFuture;

/**
 * TODO: document!
 */
public class ListenableFutureThing implements AsyncThing {
  private final Services services;

  public ListenableFutureThing(Services services) {
    this.services = services;
  }

  @Override
  public ListenableFuture<String> call(int number, final String userName) {
    ListenableFuture<LookupResult> lookupFuture = services.lookup(number, userName);

    final ListenableFuture<DecorationResult> decorationFuture =
        Futures.withFallback(Futures.transform(
        lookupFuture,
        new AsyncFunction<LookupResult, DecorationResult>() {
          @Override
          public ListenableFuture<DecorationResult> apply(LookupResult lookupResult) throws Exception {
            return getDecorationResult(lookupResult);
          }
        }), new FutureFallback<DecorationResult>() {
          @Override
          public ListenableFuture<DecorationResult> create(Throwable t) throws Exception {
            return immediateFuture(DecorationResult.DEFAULT);
          }
        });

    ListenableFuture<Void> logFuture = Futures.transform(
        lookupFuture,
        new AsyncFunction<LookupResult, Void>() {
          @Override
          public ListenableFuture<Void> apply(LookupResult lookupResult) throws Exception {
            return services.log(userName, lookupResult);
          }
        });

    ListenableFuture<List<Object>> doneSignal = Futures.allAsList(decorationFuture, logFuture);

    return Futures.transform(doneSignal, new AsyncFunction<List<Object>, String>() {
      @Override
      public ListenableFuture<String> apply(List<Object> input) throws Exception {
        return immediateFuture(decorationFuture.get().getDecorationResult());
      }
    });
  }

  private ListenableFuture<DecorationResult> getDecorationResult(LookupResult lookupResult) {
    if (lookupResult.getVersion() == Version.A) {
      return services.decorateVersionA(lookupResult);
    }
    else {
      return services.decorateVersionB(lookupResult);
    }
  }
}

package com.spotify.asynctest;

import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.FutureFallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

import static com.google.common.util.concurrent.Futures.immediateFuture;
import static com.google.common.util.concurrent.Futures.transform;

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
    ListenableFuture<LookupResult> lookup = services.lookup(number, userName);

    final ListenableFuture<DecorationResult> decorate =
        Futures.withFallback(
            transform(
                lookup,
                new AsyncFunction<LookupResult, DecorationResult>() {
                  @Override
                  public ListenableFuture<DecorationResult> apply(LookupResult lookupResult) throws Exception {
                    return lookupResult.getVersion() == Version.A ? services.decorateVersionA(lookupResult) : services.decorateVersionB(lookupResult);
                  }
                }),
            new FutureFallback<DecorationResult>() {
              @Override
              public ListenableFuture<DecorationResult> create(Throwable t) throws Exception {
                return immediateFuture(DecorationResult.DEFAULT);
              }
            });

    ListenableFuture<Void> log = transform(
        lookup,
        new AsyncFunction<LookupResult, Void>() {
          @Override
          public ListenableFuture<Void> apply(LookupResult lookupResult) throws Exception {
            return services.log(userName, lookupResult);
          }
        });

    ListenableFuture<List<Object>> doneSignal = Futures.allAsList(decorate, log);

    return transform(
        doneSignal,
        new AsyncFunction<List<Object>, String>() {
          @Override
          public ListenableFuture<String> apply(List<Object> input) throws Exception {
            return immediateFuture(decorate.get().getDecorationResult());
          }
        });
  }

}

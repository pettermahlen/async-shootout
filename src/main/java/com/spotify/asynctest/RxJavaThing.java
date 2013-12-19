package com.spotify.asynctest;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import rx.Observable;
import rx.util.functions.Action1;

/**
 * Implementation of {@link AsyncThing} using RxJava. See
 * https://github.com/Netflix/RxJava/wiki/Getting-Started for information on how to use
 * RxJava.
 */
public class RxJavaThing implements AsyncThing {
  private final RxServices services;

  public RxJavaThing(RxServices services) {
    this.services = services;
  }

  @Override
  public ListenableFuture<String> call(int number, final String userName) {
    throw new UnsupportedOperationException();
  }

  /*
   * Utility method that helps make it easier for you to return a Future based on an observable.
   */
  private <T> ListenableFuture<T> toFuture(Observable<T> observable) {
    final SettableFuture<T> result = SettableFuture.create();

    observable.subscribe(
        new Action1<T>() {
          @Override
          public void call(T t) {
            result.set(t);
          }
        },
        new Action1<Throwable>() {
          @Override
          public void call(Throwable e) {
            result.setException(e);
          }
        });

    return result;
  }}

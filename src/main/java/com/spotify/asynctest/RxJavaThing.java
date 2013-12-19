package com.spotify.asynctest;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import rx.Observable;
import rx.util.functions.Action1;
import rx.util.functions.Func1;
import rx.util.functions.Func2;

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
    Observable<LookupResult> lookup = services.lookup(number, userName);

    Observable<Boolean> log = lookup.flatMap(new Func1<LookupResult, Observable<? extends Boolean>>() {
      @Override
      public Observable<? extends Boolean> call(LookupResult lookupResult) {
        return services.log(userName, lookupResult);
      }
    });

    Observable<DecorationResult> decoration = lookup.flatMap(
        new Func1<LookupResult, Observable<? extends DecorationResult>>() {
          @Override
          public Observable<? extends DecorationResult> call(LookupResult lookupResult) {
            return (lookupResult.getVersion() == Version.A) ? services.decorateVersionA(lookupResult) : services.decorateVersionB(lookupResult);
          }
        }
    ).onErrorReturn(staticResult(DecorationResult.DEFAULT));

    Observable<String> result = Observable.zip(log, decoration, new Func2<Boolean, DecorationResult, String>() {
      @Override
      public String call(Boolean aBoolean, DecorationResult decorationResult) {
        return decorationResult.getDecorationResult();
      }
    });

    return toFuture(result);
  }

  private <R> Func1<Throwable, R> staticResult(final R result) {
    return new Func1<Throwable, R>() {
      @Override
      public R call(Throwable throwable) {
        return result;
      }
    };
  }

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

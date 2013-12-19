package com.spotify.asynctest;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import rx.Observable;
import rx.util.functions.Action1;
import rx.util.functions.Func1;
import rx.util.functions.Func2;

/**
 * TODO: document!
 */
public class RxJavaThing implements AsyncThing {
  private final RxServices services;

  public RxJavaThing(RxServices services) {
    this.services = services;
  }

  @Override
  public ListenableFuture<String> call(int number, final String userName) {
    Observable<LookupResult> lookupResultObservable = services.lookup(number, userName);

    Observable<Boolean> logObservable = lookupResultObservable.flatMap(new Func1<LookupResult, Observable<? extends Boolean>>() {
      @Override
      public Observable<? extends Boolean> call(LookupResult lookupResult) {
        return services.log(userName, lookupResult);
      }
    });

    Observable<DecorationResult> decorationResultObservable = lookupResultObservable.flatMap(
        new Func1<LookupResult, Observable<? extends DecorationResult>>() {
          @Override
          public Observable<? extends DecorationResult> call(LookupResult lookupResult) {
            if (lookupResult.getVersion() == Version.A) {
              return services.decorateVersionA(lookupResult);
            }

            return services.decorateVersionB(lookupResult);
          }
        }
    ).onErrorReturn(new Func1<Throwable, DecorationResult>() {
      @Override
      public DecorationResult call(Throwable throwable) {
        return DecorationResult.DEFAULT;
      }
    });

    Observable<String> result = Observable.zip(logObservable, decorationResultObservable, new Func2<Boolean, DecorationResult, String>() {
      @Override
      public String call(Boolean aBoolean, DecorationResult decorationResult) {
        return decorationResult.getDecorationResult();
      }
    });

    return toFuture(result);
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

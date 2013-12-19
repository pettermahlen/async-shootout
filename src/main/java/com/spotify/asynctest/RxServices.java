package com.spotify.asynctest;

import com.google.common.base.Function;
import com.google.common.util.concurrent.Futures;
import rx.Observable;
import rx.Scheduler;

/**
 * Wrapper to make it easier to use Observables in the RxJavaThing implementation. This is cheating
 * a little bit in the sense that it makes it a bit easier to use RxJava, but the rationale is that
 * in a real RxJava scenario, our services/service clients would be returning Observables rather
 * than Futures. This class handles some more or less obscure details relating to that.
 */
public class RxServices {
  private final Services services;
  private final Scheduler scheduler;

  public RxServices(Services services, Scheduler scheduler) {
    this.services = services;
    this.scheduler = scheduler;
  }

  Observable<LookupResult> lookup(int number, String userName) {
    return Observable.from(services.lookup(number, userName), scheduler);
  }

  Observable<Boolean> log(String userName, LookupResult result){
    // Observables don't handle void values, hence the conversion to boolean.
    return Observable.from(Futures.transform(services.log(userName, result), new Function<Void, Boolean>() {
      @Override
      public Boolean apply(Void input) {
        return true;
      }
    }));
  }

  Observable<DecorationResult> decorateVersionA(LookupResult result) {
    return Observable.from(services.decorateVersionA(result));
  }

  Observable<DecorationResult> decorateVersionB(LookupResult result) {
    return Observable.from(services.decorateVersionB(result));
  }
}
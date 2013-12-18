package com.spotify.asynctest;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.spotify.trickle.Graph;
import com.spotify.trickle.Name;
import com.spotify.trickle.Node1;
import com.spotify.trickle.Node2;
import com.spotify.trickle.Trickle;

import java.util.concurrent.Executor;

import static com.spotify.trickle.Name.named;

/**
 * TODO: document!
 */
public class TrickleThing implements AsyncThing {
  public static final Name<String> USER_NAME = named("userName", String.class);
  public static final Name<Integer> NUMBER = named("number", Integer.class);

  private final Graph<String> graph;
  private final Executor executor;

  public TrickleThing(final Services services, Executor executor) {
    this.executor = executor;
    Node2<Integer, String, LookupResult> lookup = new Node2<Integer, String, LookupResult>() {
      @Override
      public ListenableFuture<LookupResult> run(Integer number, String userName) {
        return services.lookup(number, userName);
      }
    };
    Node1<LookupResult, DecorationResult> decorate = new Node1<LookupResult, DecorationResult>() {
      @Override
      public ListenableFuture<DecorationResult> run(LookupResult arg) {
        return (arg.getVersion() == Version.A) ? services.decorateVersionA(arg) : services.decorateVersionB(arg);
      }
    };
    Node2<String, LookupResult, Void> log = new Node2<String, LookupResult, Void>() {
      @Override
      public ListenableFuture<Void> run(String userName, LookupResult lookupResult) {
        return services.log(userName, lookupResult);
      }
    };
    Node1<DecorationResult, String> result = new Node1<DecorationResult, String>() {
      @Override
      public ListenableFuture<String> run(DecorationResult arg) {
        return Futures.immediateFuture(arg.getDecorationResult());
      }
    };

    graph = Trickle.graph(String.class)
        .call(lookup).with(NUMBER, USER_NAME)
        .call(decorate).with(lookup).fallback(DecorationResult.DEFAULT)
        .call(log).with(USER_NAME, lookup)
        .call(result).with(decorate).after(log)
        .output(result);
  }

  @Override
  public ListenableFuture<String> call(int number, String userName) {
    return graph.bind(NUMBER, number).bind(USER_NAME, userName).run(executor);
  }
}

package com.spotify.asynctest;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.google.common.util.concurrent.Futures.immediateFuture;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * TODO: document!
 */
public abstract class ThingTester {
  Services services;
  BlockableServices blockableServices;
  FailableServices failableServices;

  protected abstract AsyncThing createThing(Services services);

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Before
  public final void setUpThingTester() throws Exception {
    blockableServices = new BlockableServices();
    failableServices = new FailableServices();
  }

  @Test
  public void shouldCallMethodsWithCorrectParametersA() throws Exception {
    services = mock(Services.class);

    LookupResult lookupResult = new LookupResult(Version.A, 897234L);

    when(services.lookup(124, "heyho")).thenReturn(immediateFuture(lookupResult));
    when(services.decorateVersionA(lookupResult)).thenReturn(immediateFuture(new DecorationResult("decoorated")));
    when(services.log("heyho", lookupResult)).thenReturn(Futures.<Void>immediateFuture(null));

    assertThat(createThing(services).call(124, "heyho").get(), equalTo("decoorated"));
    verify(services, never()).decorateVersionB(any(LookupResult.class));
  }

  @Test
  public void shouldCallMethodsWithCorrectParametersB() throws Exception {
    services = mock(Services.class);

    LookupResult lookupResult = new LookupResult(Version.B, 8787L);

    when(services.lookup(877, "sdfhwe")).thenReturn(immediateFuture(lookupResult));
    when(services.decorateVersionB(lookupResult)).thenReturn(immediateFuture(new DecorationResult("decoorated")));
    when(services.log("sdfhwe", lookupResult)).thenReturn(Futures.<Void>immediateFuture(null));

    assertThat(createThing(services).call(877, "sdfhwe").get(), equalTo("decoorated"));
    verify(services, never()).decorateVersionA(any(LookupResult.class));
  }

  @Test
  public void shouldWaitForLog() throws Exception {
    blockableServices.blockLog();

    ListenableFuture<String> future = startAndCheckFutureNotDone();

    releaseAndCheckFutureCompletes(future);
  }

  @Test
  public void shouldWaitForLookup() throws Exception {
    blockableServices.blockLookup();

    ListenableFuture<String> future = startAndCheckFutureNotDone();

    releaseAndCheckFutureCompletes(future);
  }

  @Test
  public void shouldWaitForDecoration() throws Exception {
    blockableServices.blockDecoration();

    ListenableFuture<String> future = startAndCheckFutureNotDone();

    releaseAndCheckFutureCompletes(future);
  }

  @Test
  public void shouldPropagateExceptionsFromLookup() throws Exception {
    failableServices.failLookup = true;

    thrown.expectMessage("configured failure");

    createThing(failableServices).call(123, "h").get();
  }

  @Test
  public void shouldPropagateExceptionsFromLog() throws Exception {
    failableServices.failLog = true;

    thrown.expectMessage("configured failure");

    createThing(failableServices).call(123, "h").get();
  }

  @Test
  public void shouldUseDefaultIfDecorateFails() throws Exception {
    failableServices.failDecorate = true;

    assertThat(createThing(failableServices).call(123, "h").get(), equalTo(DecorationResult.DEFAULT.getDecorationResult()));
  }

  private ListenableFuture<String> startAndCheckFutureNotDone() throws InterruptedException {
    AsyncThing thing = createThing(blockableServices);

    ListenableFuture<String> future = thing.call(98, "hi");
    Thread.sleep(50);
    assertThat(future.isDone(), is(false));
    return future;
  }

  private void releaseAndCheckFutureCompletes(ListenableFuture<String> future) throws InterruptedException, ExecutionException, TimeoutException {
    blockableServices.releaseAll();

    future.get(10, TimeUnit.MILLISECONDS);
  }

  private class BlockableServices implements Services {
    private final Semaphore lookupSemaphore = new Semaphore(1);
    private final Semaphore decorateSemaphore = new Semaphore(1);
    private final Semaphore logSemaphore = new Semaphore(1);

    @Override
    public ListenableFuture<LookupResult> lookup(int number, String userName) {
      try {
        lookupSemaphore.acquire();
        return immediateFuture(new LookupResult(Version.A, 99));
      }
      catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }

    @Override
    public ListenableFuture<Void> log(String userName, LookupResult result) {
      try {
        logSemaphore.acquire();

        return immediateFuture(null);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }

    @Override
    public ListenableFuture<DecorationResult> decorateVersionA(LookupResult result) {
      try {
        decorateSemaphore.acquire();
        return immediateFuture(new DecorationResult("dekorerad och klar"));
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }

    @Override
    public ListenableFuture<DecorationResult> decorateVersionB(LookupResult result) {
      throw new UnsupportedOperationException();
    }

    public void blockLog() throws InterruptedException {
      logSemaphore.acquire();
    }

    public void releaseLog() {
      logSemaphore.release();
    }

    public void blockLookup() throws InterruptedException {
      lookupSemaphore.acquire();
    }

    public void releaseAll() {
      logSemaphore.release();
      lookupSemaphore.release();
      decorateSemaphore.release();
    }

    public void blockDecoration() throws InterruptedException {
      decorateSemaphore.acquire();
    }
  }

  private class FailableServices implements Services {
    private boolean failLookup = false;
    private boolean failLog = false;
    private boolean failDecorate = false;

    @Override
    public ListenableFuture<LookupResult> lookup(int number, String userName) {
      if (failLookup) {
        throw new RuntimeException("configured failure");
      }

      return immediateFuture(new LookupResult(Version.B, 82734));
    }

    @Override
    public ListenableFuture<Void> log(String userName, LookupResult result) {
      if (failLog) {
        throw new RuntimeException("configured failure");
      }

      return immediateFuture(null);
    }

    @Override
    public ListenableFuture<DecorationResult> decorateVersionA(LookupResult result) {
      throw new UnsupportedOperationException();
    }

    @Override
    public ListenableFuture<DecorationResult> decorateVersionB(LookupResult result) {
      if (failDecorate) {
        throw new RuntimeException("configured failure");
      }

      return immediateFuture(new DecorationResult("this is the decorated thang"));
    }
  }
}

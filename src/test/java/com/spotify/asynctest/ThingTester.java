package com.spotify.asynctest;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import org.junit.Test;

import static com.google.common.util.concurrent.Futures.immediateFuture;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
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

  protected abstract AsyncThing createThing(Services services);

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
  public void shouldNotReturnBeforeLogIsDone() throws Exception {
    fail();
  }

  @Test
  public void shouldNotBlockOnLookup() throws Exception {
    fail();
  }

  @Test
  public void shouldNotBlockOnDecoration() throws Exception {
    fail();
  }

  @Test
  public void shouldNotBlockOnLog() throws Exception {
    fail();
  }

  @Test
  public void shouldPropagateExceptionsFromLookup() throws Exception {
    fail();
  }

  @Test
  public void shouldPropagateExceptionsFromLog() throws Exception {
    fail();
  }

  @Test
  public void shouldUseDefaultIfDecorateFails() throws Exception {
    fail();
  }
}

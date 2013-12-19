/*
 * Copyright (c) 2013 Spotify AB
 */

package com.spotify.asynctest

import com.google.common.util.concurrent.FutureCallback
import com.google.common.util.concurrent.Futures.addCallback
import scala.concurrent._

class ScalaServicesBridge(services: Services) extends ScalaServices {

  def lookup(number: Int, userName: String): Future[LookupResult] = {
    val p = Promise[LookupResult]
    addCallback(services.lookup(number, userName), new DelegatingCallback(p))
    p.future
  }

  def log(userName: String, result: LookupResult): Future[Unit] = {
    val p = Promise[Unit]
    addCallback(services.log(userName, result), new VoidCallback(p))
    p.future
  }

  def decorateVersionA(result: LookupResult): Future[DecorationResult] = {
    val p = Promise[DecorationResult]
    addCallback(services.decorateVersionA(result), new DelegatingCallback(p))
    p.future
  }

  def decorateVersionB(result: LookupResult): Future[DecorationResult] =  {
    val p = Promise[DecorationResult]
    addCallback(services.decorateVersionB(result), new DelegatingCallback(p))
    p.future
  }

  class DelegatingCallback[T](p: Promise[T]) extends FutureCallback[T] {
    def onFailure(t: Throwable) { p.failure(t) }
    def onSuccess(v: T) { p.success(v) }
  }

  class VoidCallback(p: Promise[Unit]) extends FutureCallback[Void] {
    def onFailure(t: Throwable) { p.failure(t) }
    def onSuccess(v: Void) { p.success(()) }
  }
}

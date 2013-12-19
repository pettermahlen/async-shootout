/*
 * Copyright (c) 2013 Spotify AB
 */

package com.spotify.asynctest

import com.google.common.util.concurrent._

import scala.concurrent.ExecutionContext
import scala.util.{Success, Failure}

class ScalaThingToJavaThing(thing: ScalaAsyncThing) extends AsyncThing {

  import ExecutionContext.Implicits.global

  def call(number: Int, userName: String): ListenableFuture[String] = {
    val future: SettableFuture[String] = SettableFuture.create()

    thing.call(number, userName) onComplete {
      case Success(v) => future.set(v)
      case Failure(t) => future.setException(t)
    }

    future
  }
}

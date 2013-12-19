/*
 * Copyright (c) 2013 Spotify AB
 */

package com.spotify.asynctest

import scala.concurrent.Future

trait ScalaAsyncThing {
  def call(number:Int, userName: String): Future[String]
}

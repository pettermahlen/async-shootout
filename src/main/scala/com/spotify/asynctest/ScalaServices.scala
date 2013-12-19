/*
 * Copyright (c) 2013 Spotify AB
 */

package com.spotify.asynctest

import scala.concurrent.Future

trait ScalaServices {
  def lookup(number: Int, userName: String): Future[LookupResult]

  def log(userName: String, result: LookupResult): Future[Unit]

  def decorateVersionA(result: LookupResult): Future[DecorationResult]

  def decorateVersionB(result: LookupResult): Future[DecorationResult]
}

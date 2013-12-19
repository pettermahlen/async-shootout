/*
 * Copyright (c) 2013 Spotify AB
 */

package com.spotify.asynctest

import scala.concurrent.Future
import scala.concurrent.ExecutionContext

class ScalaThing(service: ScalaServices)
  extends ScalaAsyncThing {

  import ExecutionContext.Implicits.global

  def call(number: Int, userName: String): Future[String] = for {
    lookup     <- service.lookup(number, userName)
    logged      = service.log(userName, lookup) // just fire it off

    decoration <- decorate(lookup) recover {
      case _ => DecorationResult.DEFAULT
    }

    _          <- logged // wait for logging here
  } yield decoration.getDecorationResult

  def decorate(result: LookupResult): Future[DecorationResult] =
    if (result.getVersion == Version.A)
      service.decorateVersionA(result)
    else
      service.decorateVersionB(result)

}

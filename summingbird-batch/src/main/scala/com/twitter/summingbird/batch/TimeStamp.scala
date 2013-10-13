/*
Copyright 2013 Twitter, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.twitter.summingbird.batch

import com.twitter.algebird.Monoid
import com.twitter.bijection.Bijection
import java.util.Date


case class Timestamp(milliSinceEpoch: Long) extends Ordered[Timestamp] {
  def compare(that: Timestamp) = milliSinceEpoch.compare(that.milliSinceEpoch)
  def Max = Timestamp.Max
  def Min = Timestamp.Min
  def prev = copy(milliSinceEpoch = milliSinceEpoch - 1)
  def next = copy(milliSinceEpoch = milliSinceEpoch + 1)
  def toDate = new Date(milliSinceEpoch)
}

object Timestamp {
  implicit def fromDate(d: Date) = Timestamp(d.getTime)
  implicit val orderingOnTimestamp: Ordering[Timestamp] = Ordering.by(_.milliSinceEpoch)
  implicit val maxTSMonoid: Monoid[Timestamp] = Monoid.from(Timestamp.Min)(orderingOnTimestamp.max(_, _))

  implicit val timestamp2Date: Bijection[Timestamp, Date] =
    Bijection.build[Timestamp, Date] { ts => new Date(ts.milliSinceEpoch) } { fromDate(_) }

  implicit val timestamp2Long: Bijection[Timestamp, Long] =
    Bijection.build[Timestamp, Long] { _.milliSinceEpoch } { Timestamp(_) }

}
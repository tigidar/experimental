package exp.api

import kyo.*
import exp.domain.Ticket
import scala.concurrent.Future

trait Api[F[_]]:

  def getTickets(): F[IndexedSeq[Ticket]]


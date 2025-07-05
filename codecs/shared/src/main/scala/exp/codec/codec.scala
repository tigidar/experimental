package exp

import upickle.default.{ReadWriter, macroRW}
import sttp.tapir.Schema
import sttp.tapir.generic.auto.*
import sttp.tapir.json.pickler.*

import exp.domain.*

import java.time.Instant
import java.time.format.DateTimeFormatter
import upickle.default.{ReadWriter, readwriter}

object InstantJson:
  private val fmt = DateTimeFormatter.ISO_INSTANT

  implicit val instantRW: ReadWriter[Instant] =
    readwriter[String].bimap[Instant](
      inst => fmt.format(inst),
      str  => Instant.parse(str)
    )


  // for each domain type: declare schema and ReadWriter
object codec:
  import InstantJson.{*, given}
  import upickle.default.*
  import sttp.tapir.Schema

  given ReadWriter[Priority] =
    ReadWriter.merge[Priority](
      macroRW[Priority.Low.type],
      macroRW[Priority.Medium.type],
      macroRW[Priority.High.type]
    )
  given Schema[Priority] = Schema.derived

  given ReadWriter[TicketInfo] = macroRW
  given Schema[TicketInfo] = Schema.derived

  given ReadWriter[TicketStatus] =
    ReadWriter.merge[TicketStatus](
      macroRW[TicketStatus.Open.type],
      macroRW[TicketStatus.InProgress.type],
      macroRW[TicketStatus.Resolved.type],
      macroRW[TicketStatus.Closed.type]
    )
  given Schema[TicketStatus] = Schema.derived

  given ReadWriter[UserInfo] = macroRW
  given Schema[UserInfo] = Schema.derived

  given ReadWriter[User.Assignee] = macroRW
  given ReadWriter[User.Reporter] = macroRW
  

  given ReadWriter[User] =
    ReadWriter.merge[User](
      summon[ReadWriter[User.Assignee]],
      summon[ReadWriter[User.Reporter]]
    )

  given Schema[User] = Schema.derived

  given ReadWriter[TicketStatusEvent] = macroRW
  given Schema[TicketStatusEvent] = Schema.derived

  given ReadWriter[TicketHistory] = macroRW
  given Schema[TicketHistory] = Schema.derived

  given ReadWriter[JustificationInfo] = macroRW
  given Schema[JustificationInfo] = Schema.derived

  given ReadWriter[Justification.BugReport] = macroRW
  given ReadWriter[Justification.Request] = macroRW

  given ReadWriter[Justification] =
    ReadWriter.merge[Justification](
      macroRW[Justification.BugReport],
      macroRW[Justification.Request]
    )

  given Schema[Justification.BugReport] = Schema.derived
  given Schema[Justification.Request] = Schema.derived
  given Schema[Justification] = Schema.derived

  given ReadWriter[TicketCategory.Predefined] = macroRW
  given ReadWriter[TicketCategory.General] = macroRW

  given ReadWriter[TicketCategory] =
    ReadWriter.merge[TicketCategory](
      macroRW[TicketCategory.Predefined],
      macroRW[TicketCategory.General]
    )

  given Schema[TicketCategory] = Schema.derived

  given ReadWriter[Ticket] = macroRW
  given Schema[Ticket] = Schema.derived
  given Pickler[Ticket] = Pickler.derived[Ticket]

  given ReadWriter[EmailAddress] = macroRW
  given Schema[EmailAddress] = Schema.derived


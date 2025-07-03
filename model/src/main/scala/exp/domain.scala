package exp

import upickle.default.{ReadWriter, macroRW}
import java.time.Instant
import java.util.UUID

enum Priority:
    case Low, Medium, High

import upickle.*

given ReadWriter[Instant] = new ReadWriter[String]{}.bimap[Instant](
    instant => instant.toString,                      // serialize as ISO string
    str => Instant.parse(str)                         // parse ISO string back
  )

final case class TicketInfo(
      title: String
    , description: String
    , priority: Priority
)

enum TicketStatus:
    case Open, InProgress, Resolved, Closed

final case class UserInfo(
        id: UUID
        , name: String
        , email: EmailAddress
)

object UserInfo:

  given rw: ReadWriter[UserInfo] = macroRW[UserInfo]

enum User:
    case Assignee(
        info: UserInfo
    )
    case Reporter(
        info: UserInfo
    )

object User:

 given rw: ReadWriter[User] = macroRW[User]
 // given rw: ReadWriter[Assignee] = macroRW[Assignee]
 // given rw: ReadWriter[Reporter] = macroRW[Reporter]
  

final case class TicketStatusEvent(
        id: UUID
    , status: TicketStatus
    , updatedAt: java.time.Instant
    , updatedBy: User
)

final case class TicketHistory(
  events: IndexedSeq[TicketStatusEvent]
)

enum Justification:
    case BugReport(
        id: UUID
        , title: String
        , email: EmailAddress
        , description: String
    )

    case Request(
        id: UUID
        , title: String
        , email: EmailAddress
        , description: String
    )

object Justification:

  given rw: ReadWriter[Justification] = macroRW[Justification]

enum TicketCategory: 
    case Predefined(
          id: UUID
        , info: TicketInfo
        , category: String
    )

    case General(
          id: UUID
        , info: TicketInfo
    )

object TicketCategory:
  given rw: ReadWriter[TicketCategory] = macroRW[TicketCategory]

final case class Ticket(
        id: UUID
    , category: TicketCategory
    , assignee: Option[User.Assignee]
    , reporter: User.Reporter
    , createdAt: java.time.Instant
    , updatedAt: java.time.Instant
    , justification: Justification
)

object Ticket:
  
  given rw: ReadWriter[Ticket] = macroRW[Ticket]

final case class EmailAddress(
        localPart: String
        , domain: String
)


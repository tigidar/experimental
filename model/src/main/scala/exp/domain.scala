package exp

import java.time.Instant
import java.util.UUID

enum Priority:
    case Low, Medium, High

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

enum User:
    case Assignee(
        info: UserInfo
    )
    case Reporter(
        info: UserInfo
    )

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

final case class Ticket(
        id: UUID
    , category: TicketCategory
    , assignee: Option[User.Assignee]
    , reporter: User.Reporter
    , createdAt: java.time.Instant
    , updatedAt: java.time.Instant
    , justification: Justification
)

final case class EmailAddress(
        localPart: String
        , domain: String
)


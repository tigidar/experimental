package exp.domain

import java.time.Instant
import java.util.UUID

enum Priority:
  case Low, Medium, High

final case class TicketInfo(
    title: String,
    description: String,
    priority: Priority
)

enum TicketStatus:
  case Open, InProgress, Resolved, Closed

final case class UserInfo(
    id: UUID,
    name: String,
    email: EmailAddress
)

enum User:
  case Assignee(
      info: UserInfo
  )
  case Reporter(
      info: UserInfo
  )

final case class TicketStatusEvent(
    id: UUID,
    status: TicketStatus,
    updatedAt: java.time.Instant,
    updatedBy: User
)

final case class TicketHistory(
    events: IndexedSeq[TicketStatusEvent]
)

final case class JustificationInfo(
    title: String,
    email: EmailAddress,
    description: String
)

enum Justification:
  case BugReport(
      id: UUID,
      info: JustificationInfo
  )

  case Request(
      id: UUID,
      info: JustificationInfo
  )

extension (justification: Justification)
  def info: JustificationInfo = justification match
    case Justification.BugReport(_, info) => info
    case Justification.Request(_, info) => info


enum TicketCategory:
  case Predefined(
      id: UUID,
      info: TicketInfo,
      category: String
  )

  case General(
      id: UUID,
      info: TicketInfo
  )

final case class Ticket(
    id: UUID,
    category: TicketCategory,
    assignee: Option[User.Assignee],
    reporter: User.Reporter,
    createdAt: java.time.Instant,
    updatedAt: java.time.Instant,
    justification: Justification
)

final case class EmailAddress(
    localPart: String,
    domain: String
)

package exp

import java.util.UUID

enum Priority:
    case Low, Medium, High

enum Ticket: 
    case Predefined(
          id: UUID
        , info: TicketInfo
        , category: String
    )

    case General(
          id: UUID
        , info: TicketInfo
    )

final case class TicketInfo(
      title: String
    , description: String
    , priority: Priority
)

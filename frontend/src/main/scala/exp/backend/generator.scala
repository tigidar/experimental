package exp.backend

import java.util.UUID

import exp.domain.{Ticket, TicketInfo, Priority, UserInfo, EmailAddress, TicketCategory, Justification, JustificationInfo}
import exp.domain.User.{Reporter, Assignee}

object generator:

  def ticketFactory(): Ticket =
    new Ticket(
      id = UUID.randomUUID(),
      category = TicketCategory.General(id = UUID.randomUUID(), info = TicketInfo(
        priority = Priority.Medium,
        title = "General Ticket",
        description = "This is a general ticket description"
      )),
      assignee = None,
      reporter = Reporter(
        info = UserInfo(
          id = UUID.randomUUID(),
          name = "John Doe",
          email = EmailAddress("john.doe", "example.com")
        )
      ),
      createdAt = java.time.Instant.now(),
      updatedAt = java.time.Instant.now(),
      justification = Justification.Request(
        id = UUID.randomUUID(),
        info = JustificationInfo(
          title = "Request Title",
          email = EmailAddress("requester", "example.com"),
         description = "This is a request description"
       )
      )
    )


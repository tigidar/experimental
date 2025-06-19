package exp.events

import kyo.*

import com.raquo.laminar.api.L

enum PageEvent:
  case Home
  case Todo

object Events:

  lazy val page = new L.EventBus[PageEvent]()



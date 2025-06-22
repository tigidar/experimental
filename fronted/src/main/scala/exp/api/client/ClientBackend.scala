package exp.client

import kyo.*
import sttp.client4.Backend
import scala.concurrent.Future
import sttp.client4.fetch.FetchBackend

trait ClientBackend[F[_]] {
  def get(): Backend[F]
}

object StubBackend:

  // val backend: ClientBackend[Future] = new ClientBackend {
  val backendLayer: Layer[ClientBackend[Future], IO] = Layer {
    new ClientBackend[Future] {
      def get(): Backend[Future] = exp.backend.Stub.backend
    }
  }

object RemoteBackend:

  val backendLayer: Layer[ClientBackend[Future], IO] = Layer {
    new ClientBackend[Future] {
      def get(): Backend[Future] = FetchBackend()
    }
  }

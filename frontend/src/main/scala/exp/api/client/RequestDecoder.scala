package exp.api

import sttp.tapir.DecodeResult
import sttp.tapir.DecodeResult.Missing
import sttp.client4.{Response}
import scala.concurrent.Future

object RequestDecoder:

  def apply[E, O](res: Response[DecodeResult[Either[E, O]]]): Future[O] =
    res.body match {
      case DecodeResult.Value(Right(value)) => Future.successful(value)
      case DecodeResult.Value(Left(error)) =>
        Future.failed(new Exception(s"Error: $error"))
      case _ =>
        Future.failed(
          new Exception("Unexpected response format")
        )
    }


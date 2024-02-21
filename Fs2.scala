//> using toolkit typelevel:0.1.21
import cats.effect.*
import fs2.Stream
import scala.util.Try

object StreamsTest extends IOApp.Simple {
  def run: IO[Unit] = {
    val stream = Stream.range[IO, Int](1, 10)
    val transformedStream = stream.map(i => Try(i * 2).toEither)

    transformedStream
      .evalMap {
        case Right(value) => IO(println(s"Value: $value"))
        case Left(e)      => IO(println(s"Error: ${e.getMessage}"))
      }
      .compile
      .drain
  }
}

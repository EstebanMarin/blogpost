//> using toolkit typelevel:0.1.21
import cats.effect.*
import fs2.Stream

object StreamsTest extends IOApp.Simple {
  def run: IO[Unit] =
    Stream.eval(IO { println("BEING RUN!!") }).compile.drain
}

//> using toolkit typelevel:0.1.21
//> using dependency org.http4s::http4s-dsl:0.23.25
//> using dependency org.http4s::http4s-ember-server:0.23.25

import cats.effect._
import cats.syntax.all._
import org.http4s._, org.http4s.dsl.io._, org.http4s.implicits._
import cats.effect._
import com.comcast.ip4s._
import org.http4s.HttpRoutes
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.ember.server.EmberServerBuilder

object Http4sScript extends IOApp {

  val helloWorldService = HttpRoutes
    .of[IO] { case GET -> Root / "hello" / name =>
      Ok(s"Hello, $name.")
    }
    .orNotFound

  def run(args: List[String]): IO[ExitCode] =
    EmberServerBuilder
      .default[IO]
      .withHost(ipv4"0.0.0.0")
      .withPort(port"8080")
      .withHttpApp(helloWorldService)
      .build
      .use(_ => IO.never)
      .as(ExitCode.Success)
}

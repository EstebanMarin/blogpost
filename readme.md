# A quick guide to Functional Scala and Nix

Commencing the journey into Functional Programming in Scala has become notably simplified with the integration of Nix and Scala-cli-toolkit. This blog post is crafted to provide guidance and most of the commands that facilitate an easy transition from zero to proficient with Scala.

## Introduction to Nix and Scala-cli

### Nix Package Manager

Nix has emerged as a versatile package manager (it has been 20 years in the making) renowned for its innovative approach to dependency management and reproducible builds. Its declarative language and immutable infrastructure model offer developers control over their development environments. Nix stands out as a tool to empower teams to build, deploy, and manage software with efficiency and reliability.

### Scala-cli Introduction

Scala-cli, an essential tool within the Scala ecosystem, streamlines the development process for Scala projects. Designed to enhance productivity and maintainability, Scala-cli provides developers with a unified platform for project management, dependency resolution, and build automation. By simplifying the setup and configuration of Scala projects, Scala-cli enables teams to focus on delivering high-quality code and innovative solutions without the burden of tedious manual tasks.

## Dependencies: Just install Nix

We recommend following the installation guide from the source <https://nixos.org/download#nix-install-macos> but in a Mac/Linux in one command

```bash
sh <(curl -L https://nixos.org/nix/install)
```

check the installation

```bash
❯ nix --version
nix (Nix) 2.19.3
```

## typelevel nix nix develop

With just one command (flakes need to be enabled, follow through the suggested experimental flags when/if an error is raised)
`nix develop github:typelevel/typelevel-nix#application`

This will provide us with a complete development Scala environment, with several tools (not exhaustive): Coursier, JVM, node, Gitter8, scala-fmt, scala-fix, scala-cli.

```bash
$ nix develop github:typelevel/typelevel-nix#application
🔨 Welcome to typelevel-app-shell

[general commands]

  menu      - prints this menu
  sbt       - A build tool for Scala, Java and more
  scala-cli - Command-line tool to interact with the Scala language

[versions]

  Java - 17.0.1

$ sbt -version
sbt version in this project: 1.9.8
sbt script version: 1.9.8
$ exit
```

## Scala-CLI

Up to this point, we have a `shell` with all the tooling necessary to be productive in Scala. Of those tools, we are going to explore `scala-cli` and how to be functional easily with `typelevel:toolkit`.

A toolkit of great libraries to start building Typelevel apps on JVM, Node.js, and Native!
Overview
Typelevel toolkit is a meta library that currently includes these libraries:
[Cats](https://github.com/typelevel/cats) and [Cats Effect](https://github.com/typelevel/cats-effect)
[fs2](https://github.com/typelevel/fs2) and [fs2 I/O](https://fs2.io/#/io)
[fs2 data CSV and its generic module](https://fs2-data.gnieh.org/documentation/csv/)
[Http4s Ember client](https://http4s.org/v0.23/docs/client.html)
and several more

## Scripting  3 jobs in a functional way

We are going to write 3 files/scripts, and for that, we will need a basic `vscode` metals lsp setup.

```bash
$> scala-cil setup-ide .
$> touch HttpScript.scala 
$> code . 
```

## Basic http4s client

```scala
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
```

```bash
❯ scala-cli run Http4s.scala
Compiling project (Scala 3.3.1, JVM (17))
Compiled project (Scala 3.3.1, JVM (17))
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
# Separate shell
❯ http get localhost:8080/hello/xebia
HTTP/1.1 200 OK
Connection: keep-alive
Content-Length: 13
Content-Type: text/plain; charset=UTF-8
Date: Sat, 17 Feb 2024 02:38:06 GMT

Hello, xebia.
```

## Basic f2s streams

```bash
$> touch Fs2.scala
```

```scala
//> using toolkit typelevel:0.1.21
import cats.effect.*
import fs2.Stream

object StreamsTest extends IOApp.Simple {
  def run: IO[Unit] =
    Stream.eval(IO { println("BEING RUN!!") }).compile.drain
}
```

```bash
❯ scala-cli run Fs2.scala
Compiling project (Scala 3.3.1, JVM (17))
Compiled project (Scala 3.3.1, JVM (17))
BEING RUN!!
```

## Also run tests

```scala
//> using scala 3.3.1
//> using dep "org.scalamock::scalamock:6.0.0-M1"
//> using dep "org.scalatest::scalatest:3.2.18"

import org.scalamock.scalatest.MockFactory
import org.scalatest.funsuite.AnyFunSuite

class Example:
  def method(input: String): String = "placeholder"

class MockTest extends AnyFunSuite with MockFactory:
  test("Mocking Example.method") {
    val example = mock[Example]
    (example.method _).expects("input").returning("output")
    assert(example.method("input") == "output")
  }
```

```bash
❯ scala-cli test MockTest.scala
Compiling project (Scala 3.3.1, JVM (17))
Compiled project (Scala 3.3.1, JVM (17))
MockTest:
- Mocking Example.method
Run completed in 66 milliseconds.
Total number of tests run: 1
Suites: completed 1, aborted 0
Tests: succeeded 1, failed 0, canceled 0, ignored 0, pending 0
All tests passed.
```

## Better shell scripting

Scala, is now a scripting language we recommend reading [Scala-cli is great for shell script](https://xebia.com/blog/better-shell-scripting-with-scala-cli/)

## Summary

This blog primarily focuses on providing insights into setting up Scala and simplifying the process of creating practical applications. While we have covered a range of tools here, it is important to note that there is a wealth of information to delve into regarding each one. We aspire here to serve as a valuable resource for future reference, igniting inspiration and encouraging further exploration and creation within the Scala ecosystem.

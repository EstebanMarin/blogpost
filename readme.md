# A quick guide to Functional Scala and Nix

Commencing the journey into Functional Programming in Scala has become notably simplified with the integration of Nix and Scala-cli-toolkit. This blog post is crafted to provide minimal guidance, facilitating a swift transition from novice to proficient, and ultimately, production-ready with Scala

## Introduction to Nix and Scala-cli

### Nix Package Manager

Nix has emerged as a versatile package manager (it has been 20 years in the making though) renowned for its innovative approach to dependency management and reproducible builds. Its declarative language and immutable infrastructure model offer developers unparalleled control over their development environments. As the demand for robust and reliable software delivery pipelines intensifies, Nix stands out as a game-changer, empowering teams to build, deploy, and manage software with efficiency and reliability.

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

With just one command (flakes need to be enabled, but just follow through the suggested experimental flags when there is an error)
`nix develop github:typelevel/typelevel-nix#application`

This will provide us with a

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
Cats and Cats Effect
fs2 and fs2 I/O
fs2 data CSV and its generic module
Http4s Ember client
Circe and http4s integration
Decline Effect
Munit Cats Effect

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

From here we will use the typelevel-nix, which will give us a usable development environment to play around with Scala and finish it all with `typelevel:toolkit` to start using functional libraries out of the box. So essentially we just need one install and we get a ton of tooling for free.
Getting Started Tutorials:
Create tutorials on how to use Nix to manage packages, environments, and dependencies for Scala projects.
Walkthrough the basics of setting up a Scala project using Scala-cli and managing its dependencies with Nix.
Advanced Usage and Tips:
Dive deeper into advanced features of Nix, such as overlays, custom package definitions, and environment management.
Explore advanced usage scenarios for Scala-cli, such as building and packaging Scala applications, managing plugins, and integrating with other build tools.
Best Practices and Optimization:
Share best practices for optimizing Nix configurations and workflows to improve build times and resource usage.
Provide tips and tricks for optimizing Scala-cli configurations and project setups for performance and maintainability.
Integration Guides:
Write guides on integrating Nix with other development tools and workflows commonly used in Scala development, such as sbt, Maven, and IDEs.
Explore how to leverage Nix to create reproducible development environments and CI/CD pipelines for Scala projects.
Case Studies and Use Cases:
Showcase real-world use cases and case studies of companies or projects using Nix and Scala-cli in their development workflows.
Interview developers or teams who have adopted these tools and share their experiences, challenges, and successes.
Community Engagement:
Encourage community engagement by inviting readers to share their experiences, tips, and contributions related to Nix and Scala-cli.
Host Q&A sessions, forums, or live streams to discuss topics related to Nix, Scala-cli, and broader software development practices.
Updates and News:
Stay up-to-date with the latest developments, releases, and news related to Nix and Scala-cli, and share updates with your readers.
Provide analysis and insights into how these updates may impact developers and their workflows.
Collaboration and Contributions:
Invite guest bloggers, experts, and contributors from the Nix and Scala communities to share their knowledge and insights on your blog.
Contribute back to the Nix and Scala-cli ecosystems by sharing tutorials, documentation improvements, or bug fixes upstream.
Remember to keep your content clear, concise, and beginner-friendly while also catering to more advanced users. Additionally, engage with your audience through comments, social media, and newsletters to build a thriving community around your tech blog.

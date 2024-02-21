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

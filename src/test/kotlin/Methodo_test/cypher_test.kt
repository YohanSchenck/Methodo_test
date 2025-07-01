package Methodo_test

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll
import io.kotest.property.Arb
import io.kotest.property.arbitrary.char
import io.kotest.property.arbitrary.int


class CypherStringTest : StringSpec({

    val Cypher = Cypher()

    "cipher('A', 2) should return 'C'" {
        Cypher.cypher('A', 2) shouldBe 'C'
    }

    "cipher('A', 5) should return 'F'" {
        Cypher.cypher('A', 5) shouldBe 'F'
    }

    "cipher('Z', 1) should wrap around to 'A'" {
        Cypher.cypher('Z', 1) shouldBe 'A'
    }

    "cipher('C', 28) should equal 'E' (cycle key > 26)" {
        Cypher.cypher('C', 28) shouldBe 'E'
    }

    "cipher with negative key should throw IllegalArgumentException" {
        shouldThrow<IllegalArgumentException> {
            Cypher.cypher('A', -1)
        }
    }

    "cipher with non-uppercase char should throw IllegalArgumentException" {
        shouldThrow<IllegalArgumentException> {
            Cypher.cypher('a', 3)
        }
    }

    "Test key 0 ne change pas la lettre" {
        checkAll(Arb.char('A'..'Z')) { a ->
            val result = Cypher.cypher(a, 0)
            result shouldBe a
        }
    }

    "Test sur le modulo" {
        checkAll(Arb.char('A'..'Z'), Arb.int(1,100)) { a, b ->
            val result = Cypher.cypher(a, b)
            result shouldBe Cypher.cypher(a, b+b*26)
        }
    }


})

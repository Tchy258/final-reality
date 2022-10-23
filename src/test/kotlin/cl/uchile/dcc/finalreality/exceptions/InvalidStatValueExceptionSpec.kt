/*
 * "Final Reality" (c) by R8V and Tchy258
 * "Final Reality" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.uchile.dcc.finalreality.exceptions

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll
import org.junit.jupiter.api.assertThrows

class InvalidStatValueExceptionSpec : FunSpec({
    val prefix = "The provided value is not a valid stat value. "

    test("An invalid stat value exception can be thrown with a message.") {
        checkAll<String> { message ->
            assertThrows<InvalidStatValueException> {
                throw InvalidStatValueException(message)
            }.message shouldBe prefix + message
        }
    }
})

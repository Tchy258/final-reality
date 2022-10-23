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

class NoWeaponEquippedExceptionTest : FunSpec({
    val prefix = "Attempted to join turn queue unarmed. "
    val suffix = " has no weapon equipped"

    test("A NoWeaponEquippedException can be thrown with a character's name in its message") {
        checkAll<String> {
            name ->
            assertThrows<NoWeaponEquippedException> {
                throw NoWeaponEquippedException(name)
            }.message shouldBe prefix + name + suffix
        }
    }
})

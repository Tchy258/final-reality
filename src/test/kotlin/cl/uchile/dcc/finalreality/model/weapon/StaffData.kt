package cl.uchile.dcc.finalreality.model.weapon

import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.nonNegativeInt
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.arbitrary.string
/**
 * Class that contains a [Staff]'s data
 * @property name the staff's name
 * @property damage the staff's damage
 * @property weight the staff's weight
 * @property magicDamage the staff's magicDamage
 *
 * @author <a href="https://www.github.com/tchy258">Tchy258</a>
 */
internal data class StaffData(val name: String, val damage: Int, val weight: Int, val magicDamage: Int) {
    companion object {
        /**
         * [Arb] generator for arbitrary valid [Staff] data
         */
        val validStaffGenerator = arbitrary {
            val name = Arb.string().bind()
            val damage = Arb.nonNegativeInt().bind()
            val weight = Arb.positiveInt().bind()
            val magicDamage = Arb.nonNegativeInt().bind()
            StaffData(name, damage, weight, magicDamage)
        }
        /**
         * [Arb] generator for arbitrary [Staff] data, the generated data might not be valid.
         */
        val arbitraryStaffGenerator = arbitrary {
            val name = Arb.string().bind()
            val damage = Arb.int().bind()
            val weight = Arb.int().bind()
            val magicDamage = Arb.int().bind()
            StaffData(name, damage, weight, magicDamage)
        }
    }
}

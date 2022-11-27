package cl.uchile.dcc.finalreality.model.weapon

import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.nonNegativeInt
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.arbitrary.string
/**
 * Class that contains a [Weapon]'s data
 * @property name the weapon's name
 * @property damage the weapon's damage
 * @property weight the weapon's weight
 *
 * @author <a href="https://www.github.com/tchy258">Tchy258</a>
 */
internal data class NonMagicalWeaponData(val name: String, val damage: Int, val weight: Int) : WeaponData {
    override val arbitraryGenerator: Arb<NonMagicalWeaponData> = NonMagicalWeaponData.arbitraryGenerator
    override val validGenerator: Arb<NonMagicalWeaponData> = NonMagicalWeaponData.validGenerator

    override fun process(factory: WeaponTestingFactory): Weapon {
        return factory.createNonMagicalWeapon(this)
    }
    companion object {
        val validGenerator: Arb<NonMagicalWeaponData> = arbitrary {
            val name = Arb.string().bind()
            val damage = Arb.nonNegativeInt().bind()
            val weight = Arb.positiveInt().bind()
            NonMagicalWeaponData(name, damage, weight)
        }
        val arbitraryGenerator: Arb<NonMagicalWeaponData> = arbitrary {
            val name = Arb.string().bind()
            val damage = Arb.int().bind()
            val weight = Arb.int().bind()
            NonMagicalWeaponData(name, damage, weight)
        }
    }
}

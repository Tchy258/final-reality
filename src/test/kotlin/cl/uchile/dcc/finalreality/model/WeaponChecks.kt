package cl.uchile.dcc.finalreality.model

import cl.uchile.dcc.finalreality.exceptions.InvalidStatValueException
import cl.uchile.dcc.finalreality.model.weapon.AxeTestingFactory
import cl.uchile.dcc.finalreality.model.weapon.BowTestingFactory
import cl.uchile.dcc.finalreality.model.weapon.KnifeTestingFactory
import cl.uchile.dcc.finalreality.model.weapon.NonMagicalWeaponData
import cl.uchile.dcc.finalreality.model.weapon.StaffData
import cl.uchile.dcc.finalreality.model.weapon.SwordTestingFactory
import cl.uchile.dcc.finalreality.model.weapon.WeaponData
import cl.uchile.dcc.finalreality.model.weapon.WeaponTestingFactory
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.Arb
import io.kotest.property.assume
import io.kotest.property.checkAll
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

internal suspend fun weaponEqualityCheck(generator: Arb<WeaponData>, weaponFactory: WeaponTestingFactory) {
    checkAll(generator) { weapon ->
        val randomWeapon1 = weapon.process(weaponFactory)
        val randomWeapon2 = weapon.process(weaponFactory)
        randomWeapon1 shouldBe randomWeapon2
    }
}

internal suspend fun weaponSelfEqualityCheck(generator: Arb<WeaponData>, weaponFactory: WeaponTestingFactory) {
    checkAll(generator) { weapon ->
        val randomWeapon1 = weapon.process(weaponFactory)
        randomWeapon1 shouldBe randomWeapon1
    }
}

internal suspend fun weaponInequalityCheck(weaponFactory: WeaponTestingFactory) {
    checkAll(NonMagicalWeaponData.validGenerator, NonMagicalWeaponData.validGenerator) { weapon1, weapon2 ->
        assume (
            weapon1.name != weapon2.name ||
                weapon1.damage != weapon2.damage ||
                weapon1.weight != weapon2.weight
        )
        val randomWeapon1 = weapon1.process(weaponFactory)
        val randomWeapon2 = weapon2.process(weaponFactory)
        randomWeapon1 shouldNotBe randomWeapon2
    }
}

internal suspend fun weaponNotNullCheck(generator: Arb<WeaponData>, weaponFactory: WeaponTestingFactory) {
    checkAll(generator) { weapon ->
        val randomWeapon = weapon.process(weaponFactory)
        randomWeapon shouldNotBe null
    }
}

internal suspend fun weaponValidStatsCheck(weaponFactory: WeaponTestingFactory) {
    checkAll(NonMagicalWeaponData.arbitraryGenerator) { weapon ->
        if (weapon.damage < 0 || weapon.weight < 1) {
            assertThrows<InvalidStatValueException> {
                weapon.process(weaponFactory)
            }
        } else {
            assertDoesNotThrow {
                weapon.process(weaponFactory)
            }
        }
    }
}

internal suspend fun differentWeaponInequalityCheck(weaponFactory: WeaponTestingFactory) {
    val factories: List<WeaponTestingFactory> =
        listOf(
            AxeTestingFactory(),
            BowTestingFactory(),
            SwordTestingFactory(),
            KnifeTestingFactory()
        )
    checkAll(NonMagicalWeaponData.validGenerator) { data ->
        val randomWeapon = data.process(weaponFactory)

        for (factory in factories) {
            if (weaponFactory == factory) {
                continue
            } else {
                val differentWeapon = data.process(factory)
                randomWeapon shouldNotBe differentWeapon
            }
        }
        val dummyValue = randomWeapon.damage
        val staffData = StaffData(randomWeapon.name, randomWeapon.damage, randomWeapon.weight, dummyValue)
        randomWeapon shouldNotBe staffData
    }
}

package cl.uchile.dcc.finalreality.model

import cl.uchile.dcc.finalreality.exceptions.InvalidStatValueException
import cl.uchile.dcc.finalreality.model.weapon.AxeTestingFactory
import cl.uchile.dcc.finalreality.model.weapon.BowTestingFactory
import cl.uchile.dcc.finalreality.model.weapon.KnifeTestingFactory
import cl.uchile.dcc.finalreality.model.weapon.StaffData
import cl.uchile.dcc.finalreality.model.weapon.StaffTestingFactory
import cl.uchile.dcc.finalreality.model.weapon.SwordTestingFactory
import cl.uchile.dcc.finalreality.model.weapon.WeaponData
import cl.uchile.dcc.finalreality.model.weapon.WeaponTestingFactory
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.assume
import io.kotest.property.checkAll
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

internal suspend fun weaponEqualityCheck(weaponFactory: WeaponTestingFactory) {
    checkAll(WeaponData.validGenerator) { modelData ->
        val randomWeapon1 = weaponFactory.create(modelData)
        val randomWeapon2 = weaponFactory.create(modelData)
        randomWeapon1 shouldBe randomWeapon2
    }
}

internal suspend fun weaponSelfEqualityCheck(weaponFactory: WeaponTestingFactory) {
    checkAll(WeaponData.validGenerator) { modelData ->
        val randomWeapon1 = weaponFactory.create(modelData)
        randomWeapon1 shouldBe randomWeapon1
    }
}

internal suspend fun weaponInequalityCheck(weaponFactory: WeaponTestingFactory) {
    checkAll(WeaponData.validGenerator, WeaponData.validGenerator) { weapon1, weapon2 ->
        assume {
            weapon1.name != weapon2.name ||
                weapon1.damage != weapon2.damage ||
                weapon1.weight != weapon2.weight
        }
        val randomWeapon1 = weaponFactory.create(weapon1)
        val randomWeapon2 = weaponFactory.create(weapon2)
        randomWeapon1 shouldNotBe randomWeapon2
    }
}

internal suspend fun weaponNotNullCheck(weaponFactory: WeaponTestingFactory) {
    checkAll(WeaponData.validGenerator) { weapon ->
        val randomWeapon = weaponFactory.create(weapon)
        randomWeapon shouldNotBe null
    }
}

internal suspend fun weaponValidStatsCheck(weaponFactory: WeaponTestingFactory) {
    checkAll(WeaponData.arbitraryGenerator) { weapon ->
        if (weapon.damage < 0 || weapon.weight < 1) {
            assertThrows<InvalidStatValueException> {
                weaponFactory.create(weapon)
            }
        } else {
            assertDoesNotThrow {
                weaponFactory.create(weapon)
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
            KnifeTestingFactory(),
            StaffTestingFactory()
        )
    checkAll(StaffData.validGenerator) { staff ->
        val weaponData = WeaponData(staff.name, staff.damage, staff.weight)
        val randomWeapon = if (weaponFactory == factories.last()) {
            weaponFactory.create(staff)
        } else {
            weaponFactory.create(weaponData)
        }

        for (factory in factories) {
            if (weaponFactory == factory) {
                continue
            } else {
                val differentWeapon = if (factory == factories.last()) {
                    factory.create(staff)
                } else {
                    factory.create(weaponData)
                }
                randomWeapon shouldNotBe differentWeapon
            }
        }
    }
}

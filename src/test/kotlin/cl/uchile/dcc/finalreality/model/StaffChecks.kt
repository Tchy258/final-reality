package cl.uchile.dcc.finalreality.model

import cl.uchile.dcc.finalreality.exceptions.InvalidStatValueException
import cl.uchile.dcc.finalreality.model.weapon.AxeTestingFactory
import cl.uchile.dcc.finalreality.model.weapon.BowTestingFactory
import cl.uchile.dcc.finalreality.model.weapon.KnifeTestingFactory
import cl.uchile.dcc.finalreality.model.weapon.NonMagicalWeaponData
import cl.uchile.dcc.finalreality.model.weapon.StaffData
import cl.uchile.dcc.finalreality.model.weapon.StaffTestingFactory
import cl.uchile.dcc.finalreality.model.weapon.SwordTestingFactory
import cl.uchile.dcc.finalreality.model.weapon.WeaponTestingFactory
import io.kotest.matchers.shouldNotBe
import io.kotest.property.assume
import io.kotest.property.checkAll
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

internal suspend fun staffValidStatsCheck() {
    checkAll(StaffData.arbitraryGenerator) {
        staff ->
        if (staff.damage < 0 || staff.weight < 1 || staff.magicDamage < 0) {
            assertThrows<InvalidStatValueException> {
                StaffTestingFactory().create(staff)
            }
        } else {
            assertDoesNotThrow {
                StaffTestingFactory().create(staff)
            }
        }
    }
}
internal suspend fun staffInequalityCheck() {
    checkAll(StaffData.validGenerator, StaffData.validGenerator) { weapon1, weapon2 ->
        assume(
            weapon1.name != weapon2.name ||
                weapon1.damage != weapon2.damage ||
                weapon1.weight != weapon2.weight ||
                weapon1.magicDamage != weapon2.magicDamage
        )
        val randomWeapon1 = StaffTestingFactory().create(weapon1)
        val randomWeapon2 = StaffTestingFactory().create(weapon2)
        randomWeapon1 shouldNotBe randomWeapon2
    }
}

internal suspend fun differentStaffInequalityCheck() {
    val factories: List<WeaponTestingFactory> =
        listOf(
            AxeTestingFactory(),
            BowTestingFactory(),
            SwordTestingFactory(),
            KnifeTestingFactory()
        )
    checkAll(StaffData.validGenerator) { data ->
        val randomWeapon = data.process(StaffTestingFactory())
        val weaponData = NonMagicalWeaponData(randomWeapon.name, randomWeapon.damage, randomWeapon.weight)
        for (factory in factories) {
            val differentWeapon = weaponData.process(factory)
            randomWeapon shouldNotBe differentWeapon
        }
    }
}

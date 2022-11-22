package cl.uchile.dcc.finalreality.model

import cl.uchile.dcc.finalreality.exceptions.InvalidStatValueException
import cl.uchile.dcc.finalreality.model.weapon.StaffData
import cl.uchile.dcc.finalreality.model.weapon.StaffTestingFactory
import io.kotest.matchers.shouldBe
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

internal suspend fun staffEqualityCheck() {
    checkAll(StaffData.validGenerator) { modelData ->
        val randomWeapon1 = StaffTestingFactory().create(modelData)
        val randomWeapon2 = StaffTestingFactory().create(modelData)
        randomWeapon1 shouldBe randomWeapon2
    }
}

internal suspend fun staffInequalityCheck() {
    checkAll(StaffData.validGenerator, StaffData.validGenerator) { weapon1, weapon2 ->
        assume {
            weapon1.name != weapon2.name ||
                weapon1.damage != weapon2.damage ||
                weapon1.weight != weapon2.weight ||
                weapon1.magicDamage != weapon2.magicDamage
        }
        val randomWeapon1 = StaffTestingFactory().create(weapon1)
        val randomWeapon2 = StaffTestingFactory().create(weapon2)
        randomWeapon1 shouldNotBe randomWeapon2
    }
}

internal suspend fun staffNotNullCheck() {
    checkAll(StaffData.validGenerator) {
        weapon ->
        val randomWeapon = StaffTestingFactory().create(weapon)
        randomWeapon shouldNotBe null
    }
}

internal suspend fun staffSelfEqualityCheck() {
    checkAll(StaffData.validGenerator) { modelData ->
        val randomWeapon1 = StaffTestingFactory().create(modelData)
        randomWeapon1 shouldBe randomWeapon1
    }
}

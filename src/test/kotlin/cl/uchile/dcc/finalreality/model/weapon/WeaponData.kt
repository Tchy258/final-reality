package cl.uchile.dcc.finalreality.model.weapon

import io.kotest.property.Arb

internal interface WeaponData {
    val arbitraryGenerator: Arb<WeaponData>
    val validGenerator: Arb<WeaponData>
    fun process(factory: WeaponTestingFactory): Weapon
}

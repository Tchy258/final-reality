package cl.uchile.dcc.finalreality.model.weapon

import cl.uchile.dcc.finalreality.model.ModelData

/**
 * Abstract weapon factory
 */
internal interface WeaponTestingFactory {
    fun create(data: ModelData): Weapon
}

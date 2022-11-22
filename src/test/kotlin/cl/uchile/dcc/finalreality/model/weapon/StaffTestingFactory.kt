package cl.uchile.dcc.finalreality.model.weapon

import cl.uchile.dcc.finalreality.model.ModelData

internal class StaffTestingFactory : WeaponTestingFactory {
    override fun create(data: ModelData): Staff {
        data as StaffData
        return Staff(data.name, data.damage, data.weight, data.magicDamage)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

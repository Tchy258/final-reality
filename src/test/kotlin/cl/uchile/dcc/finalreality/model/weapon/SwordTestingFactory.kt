package cl.uchile.dcc.finalreality.model.weapon

import cl.uchile.dcc.finalreality.model.ModelData

internal class SwordTestingFactory : WeaponTestingFactory {
    override fun create(data: ModelData): Sword {
        data as WeaponData
        return Sword(data.name, data.damage, data.weight)
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

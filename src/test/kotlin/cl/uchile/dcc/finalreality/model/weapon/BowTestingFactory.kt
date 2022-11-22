package cl.uchile.dcc.finalreality.model.weapon

import cl.uchile.dcc.finalreality.model.ModelData

internal class BowTestingFactory : WeaponTestingFactory {
    override fun create(data: ModelData): Bow {
        data as WeaponData
        return Bow(data.name, data.damage, data.weight)
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

package cl.uchile.dcc.finalreality.model.weapon

internal class KnifeTestingFactory : WeaponTestingFactory {
    override fun createNonMagicalWeapon(data: NonMagicalWeaponData): Knife {
        return Knife(data.name, data.damage, data.weight)
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

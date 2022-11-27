package cl.uchile.dcc.finalreality.model.weapon

internal class AxeTestingFactory : WeaponTestingFactory {
    override fun createNonMagicalWeapon(data: NonMagicalWeaponData): Axe {
        return Axe(data.name, data.damage, data.weight)
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

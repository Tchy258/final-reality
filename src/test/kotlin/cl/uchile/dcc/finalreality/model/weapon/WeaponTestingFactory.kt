package cl.uchile.dcc.finalreality.model.weapon

/**
 * Abstract weapon factory
 */
internal interface WeaponTestingFactory {
    fun create(data: WeaponData): Weapon {
        return data.process(this)
    }
    fun createNonMagicalWeapon(data: NonMagicalWeaponData): Weapon {
        throw IllegalArgumentException("Tried to create a Staff with NonMagicalWeaponData")
    }
    fun createMagicalWeapon(data: StaffData): Staff {
        throw IllegalArgumentException("Tried to create a non magical weapon with StaffData")
    }
}

package cl.uchile.dcc.finalreality.model.weapon

internal class StaffTestingFactory : WeaponTestingFactory {
    override fun createMagicalWeapon(data: StaffData): Staff {
        return Staff(data.name, data.damage, data.weight, data.magicDamage)
    }
}

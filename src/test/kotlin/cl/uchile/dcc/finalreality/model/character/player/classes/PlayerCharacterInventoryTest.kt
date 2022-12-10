package cl.uchile.dcc.finalreality.model.character.player.classes

import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.player.classes.PlayerCharacter.Inventory.addWeaponToInventory
import cl.uchile.dcc.finalreality.model.character.player.classes.PlayerCharacter.Inventory.getInventory
import cl.uchile.dcc.finalreality.model.character.player.classes.PlayerCharacter.Inventory.resetInventory
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.BlackMage
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.Knight
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.Thief
import cl.uchile.dcc.finalreality.model.weapon.Axe
import cl.uchile.dcc.finalreality.model.weapon.Bow
import cl.uchile.dcc.finalreality.model.weapon.Knife
import cl.uchile.dcc.finalreality.model.weapon.Staff
import cl.uchile.dcc.finalreality.model.weapon.Sword
import cl.uchile.dcc.finalreality.model.weapon.Weapon
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.util.concurrent.LinkedBlockingQueue

class PlayerCharacterInventoryTest : FunSpec({
    lateinit var character1: PlayerCharacter
    lateinit var character2: PlayerCharacter
    lateinit var character3: PlayerCharacter
    lateinit var weaponList: List<Weapon>
    val queue = LinkedBlockingQueue<GameCharacter>()

    beforeEach {
        character1 = Knight("TestKnight", 100, 10, queue)
        character2 = Thief("TestThief", 100, 10, queue)
        character3 = BlackMage("TestBlackMage", 100, 100, 10, queue)
        weaponList = listOf(
            Axe("TestAxe1", 20, 10),
            Axe("TestAxe2", 25, 16),
            Bow("TestBow1", 15, 8),
            Bow("TestBow2", 18, 10),
            Knife("TestKnife", 10, 6),
            Knife("TestKnife", 10, 6),
            Sword("TestSword", 30, 20),
            Sword("TestSword", 30, 20),
            Staff("TestStaff1", 0, 10, 20),
            Staff("TestStaff2", 0, 15, 30),
        )
    }
    test("The PlayerCharacter inventory can have weapons added and be reset") {
        getInventory() shouldBe listOf(Axe("BasicAxe", 80, 40))
        addWeaponToInventory(Sword("TestSword", 10, 10))
        getInventory() shouldBe listOf(Axe("BasicAxe", 80, 40), Sword("TestSword", 10, 10))
        resetInventory()
        getInventory() shouldBe listOf(Axe("BasicAxe", 80, 40))
    }
    context("PlayerCharacters should:") {
        test("Be able to add recently acquired weapons to the inventory") {
            val newWeapon = Sword("New Sword", 20, 10)
            val duplicateBow = Bow("TestBow2", 18, 10)
            val duplicateStaff = Staff("TestStaff2", 0, 15, 30)
            var inventory: List<Weapon> = getInventory()
            character1.equip(newWeapon)
            character2.equip(duplicateBow)
            character3.equip(duplicateStaff)
            inventory.size shouldBe 1
            inventory[0] shouldBe Axe("BasicAxe", 80, 40)

            character1.equip(weaponList[0])
            inventory = getInventory()
            inventory.size shouldBe 2
            inventory[1] shouldBe newWeapon

            character2.equip(weaponList[4])
            inventory = getInventory()
            inventory.size shouldBe 3
            inventory[2] shouldBe duplicateBow

            character3.equip(weaponList[8])
            inventory = getInventory()
            inventory.size shouldBe 4
            inventory[3] shouldBe duplicateStaff
        }
        test("Be able to equip weapons from the inventory, removing them in the process") {
            addWeaponToInventory(weaponList[6])
            var inventory = getInventory()
            inventory.size shouldBe 2
            character1.equip(inventory[0])
            inventory = getInventory()
            inventory.size shouldBe 1
        }
    }
    // Clear the inventory
    afterEach {
        resetInventory()
    }
})

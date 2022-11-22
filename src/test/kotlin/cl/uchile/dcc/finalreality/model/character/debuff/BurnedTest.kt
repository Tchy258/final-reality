package cl.uchile.dcc.finalreality.model.character.debuff

import cl.uchile.dcc.finalreality.model.character.Enemy
import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.player.classes.PlayerCharacter
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.BlackMage
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.Knight
import cl.uchile.dcc.finalreality.model.magic.blackmagic.Fire
import cl.uchile.dcc.finalreality.model.magic.blackmagic.RNGSeeder
import cl.uchile.dcc.finalreality.model.weapon.Staff
import cl.uchile.dcc.finalreality.model.weapon.Sword
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.util.concurrent.LinkedBlockingQueue

class BurnedTest : FunSpec({
    lateinit var burnEffect1: Burned
    lateinit var burnEffect2: Burned
    lateinit var burnEffect3: Burned
    lateinit var character: PlayerCharacter
    lateinit var mage: BlackMage
    lateinit var enemy: Enemy
    lateinit var queue: LinkedBlockingQueue<GameCharacter>

    beforeEach {
        burnEffect1 = Burned(10)
        burnEffect2 = Burned(10)
        burnEffect3 = Burned(20)
        queue = LinkedBlockingQueue()
        character = Knight("TestKnight", 100, 10, queue)
        mage = BlackMage("TestMage", 100, 50, 10, queue)
        character.addDebuff(burnEffect1)
        enemy = Enemy("TestEnemy", 20, 10, 100, 2, queue)
        RNGSeeder.setSeed(10)
    }
    test("When a character is burned, they should receive damage upon attacking (using a turn)") {
        val testSword = Sword("TestSword", 10, 5)
        character.equip(testSword)
        character.attack(enemy)
        character.currentHp shouldBe character.maxHp - 10
    }
    context("When a black mage casts fire") {
        test("The target should remain burned if it already was") {
            character.isBurned() shouldBe true
            val testStaff = Staff("TestStaff", 10, 10, 20)
            mage.equip(testStaff)
            mage.castBlackMagicSpell(Fire(), character)
            character.isBurned() shouldBe true
        }
        test("A non burned target might be burned") {
            // The given seed ensures the enemy will be burned on the third cast
            val testStaff = Staff("TestStaff", 10, 10, 20)
            mage.equip(testStaff)
            mage.castBlackMagicSpell(Fire(), enemy)
            enemy.isBurned() shouldNotBe true
            mage.castBlackMagicSpell(Fire(), enemy)
            enemy.isBurned() shouldNotBe true
            mage.castBlackMagicSpell(Fire(), enemy)
            enemy.isBurned() shouldBe true
        }
    }
})

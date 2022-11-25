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
    lateinit var enemy1: Enemy
    lateinit var enemy2: Enemy
    lateinit var queue: LinkedBlockingQueue<GameCharacter>

    beforeEach {
        burnEffect1 = Burned(10)
        burnEffect2 = Burned(10)
        burnEffect3 = Burned(20)
        queue = LinkedBlockingQueue()
        character = Knight("TestKnight", 100, 10, queue)
        mage = BlackMage("TestMage", 100, 50, 10, queue)
        character.addDebuff(burnEffect1)
        enemy1 = Enemy("TestEnemy1", 20, 10, 100, 2, queue)
        enemy2 = Enemy("TestEnemy2", 20, 10, 100, 2, queue)
        enemy2.addDebuff(burnEffect3)
        RNGSeeder.setSeed(10)
    }
    context("Burned adverse effects should") {
        test("Have a string representation") {
            burnEffect1.toString() shouldBe "Burned { finalDamage: 10, turnLimit: 5 }"
            burnEffect2.toString() shouldBe "Burned { finalDamage: 10, turnLimit: 5 }"
            burnEffect3.toString() shouldBe "Burned { finalDamage: 20, turnLimit: 5 }"
        }
        test("Always be equal to each other") {
            burnEffect1 shouldBe burnEffect1
            burnEffect1 shouldBe burnEffect2
            burnEffect1 shouldBe burnEffect3
            burnEffect2 shouldBe burnEffect3
        }
        test("Not be equal to other adverse effects") {
            val paralyzed = Paralyzed()
            val poisoned = Poisoned(10)
            burnEffect1 shouldNotBe paralyzed
            burnEffect1 shouldNotBe poisoned
        }
    }
    test("When a character is burned, they should receive damage upon attacking (using a turn)") {
        val testSword = Sword("TestSword", 10, 5)
        character.equip(testSword)
        character.rollEffects()
        character.attack(enemy1)
        character.currentHp shouldBe character.maxHp - 10
        enemy2.rollEffects()
        enemy2.attack(character)
        enemy2.currentHp shouldBe enemy2.maxHp - 20
    }
    context("When a black mage casts fire") {
        test("The target should remain burned if it already was, and the duration hasn't expired") {
            character.isBurned() shouldBe true
            val testStaff = Staff("TestStaff", 10, 10, 20)
            mage.equip(testStaff)
            mage.castBlackMagicSpell(Fire(), character)
            character.isBurned() shouldBe true
            mage.castBlackMagicSpell(Fire(), enemy2)
            enemy2.isBurned() shouldBe true
        }
        test("A non burned target might be burned") {
            // The given seed ensures the enemy will be burned on the third cast
            val testStaff = Staff("TestStaff", 10, 10, 20)
            mage.equip(testStaff)
            mage.castBlackMagicSpell(Fire(), enemy1)
            enemy1.isBurned() shouldNotBe true
            mage.castBlackMagicSpell(Fire(), enemy1)
            enemy1.isBurned() shouldNotBe true
            mage.castBlackMagicSpell(Fire(), enemy1)
            enemy1.isBurned() shouldBe true
        }
    }
    test("When 5 turns have passed, the burned adverse effect should wear off") {
        val testSword = Sword("ReallyWeakSword", 1, 10)
        character.equip(testSword)
        for (i in 0..3) {
            character.rollEffects()
            character.attack(enemy1)
            character.isBurned() shouldBe true
        }
        character.rollEffects()
        character.attack(enemy1)
        character.isBurned() shouldNotBe true
    }
})

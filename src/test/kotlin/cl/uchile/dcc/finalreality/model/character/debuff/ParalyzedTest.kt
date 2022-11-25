package cl.uchile.dcc.finalreality.model.character.debuff

import cl.uchile.dcc.finalreality.model.character.Enemy
import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.BlackMage
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.Knight
import cl.uchile.dcc.finalreality.model.magic.blackmagic.RNGSeeder
import cl.uchile.dcc.finalreality.model.magic.blackmagic.Thunder
import cl.uchile.dcc.finalreality.model.weapon.Staff
import cl.uchile.dcc.finalreality.model.weapon.Sword
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.util.concurrent.LinkedBlockingQueue

class ParalyzedTest : FunSpec({
    lateinit var paralyzed1: Paralyzed
    lateinit var paralyzed2: Paralyzed
    lateinit var paralyzed3: Paralyzed
    lateinit var mage: BlackMage
    lateinit var testCharacter: Knight
    lateinit var enemy1: Enemy
    lateinit var enemy2: Enemy
    lateinit var queue: LinkedBlockingQueue<GameCharacter>

    beforeEach {
        queue = LinkedBlockingQueue()
        paralyzed1 = Paralyzed()
        paralyzed2 = Paralyzed()
        paralyzed3 = Paralyzed()
        mage = BlackMage("TestBlackMage", 100, 50, 10, queue)
        testCharacter = Knight("TestKnight", 100, 10, queue)
        testCharacter.addDebuff(paralyzed1)
        enemy1 = Enemy("TestEnemy", 15, 30, 100, 10, queue)
        enemy2 = Enemy("TestEnemy2", 20, 10, 100, 2, queue)
        enemy2.addDebuff(paralyzed3)
        RNGSeeder.setSeed(6)
    }
    context("Paralyzed adverse effect should") {
        test("Be equal to themselves and any other instance of Paralyzed") {
            paralyzed1 shouldBe paralyzed1
            paralyzed1 shouldBe paralyzed2
            paralyzed2 shouldBe paralyzed3
            paralyzed1 shouldBe paralyzed3
        }
        test("Not be equal to other adverse effects") {
            paralyzed1 shouldNotBe Burned(5)
            paralyzed1 shouldNotBe Poisoned(5)
        }
    }
    test("A paralyzed character should not be able to attack for 1 turn") {
        val sword = Sword("PowerfulSword", 1000, 20)
        testCharacter.equip(sword)
        testCharacter.isParalyzed() shouldBe true
        testCharacter.attack(enemy1)
        enemy1.currentHp shouldBe enemy1.maxHp
        testCharacter.isParalyzed() shouldNotBe true
        testCharacter.attack(enemy1)
        enemy1.currentHp shouldNotBe enemy1.maxHp
    }
    context("When a black mage casts thunder") {
        test("The target should remain paralyzed if it already was") {
            testCharacter.isParalyzed() shouldBe true
            val testStaff = Staff("TestStaff", 10, 10, 20)
            mage.equip(testStaff)
            mage.castBlackMagicSpell(Thunder(), testCharacter)
            testCharacter.isParalyzed() shouldBe true
            mage.castBlackMagicSpell(Thunder(), enemy2)
            enemy2.isParalyzed() shouldBe true
        }
        test("A non paralyzed target might be paralyzed") {
            // The given seed ensures the enemy will be burned on the third cast
            val testStaff = Staff("TestStaff", 10, 10, 20)
            mage.equip(testStaff)
            mage.castBlackMagicSpell(Thunder(), enemy1)
            enemy1.isParalyzed() shouldNotBe true
            mage.castBlackMagicSpell(Thunder(), enemy1)
            enemy1.isParalyzed() shouldNotBe true
            mage.castBlackMagicSpell(Thunder(), enemy1)
            enemy1.isParalyzed() shouldBe true
        }
    }
})

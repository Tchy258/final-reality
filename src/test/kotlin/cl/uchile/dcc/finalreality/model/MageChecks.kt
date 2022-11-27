package cl.uchile.dcc.finalreality.model

import cl.uchile.dcc.finalreality.exceptions.InvalidSpellCastException
import cl.uchile.dcc.finalreality.exceptions.InvalidStatValueException
import cl.uchile.dcc.finalreality.exceptions.NoWeaponEquippedException
import cl.uchile.dcc.finalreality.model.character.CharacterTestingFactory
import cl.uchile.dcc.finalreality.model.character.EnemyData
import cl.uchile.dcc.finalreality.model.character.EnemyTestingFactory
import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.player.classes.CharacterData
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.BlackMageTestingFactory
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.MageData
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.WhiteMageTestingFactory
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.EngineerTestingFactory
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.KnightTestingFactory
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.ThiefTestingFactory
import cl.uchile.dcc.finalreality.model.magic.Magic
import cl.uchile.dcc.finalreality.model.magic.blackmagic.Fire
import cl.uchile.dcc.finalreality.model.magic.blackmagic.Thunder
import cl.uchile.dcc.finalreality.model.magic.whitemagic.Cure
import cl.uchile.dcc.finalreality.model.magic.whitemagic.Paralysis
import cl.uchile.dcc.finalreality.model.magic.whitemagic.Poison
import cl.uchile.dcc.finalreality.model.weapon.KnifeTestingFactory
import cl.uchile.dcc.finalreality.model.weapon.NonMagicalWeaponData
import cl.uchile.dcc.finalreality.model.weapon.StaffData
import cl.uchile.dcc.finalreality.model.weapon.StaffTestingFactory
import io.kotest.matchers.comparables.shouldBeGreaterThanOrEqualTo
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.ints.shouldBeLessThan
import io.kotest.matchers.ints.shouldBeLessThanOrEqual
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.assume
import io.kotest.property.checkAll
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.util.concurrent.LinkedBlockingQueue
import kotlin.math.ceil

internal suspend fun mageInequalityCheck(mageFactory: CharacterTestingFactory) {
    checkAll(MageData.validGenerator, MageData.validGenerator) { mage1, mage2 ->
        assume(
            mage1.name != mage2.name ||
                mage1.maxHp != mage2.maxHp ||
                mage1.maxMp != mage2.maxMp ||
                mage1.defense != mage2.defense
        )
        val randomMage1 = mage1.process(mageFactory)
        val randomMage2 = mage2.process(mageFactory)
        randomMage1 shouldNotBe randomMage2
    }
}

internal suspend fun mageValidStatCheck(characterFactory: CharacterTestingFactory) {
    checkAll(MageData.arbitraryGenerator) { mage ->
        if (mage.maxHp <= 0 || mage.maxMp <= 0 || mage.defense < 0) {
            assertThrows<InvalidStatValueException> {
                mage.process(characterFactory)
            }
        } else {
            assertDoesNotThrow {
                mage.process(characterFactory)
            }
        }
    }
}

internal suspend fun differentMageInequalityCheck(
    characterFactory: CharacterTestingFactory,
) {
    val queue = characterFactory.queue
    val oppositeFactory = if (characterFactory == WhiteMageTestingFactory(queue)) {
        BlackMageTestingFactory(queue)
    } else {
        WhiteMageTestingFactory(queue)
    }
    checkAll(MageData.validGenerator) { data ->
        val randomComparedCharacter = data.process(characterFactory)
        val differentMage = data.process(oppositeFactory)
        val character = CharacterData(randomComparedCharacter.name, randomComparedCharacter.maxHp, randomComparedCharacter.defense)
        val dummyValue = randomComparedCharacter.maxMp
        val enemy = EnemyData(character.name,dummyValue,dummyValue,character.maxHp,character.defense)
        randomComparedCharacter shouldNotBe differentMage
        randomComparedCharacter shouldNotBe character.process(EngineerTestingFactory(queue))
        randomComparedCharacter shouldNotBe character.process(ThiefTestingFactory(queue))
        randomComparedCharacter shouldNotBe character.process(KnightTestingFactory(queue))
        randomComparedCharacter shouldNotBe enemy.process(EnemyTestingFactory(queue))
    }
}

internal suspend fun mageUnarmedActionCheck(characterFactory: CharacterTestingFactory) {
    checkAll(MageData.validGenerator) { mage ->
        val randomMage = mage.process(characterFactory)
        val randomMage2 = mage.process(characterFactory)
        assertThrows<NoWeaponEquippedException> {
            randomMage.waitTurn()
        }
        assertThrows<NoWeaponEquippedException> {
            // The concrete mage doesn't matter, even if it's the wrong spell color
            // the mage won't be able to cast unarmed
            randomMage.cast(Thunder(), randomMage2)
        }
        assertThrows<NoWeaponEquippedException> {
            randomMage.attack(randomMage2)
        }
    }
}

internal suspend fun blackMagicStaffCastTest(queue: LinkedBlockingQueue<GameCharacter>) {
    checkAll(
        MageData.validGenerator,
        MageData.validGenerator,
        CharacterData.validGenerator,
        EnemyData.validGenerator,
        StaffData.validGenerator
    ) { mage1, mage2, character, enemy, staff ->
        assume {
            // To successfully cast both spells on all other characters
            mage1.maxMp shouldBeGreaterThan 180
        }
        val randomBlackMage = mage1.process(BlackMageTestingFactory(queue))
        val randomStaff = staff.process(StaffTestingFactory())
        randomBlackMage.equip(randomStaff)
        val randomCharacters: List<GameCharacter> = listOf(
            mage2.process(BlackMageTestingFactory(queue)),
            mage2.process(WhiteMageTestingFactory(queue)),
            character.process(EngineerTestingFactory(queue)),
            character.process(KnightTestingFactory(queue)),
            character.process(ThiefTestingFactory(queue)),
            enemy.process(EnemyTestingFactory(queue))
        )
        for (gameCharacter in randomCharacters) {
            var hpBefore = gameCharacter.currentHp
            randomBlackMage.cast(Thunder(), gameCharacter)
            gameCharacter.currentHp shouldBeLessThanOrEqual gameCharacter.maxHp
            gameCharacter.currentHp shouldBe Integer.max((hpBefore - randomStaff.magicDamage), 0)
            hpBefore = gameCharacter.currentHp
            randomBlackMage.cast(Fire(), gameCharacter)
            gameCharacter.currentHp shouldBe Integer.max((hpBefore - randomStaff.magicDamage), 0)
        }
    }
}

internal suspend fun blackMagicNoStaffCastTest(queue: LinkedBlockingQueue<GameCharacter>) {
    checkAll(
        MageData.validGenerator,
        MageData.validGenerator,
        CharacterData.validGenerator,
        EnemyData.validGenerator,
        NonMagicalWeaponData.validGenerator
    ) { mage1, mage2, character, enemy, knife ->
        assume {
            // To successfully cast both spells on all other characters
            mage1.maxMp shouldBeGreaterThan 180
        }
        val randomBlackMage = mage1.process(BlackMageTestingFactory(queue))
        val randomKnife = knife.process(KnifeTestingFactory())
        randomBlackMage.equip(randomKnife)
        val randomCharacters: List<GameCharacter> = listOf(
            mage2.process(BlackMageTestingFactory(queue)),
            mage2.process(WhiteMageTestingFactory(queue)),
            character.process(EngineerTestingFactory(queue)),
            character.process(KnightTestingFactory(queue)),
            character.process(ThiefTestingFactory(queue)),
            enemy.process(EnemyTestingFactory(queue))
        )
        for (gameCharacter in randomCharacters) {
            randomBlackMage.cast(Thunder(), gameCharacter)
            gameCharacter.currentHp shouldBe gameCharacter.maxHp
            randomBlackMage.cast(Fire(), gameCharacter)
            gameCharacter.currentHp shouldBe gameCharacter.maxHp
        }
    }
}

internal suspend fun whiteMagicCastTest(queue: LinkedBlockingQueue<GameCharacter>) {
    checkAll(
        MageData.validGenerator,
        MageData.validGenerator,
        CharacterData.validGenerator,
        EnemyData.validGenerator,
        StaffData.validGenerator
    ) { mage1, mage2, character, enemy, staff ->
        assume {
            // To successfully cast both spells on all other characters
            mage1.maxMp shouldBeGreaterThan 400
        }
        val randomWhiteMage = mage1.process(WhiteMageTestingFactory(queue))
        val randomStaff = staff.process(StaffTestingFactory())
        randomWhiteMage.equip(randomStaff)
        val randomCharacters: List<GameCharacter> = listOf(
            mage2.process(BlackMageTestingFactory(queue)),
            mage2.process(WhiteMageTestingFactory(queue)),
            character.process(EngineerTestingFactory(queue)),
            character.process(KnightTestingFactory(queue)),
            character.process(ThiefTestingFactory(queue)),
            enemy.process(EnemyTestingFactory(queue))
        )
        for (gameCharacter in randomCharacters) {
            randomWhiteMage.cast(Poison(), gameCharacter)
            gameCharacter.isPoisoned() shouldBe true
            if (gameCharacter == randomCharacters.last()) {
                gameCharacter.attack(randomWhiteMage)
                gameCharacter.currentHp shouldBeLessThanOrEqual gameCharacter.maxHp
            }
            if (gameCharacter.currentHp > 0 && gameCharacter.currentHp <= gameCharacter.maxHp) {
                gameCharacter.receiveMagicDamage(ceil(gameCharacter.maxHp.toDouble() * 3 / 10f).toInt())
            }
            val hpBefore = gameCharacter.currentHp
            randomWhiteMage.cast(Cure(), gameCharacter)
            gameCharacter.currentHp shouldBe Integer.min((hpBefore + ceil(gameCharacter.maxHp.toDouble() * 3 / 10f).toInt()), gameCharacter.maxHp)
            randomWhiteMage.cast(Paralysis(), gameCharacter)
            gameCharacter.isParalyzed() shouldBe true
        }
    }
}

internal suspend fun blackMageUnusableSpellCastCheck(queue: LinkedBlockingQueue<GameCharacter>) {
    checkAll(
        MageData.validGenerator,
        EnemyData.validGenerator,
        StaffData.validGenerator
    ) { mage, enemy, staff ->
        assume {
            mage.maxMp shouldBeGreaterThanOrEqualTo 15
        }
        val randomBlackMage = mage.process(BlackMageTestingFactory(queue))
        val randomStaff = staff.process(StaffTestingFactory())
        val randomEnemy = enemy.process(EnemyTestingFactory(queue))
        randomBlackMage.equip(randomStaff)
        assertThrows<InvalidSpellCastException> {
            randomBlackMage.cast(Cure(), randomEnemy)
        }
    }
}

internal suspend fun insufficientMpSpellCastCheck(spell: Magic, generator: Arb<MageData>, factory: CharacterTestingFactory) {
    checkAll(
        // The upper limit of spell costs is a very tiny value compared to,
        // all the possible integers this test may try with
        PropTestConfig(maxDiscardPercentage = 99),
        generator,
        StaffData.validGenerator
    ) { mage, staff ->
        assume {
            mage.maxMp shouldBeLessThan spell.cost
        }
        val randomMage = mage.process(factory)
        val randomMage2 = mage.process(factory)
        val randomStaff = staff.process(StaffTestingFactory())
        randomMage.equip(randomStaff)
        randomMage.cast(spell,randomMage2) shouldBe Pair(-1,null)
    }
}

internal suspend fun whiteMageUnusableSpellCastCheck(queue: LinkedBlockingQueue<GameCharacter>) {
    checkAll(
        MageData.validGenerator,
        EnemyData.validGenerator,
        StaffData.validGenerator
    ) { mage, enemy, staff ->
        assume {
            mage.maxMp shouldBeGreaterThanOrEqualTo 15
        }
        val randomWhiteMage = mage.process(WhiteMageTestingFactory(queue))
        val randomStaff = staff.process(StaffTestingFactory())
        val randomEnemy = enemy.process(EnemyTestingFactory(queue))
        randomWhiteMage.equip(randomStaff)
        assertThrows<InvalidSpellCastException> {
            randomWhiteMage.cast(Thunder(), randomEnemy)
        }
    }
}

internal suspend fun mpIncreaseCheck(characterFactory: CharacterTestingFactory) {
    checkAll(
        PropTestConfig(maxDiscardPercentage = 55),
        MageData.validGenerator,
        Arb.positiveInt(),
        Arb.positiveInt()
    ) { mage, randomRestoration, randomCost ->
        assume {
            randomCost shouldBeLessThanOrEqual mage.maxMp
        }
        val randomCharacter = mage.process(characterFactory)

        randomCharacter.currentMp shouldBe mage.maxMp
        randomCharacter.canUseMp(randomCost)
        randomCharacter.currentMp shouldNotBe mage.maxMp
        assertDoesNotThrow {
            randomCharacter.restoreMp(randomRestoration)
        }
        randomCharacter.currentMp shouldBeLessThanOrEqual randomCharacter.maxMp
        randomCharacter.currentMp shouldBeGreaterThan 0
    }
}

internal suspend fun mpDecreaseCheck(characterFactory: CharacterTestingFactory) {
    checkAll(
        MageData.validGenerator,
        Arb.positiveInt()
    ) { mage, randomCost ->
        val randomMage = mage.process(characterFactory)
        randomMage.currentMp shouldBe mage.maxMp
        if (randomMage.canUseMp(randomCost)) {
            randomMage.currentMp shouldNotBe mage.maxMp
            randomMage.currentMp shouldBeGreaterThanOrEqualTo 0
        } else {
            randomMage.currentMp shouldBe mage.maxMp
        }
    }
}

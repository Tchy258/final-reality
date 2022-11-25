package cl.uchile.dcc.finalreality.model

import cl.uchile.dcc.finalreality.exceptions.InvalidSpellCastException
import cl.uchile.dcc.finalreality.exceptions.InvalidStatValueException
import cl.uchile.dcc.finalreality.exceptions.NoWeaponEquippedException
import cl.uchile.dcc.finalreality.model.character.CharacterTestingFactory
import cl.uchile.dcc.finalreality.model.character.EnemyData
import cl.uchile.dcc.finalreality.model.character.EnemyTestingFactory
import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.player.classes.CharacterData
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.BlackMage
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.BlackMageTestingFactory
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.MageData
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.WhiteMage
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.WhiteMageTestingFactory
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.EngineerTestingFactory
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.KnightTestingFactory
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.ThiefTestingFactory
import cl.uchile.dcc.finalreality.model.magic.blackmagic.Fire
import cl.uchile.dcc.finalreality.model.magic.blackmagic.Thunder
import cl.uchile.dcc.finalreality.model.magic.whitemagic.Cure
import cl.uchile.dcc.finalreality.model.magic.whitemagic.Paralysis
import cl.uchile.dcc.finalreality.model.magic.whitemagic.Poison
import cl.uchile.dcc.finalreality.model.weapon.KnifeTestingFactory
import cl.uchile.dcc.finalreality.model.weapon.StaffData
import cl.uchile.dcc.finalreality.model.weapon.StaffTestingFactory
import cl.uchile.dcc.finalreality.model.weapon.WeaponData
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.ints.shouldBeLessThanOrEqual
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
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
        val randomMage1 = mageFactory.create(mage1)
        val randomMage2 = mageFactory.create(mage2)
        randomMage1 shouldNotBe randomMage2
    }
}

internal suspend fun mageValidStatCheck(characterFactory: CharacterTestingFactory) {
    checkAll(MageData.arbitraryGenerator) { mage ->
        if (mage.maxHp <= 0 || mage.maxMp <= 0 || mage.defense < 0) {
            assertThrows<InvalidStatValueException> {
                characterFactory.create(mage)
            }
        } else {
            assertDoesNotThrow {
                characterFactory.create(mage)
            }
        }
    }
}

internal suspend fun mageUnarmedActionCheck(characterFactory: CharacterTestingFactory) {
    checkAll(MageData.validGenerator) { mage ->
        val randomMage = characterFactory.create(mage)
        val randomMage2 = characterFactory.create(mage)
        assertThrows<NoWeaponEquippedException> {
            randomMage.waitTurn()
        }
        assertThrows<NoWeaponEquippedException> {
            if (characterFactory.isBlackMageFactory()) {
                randomMage as BlackMage
                randomMage.cast(Thunder(), randomMage2)
            } else {
                randomMage as WhiteMage
                randomMage.castWhiteMagicSpell(Poison(), randomMage2)
            }
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
        val randomBlackMage = BlackMageTestingFactory(queue).create(mage1)
        val randomStaff = StaffTestingFactory().create(staff)
        randomBlackMage.equip(randomStaff)
        val randomCharacters: List<GameCharacter> = listOf(
            BlackMageTestingFactory(queue).create(mage2),
            WhiteMageTestingFactory(queue).create(mage2),
            EngineerTestingFactory(queue).create(character),
            KnightTestingFactory(queue).create(character),
            ThiefTestingFactory(queue).create(character),
            EnemyTestingFactory(queue).create(enemy)
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
        WeaponData.validGenerator
    ) { mage1, mage2, character, enemy, knife ->
        assume {
            // To successfully cast both spells on all other characters
            mage1.maxMp shouldBeGreaterThan 180
        }
        val randomBlackMage = BlackMageTestingFactory(queue).create(mage1)
        val randomKnife = KnifeTestingFactory().create(knife)
        randomBlackMage.equip(randomKnife)
        val randomCharacters: List<GameCharacter> = listOf(
            BlackMageTestingFactory(queue).create(mage2),
            WhiteMageTestingFactory(queue).create(mage2),
            EngineerTestingFactory(queue).create(character),
            KnightTestingFactory(queue).create(character),
            ThiefTestingFactory(queue).create(character),
            EnemyTestingFactory(queue).create(enemy)
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
        val randomWhiteMage = WhiteMageTestingFactory(queue).create(mage1)
        val randomStaff = StaffTestingFactory().create(staff)
        randomWhiteMage.equip(randomStaff)
        val randomCharacters: List<GameCharacter> = listOf(
            BlackMageTestingFactory(queue).create(mage2),
            WhiteMageTestingFactory(queue).create(mage2),
            EngineerTestingFactory(queue).create(character),
            KnightTestingFactory(queue).create(character),
            ThiefTestingFactory(queue).create(character),
            EnemyTestingFactory(queue).create(enemy)
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
        val randomBlackMage = BlackMageTestingFactory(queue).create(mage)
        val randomStaff = StaffTestingFactory().create(staff)
        val randomEnemy = EnemyTestingFactory(queue).create(enemy)
        randomBlackMage.equip(randomStaff)
        assertThrows<InvalidSpellCastException> {
            randomBlackMage.cast(Cure(), randomEnemy)
        }
    }
}

internal suspend fun whiteMageUnusableSpellCastCheck(queue: LinkedBlockingQueue<GameCharacter>) {
    checkAll(
        MageData.validGenerator,
        EnemyData.validGenerator,
        StaffData.validGenerator
    ) { mage, enemy, staff ->
        val randomWhiteMage = WhiteMageTestingFactory(queue).create(mage)
        val randomStaff = StaffTestingFactory().create(staff)
        val randomEnemy = EnemyTestingFactory(queue).create(enemy)
        randomWhiteMage.equip(randomStaff)
        assertThrows<InvalidSpellCastException> {
            randomWhiteMage.cast(Thunder(), randomEnemy)
        }
    }
}

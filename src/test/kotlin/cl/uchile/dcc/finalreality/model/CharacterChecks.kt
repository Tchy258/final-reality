package cl.uchile.dcc.finalreality.model

import cl.uchile.dcc.finalreality.exceptions.InvalidStatValueException
import cl.uchile.dcc.finalreality.exceptions.InvalidWeaponException
import cl.uchile.dcc.finalreality.exceptions.NoWeaponEquippedException
import cl.uchile.dcc.finalreality.model.character.CharacterTestingFactory
import cl.uchile.dcc.finalreality.model.character.EnemyData
import cl.uchile.dcc.finalreality.model.character.EnemyTestingFactory
import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.player.classes.CharacterData
import cl.uchile.dcc.finalreality.model.character.player.classes.PlayerCharacter
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.BlackMageTestingFactory
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.Mage
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.MageData
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.WhiteMageTestingFactory
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.EngineerTestingFactory
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.KnightTestingFactory
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.ThiefTestingFactory
import cl.uchile.dcc.finalreality.model.weapon.StaffData
import cl.uchile.dcc.finalreality.model.weapon.WeaponData
import cl.uchile.dcc.finalreality.model.weapon.WeaponTestingFactory
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

internal suspend fun equalityCheck(generator: Arb<ModelData>, characterFactory: CharacterTestingFactory) {
    checkAll(generator) { modelData ->
        val randomCharacter1 = characterFactory.create(modelData)
        val randomCharacter2 = characterFactory.create(modelData)
        randomCharacter1 shouldBe randomCharacter2
    }
}

internal suspend fun selfEqualityCheck(generator: Arb<ModelData>, characterFactory: CharacterTestingFactory) {
    checkAll(generator) { modelData ->
        val randomCharacter1 = characterFactory.create(modelData)
        randomCharacter1 shouldBe randomCharacter1
    }
}

internal suspend fun characterInequalityCheck(characterFactory: CharacterTestingFactory) {
    checkAll(CharacterData.validGenerator, CharacterData.validGenerator) { character1, character2 ->
        assume(
            character1.name != character2.name ||
                character1.maxHp != character2.maxHp ||
                character1.defense != character2.defense
        )
        val randomCharacter1 = characterFactory.create(character1)
        val randomCharacter2 = characterFactory.create(character2)
        randomCharacter1 shouldNotBe randomCharacter2
    }
}

internal suspend fun notNullCheck(generator: Arb<ModelData>, characterFactory: CharacterTestingFactory) {
    checkAll(generator) { characterData ->
        val randomCharacter = characterFactory.create(characterData)
        randomCharacter shouldNotBe null
    }
}

internal suspend fun characterValidStatCheck(characterFactory: CharacterTestingFactory) {
    checkAll(CharacterData.arbitraryGenerator) {
        character ->
        if (character.maxHp <= 0 || character.defense < 0) {
            assertThrows<InvalidStatValueException> {
                characterFactory.create(character)
            }
        } else {
            assertDoesNotThrow {
                characterFactory.create(character)
            }
        }
    }
}

internal suspend fun hpDecreaseCheck(generator: Arb<ModelData>, characterFactory: CharacterTestingFactory) {
    checkAll(
        PropTestConfig(maxDiscardPercentage = 55),
        generator,
        Arb.positiveInt()
    ) { character, randomDamage ->
        // These casts are needed to use the same function for characters, mages and enemies
        var defense: Int
        var maxHp: Int
        try {
            character as CharacterData
            defense = character.defense
            maxHp = character.maxHp
        } catch (e: ClassCastException) {
            try {
                character as MageData
                defense = character.defense
                maxHp = character.maxHp
            } catch (e: ClassCastException) {
                character as EnemyData
                defense = character.defense
                maxHp = character.maxHp
            }
        }

        assume {
            randomDamage shouldBeGreaterThan defense
        }
        val randomCharacter = characterFactory.create(character)
        randomCharacter.currentHp shouldBe maxHp
        assertDoesNotThrow {
            randomCharacter.receiveAttack(randomDamage)
        }
        randomCharacter.currentHp shouldNotBe maxHp
        randomCharacter.currentHp shouldBeGreaterThanOrEqualTo 0
    }
}

internal suspend fun hpIncreaseCheck(generator: Arb<ModelData>, characterFactory: CharacterTestingFactory) {
    checkAll(
        PropTestConfig(maxDiscardPercentage = 55),
        generator,
        Arb.positiveInt(),
        Arb.positiveInt()
    ) { character, randomHealing, randomDamage ->
        var defense: Int
        var maxHp: Int
        try {
            character as CharacterData
            defense = character.defense
            maxHp = character.maxHp
        } catch (e: ClassCastException) {
            try {
                character as MageData
                defense = character.defense
                maxHp = character.maxHp
            } catch (e: ClassCastException) {
                character as EnemyData
                defense = character.defense
                maxHp = character.maxHp
            }
        }
        assume {
            randomDamage shouldBeGreaterThan defense
        }
        val randomCharacter = characterFactory.create(character)
        randomCharacter.currentHp shouldBe maxHp
        randomCharacter.receiveAttack(randomDamage)
        randomCharacter.currentHp shouldNotBe maxHp
        assertDoesNotThrow {
            randomCharacter.receiveHealing(randomHealing)
        }
        randomCharacter.currentHp shouldBeLessThanOrEqual randomCharacter.maxHp
        randomCharacter.currentHp shouldBeGreaterThan 0
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
        val randomCharacter = characterFactory.create(mage) as Mage

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
        val randomMage = characterFactory.create(mage) as Mage
        randomMage.currentMp shouldBe mage.maxMp
        if (randomMage.canUseMp(randomCost)) {
            randomMage.currentMp shouldNotBe mage.maxMp
            randomMage.currentMp shouldBeGreaterThanOrEqualTo 0
        } else {
            randomMage.currentMp shouldBe mage.maxMp
        }
    }
}

internal suspend fun differentCharacterInequalityCheck(
    characterFactory: CharacterTestingFactory,
) {
    val queue = characterFactory.queue
    val factories: List<CharacterTestingFactory> =
        listOf(
            BlackMageTestingFactory(queue),
            WhiteMageTestingFactory(queue),
            EngineerTestingFactory(queue),
            KnightTestingFactory(queue),
            ThiefTestingFactory(queue)
        )
    checkAll(MageData.validGenerator) { mageData ->
        val characterData = CharacterData(
            mageData.name,
            mageData.maxHp,
            mageData.defense
        )
        val randomComparedCharacter =
            if (characterFactory.isBlackMageFactory() || characterFactory.isWhiteMageFactory()) {
                characterFactory.create(mageData)
            } else {
                characterFactory.create(characterData)
            }
        for (factory in factories) {
            if (characterFactory == factory) {
                continue
            } else {
                val differentCharacter =
                    if (factory.isBlackMageFactory() || factory.isWhiteMageFactory()) {
                        factory.create(mageData)
                    } else {
                        factory.create(characterData)
                    }

                randomComparedCharacter shouldNotBe differentCharacter
            }
        }
    }
}

internal suspend fun validEquippableWeaponCheck(
    characterGenerator: Arb<ModelData>,
    weaponGenerator: Arb<ModelData>,
    characterFactory: CharacterTestingFactory,
    weaponFactory: WeaponTestingFactory
) {
    checkAll(characterGenerator, weaponGenerator) { character, weapon ->
        val randomWeapon = weaponFactory.create(weapon)
        val randomCharacter = characterFactory.create(character) as PlayerCharacter
        assertDoesNotThrow {
            randomCharacter.equip(randomWeapon)
        }
        randomCharacter.equippedWeapon shouldBe randomWeapon
    }
}

internal suspend fun invalidEquippableWeaponCheck(
    characterGenerator: Arb<ModelData>,
    weaponGenerator: Arb<ModelData>,
    characterFactory: CharacterTestingFactory,
    weaponFactory: WeaponTestingFactory
) {
    checkAll(characterGenerator, weaponGenerator) { character, weapon ->
        val randomWeapon = weaponFactory.create(weapon)
        val randomCharacter = characterFactory.create(character) as PlayerCharacter
        assertThrows<InvalidWeaponException> {
            randomCharacter.equip(randomWeapon)
        }
    }
}

internal suspend fun characterUnarmedActionCheck(characterFactory: CharacterTestingFactory) {
    checkAll(CharacterData.validGenerator) { character ->
        val randomCharacter = characterFactory.create(character)
        val randomCharacter2 = characterFactory.create(character)
        assertThrows<NoWeaponEquippedException> {
            randomCharacter.waitTurn()
        }
        assertThrows<NoWeaponEquippedException> {
            randomCharacter.attack(randomCharacter2)
        }
    }
}

internal suspend fun characterQueueJoinCheck(
    generator1: Arb<ModelData>,
    generator2: Arb<ModelData>,
    characterFactory: CharacterTestingFactory,
    weaponFactory: WeaponTestingFactory
) {
    checkAll(
        generator1,
        generator2
    ) { character, weapon ->
        val randomWeapon = weaponFactory.create(weapon)
        val randomCharacter = characterFactory.create(character) as PlayerCharacter
        randomCharacter.equip(randomWeapon)
        assertDoesNotThrow {
            randomCharacter.waitTurn()
        }
    }
}

internal suspend fun characterAttackTest(
    testedCharacterGenerator: Arb<ModelData>,
    weaponGenerator: Arb<ModelData>,
    characterFactory: CharacterTestingFactory,
    weaponFactory: WeaponTestingFactory
) {
    checkAll(
        PropTestConfig(maxDiscardPercentage = 80),
        testedCharacterGenerator,
        CharacterData.validGenerator,
        MageData.validGenerator,
        weaponGenerator,
        EnemyData.validGenerator
    ) { thisCharacter, character, mage, weapon, enemy ->
        val weaponDamage: Int = try {
            (weapon as WeaponData).damage
        } catch (e: java.lang.ClassCastException) {
            (weapon as StaffData).damage
        }
        assume {
            weaponDamage shouldBeGreaterThan mage.defense
            weaponDamage shouldBeGreaterThan character.defense
            weaponDamage shouldBeGreaterThan enemy.defense
        }
        val thisRandomCharacter = characterFactory.create(thisCharacter) as PlayerCharacter
        val randomWeapon = weaponFactory.create(weapon)

        thisRandomCharacter.equip(randomWeapon)
        val queue = characterFactory.queue
        val randomCharacters: List<GameCharacter> = listOf(
            EnemyTestingFactory(queue).create(enemy),
            EngineerTestingFactory(queue).create(character),
            BlackMageTestingFactory(queue).create(mage),
            KnightTestingFactory(queue).create(character),
            ThiefTestingFactory(queue).create(character),
            WhiteMageTestingFactory(queue).create(mage)
        )
        for (gameCharacter in randomCharacters) {
            gameCharacter.currentHp shouldBe gameCharacter.maxHp
            thisRandomCharacter.attack(gameCharacter)
            gameCharacter.currentHp shouldBeLessThan gameCharacter.maxHp
        }
    }
}

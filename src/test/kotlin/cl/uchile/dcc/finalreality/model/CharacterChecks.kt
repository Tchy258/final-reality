package cl.uchile.dcc.finalreality.model

import cl.uchile.dcc.finalreality.exceptions.InvalidStatValueException
import cl.uchile.dcc.finalreality.exceptions.InvalidWeaponException
import cl.uchile.dcc.finalreality.exceptions.NoWeaponEquippedException
import cl.uchile.dcc.finalreality.model.character.CharacterTestingFactory
import cl.uchile.dcc.finalreality.model.character.EnemyData
import cl.uchile.dcc.finalreality.model.character.EnemyTestingFactory
import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.player.PlayerCharacterData
import cl.uchile.dcc.finalreality.model.character.player.classes.CharacterData
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.BlackMageTestingFactory
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.MageData
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.WhiteMageTestingFactory
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.EngineerTestingFactory
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.KnightTestingFactory
import cl.uchile.dcc.finalreality.model.character.player.classes.physical.ThiefTestingFactory
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

internal suspend fun equalityCheck(generator: Arb<GameCharacterData>, characterFactory: CharacterTestingFactory) {
    checkAll(generator) { modelData ->
        val randomCharacter1 = modelData.process(characterFactory)
        val randomCharacter2 = modelData.process(characterFactory)
        randomCharacter1 shouldBe randomCharacter2
    }
}

internal suspend fun selfEqualityCheck(generator: Arb<GameCharacterData>, characterFactory: CharacterTestingFactory) {
    checkAll(generator) { modelData ->
        val randomCharacter1 = modelData.process(characterFactory)
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

internal suspend fun notNullCheck(generator: Arb<GameCharacterData>, characterFactory: CharacterTestingFactory) {
    checkAll(generator) { characterData ->
        val randomCharacter = characterData.process(characterFactory)
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

internal suspend fun hpDecreaseCheck(generator: Arb<GameCharacterData>, characterFactory: CharacterTestingFactory) {
    checkAll(
        PropTestConfig(maxDiscardPercentage = 55),
        generator,
        Arb.positiveInt()
    ) { character, randomDamage ->
        // These casts are needed to use the same function for characters, mages and enemies
        val randomCharacter = character.process(characterFactory)
        assume {
            randomDamage shouldBeGreaterThan randomCharacter.defense
        }
        val maxHp = randomCharacter.maxHp
        randomCharacter.currentHp shouldBe maxHp
        assertDoesNotThrow {
            randomCharacter.receiveAttack(randomDamage)
        }
        randomCharacter.currentHp shouldNotBe maxHp
        randomCharacter.currentHp shouldBeGreaterThanOrEqualTo 0
    }
}

internal suspend fun hpIncreaseCheck(generator: Arb<GameCharacterData>, characterFactory: CharacterTestingFactory) {
    checkAll(
        PropTestConfig(maxDiscardPercentage = 55),
        generator,
        Arb.positiveInt(),
        Arb.positiveInt()
    ) { character, randomHealing, randomDamage ->
        val randomCharacter = character.process(characterFactory)
        val defense: Int = randomCharacter.defense
        val maxHp: Int = randomCharacter.maxHp
        assume {
            randomDamage shouldBeGreaterThan defense
        }
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

internal suspend fun differentCharacterInequalityCheck(
    characterFactory: CharacterTestingFactory,
) {
    val queue = characterFactory.queue
    val factories: List<CharacterTestingFactory> =
        listOf(
            EngineerTestingFactory(queue),
            KnightTestingFactory(queue),
            ThiefTestingFactory(queue),
        )
    checkAll(CharacterData.validGenerator) { data ->
        val randomComparedCharacter = data.process(characterFactory)
        for (factory in factories) {
            if (characterFactory == factory) {
                continue
            } else {
                val differentCharacter = data.process(factory)

                randomComparedCharacter shouldNotBe differentCharacter
            }
        }
        val someDummyValue = randomComparedCharacter.maxHp
        val mageData = MageData(randomComparedCharacter.name, randomComparedCharacter.maxHp, someDummyValue, randomComparedCharacter.defense)
        randomComparedCharacter shouldNotBe mageData.process(WhiteMageTestingFactory(queue))
        randomComparedCharacter shouldNotBe mageData.process(BlackMageTestingFactory(queue))
    }
}

internal suspend fun validEquippableWeaponCheck(
    characterGenerator: Arb<PlayerCharacterData>,
    weaponGenerator: Arb<WeaponData>,
    characterFactory: CharacterTestingFactory,
    weaponFactory: WeaponTestingFactory
) {
    checkAll(characterGenerator, weaponGenerator) { character, weapon ->
        val randomWeapon = weapon.process(weaponFactory)
        val randomCharacter = character.process(characterFactory)
        assertDoesNotThrow {
            randomCharacter.equip(randomWeapon)
        }
        randomCharacter.equippedWeapon shouldBe randomWeapon
    }
}

internal suspend fun invalidEquippableWeaponCheck(
    characterGenerator: Arb<PlayerCharacterData>,
    weaponGenerator: Arb<WeaponData>,
    characterFactory: CharacterTestingFactory,
    weaponFactory: WeaponTestingFactory
) {
    checkAll(characterGenerator, weaponGenerator) { character, weapon ->
        val randomWeapon = weapon.process(weaponFactory)
        val randomCharacter = character.process(characterFactory)
        assertThrows<InvalidWeaponException> {
            randomCharacter.equip(randomWeapon)
        }
    }
}

internal suspend fun characterUnarmedActionCheck(characterFactory: CharacterTestingFactory) {
    checkAll(CharacterData.validGenerator) { character ->
        val randomCharacter = character.process(characterFactory)
        val randomCharacter2 = character.process(characterFactory)
        assertThrows<NoWeaponEquippedException> {
            randomCharacter.waitTurn()
        }
        assertThrows<NoWeaponEquippedException> {
            randomCharacter.attack(randomCharacter2)
        }
    }
}

internal suspend fun characterQueueJoinCheck(
    generator1: Arb<PlayerCharacterData>,
    generator2: Arb<WeaponData>,
    characterFactory: CharacterTestingFactory,
    weaponFactory: WeaponTestingFactory
) {
    checkAll(
        generator1,
        generator2
    ) { character, weapon ->
        val randomWeapon = weapon.process(weaponFactory)
        val randomCharacter = character.process(characterFactory)
        randomCharacter.equip(randomWeapon)
        assertDoesNotThrow {
            randomCharacter.waitTurn()
        }
    }
}

internal suspend fun characterAttackTest(
    testedCharacterGenerator: Arb<PlayerCharacterData>,
    weaponGenerator: Arb<WeaponData>,
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
        val thisRandomCharacter = thisCharacter.process(characterFactory)
        val randomWeapon = weapon.process(weaponFactory)
        val weaponDamage: Int = randomWeapon.damage
        assume {
            weaponDamage shouldBeGreaterThan mage.defense
            weaponDamage shouldBeGreaterThan character.defense
            weaponDamage shouldBeGreaterThan enemy.defense
        }

        thisRandomCharacter.equip(randomWeapon)
        val queue = characterFactory.queue
        val randomCharacters: List<GameCharacter> = listOf(
            enemy.process(EnemyTestingFactory(queue)),
            character.process(EngineerTestingFactory(queue)),
            mage.process(BlackMageTestingFactory(queue)),
            character.process(KnightTestingFactory(queue)),
            character.process(ThiefTestingFactory(queue)),
            mage.process(WhiteMageTestingFactory(queue))
        )
        for (gameCharacter in randomCharacters) {
            gameCharacter.currentHp shouldBe gameCharacter.maxHp
            thisRandomCharacter.attack(gameCharacter)
            gameCharacter.currentHp shouldBeLessThan gameCharacter.maxHp
        }
    }
}

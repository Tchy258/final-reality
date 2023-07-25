package cl.uchile.dcc.finalreality.model

import cl.uchile.dcc.finalreality.exceptions.InvalidStatValueException
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
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.ints.shouldBeLessThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.PropTestConfig
import io.kotest.property.assume
import io.kotest.property.checkAll
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.util.concurrent.LinkedBlockingQueue

internal suspend fun enemyAttackTest() {
    checkAll(
        PropTestConfig(maxDiscardPercentage = 80),
        EnemyData.validGenerator,
        CharacterData.validGenerator,
        MageData.validGenerator,
        EnemyData.validGenerator
    ) { thisEnemy, character, mage, enemy ->
        val queue = LinkedBlockingQueue<GameCharacter>()
        assume {
            thisEnemy.damage shouldBeGreaterThan mage.defense
            thisEnemy.damage shouldBeGreaterThan character.defense
            thisEnemy.damage shouldBeGreaterThan enemy.defense
        }
        val thisRandomEnemy = thisEnemy.process(EnemyTestingFactory(queue))

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
            thisRandomEnemy.attack(gameCharacter)
            gameCharacter.currentHp shouldBeLessThan gameCharacter.maxHp
        }
    }
}

internal suspend fun enemyInequalityCheck() {
    checkAll(EnemyData.validGenerator, EnemyData.validGenerator) { enemy1, enemy2 ->
        assume(
            enemy1.name != enemy2.name ||
                enemy1.damage != enemy2.damage ||
                enemy1.maxHp != enemy2.maxHp ||
                enemy1.defense != enemy2.defense ||
                enemy1.weight != enemy2.weight
        )
        val factory = EnemyTestingFactory(LinkedBlockingQueue<GameCharacter>())
        val randomEnemy1 = enemy1.process(factory)
        val randomEnemy2 = enemy2.process(factory)
        randomEnemy1 shouldNotBe randomEnemy2
    }
}

internal suspend fun enemyValidStatCheck() {
    checkAll(EnemyData.arbitraryGenerator) { enemy ->
        val enemyFactory = EnemyTestingFactory(LinkedBlockingQueue<GameCharacter>())
        if (enemy.maxHp <= 0 || enemy.defense < 0 || enemy.damage < 0 || enemy.weight <= 0) {
            assertThrows<InvalidStatValueException> {
                enemy.process(enemyFactory)
            }
        } else {
            assertDoesNotThrow {
                enemy.process(enemyFactory)
            }
        }
    }
}

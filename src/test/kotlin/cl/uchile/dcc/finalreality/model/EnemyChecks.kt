package cl.uchile.dcc.finalreality.model

import cl.uchile.dcc.finalreality.exceptions.InvalidStatValueException
import cl.uchile.dcc.finalreality.model.character.Enemy
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

internal suspend fun enemyAttackTest(queue: LinkedBlockingQueue<GameCharacter>) {
    checkAll(
        PropTestConfig(maxDiscardPercentage = 80),
        EnemyData.validGenerator,
        CharacterData.validGenerator,
        MageData.validGenerator,
        EnemyData.validGenerator
    ) { thisEnemy, character, mage, enemy ->
        assume {
            thisEnemy.damage shouldBeGreaterThan mage.defense
            thisEnemy.damage shouldBeGreaterThan character.defense
            thisEnemy.damage shouldBeGreaterThan enemy.defense
        }
        val thisRandomEnemy = EnemyTestingFactory(queue).create(thisEnemy)

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
            thisRandomEnemy.attack(gameCharacter)
            gameCharacter.currentHp shouldBeLessThan gameCharacter.maxHp
        }
    }
}

internal suspend fun enemyInequalityCheck(enemyFactory: EnemyTestingFactory) {
    checkAll(EnemyData.validGenerator, EnemyData.validGenerator) { enemy1, enemy2 ->
        assume(
            enemy1.name != enemy2.name ||
                enemy1.damage != enemy2.damage ||
                enemy1.maxHp != enemy2.maxHp ||
                enemy1.defense != enemy2.defense ||
                enemy1.weight != enemy2.weight
        )
        val randomEnemy1 = enemyFactory.create(enemy1)
        val randomEnemy2 = enemyFactory.create(enemy2)
        randomEnemy1 shouldNotBe randomEnemy2
    }
}

internal suspend fun enemyValidStatCheck() {
    checkAll(EnemyData.arbitraryGenerator) { enemy ->
        val enemyFactory = EnemyTestingFactory(LinkedBlockingQueue<GameCharacter>())
        val queue = enemyFactory.queue
        if (enemy.maxHp <= 0 || enemy.defense < 0 || enemy.damage < 0 || enemy.weight <= 0) {
            assertThrows<InvalidStatValueException> {
                Enemy(enemy.name, enemy.damage, enemy.weight, enemy.maxHp, enemy.defense, queue)
            }
        } else {
            assertDoesNotThrow {
                Enemy(enemy.name, enemy.damage, enemy.weight, enemy.maxHp, enemy.defense, queue)
            }
        }
    }
}

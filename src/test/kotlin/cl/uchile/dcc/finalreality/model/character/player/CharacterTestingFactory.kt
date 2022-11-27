package cl.uchile.dcc.finalreality.model.character

import cl.uchile.dcc.finalreality.model.character.player.classes.CharacterData
import cl.uchile.dcc.finalreality.model.character.player.classes.PlayerCharacter
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.Mage
import cl.uchile.dcc.finalreality.model.character.player.classes.magical.MageData
import java.util.concurrent.LinkedBlockingQueue

/**
 * Abstract GameCharacter factory
 */
internal interface CharacterTestingFactory {
    val queue: LinkedBlockingQueue<GameCharacter>
    fun create(data: CharacterData): GameCharacter {
        return data.process(this)
    }
    fun createCharacter(data: CharacterData): PlayerCharacter {
        throw IllegalArgumentException("Attempted to create a magical character with CharacterData")
    }
    fun createMage(data: MageData): Mage {
        throw IllegalArgumentException("Attempted to create a non magical character with MageData")
    }
    fun createEnemy(data: EnemyData): Enemy {
        throw IllegalArgumentException("Attempted to create a non enemy character with EnemyData")
    }
}

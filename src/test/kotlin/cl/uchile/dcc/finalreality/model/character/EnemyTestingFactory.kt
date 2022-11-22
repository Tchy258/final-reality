package cl.uchile.dcc.finalreality.model.character

import cl.uchile.dcc.finalreality.model.ModelData
import java.util.concurrent.LinkedBlockingQueue

/**
 * Enemy factory to generate create enemies
 */
internal class EnemyTestingFactory(override val queue: LinkedBlockingQueue<GameCharacter>) : CharacterTestingFactory {
    override fun create(data: ModelData): Enemy {
        data as EnemyData
        return Enemy(data.name, data.damage, data.weight, data.maxHp, data.defense, queue)
    }
}

package cl.uchile.dcc.finalreality.model.character

import java.util.concurrent.LinkedBlockingQueue

internal class EnemyTestingFactory(override val queue: LinkedBlockingQueue<GameCharacter>) : CharacterTestingFactory {
    override fun createEnemy(data: EnemyData): Enemy {
        return Enemy(data.name, data.damage, data.weight, data.maxHp, data.defense, queue)
    }
}

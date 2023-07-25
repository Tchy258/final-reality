package cl.uchile.dcc.finalreality.model.character.player.classes.magical

import cl.uchile.dcc.finalreality.model.character.CharacterTestingFactory
import cl.uchile.dcc.finalreality.model.character.GameCharacter
import java.util.concurrent.LinkedBlockingQueue

internal class BlackMageTestingFactory(override val queue: LinkedBlockingQueue<GameCharacter>) :
    CharacterTestingFactory {
    override fun createMage(data: MageData): BlackMage {
        return BlackMage(data.name, data.maxHp, data.maxMp, data.defense, queue)
    }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BlackMageTestingFactory

        if (queue != other.queue) return false

        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

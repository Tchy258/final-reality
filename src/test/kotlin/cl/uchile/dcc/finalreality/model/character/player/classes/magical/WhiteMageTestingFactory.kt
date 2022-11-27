package cl.uchile.dcc.finalreality.model.character.player.classes.magical

import cl.uchile.dcc.finalreality.model.character.CharacterTestingFactory
import cl.uchile.dcc.finalreality.model.character.GameCharacter
import java.util.concurrent.LinkedBlockingQueue

internal class WhiteMageTestingFactory(override val queue: LinkedBlockingQueue<GameCharacter>) :
    CharacterTestingFactory {
    override fun createMage(data: MageData): WhiteMage {
        return WhiteMage(data.name, data.maxHp, data.maxMp, data.defense, queue)
    }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WhiteMageTestingFactory

        if (queue != other.queue) return false

        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

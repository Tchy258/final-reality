package cl.uchile.dcc.finalreality.model.character.player.classes.physical

import cl.uchile.dcc.finalreality.model.character.CharacterTestingFactory
import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.player.classes.CharacterData
import java.util.concurrent.LinkedBlockingQueue

internal class EngineerTestingFactory(override val queue: LinkedBlockingQueue<GameCharacter>) :
    CharacterTestingFactory {
    override fun createCharacter(data: CharacterData): Engineer {
        return Engineer(data.name, data.maxHp, data.defense, queue)
    }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EngineerTestingFactory

        if (queue != other.queue) return false

        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

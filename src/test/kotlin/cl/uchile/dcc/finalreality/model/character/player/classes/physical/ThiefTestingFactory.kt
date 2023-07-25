package cl.uchile.dcc.finalreality.model.character.player.classes.physical

import cl.uchile.dcc.finalreality.model.character.CharacterTestingFactory
import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.player.classes.CharacterData
import cl.uchile.dcc.finalreality.model.character.player.classes.PlayerCharacter
import java.util.concurrent.LinkedBlockingQueue

internal class ThiefTestingFactory(override val queue: LinkedBlockingQueue<GameCharacter>) :
    CharacterTestingFactory {
    override fun createCharacter(data: CharacterData): PlayerCharacter {
        return Thief(data.name, data.maxHp, data.defense, queue)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ThiefTestingFactory

        if (queue != other.queue) return false

        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

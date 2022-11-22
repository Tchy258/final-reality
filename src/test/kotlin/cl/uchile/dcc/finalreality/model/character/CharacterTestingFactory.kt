package cl.uchile.dcc.finalreality.model.character

import cl.uchile.dcc.finalreality.model.ModelData
import java.util.concurrent.LinkedBlockingQueue

/**
 * Abstract GameCharacter factory
 */
interface CharacterTestingFactory {
    val queue: LinkedBlockingQueue<GameCharacter>
    fun create(data: ModelData): GameCharacter
    fun isBlackMageFactory(): Boolean = false
    fun isWhiteMageFactory(): Boolean = false
    fun isEngineerFactory(): Boolean = false
    fun isKnightFactory(): Boolean = false
    fun isThiefFactory(): Boolean = false
}

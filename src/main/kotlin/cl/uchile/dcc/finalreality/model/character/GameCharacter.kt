package cl.uchile.dcc.finalreality.model.character

/**
 * This represents a character from the game.
 * A character can be controlled by the player or by the CPU (an enemy).
 *
 * @property name the name of the character.
 * @property maxHp the maximum health points of the character.
 * @property defense the defense of the character.
 * @property currentHp the current health points of the character.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @author <a href="https://www.github.com/tchy258">Tchy258</a>
 */
interface GameCharacter {
    val name: String
    val maxHp: Int
    var currentHp: Int
    val defense: Int
    /**
     * Sets a scheduled executor to make this character (thread) wait for `speed / 10`
     * seconds (that is the same as `speed * 100` milliseconds) before adding the character to the queue.
     */
    fun waitTurn()
}

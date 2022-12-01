package cl.uchile.dcc.finalreality.controller.state

import cl.uchile.dcc.finalreality.controller.GameController
import cl.uchile.dcc.finalreality.model.character.player.classes.PlayerCharacter
import cl.uchile.dcc.finalreality.model.weapon.Axe
import cl.uchile.dcc.finalreality.model.weapon.Bow
import cl.uchile.dcc.finalreality.model.weapon.Knife
import cl.uchile.dcc.finalreality.model.weapon.Staff
import cl.uchile.dcc.finalreality.model.weapon.Sword
import cl.uchile.dcc.finalreality.model.weapon.Weapon
import kotlin.random.Random

/**
 * This state represents player victory, here the game could end
 * or go to the enemy generation state
 *
 * @author <a href="https://github.com/Tchy258">Tchy258</a>
 */
class EnemyDefeatedState(override val controller: GameController) : GameState {
    override fun isEnemyDefeated(): Boolean = true
    override fun toEnemyGeneration() {
        controller.setState(EnemyGenerationState(controller))
    }
    override fun onPlayerWin(nextBattle: Boolean) {
        if (nextBattle) {
            val k = Random.nextInt(0, 5)
            val j = Random.nextInt(0, 5)
            val prefix: String = when (j) {
                0 -> "Ancient"
                1 -> "Good Quality"
                2 -> "Mystic"
                3 -> "Enchanted"
                else -> "Blessed"
            }
            val damage = Integer.min(300, Random.nextInt(30, 60) * (j + 1))
            val weight = Integer.max(15, Random.nextInt(20, 40) / (j + 1))
            val magicDamage = Integer.min(200, Random.nextInt(30, 60) * (j + 1))
            val newWeapon: Weapon = when (k) {
                0 -> Axe("$prefix Axe", damage, weight)
                1 -> Bow("$prefix Bow", damage, weight)
                2 -> Knife("$prefix Knife", damage, weight)
                3 -> Sword("$prefix Sword", damage, weight)
                else -> Staff("$prefix Staff", damage, weight, magicDamage)
            }
            PlayerCharacter.addWeaponToInventory(newWeapon)
            toEnemyGeneration()
        } else {
            return
        }
    }
}

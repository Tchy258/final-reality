package cl.uchile.dcc.finalreality.exceptions

import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.player.PlayerCharacter

/**
 * This error is used to represent an illegal action,
 * it's shown when a character tries to join the turn queue
 * without a weapon equipped
 *
 * @constructor Creates a new [NoWeaponEquippedException] with the [PlayerCharacter] who
 * tried to enqueue itself unarmed.
 *
 * @author <a href="https://github.com/Tchy258">Tchy258</a>
 */

class NoWeaponEquippedException(aCharacter: PlayerCharacter) :
    Exception("Attempted to join turn queue unarmed. ${(aCharacter as GameCharacter).name} has no weapon equipped")

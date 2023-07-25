package cl.uchile.dcc.finalreality.exceptions

/**
 * This error is raised when a WeaponEquipState tries to transition
 * to the wrong character state (e.g. to NonMagicalTurnState when a mage is equipping weapons)
 *
 * @constructor Creates a new [InconsistentReturnStateException] with the state
 *
 * @author <a href="https://github.com/Tchy258">Tchy258</a>
 */

class InconsistentReturnStateException(actualState: String, expectedState: String) :
    Exception("Tried to transition from WeaponEquipState to $actualState but $expectedState was expected")

package cl.uchile.dcc.finalreality.exceptions

/**
 * This error is raised when an action that shouldn't be possible on a particular
 * state is attempted
 *
 * @constructor Creates a new [IllegalStateActionException] with the attempted action
 * and the actual game state that doesn't support said action
 *
 * @author <a href="https://github.com/Tchy258">Tchy258</a>
 */
class IllegalStateActionException(action: String, state: String) :
    Exception("Tried to $action while on the $state")

package cl.uchile.dcc.finalreality.exceptions

/**
 * This error is raised when a non-existent transition between states
 * is attempted
 *
 * @constructor Creates a new [InvalidStateTransitionException] with the
 * state attempting the transition and the state it's trying to transition to
 *
 * @author <a href="https://github.com/Tchy258">Tchy258</a>
 */
class InvalidStateTransitionException(from: String, to: String) :
    Exception("Invalid state transition from $from to $to")

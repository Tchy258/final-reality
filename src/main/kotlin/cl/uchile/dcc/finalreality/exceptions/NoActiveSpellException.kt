package cl.uchile.dcc.finalreality.exceptions

/**
 * This error is raised when a mage tries to cast a spell without
 * setting a spell first
 *
 * @constructor Creates a new [NoActiveSpellException] with the name of
 * the mage that had no active spell
 *
 * @author <a href="https://github.com/Tchy258">Tchy258</a>
 */
class NoActiveSpellException(mageName: String) :
    Exception("The mage $mageName tried to cast a spell with no active spell")

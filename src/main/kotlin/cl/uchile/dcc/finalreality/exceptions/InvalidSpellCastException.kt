/*
 * "Final Reality" (c) by R8V and Tchy258
 * "Final Reality" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.uchile.dcc.finalreality.exceptions

/**
 * This error is raised when a BlackMage tries to cast WhiteMagic and vice-versa
 *
 * @constructor Creates a new [InvalidSpellCastException] with the mage's type
 * and the spell that mage tried to cast
 * @author <a href="https://github.com/Tchy258">Tchy258</a>
 */
class InvalidSpellCastException(mage: String, spell: String) :
    Exception("A $mage tried to cast $spell, magic type mismatch")

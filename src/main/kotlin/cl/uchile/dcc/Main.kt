/*
 * "Final Reality" (c) by R8V and Tchy258
 * "Final Reality" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <http://creativecommons.org/licenses/by/4.0/>.
 */
package cl.uchile.dcc

import cl.uchile.dcc.finalreality.exceptions.InvalidStatValueException
import cl.uchile.dcc.finalreality.exceptions.InvalidWeaponException
import cl.uchile.dcc.finalreality.exceptions.NoWeaponEquippedException
import cl.uchile.dcc.finalreality.model.character.Enemy
import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.player.BlackMage
import cl.uchile.dcc.finalreality.model.character.player.Engineer
import cl.uchile.dcc.finalreality.model.character.player.Knight
import cl.uchile.dcc.finalreality.model.character.player.PlayerCharacter
import cl.uchile.dcc.finalreality.model.character.player.Thief
import cl.uchile.dcc.finalreality.model.character.player.WhiteMage
import cl.uchile.dcc.finalreality.model.character.player.weapon.Axe
import cl.uchile.dcc.finalreality.model.character.player.weapon.Bow
import cl.uchile.dcc.finalreality.model.character.player.weapon.Knife
import cl.uchile.dcc.finalreality.model.character.player.weapon.Staff
import cl.uchile.dcc.finalreality.model.character.player.weapon.Sword
import java.lang.Long.max
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.PriorityBlockingQueue
import kotlin.random.Random

fun main() {
    // ---------------------------------Tests-------------------------------
    val exampleQueue = LinkedBlockingQueue<GameCharacter>()
    // ------------------------Character creation---------------------------
    println("Testing GameCharacter implementations")
    // -------------------------Knight creation-----------------------------

    val exampleKnight = Knight("ExampleKnight", Random.nextInt(10, 20), Random.nextInt(12, 18), exampleQueue)
    val sameKnight = Knight("ExampleKnight", exampleKnight.maxHp, exampleKnight.defense, exampleQueue)
    val differentKnight = Knight("ExampleKnight2", Random.nextInt(10, 20), Random.nextInt(12, 18), exampleQueue)

    // -------------------------Thief creation-------------------------------

    val exampleThief = Thief("ExampleThief", Random.nextInt(10, 15), Random.nextInt(9, 12), exampleQueue)
    val sameThief = Thief("ExampleThief", exampleThief.maxHp, exampleThief.defense, exampleQueue)
    val differentThief = Thief("ExampleThief2", Random.nextInt(10, 15), Random.nextInt(9, 12), exampleQueue)

    // -------------------------Engineer creation-------------------------------

    val exampleEngineer = Engineer("ExampleEngineer", Random.nextInt(10, 18), Random.nextInt(10, 13), exampleQueue)
    val sameEngineer = Engineer("ExampleEngineer", exampleEngineer.maxHp, exampleEngineer.defense, exampleQueue)
    val differentEngineer = Engineer("ExampleEngineer2", Random.nextInt(10, 18), Random.nextInt(10, 13), exampleQueue)

    // -------------------------WhiteMage creation-------------------------------

    val exampleWhiteMage = WhiteMage("ExampleWhiteMage", Random.nextInt(10, 15), Random.nextInt(10, 15), Random.nextInt(6, 10), exampleQueue)
    val sameWhiteMage = WhiteMage("ExampleWhiteMage", exampleWhiteMage.maxHp, exampleWhiteMage.maxMp, exampleWhiteMage.defense, exampleQueue)
    val differentWhiteMage = WhiteMage("ExampleWhiteMage2", Random.nextInt(10, 15), Random.nextInt(10, 15), Random.nextInt(6, 10), exampleQueue)

    // -------------------------BlackMage creation-------------------------------

    val exampleBlackMage = BlackMage("ExampleBlackMage", Random.nextInt(10, 15), Random.nextInt(10, 15), Random.nextInt(6, 10), exampleQueue)
    val sameBlackMage = BlackMage("ExampleBlackMage", exampleBlackMage.maxHp, exampleBlackMage.maxMp, exampleBlackMage.defense, exampleQueue)
    val differentBlackMage = BlackMage("ExampleBlackMage2", Random.nextInt(10, 15), Random.nextInt(10, 15), Random.nextInt(6, 10), exampleQueue)

    // -------------------------Enemy creation-------------------------------

    val exampleEnemy = Enemy("ExampleEnemy", Random.nextInt(1, 10), Random.nextInt(10, 50), Random.nextInt(10, 20), Random.nextInt(5, 15), exampleQueue)
    val sameEnemy = Enemy("ExampleEnemy", exampleEnemy.damage, exampleEnemy.weight, exampleEnemy.maxHp, exampleEnemy.defense, exampleQueue)
    val differentEnemy = Enemy("ExampleEnemy2", Random.nextInt(1, 10), Random.nextInt(10, 50), Random.nextInt(10, 20), Random.nextInt(5, 15), exampleQueue)

    println()
    // ------------------------Testing equals method--------------------------
    println("Testing equals() method: ")
    println()
    // ------------------------Knights----------------------------------------
    println("Knight:")
    println()

    print("A knight and itself")
    assert(exampleKnight == exampleKnight)
    println(exampleKnight == exampleKnight)
    print("Two knights that are the same: ")
    assert(exampleKnight == sameKnight)
    assert(sameKnight == exampleKnight)
    println(exampleKnight == sameKnight)

    print("Two different knights: ")
    assert(exampleKnight != differentKnight)
    assert(differentKnight != exampleKnight)
    println(exampleKnight == differentKnight)

    print("A knight and a thief: ")
    assert(!exampleKnight.equals(exampleThief))
    assert(!exampleThief.equals(exampleKnight))
    println(exampleKnight.equals(exampleThief))

    print("A knight and an engineer: ")
    assert(!exampleKnight.equals(exampleEngineer))
    assert(!exampleEngineer.equals(exampleKnight))
    println(exampleKnight.equals(exampleEngineer))

    print("A knight and a white mage: ")
    assert(!exampleKnight.equals(exampleWhiteMage))
    assert(!exampleWhiteMage.equals(exampleKnight))
    println(exampleKnight.equals(exampleWhiteMage))

    print("A knight and a black mage: ")
    assert(!exampleKnight.equals(exampleBlackMage))
    assert(!exampleBlackMage.equals(exampleKnight))
    println(exampleKnight.equals(exampleBlackMage))

    print("A knight and an enemy: ")
    assert(!exampleKnight.equals(exampleEnemy))
    assert(!exampleEnemy.equals(exampleKnight))
    println(exampleKnight.equals(exampleEnemy))

    println()
    Thread.sleep(3000)
    // ---------------------------------Engineers---------------------------------------
    println("Engineer:")
    println()

    print("Two engineers that are the same: ")
    assert(exampleEngineer == sameEngineer)
    assert(sameEngineer == exampleEngineer)
    println(exampleEngineer == sameEngineer)

    print("Two different engineers: ")
    assert(exampleEngineer != differentEngineer)
    assert(differentEngineer != exampleEngineer)
    println(exampleEngineer == differentEngineer)

    print("An engineer and a thief: ")
    assert(!exampleEngineer.equals(exampleThief))
    assert(!exampleThief.equals(exampleEngineer))
    println(exampleEngineer.equals(exampleThief))

    print("An engineer and a white mage: ")
    assert(!exampleEngineer.equals(exampleWhiteMage))
    assert(!exampleWhiteMage.equals(exampleEngineer))
    println(exampleEngineer.equals(exampleWhiteMage))

    print("An engineer and a black mage: ")
    assert(!exampleEngineer.equals(exampleBlackMage))
    assert(!exampleBlackMage.equals(exampleEngineer))
    println(exampleEngineer.equals(exampleBlackMage))

    print("An engineer and an enemy: ")
    assert(!exampleEngineer.equals(exampleEnemy))
    assert(!exampleEnemy.equals(exampleEngineer))
    println(exampleEngineer.equals(exampleEnemy))

    println()
    Thread.sleep(3000)
    // --------------------------------Thief----------------------------------
    println("Thief:")
    println()

    print("Two thieves that are the same: ")
    assert(exampleThief == sameThief)
    assert(sameThief == exampleThief)
    println(exampleThief == sameThief)

    print("Two different thieves: ")
    assert(exampleThief != differentThief)
    assert(differentThief != exampleThief)
    println(exampleThief == differentThief)

    print("A thief and a white mage: ")
    assert(!exampleThief.equals(exampleWhiteMage))
    assert(!exampleWhiteMage.equals(exampleThief))
    println(exampleThief.equals(exampleWhiteMage))

    print("A thief and a black mage: ")
    assert(!exampleThief.equals(exampleBlackMage))
    assert(!exampleBlackMage.equals(exampleThief))
    println(exampleThief.equals(exampleBlackMage))

    print("A thief and an enemy: ")
    assert(!exampleThief.equals(exampleEnemy))
    assert(!exampleEnemy.equals(exampleThief))
    println(exampleThief.equals(exampleEnemy))

    println()
    Thread.sleep(3000)
    // ---------------------------------White Mage----------------------------------------------
    print("White Mage:")
    println()

    print("Two white mages that are the same: ")
    assert(exampleWhiteMage == sameWhiteMage)
    assert(sameWhiteMage == exampleWhiteMage)
    println(exampleWhiteMage == sameWhiteMage)

    print("Two different white mages: ")
    assert(exampleWhiteMage != differentWhiteMage)
    assert(differentWhiteMage != exampleWhiteMage)
    println(exampleWhiteMage == differentWhiteMage)

    print("A whiteMage and a black mage: ")
    assert(!exampleWhiteMage.equals(exampleBlackMage))
    assert(!exampleBlackMage.equals(exampleWhiteMage))
    println(exampleWhiteMage.equals(exampleBlackMage))

    print("A white mage and an enemy: ")
    assert(!exampleWhiteMage.equals(exampleEnemy))
    assert(!exampleEnemy.equals(exampleWhiteMage))
    println(exampleWhiteMage.equals(exampleEnemy))

    println()
    Thread.sleep(3000)
    // --------------------------------Black Mage------------------------------------------------
    println("Black Mage:")
    println()

    print("Two black mages that are the same: ")
    assert(exampleBlackMage == sameBlackMage)
    assert(sameBlackMage == exampleBlackMage)
    println(exampleBlackMage == sameBlackMage)

    print("Two different black mages: ")
    assert(exampleBlackMage != differentBlackMage)
    assert(differentBlackMage != exampleBlackMage)
    println(exampleBlackMage == differentBlackMage)

    print("A black mage and an enemy: ")
    assert(!exampleBlackMage.equals(exampleEnemy))
    assert(!exampleEnemy.equals(exampleBlackMage))
    println(exampleBlackMage.equals(exampleEnemy))

    println()
    Thread.sleep(3000)
    // -------------------------------Enemy-------------------------------------------------------
    println("Enemy: ")
    println()

    print("Two enemies that are the same: ")
    assert(exampleEnemy == sameEnemy)
    assert(sameEnemy == exampleEnemy)
    println(exampleEnemy == sameEnemy)

    print("Two different enemies: ")
    assert(exampleEnemy != differentEnemy)
    assert(differentEnemy != exampleEnemy)
    println(exampleEnemy == differentEnemy)

    println()
    println()
    Thread.sleep(3000)
    // -----------------------------------------Weapon equipping tests-----------------------------
    val exampleAxe = Axe("ExampleAxe", Random.nextInt(4, 18), Random.nextInt(10, 45))
    val exampleBow = Bow("ExampleBow", Random.nextInt(3, 12), Random.nextInt(10, 30))
    val exampleKnife = Knife("ExampleKnife", Random.nextInt(2, 10), Random.nextInt(10, 20))
    val exampleStaff = Staff("ExampleStaff", Random.nextInt(1, 8), Random.nextInt(6, 25), Random.nextInt(4, 15))
    val exampleSword = Sword("ExampleSword", Random.nextInt(4, 15), Random.nextInt(10, 40))
    // ---------------------------------------Knight tests-----------------------------------------
    println("Knight tries to equip weapons... ")
    println("Equipping Axe, then Knife and finally Sword")
    exampleKnight.equip(exampleAxe)
    println("Equipped Axe: ${exampleKnight.equippedWeapon}")
    exampleKnight.equip(exampleKnife)
    println("Equipped Knife: ${exampleKnight.equippedWeapon}")
    println("Equipped Sword: ${exampleKnight.equippedWeapon}")
    println()
    Thread.sleep(3000)
    // ---------------------------------------Engineer tests---------------------------------------
    println("Engineer tries to equip weapons... ")
    println("Equipping Axe and then Bow")
    exampleEngineer.equip(exampleAxe)
    println("Equipped Axe: ${exampleEngineer.equippedWeapon}")
    exampleEngineer.equip(exampleBow)
    println("Equipped Bow: ${exampleEngineer.equippedWeapon}")
    println()
    Thread.sleep(3000)
    // -----------------------------------------Thief tests--------------------------------
    println("Thief tries to equip weapons... ")
    println("Equipping Bow, then Knife and finally Sword")
    exampleThief.equip(exampleBow)
    println("Equipped Bow: ${exampleThief.equippedWeapon}")
    exampleThief.equip(exampleKnife)
    println("Equipped Knife: ${exampleThief.equippedWeapon}")
    exampleThief.equip(exampleSword)
    println("Equipped Sword: ${exampleThief.equippedWeapon}")
    println()
    Thread.sleep(3000)
    // -----------------------------------------WhiteMage tests------------------------------
    println("WhiteMage tries to equip a staff... ")
    exampleWhiteMage.equip(exampleStaff)
    println("Equipped Staff: ${exampleWhiteMage.equippedWeapon} ")
    println()
    Thread.sleep(3000)
    // -----------------------------------------BlackMage tests-------------------------------
    println("BlackMage tries to equip weapons... ")
    exampleBlackMage.equip(exampleKnife)
    println("Equipped Knife: ${exampleBlackMage.equippedWeapon} ")
    exampleBlackMage.equip(exampleStaff)
    println("Equipped Staff: ${exampleBlackMage.equippedWeapon} ")
    println()
    Thread.sleep(3000)
    // ----------------------------------Exception handling------------------------------------
    println("Trying to generate a character with invalid stats")
    try {
        val invalidCharacter = Knight("Nope", -1, -1, exampleQueue)
    } catch (e: InvalidStatValueException) {
        println("Exception caught, character not created")
    }
    println()
    println("Trying to an equip an invalid weapon to a knight")
    println("Knight's current weapon: ${exampleKnight.equippedWeapon}")
    println("Unusable weapon: $exampleStaff")
    try {
        exampleKnight.equip(exampleStaff)
    } catch (e: InvalidWeaponException) {
        println("Exception caught, weapon not equipped")
    }
    println("Weapon after catching exception: ${exampleKnight.equippedWeapon}")
    println()
    println("Trying to add to the queue an unarmed character")
    try {
        sameKnight.waitTurn()
    } catch (e: NoWeaponEquippedException) {
        println("Exception caught, knight not on turn queue")
        println("Queue size: ${exampleQueue.size}")
    }
    println()
    Thread.sleep(3000)
    // ----------------------------------Turns and toString() tests with examples-----------------

    // The first queue holds the actual turn queue
    // The second queue holds the weights of the respective weapons or enemies to determine the wait time
    val queue = LinkedBlockingQueue<GameCharacter>()
    val turnOrder = PriorityBlockingQueue<Long>()
    println("Beginning character generation for turn examples...")
    println()

    // ------------------------------------Weapon Generation---------------------------------------
    // Gives a random speed to each character to generate different waiting times
    // Characters of each class will be generated

    val bartzWeapon = Sword("Excalipoor", Random.nextInt(1, 30), Random.nextInt(5, 60))
    val cidWeapon = Axe("Gigant Axe", Random.nextInt(1, 30), Random.nextInt(8, 70))
    val viviWeapon = Knife("Mage Masher", Random.nextInt(1, 20), Random.nextInt(3, 50))
    val onionKnightWeapon = Bow("Yoichi Bow", Random.nextInt(1, 20), Random.nextInt(2, 60))
    val minwuWeapon = Staff("Stardust Rod", Random.nextInt(1, 10), Random.nextInt(2, 60), Random.nextInt(4, 40))

    // ------------------------------------Character Generation------------------------------------
    val knight = Knight("Bartz Klauser", 20, 12, queue)
    val engineer = Engineer("Cid Pollendina", 15, 10, queue)
    val thief = Thief("Onion Knight", 10, 10, queue)
    val blackMage = BlackMage("Vivi", 10, 10, 5, queue)
    val whiteMage = WhiteMage("Minwu", 10, 10, 5, queue)
    // -------------------------------------Weapon equips and turn waits---------------------------
    knight.equip(bartzWeapon)
    knight.waitTurn()
    turnOrder.add(knight.equippedWeapon.weight.toLong())

    engineer.equip(cidWeapon)
    engineer.waitTurn()
    turnOrder.add(engineer.equippedWeapon.weight.toLong())

    thief.equip(onionKnightWeapon)
    thief.waitTurn()
    turnOrder.add(thief.equippedWeapon.weight.toLong())

    blackMage.equip(viviWeapon)
    blackMage.waitTurn()
    turnOrder.add(blackMage.equippedWeapon.weight.toLong())

    whiteMage.equip(minwuWeapon)
    whiteMage.waitTurn()
    turnOrder.add(whiteMage.equippedWeapon.weight.toLong())

    println()
    for (j in 0 until 6) {
        // Six random enemies are generated
        val enemy = Enemy("Enemy$j", Random.nextInt(1, 10), Random.nextInt(10, 60), Random.nextInt(10, 20), 5, queue)
        enemy.waitTurn()
        turnOrder.add(enemy.weight.toLong())
    }
    var previousTime: Long = 0
    var turnNumber = 1

    println()
    println("Turn count starting...")
    println()

    while (!turnOrder.isEmpty()) {
        val characterWeight: Long = turnOrder.poll()
        // Since the speed of a character is its weight (or weapon's weight) divided by 10 in seconds,
        // then the time each character has to wait will be their weight multiplied by 100 milliseconds
        // minus the time that has "already been waited" (by other characters) and an extra 100 ms to
        // have a margin of error
        val waitingTime: Long = max((characterWeight * 100) - previousTime, 0) + 100
        previousTime += waitingTime
        Thread.sleep(waitingTime)
        println("Turn $turnNumber")
        turnNumber++
        // Pops and prints the names of the characters of the queue to illustrate the turns
        // order
        val queueFront: GameCharacter = queue.poll()
        println(queueFront)
        if (queueFront is PlayerCharacter) {
            println(queueFront.equippedWeapon)
        }
        println()
    }
}

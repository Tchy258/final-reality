package cl.uchile.dcc

import cl.uchile.dcc.finalreality.model.character.Enemy
import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.player.BlackMage
import cl.uchile.dcc.finalreality.model.character.player.Engineer
import cl.uchile.dcc.finalreality.model.character.player.Knight
import cl.uchile.dcc.finalreality.model.character.player.PlayerCharacter
import cl.uchile.dcc.finalreality.model.character.player.Thief
import cl.uchile.dcc.finalreality.model.character.player.WhiteMage
import cl.uchile.dcc.finalreality.model.weapon.Axe
import cl.uchile.dcc.finalreality.model.weapon.Bow
import cl.uchile.dcc.finalreality.model.weapon.Knife
import cl.uchile.dcc.finalreality.model.weapon.Staff
import cl.uchile.dcc.finalreality.model.weapon.Sword
import java.lang.Long.max
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.PriorityBlockingQueue
import kotlin.random.Random

fun main() {
    // ---------------------------------Tests-------------------------------
    val exampleQueue = LinkedBlockingQueue<GameCharacter>()
    // ------------------------Character creation---------------------------
    println("Testing GameCharacter implementations")
    val exampleKnight = Knight("ExampleKnight", Random.nextInt(10, 20), Random.nextInt(12, 18), exampleQueue)
    val sameKnight = Knight("ExampleKnight", exampleKnight.maxHp, exampleKnight.defense, exampleQueue)
    val differentKnight = Knight("ExampleKnight2", Random.nextInt(10, 20), Random.nextInt(12, 18), exampleQueue)
    val exampleThief = Thief("ExampleThief", Random.nextInt(10, 15), Random.nextInt(9, 12), exampleQueue)
    val sameThief = Thief("ExampleThief", exampleThief.maxHp, exampleThief.defense, exampleQueue)
    val differentThief = Thief("ExampleThief2", Random.nextInt(10, 15), Random.nextInt(9, 12), exampleQueue)
    val exampleEngineer = Engineer("ExampleEngineer", Random.nextInt(10, 18), Random.nextInt(10, 13), exampleQueue)
    val sameEngineer = Engineer("ExampleEngineer", exampleEngineer.maxHp, exampleEngineer.defense, exampleQueue)
    val differentEngineer = Engineer("ExampleEngineer2", Random.nextInt(10, 18), Random.nextInt(10, 13), exampleQueue)
    val exampleWhiteMage = WhiteMage("ExampleWhiteMage", Random.nextInt(10, 15), Random.nextInt(10, 15), Random.nextInt(6, 10), exampleQueue)
    val sameWhiteMage = WhiteMage("ExampleWhiteMage", exampleWhiteMage.maxHp, exampleWhiteMage.maxMp, exampleWhiteMage.defense, exampleQueue)
    val differentWhiteMage = WhiteMage("ExampleWhiteMage2", Random.nextInt(10, 15), Random.nextInt(10, 15), Random.nextInt(6, 10), exampleQueue)
    val exampleBlackMage = BlackMage("ExampleBlackMage", Random.nextInt(10, 15), Random.nextInt(10, 15), Random.nextInt(6, 10), exampleQueue)
    val sameBlackMage = BlackMage("ExampleBlackMage", exampleBlackMage.maxHp, exampleBlackMage.maxMp, exampleBlackMage.defense, exampleQueue)
    val differentBlackMage = BlackMage("ExampleBlackMage2", Random.nextInt(10, 15), Random.nextInt(10, 15), Random.nextInt(6, 10), exampleQueue)
    val exampleEnemy = Enemy("ExampleEnemy", Random.nextInt(1, 10), Random.nextInt(10, 50), Random.nextInt(10, 20), Random.nextInt(5, 15), exampleQueue)
    val sameEnemy = Enemy("ExampleEnemy", exampleEnemy.damage, exampleEnemy.weight, exampleEnemy.maxHp, exampleEnemy.defense, exampleQueue)
    val differentEnemy = Enemy("ExampleEnemy2", Random.nextInt(1, 10), Random.nextInt(10, 50), Random.nextInt(10, 20), Random.nextInt(5, 15), exampleQueue)
    println()
    // ------------------------Testing equals method--------------------------
    println("Testing equals() method: ")
    println()
    // ------------------------Knights----------------------------------------
    print("Two knights that are the same: ")
    assert(exampleKnight == sameKnight)
    assert(sameKnight == exampleKnight)
    println(exampleKnight == sameKnight)
    println()
    print("Two different knights: ")
    assert(exampleKnight != differentKnight)
    assert(differentKnight != exampleKnight)
    println(exampleKnight == differentKnight)
    println()
    print("A knight and a thief: ")
    assert(!exampleKnight.equals(exampleThief))
    assert(!exampleThief.equals(exampleKnight))
    println(exampleKnight.equals(exampleThief))
    println()
    print("A knight and an engineer: ")
    assert(!exampleKnight.equals(exampleEngineer))
    assert(!exampleEngineer.equals(exampleKnight))
    println(exampleKnight.equals(exampleEngineer))
    println()
    print("A knight and a white mage: ")
    assert(!exampleKnight.equals(exampleWhiteMage))
    assert(!exampleWhiteMage.equals(exampleKnight))
    println(exampleKnight.equals(exampleWhiteMage))
    println()
    print("A knight and a black mage: ")
    assert(!exampleKnight.equals(exampleBlackMage))
    assert(!exampleBlackMage.equals(exampleKnight))
    println(exampleKnight.equals(exampleBlackMage))
    println()
    print("A knight and an enemy: ")
    assert(!exampleKnight.equals(exampleEnemy))
    assert(!exampleEnemy.equals(exampleKnight))
    println(exampleKnight.equals(exampleEnemy))
    println()
    // ---------------------------------Engineers---------------------------------------
    print("Two engineers that are the same: ")
    assert(exampleEngineer == sameEngineer)
    assert(sameEngineer == exampleEngineer)
    println(exampleEngineer == sameEngineer)
    println()
    print("Two different engineers: ")
    assert(exampleEngineer != differentEngineer)
    assert(differentEngineer != exampleEngineer)
    println(exampleEngineer == differentEngineer)
    println()
    print("An engineer and a thief: ")
    assert(!exampleEngineer.equals(exampleThief))
    assert(!exampleThief.equals(exampleEngineer))
    println(exampleEngineer.equals(exampleThief))
    println()
    print("An engineer and a white mage: ")
    assert(!exampleEngineer.equals(exampleWhiteMage))
    assert(!exampleWhiteMage.equals(exampleEngineer))
    println(exampleEngineer.equals(exampleWhiteMage))
    println()
    print("An engineer and a black mage: ")
    assert(!exampleEngineer.equals(exampleBlackMage))
    assert(!exampleBlackMage.equals(exampleEngineer))
    println(exampleEngineer.equals(exampleBlackMage))
    println()
    print("An engineer and an enemy: ")
    assert(!exampleEngineer.equals(exampleEnemy))
    assert(!exampleEnemy.equals(exampleEngineer))
    println(exampleEngineer.equals(exampleEnemy))
    println()
    // --------------------------------Thief----------------------------------
    print("Two thieves that are the same: ")
    assert(exampleThief == sameThief)
    assert(sameThief == exampleThief)
    println(exampleThief == sameThief)
    println()
    print("Two different thieves: ")
    assert(exampleThief != differentThief)
    assert(differentThief != exampleThief)
    println(exampleThief == differentThief)
    println()
    print("A thief and a white mage: ")
    assert(!exampleThief.equals(exampleWhiteMage))
    assert(!exampleWhiteMage.equals(exampleThief))
    println(exampleThief.equals(exampleWhiteMage))
    println()
    print("A thief and a black mage: ")
    assert(!exampleThief.equals(exampleBlackMage))
    assert(!exampleBlackMage.equals(exampleThief))
    println(exampleThief.equals(exampleBlackMage))
    println()
    print("A thief and an enemy: ")
    assert(!exampleThief.equals(exampleEnemy))
    assert(!exampleEnemy.equals(exampleThief))
    println(exampleThief.equals(exampleEnemy))
    println()
    // ---------------------------------White Mage----------------------------------------------
    print("Two white mages that are the same: ")
    assert(exampleWhiteMage == sameWhiteMage)
    assert(sameWhiteMage == exampleWhiteMage)
    println(exampleWhiteMage == sameWhiteMage)
    println()
    print("Two different white mages: ")
    assert(exampleWhiteMage != differentWhiteMage)
    assert(differentWhiteMage != exampleWhiteMage)
    println(exampleWhiteMage == differentWhiteMage)
    println()
    print("A whiteMage and a black mage: ")
    assert(!exampleWhiteMage.equals(exampleBlackMage))
    assert(!exampleBlackMage.equals(exampleWhiteMage))
    println(exampleWhiteMage.equals(exampleBlackMage))
    println()
    print("A white mage and an enemy: ")
    assert(!exampleWhiteMage.equals(exampleEnemy))
    assert(!exampleEnemy.equals(exampleWhiteMage))
    println(exampleWhiteMage.equals(exampleEnemy))
    println()
    // --------------------------------Black Mage------------------------------------------------
    print("Two black mages that are the same: ")
    assert(exampleBlackMage == sameBlackMage)
    assert(sameBlackMage == exampleBlackMage)
    println(exampleBlackMage == sameBlackMage)
    println()
    print("Two different black mages: ")
    assert(exampleBlackMage != differentBlackMage)
    assert(differentBlackMage != exampleBlackMage)
    println(exampleBlackMage == differentBlackMage)
    println()
    print("A black mage and an enemy: ")
    assert(!exampleBlackMage.equals(exampleEnemy))
    assert(!exampleEnemy.equals(exampleBlackMage))
    println(exampleBlackMage.equals(exampleEnemy))
    println()
    // -------------------------------Enemy-------------------------------------------------------
    print("Two enemies that are the same: ")
    assert(exampleEnemy == sameEnemy)
    assert(sameEnemy == exampleEnemy)
    println(exampleEnemy == sameEnemy)
    println()
    print("Two different enemies: ")
    assert(exampleEnemy != differentEnemy)
    assert(differentEnemy != exampleEnemy)
    println(exampleEnemy == differentEnemy)
    println()
    println()
    // -----------------------------------------Weapon equipping tests-----------------------------
    val exampleAxe = Axe("ExampleAxe", Random.nextInt(4, 18), Random.nextInt(10, 45))
    val exampleBow = Bow("ExampleBow", Random.nextInt(3, 12), Random.nextInt(10, 30))
    val exampleKnife = Knife("ExampleKnife", Random.nextInt(2, 10), Random.nextInt(10, 20))
    val exampleStaff = Staff("ExampleStaff", Random.nextInt(1, 8), Random.nextInt(6, 25), Random.nextInt(4, 15))
    val exampleSword = Sword("ExampleSword", Random.nextInt(4, 15), Random.nextInt(10, 40))
    println("Knight tries to equip weapons: ")
    assert(exampleKnight.equip(exampleAxe))
    println("Axe: " + exampleKnight.equip(exampleAxe))
    assert(!exampleKnight.equip(exampleBow))
    println("Bow: " + exampleKnight.equip(exampleBow))
    assert(exampleKnight.equip(exampleKnife))
    println("Knife: " + exampleKnight.equip(exampleKnife))
    assert(!exampleKnight.equip(exampleStaff))
    println("Staff: " + exampleKnight.equip(exampleStaff))
    assert(exampleKnight.equip(exampleSword))
    println("Sword: " + exampleKnight.equip(exampleSword))
    println()
    println("Engineer tries to equip weapons: ")
    assert(exampleEngineer.equip(exampleAxe))
    println("Axe: " + exampleEngineer.equip(exampleAxe))
    assert(exampleEngineer.equip(exampleBow))
    println("Bow: " + exampleEngineer.equip(exampleBow))
    assert(!exampleEngineer.equip(exampleKnife))
    println("Knife: " + exampleEngineer.equip(exampleKnife))
    assert(!exampleEngineer.equip(exampleStaff))
    println("Staff: " + exampleEngineer.equip(exampleStaff))
    assert(!exampleEngineer.equip(exampleSword))
    println("Sword: " + exampleEngineer.equip(exampleSword))
    println()
    println("Thief tries to equip weapons: ")
    assert(!exampleThief.equip(exampleAxe))
    println("Axe: " + exampleThief.equip(exampleAxe))
    assert(exampleThief.equip(exampleBow))
    println("Bow: " + exampleThief.equip(exampleBow))
    assert(exampleThief.equip(exampleKnife))
    println("Knife: " + exampleThief.equip(exampleKnife))
    assert(!exampleThief.equip(exampleStaff))
    println("Staff: " + exampleThief.equip(exampleStaff))
    assert(exampleThief.equip(exampleSword))
    println("Sword: " + exampleThief.equip(exampleSword))
    println()
    println("WhiteMage tries to equip weapons: ")
    assert(!exampleWhiteMage.equip(exampleAxe))
    println("Axe: " + exampleWhiteMage.equip(exampleAxe))
    assert(!exampleWhiteMage.equip(exampleBow))
    println("Bow: " + exampleWhiteMage.equip(exampleBow))
    assert(!exampleWhiteMage.equip(exampleKnife))
    println("Knife: " + exampleWhiteMage.equip(exampleKnife))
    assert(exampleWhiteMage.equip(exampleStaff))
    println("Staff: " + exampleWhiteMage.equip(exampleStaff))
    assert(!exampleWhiteMage.equip(exampleSword))
    println("Sword: " + exampleWhiteMage.equip(exampleSword))
    println()
    println("BlackMage tries to equip weapons: ")
    assert(!exampleBlackMage.equip(exampleAxe))
    println("Axe: " + exampleBlackMage.equip(exampleAxe))
    assert(!exampleBlackMage.equip(exampleBow))
    println("Bow: " + exampleBlackMage.equip(exampleBow))
    assert(exampleBlackMage.equip(exampleKnife))
    println("Knife: " + exampleBlackMage.equip(exampleKnife))
    assert(exampleBlackMage.equip(exampleStaff))
    println("Staff: " + exampleBlackMage.equip(exampleStaff))
    assert(!exampleBlackMage.equip(exampleSword))
    println("Sword: " + exampleBlackMage.equip(exampleSword))
    println()
    // ----------------------------------Turns and toString() tests with examples-----------------
    // The first queue holds the actual turn queue
    // The second queue holds the weights of the respective weapons or enemies to determine the wait time
    val queue = LinkedBlockingQueue<GameCharacter>()
    val turnOrder = PriorityBlockingQueue<Long>()
    var i = 0
    while (i < 5) {
        // Gives a random speed to each character to generate different waiting times
        // Characters of each class will be generated with random weapons they might be able to use
        // until there's 5 characters in total
        val k: Double = Random.nextDouble()
        val weapon = if (k > 0.8) {
            Sword("RandomSword", Random.nextInt(1, 30), Random.nextInt(5, 60))
        } else if (k > 0.6) {
            Axe("RandomAxe", Random.nextInt(1, 30), Random.nextInt(8, 70))
        } else if (k > 0.4) {
            Knife("RandomKnife", Random.nextInt(1, 20), Random.nextInt(3, 50))
        } else if (k > 0.2) {
            Bow("RandomBow", Random.nextInt(1, 20), Random.nextInt(2, 60))
        } else {
            Staff("RandomStaff", Random.nextInt(1, 10), Random.nextInt(2, 60), Random.nextInt(4, 40))
        }
        val character = when (i) {
            0 -> Knight("Knight", 20, 12, queue)
            1 -> Engineer("Engineer", 15, 10, queue)
            2 -> Thief("Thief", 10, 10, queue)
            3 -> BlackMage("BlackMage", 10, 10, 5, queue)
            else -> WhiteMage("WhiteMage", 10, 10, 5, queue)
        }
        val validWeapon: Boolean = character.equip(weapon)
        character.waitTurn()
        if (validWeapon) {
            turnOrder.add(weapon.weight.toLong())
            i++
        }
    }
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
        // have margin of error
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

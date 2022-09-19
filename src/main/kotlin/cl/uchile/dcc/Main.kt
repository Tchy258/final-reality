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
        } else {
            println()
        }
    }
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

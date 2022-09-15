package cl.uchile.dcc

import cl.uchile.dcc.finalreality.model.character.Enemy
import cl.uchile.dcc.finalreality.model.character.GameCharacter
import cl.uchile.dcc.finalreality.model.character.player.AbstractPlayerCharacter
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
import cl.uchile.dcc.finalreality.model.weapon.Weapon
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.PriorityBlockingQueue
import kotlin.random.Random

fun main() {
    // The first queue holds the actual turn queue
    // The second queue holds the weights of the respective weapons or enemies to determine the wait time
    val queue = LinkedBlockingQueue<GameCharacter>()
    val turnOrder = PriorityBlockingQueue<Long>()
    for (i in 0 until 10) {
        // Gives a random speed to each character to generate different waiting times
        // Two characters of each class will be generated with random weapons they can use
        val weapon: Weapon
        val character: AbstractPlayerCharacter
        val k: Double = Random.nextDouble()
        when (i % 5) {
            0 -> {
                weapon = if (k > 0.6) {
                    Sword("ExampleSword$i", Random.nextInt(1, 30), Random.nextInt(5, 60))
                } else if (k > 0.3) {
                    Axe("ExampleAxe$i", Random.nextInt(1, 30), Random.nextInt(8, 70))
                } else {
                    Knife("ExampleKnife$i", Random.nextInt(1, 20), Random.nextInt(1, 50))
                }
                character = Knight("Knight$i", 20, 12, queue)
                character.equip(weapon)
                character.waitTurn()
                turnOrder.add(weapon.weight.toLong())
            }
            1 -> {
                weapon = if (k > 0.5) {
                    Axe("ExampleAxe$i", Random.nextInt(1, 30), Random.nextInt(8, 70))
                } else {
                    Bow("ExampleBow$i", Random.nextInt(1, 20), Random.nextInt(1, 50))
                }
                character = Engineer("Engineer$i", 15, 10, queue)
                character.equip(weapon)
                character.waitTurn()
                turnOrder.add(weapon.weight.toLong())
            }
            2 -> {
                weapon = if (k > 0.6) {
                    Knife("ExampleKnife$i", Random.nextInt(1, 20), Random.nextInt(1, 50))
                } else if (k > 0.3) {
                    Sword("ExampleSword$i", Random.nextInt(1, 30), Random.nextInt(5, 60))
                } else {
                    Bow("ExampleBow$i", Random.nextInt(1, 20), Random.nextInt(1, 50))
                }
                character = Thief("Thief$i", 10, 10, queue)
                character.equip(weapon)
                character.waitTurn()
                turnOrder.add(weapon.weight.toLong())
            }
            3 -> {
                weapon = if (k > 0.5) {
                    Staff("ExampleStaff$i", Random.nextInt(1, 10), Random.nextInt(1, 50), Random.nextInt(1, 20))
                } else {
                    Knife("ExampleKnife$i", Random.nextInt(1, 20), Random.nextInt(1, 50))
                }
                character = BlackMage("BlackMage$i", 10, 10, 5, queue)
                character.equip(weapon)
                character.waitTurn()
                turnOrder.add(weapon.weight.toLong())
            }
            4 -> {
                weapon = Staff("ExampleStaff$i", Random.nextInt(1, 10), Random.nextInt(1, 50), Random.nextInt(1, 20))
                character = WhiteMage("WhiteMage$i", 10, 10, 5, queue)
                character.equip(weapon)
                character.waitTurn()
                turnOrder.add(weapon.weight.toLong())
            }
        }
    }
    for (j in 0 until 5) {
        // Ten random enemies are generated
        val enemy = Enemy("Enemy$j", Random.nextInt(1, 10), Random.nextInt(10, 60), Random.nextInt(10, 20), 5, queue)
        enemy.waitTurn()
        turnOrder.add(enemy.weight.toLong())
    }
    var previousTime: Long = 0
    var turnNumber = 1
    while (!turnOrder.isEmpty()) {
        val characterWeight: Long = turnOrder.poll()
        // Since the speed of a character is its weight (or weapon's weight) divided by 10 in seconds,
        // then the time each character has to wait will be their weight multiplied by 100 milliseconds
        // minus the time that has "already been waited" (by other characters) and an extra 100 ms to
        // have margin of error
        val waitingTime: Long = (characterWeight * 100) - previousTime + 100
        previousTime += waitingTime
        println("Turn $turnNumber")
        turnNumber++
        Thread.sleep(waitingTime)
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

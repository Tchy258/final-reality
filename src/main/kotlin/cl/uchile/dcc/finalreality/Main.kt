package cl.uchile.dcc.finalreality

import cl.uchile.dcc.finalreality.controller.GameController
import cl.uchile.dcc.finalreality.exceptions.IllegalStateActionException
import java.util.InputMismatchException
import java.util.Scanner

// All the functions below encapsulate states of the game
/**
 * Function used to check the damage a character has received to display
 * a relevant message about it
 * @param controller the game controller
 * @param oldCharHp the hp of the characters before an enemy turn
 * @return the index of the damaged character and the amount of damage, or -1 if no character was attacked
 */
fun checkEnemyAttacks(controller: GameController, oldCharHp: List<Pair<Int, Int>>): Pair<Int, Int> {
    val charHp = controller.getCharacterHp()
    for (i in charHp.indices) {
        if (oldCharHp[i].first != charHp[i].first) {
            return Pair(i, (oldCharHp[i].first - charHp[i].first))
        }
    }
    return Pair(-1, -1)
}

/**
 * Function used to handle user input on player turns
 * @param controller the game controller
 * @param stdin the standard input
 * @return the result of the first value in an attack or spell cast action, or -1 if
 * the action could not be executed
 */
fun playerTurn(controller: GameController, stdin: Scanner): Int {
    println("Choose an action: ")
    val magician = try {
        controller.getMagicDamage()
        true
    } catch (_: IllegalStateActionException) {
        false
    }
    return if (magician) {
        magicianTurn(controller, stdin)
    } else {
        nonMagicianTurn(controller, stdin)
    }
}

/**
 * Function to handle input for non-magic character turns
 * @param controller the game controller
 * @param stdin the standard input
 * @return the result of the first value in an attack action, or -1 if
 * the action could not be executed
 */
fun nonMagicianTurn(controller: GameController, stdin: Scanner): Int {
    println("1 Attack")
    println("3 SwapWeapons")
    return when (actionSelect(stdin, false)) {
        1 -> attackTargetSelect(controller, stdin, false)
        3 -> weaponSwapping(controller, stdin)
        else -> throw InputMismatchException()
    }
}
/**
 * Function used to handle user input on mage player turns
 * @param controller the game controller
 * @param stdin the standard input
 * @return the result of the first value in an attack or spell cast action, or -1 if
 * the action could not be executed
 */
fun magicianTurn(controller: GameController, stdin: Scanner): Int {
    println("1 Attack")
    println("2 CastMagic")
    println("3 SwapWeapons")
    return when (actionSelect(stdin, true)) {
        1 -> attackTargetSelect(controller, stdin, true)
        2 -> spellCastSelect(controller, stdin)
        3 -> weaponSwapping(controller, stdin)
        else -> throw InputMismatchException()
    }
}

/**
 * Function to parse a player action
 * @param stdin the standard input
 * @param magician whether the player character is a magician or not
 * @return the chosen action
 */
fun actionSelect(stdin: Scanner, magician: Boolean): Int {
    var action = -1
    while (action == -1) {
        print("Please input an action: ")
        action = try {
            stdin.nextInt()
        } catch (e: InputMismatchException) {
            -1
        }
        if (!magician && action==2) action=-1
        if (action == -1) println("Action Invalid. Please input a valid action")
    }
    return action
}

/**
 * Function to handle weapon swaps
 * @param controller the game controller
 * @param stdin the standard input
 * @return the result of a future player action (this function returns to the
 * player turn function when it ends)
 */
fun weaponSwapping(controller: GameController, stdin: Scanner): Int {
    val inventory = controller.getInventory()
    println("Current inventory: ")
    var i = 1
    for ((name, damage, weight) in inventory) {
        println("$i Name: $name, Damage: $damage, Weight: $weight ")
        i++
    }
    var selection = -1
    println("Please choose a valid weapon to equip")
    while (selection == -1) {
        var equipped = false
        selection = try {
            stdin.nextInt()
        } catch (_: InputMismatchException) {
            println("Invalid option, please choose a valid weapon")
            -1
        }
        if (selection < 1 || selection > inventory.size) selection = -1
        else equipped = controller.equipWeapon(selection - 1)
        if (!equipped) {
            println("This character is unable to equip that weapon, choose another one")
            selection = -1
        }
    }
    return playerTurn(controller, stdin)
}

/**
 * Function used to display a side's available targets and hp when choosing a target
 * @param controller the game controller
 * @param stdin the standard input
 * @param enemies whether to target enemies or not
 * @return the chosen target index
 */
fun sideDisplay(controller: GameController, stdin: Scanner, enemies: Boolean): Int {
    val sideHp: List<Pair<Int, Int>>
    val side = if (enemies) {
        sideHp = controller.getEnemyHp()
        "Enemy"
    } else {
        sideHp = controller.getCharacterHp()
        "Ally"
    }
    for (i in sideHp.indices) {
        val (current, max) = sideHp[i]
        println("$side ${i + 1} Hp: $current/$max")
    }
    println("Input -1 to go back to action selection")
    return targetSelect(stdin, sideHp.size)
}

/**
 * Function to handle player attacks
 * @param controller the game controller
 * @param stdin the standard input
 * @param magician whether the character is a magician or not
 * @return the amount of damage dealt or -1 if attacking was not possible
 */
fun attackTargetSelect(controller: GameController, stdin: Scanner, magician: Boolean): Int {
    println("Please choose an enemy to attack: ")
    val target = sideDisplay(controller, stdin, true)
    return if (target == -2) {
        if (magician) {
            magicianTurn(controller, stdin)
        } else {
            nonMagicianTurn(controller, stdin)
        }
    } else {
        controller.attack(target)
    }
}

/**
 * Function used to handle target selection input
 * @param stdin the standard input
 * @param amountOfTargets the upper limit of target indexes
 * @return the chosen target
 */
fun targetSelect(stdin: Scanner, amountOfTargets: Int): Int {
    var target = 0
    while (target == 0) {
        try {
            target = stdin.nextInt()
            if ((target < 1 || target > amountOfTargets) && target != -1) throw InputMismatchException()
        } catch (e: InputMismatchException) {
            target = 0
        }
        if (target == 0) println("Target Invalid. Please input a valid target")
    }
    return target - 1
}

/**
 * Function used to handle magician spell casts
 * @param controller the game controller
 * @param stdin the standard input
 * @return the amount of damage the spell dealt or -1 if it couldn't be cast
 */
fun spellCastSelect(controller: GameController, stdin: Scanner): Int {
    val spells = controller.getAvailableSpells()
    val thisMpValue = controller.getCharacterMp()[controller.getCurrentCharacter()]
    println("CurrentMp: ${thisMpValue.first}/${thisMpValue.second}")
    println("Available Spells: ")
    var i = 1
    for ((name, cost, effect) in spells) {
        println("$i $name, cost: $cost, possible effect: $effect")
        i++
    }
    print("Please choose a spell (-1 to go back to action selection): ")
    var selection = 0
    while (selection == 0) {
        try {
            selection = stdin.nextInt()
            if ((selection < 1 || selection > i) && selection != -1) throw InputMismatchException()
        } catch (_: InputMismatchException) {
            selection = 0
        }
        if (selection == 0) print("Please choose a valid spell: ")
    }
    if (selection == -1) {
        return magicianTurn(controller, stdin)
    }
    selection--
    println("Target enemies (1) or allies (2)? (Input -1 to go back to action selection)")
    var side = 0
    while (side == 0) {
        try {
            side = stdin.nextInt()
            if (side < -1 || side == 0 || side > 2) throw InputMismatchException()
        } catch (_: InputMismatchException) {
            side = 0
        }
        if (side == 0) println("Please choose a valid target side")
    }
    if (side == -1) return magicianTurn(controller, stdin)
    val target = sideDisplay(controller, stdin, side == 1)
    return if (target == -2) {
        magicianTurn(controller, stdin)
    } else {
        val (dmg, debuff) = controller.useMagic(selection, target, side == 1)
        if (debuff != "NoDebuff") {
            println("Inflicted with the $debuff adverse effect!")
        }
        dmg
    }
}

fun main() {
    val controller = GameController()
    // This section is meant to be filled with some form of view
    // that will use the game controller's api, for now
    // the terminal will serve as a view
    var programFinished = false
    val stdin = Scanner(System.`in`)
    while (!programFinished) {
        val charNames = mutableListOf<String>()
        val enemyNames = controller.getEnemyNames()
        var turnCount = 1
        println("Starting game with ${controller.getEnemyNames().size} enemies")
        Thread.sleep(3000)
        println("Please create your characters with the format")
        println("<Class>")
        println("<Name>")
        println("E.g:")
        println("Engineer")
        println("Bob")
        Thread.sleep(3000)
        println("Available classes:")
        println("Engineer")
        println("Knight")
        println("Thief")
        println("BlackMage")
        println("WhiteMage")
        Thread.sleep(2000)
        println("Please create 5 characters")
        var amountOfCharacters = 0
        while (amountOfCharacters < 5) {
            print("Enter class: ")
            val charClass = stdin.next().uppercase()
            print("Enter name: ")
            val charName = stdin.next()
            var createSuccessful = true
            when (charClass) {
                "ENGINEER" -> controller.createEngineer(charName)
                "KNIGHT" -> controller.createKnight(charName)
                "THIEF" -> controller.createThief(charName)
                "BLACKMAGE" -> controller.createBlackMage(charName)
                "WHITEMAGE" -> controller.createWhiteMage(charName)
                else -> createSuccessful = false
            }
            if (createSuccessful) {
                amountOfCharacters++
                charNames.add(charName)
            } else {
                println("Please enter a valid class name")
            }
        }
        var gameFinished = false
        var playerLost = false
        println("Starting battle")
        Thread.sleep(1000)
        controller.startBattle()
        while (!playerLost) {
            while (!gameFinished) {
                val charHp = controller.getCharacterHp()
                val enemyHp = controller.getEnemyHp()
                println("Turn $turnCount")
                Thread.sleep(3000)
                println("Characters: ")
                for (name in charNames) {
                    print("$name  ")
                }
                print("\n")
                for ((curr, max) in charHp) {
                    print("Hp: $curr/$max ")
                }
                Thread.sleep(3000)
                print("\n")
                println("Debuffs: ")
                for (i in charNames.indices) {
                    val debuffs = controller.getAdverseEffects(i, false)
                    print("${charNames[i]}: ")
                    for (debuff in debuffs) {
                        print("$debuff ")
                    }
                    print("\n")
                }
                Thread.sleep(2000)
                println("Enemies: ")
                for (name in enemyNames) {
                    print("$name  ")
                }
                print("\n")
                for ((curr, max) in enemyHp) {
                    print("Hp: $curr/$max ")
                }
                print("\n")
                Thread.sleep(3000)
                println("Debuffs: ")
                for (i in enemyNames.indices) {
                    val debuffs = controller.getAdverseEffects(i, true)
                    print("${enemyNames[i]}: ")
                    for (debuff in debuffs) {
                        print("$debuff ")
                    }
                    print("\n")
                }
                Thread.sleep(2000)
                controller.nextTurn()
                val activeCharacter = controller.getCurrentCharacter()
                if (activeCharacter == -1) {
                    val (index, damage) = checkEnemyAttacks(controller, charHp)
                    if (index != -1) {
                        println("${charNames[index]} received $damage points of damage!")
                        Thread.sleep(3000)
                    }
                } else {
                    println("${charNames[activeCharacter]}'s turn!")
                    Thread.sleep(1000)
                    var result = playerTurn(controller, stdin)
                    while (result == -1) {
                        println("Action could not be performed")
                        println("Did you attack a dead enemy or cast a spell without enough mp?")
                        result = playerTurn(controller, stdin)
                    }
                    println("Last action did $result points of damage or healing")
                    Thread.sleep(3000)
                }
                turnCount++
                gameFinished = controller.isGameOver()
            }
            playerLost = !controller.isPlayerWinner()
            if (!playerLost) {
                println("The enemy party has been defeated!")
                println("Continue to the next battle? [Y/n]")
                val ans = stdin.next().uppercase()
                if (ans == "N") {
                    controller.onPlayerWin(false)
                    programFinished = true
                } else {
                    controller.onPlayerWin(true)
                }
            } else {
                println("Your party has been defeated...")
                println("Try again? [Y/n]")
                val ans = stdin.next().uppercase()
                if (ans == "N") {
                    controller.onEnemyWin(false)
                    programFinished = true
                } else {
                    controller.onEnemyWin(true)
                }
            }
        }
    }
}

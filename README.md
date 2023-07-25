Final Reality
=============

![http://creativecommons.org/licenses/by/4.0/](https://i.creativecommons.org/l/by/4.0/88x31.png)

This work is licensed under a
[Creative Commons Attribution 4.0 International License](http://creativecommons.org/licenses/by/4.0/)

Context
-------

This project's goal is to create a (simplified) clone of _Final Fantasy_'s combat, a game developed
by [_Square Enix_](https://www.square-enix.com)
Broadly speaking for the combat the player has a group of characters to control and a group of
enemies controlled by the computer.

---

Execution instructions
--------------

Run the Main.kt file located on *src>main>kotlin>Main.kt* on Intellij to try the game.

First revision
--------------

The base code had a few design flaws, the first of which was the way weapons were implemented, they were represented
by a single all encompassing weapon class to represent any generic weapon and an enum class to represent
a weapon's type. \
There's also multiple property based tests that ensure all the project's functionality.


With this implementation, each time a character wanted to equip a weapon it'd **need
to ask what kind of weapon the object actually is**, this clearly breaks the **dependency inversion principle**, weapon types are *concretions*, not *abstractions.*

To address this issue, each weapon got its own class and a common interface (an abstraction) through which
the weapon equips are handled by using the double dispatch pattern.

For now if a character tries to equip a weapon it shouldn't, an exception will be thrown. This will be fixed in the
future to completely prevent any attempts to equip invalid weapons in way that won't let the code compile if such a mistake is made in the code.

The reason double dispatch results in a more maintainable and extensible code is that it allows for easy
addition of new weapons or characters without modifying any existing code from the character and weapon class, whereas having to
ask for a weapon's type property would have required a modification to all equip methods for every character class doing the type checking.

Making new classes for each weapon also made more apparent a missing property as per the project's requirements, a *magicDamage* value on staves.


Another minor design flaw was the absence of a few basic *toString()* and *equals()* methods on some classes.

Finally, the way the turns were taken had a very specific issue, it was discarding decimals on integer 
division and the speeds of each character were computed incorrectly. This was changed to use milliseconds instead.  


Second revision
--------------------

This time only tests were made, since the double dispatch implementation was already done beforehand.\
Most of the tests are property based tests which tests many possible semi-random values including some edge cases like empty strings, zeroes, or very big absolute values of numbers (both positive and negative). \
However, most of the tests are done with data that "makes sense", like only trying positive values on character and weapon stats if these stats themselves are not being tested.

Third revision
---------------------
This one has been the toughest yet, this time all the remaining game elements were implemented, such as spells, debuffs (this is the name I'll use to refer to adverse effects from now on) and all the logic behind them.\
The spells were implemented using the strategy pattern and the debuffs were implemented using both observer and null object patterns. The debuffs are subscribers to the characters through the spells, and the characters notify them to activate their effect. Since not every spell has an effect, a "NoDebuff" effect was implemented, this is the null object and it's always a part of the mutable set of subscribers each character has, this effect does absolutely nothing.\
However the hardest part of this work was the implementation of all the game logic that acts in the background, this logic was implemented using the state pattern, trying to ensure all the game flow "made sense" and not have (too many) edge cases was a nightmare, I even caught logic bugs while playing the game myself.\
As a personal touch I also added the feature of getting new weapons on the player's inventory each time the player wins a battle, these weapons have standardized names with an underlying multiplier that's not so hard to figure out (it does have a limit tough, we wouldn't want to make the game too easy in the long run).
The player's characters also restore 10% of their hp and mp (if applicable) each time the player wins a battle.\
I did the best I could with exception handling overall, however I didn't have enough time to properly test the Main program thoroughly, so providing unexpected user input could still make the program crash with an exception.
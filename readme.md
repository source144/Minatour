# Minatour Assignment

## Implementation
### Minatour Party (Labrinth)
In the first portion (the party) of the assignment, Minatour calls a random Guest (Test Subject) to enter the Labrinth.\
The guest will request a new cake from Minatour if there is no cake at the end of the labyrinth. The guest may also decide whether or not to eat the cake that was served to him.\

Before the party began all guests decided that who ever enters the labyrinth must stand on one side of the court (he should not mingle with those who have not entered yet).\
When a guest leaves the labrinth, he will let Minatour know that all guests have entered the labyrinth ONLY IF they see that there are no guests left in the "not visited" side of the court.

Further explanation available inside `MinatourParty.java`.\
Another possible implementation that does not involve communcation is to have all guests agree to only eat one cake (sad I know ...) have a designated-delagate guest monitor the number of cakes minatour makes.\
Once the number of cakes Minatour makes and resuplies to the labyrinth equals the total number of guests he shall notify Minatour.\
However, I feel like the first implementation is a bit more feasable in a realistic setting (with, let's say, 500 guests.. in a party..)

### Minatour Show Room (Labrinth)
The Queue solution makes the most senses. This is sorta like having a Fast Pass in Disney compared to just rushing to the most popular Star Wars ride when it opened. You'd stand in line for 4 hours at least (still a queue). But the Fast Pass approach means that you enjoy the rest of the park until you get notified that it is your turn to enter the ride. You're not crammed up, you get to enjoy roaming the park, and you get to stand in a relatively short line ("priority" queue, however.. in this assignment/scope, you just enter the Show Room when it is your turn).

Further explanation available inside `MinatourShowroom.java`.

## Running the Programs
### Command Line Arguments
You may set the number of guests/threads through a command line argument to each of the programs (that's the only configurable thing right now - output will always be shown)\
By default, the number of threads will be 8 and no less than 2 (regardless of what the users sets).

_Note:_ there are no safeguards or validation to the number. Don't use any absurd number (larger than a Java integer) as the program may crash while trying to parse that number into an integer.

Example for setting the number of threads to `24`:
```
javac MinatourParty.java
java MinatourParty -threads 24
```


### Compiling and Running
Run as you would any other java file.\
For example, execute the `MinatourParty.java` file
```
javac MinatourParty.java
java MinatourParty
```

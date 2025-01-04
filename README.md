# King_and_Courtesan_game
Implementation using Gradle 8.10 and Java 11 of [King and Courtesan game by Mark Steere](https://www.marksteeregames.com/King_and_Courtesan_rules.pdf) with algorithms to play with it.

## How to launch the game

You can play locally or implement a client challenger to play with another challenger and a referee.

### Local game

The main classes for the game are in the folder `src/main/java/games/king_and_courtesan`. The main file is _KCGame.java_ where you specify which genetic algorithm you want to use. Currently, you can instantiate a purely random player, one using a MiniMax algorithm and one using alpha-beta pruning. Those genetic algorithms are located in the folder `src/main/java/iialib/games/algs/algorithms`.

It is also possible to read and instantiate the current state of the board from a file with the method _setBoardFromFile_ and then save the new state of the board after the current player has done his movement with the method _saveBoardToFile_. The sample file _example_file_to_read.txt_ with initial state of the board is given.

Once you compile the Gradle project using `gradle build`, it generates the compiled _iialib.jar_ file in the folder `build/libs`.

You can launch the game with the command

`java -cp build/libs/iialib.jar games.king_and_courtesan.KCGame`

### Client/Server game

It is possible to play the game with a Client/Server architecture with a referee and two challengers. There are three possible challengers :
* _KCRandomChallenger.java_ : Challenger playing randomly
* _KCAlphaBetaChallenger.java_ : Challenger playing with alpha-beta pruning algorithm
* _KCHumanChallenger.java_ : Challenger with each movement inputed by the user turn-by-turn. The moves are encoded as they are displayed on the board. Therefore, positions of the pawns are located using letters A to F for lines from bottom to top and numbers from 0 to 5 for columns from left to right. Moves are sent by the challengers with initial position to new position separated by `-`. For example, initial position of the RED king is `A0` at the bottom-left corner and initial position of the BLUE king is `F5` at the top-right corner. To move from one position to another, the challenger send the move as `A0-F5` (which is obviously an illegal move in this game !)

The server is launched with the class _KCDuel.java_. The server architecture (_GameServer_, _Client_ and _Referee_) is located in the folder `src/main/java/iialib/games/contest`.

#### Testing the server

To test the server, you can open a first terminal and run it on `localhost` with :
    
`java -cp ./build/libs/iialib.jar:commons-cli-1.4.jar games.king_and_courtesan.KCDuel -p 4536`

The `-p` argument represents the port on which the server will be listening. The default port we use here is `4536`. The _GameServer_ has a timeout constraint of 15 seconds. When the server is listening, open a second terminal and connect the first challenger with a command line such as :

`java -cp ./build/libs/iialib.jar:commons-cli-1.4.jar iialib.games.contest.Client -p 4536 -s localhost -c games.king_and_courtesan.KCRandomChallenger`

This command line will try to connect the challenger playing randomly to the server on `localhost` using the port `4536`. You can replace with other challengers. To start a duel, you need to connect a second player. Open a third terminal and connect the second player with a command line such as:

`java -cp ./build/libs/iialib.jar:PATH_TO_COMMONS-CLI/commons-cli-1.4.jar iialib.games.contest.Client -p 4536 -s localhost -c games.king_and_courtesan.KCRandomChallenger`

This command line will try to connect a second challenger playing randomly to the server on `localhost` using the port `4536`.
When the two opponents are connected, the server automatically start the duel. The first player to connect is the first to play and has the color RED, the second player to connect plays after and has the color BLUE.

#### Executing the command lines

The `-cp` argument defines the classpath. To establish a server, we need to pass both the jar of the compiled gradle project first _iialib.jar_ and then the API of the commons-cli library _commons-cli-1.4.jar_. The API is generated when building using gradle, as specified in the file _build.gradle_ containing the dependencies (line `api 'commons-cli:commons-cli:1.4'`). You can directly use the file from this repository, otherwise, the _commons-cli-1.4.jar_ file can be find under the folder $HOME/.gradle using the command line :
    
`find $HOME/.gradle -name "commons-cli-1.4.jar"`
    
Be careful that on Windows, the separator for classpath is `;` instead of `:`, which changes the command line to the format `path_to_iialib/iialib.jar;path_to_common-cli/common-cli-1.4.jar`. The path `$HOME/.gradle/caches/modules-2/files-2.1/` always contains the _common-cli-1.4.jar_ file somewhere, both on Windows and Linux.
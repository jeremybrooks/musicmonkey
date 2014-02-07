HTML documentation is available in the doc directory.

DESCRIPTION:
Music Monkey is a music quiz game for one or two players.  

INSTALLING MUSIC MONKEY:
Just unzip/untar the archive you downloaded.  This will create a musicmonkey directory containing the source, documentation, scripts, configuration file, and ready-to-run jar file.


HOW TO START MUSIC MONKEY:
First, you need some music.  Music goes in the tunes directory.  You can add individual files or entire directories.  The MP3 files must be tagged properly or they will not be found.  The MP3 tag library used by Music Monkey currently only supports ID3v1 tags. 

Once you have some music, fire up the application.  If you are running on a Linux system, you should be able to use the included script.  Make sure the script is executable by doing a chmod a+x musicmonkey.  Then, just type ./musicmonkey to start the program.  Note that if the machine you are running on has more than one network interface, you may need to set the -i option in the musicmonkey script to tell the application which interface to bind to.

When you start the application, it will scan through the tunes directory and create an index of the available songs.  This may take some time, depending on the speed of the computer and the number of files in the tunes directory.  On a Pentium II 450 with 256MB of RAM, it takes about 8 minutes to index 17000 songs.  This only happens at application startup.  If you add more songs or remove songs, you will need to stop and start the application to allow it to rebuild the music index.  Once it has finished, you can play!

If there are fatal errors (such as no usable files in the tunes directory), you will see a message on the console.  You can also look at the musicmonkey.log file to see what went wrong.


HOW TO USE MUSIC MONKEY:
Once the application has been started on the server, navigate to the Music, Movies, and More menu on your Tivo, and select Music Monkey.  You will be greeted with the Main Menu.  To make a selection, use the direction buttons on the remote.  The icons will be highlighed when a choice is made, and a message will appear on the screen telling you what that selection does.  To make a selection, press the Select button.  If you want to exit the application, press Clear.

You will see various message at the bottom of thie Menu Menu screen.  After a period of time, the display will automatically switch to High Score view.

When you are not playing a game, random tracks from your tunes directory will be played.  If you want a different selection of background music, create a directory and put the mp3 files you want to use as background music in it.  Then, change the musicmonkey.background_music.dir property in the musicmonkey.properties file.  Background music will then be selected from the mp3 files in the new directory.

MAIN MENU:
High Scores (up):
This will display the high scores for each game mode as follows:
  Single Player 3 Guess High Scores
  Single Player 5 Minute High Scores
  Two Player 10-Round High Scores
  Two Player 25-Round High Scores
  Two Player 50-Round High Scores

Once all the high scores have been displayed, you will return to the Main Menu.  You may press any key on the remote during high score display to return to the Main Menu.

Help (down):
Displays a simple help message.  Press any key to return to the Main Menu.

One Player (left):
After selecting a one player game, you will be prompted to select the game type you want to play.  One player games are:
  3 Guess:
  Play until you miss three times.  

  5 Minutes:
  Play for 5 minutes.  The faster you can guess the songs, the more points you can score.  


Two Players (right):
After selecting a two player game, you will be prompted to select the game type you want to play.  Two player games are:
  10 Rounds:
  Play 10 rounds.  The winner is the player with the highest score at the end of 10 rounds.
  25 Rounds:
  Play 25 rounds.  The winner is the player with the highest score at the end of 25 rounds.
  50 Rounds:
  Play 50 rounds.  The winner is the player with the highest score at the end of 50 rounds.

  500 Points:
  Play until somebody gets 500 points.
  1500 Points:
  Play until somebody gets 1500 points.
  3000 Points:
  Play until somebody gets 3000 points.

 
GAME PLAY:
Game play is simple.  At the beginning of a round, a song will start playing, and you will see four song titles on the screen.  The object of the game is to guess the song as quickly as possible to score points.  The number of points scored depends on how quickly you can guess the song.  As time goes by, the number of points counts down.  If it gets to zero, the round is over.

Hint: If you take too long to guess, the artists will be revealed below the song titles.  This may help you with your guess!

In a one player game (3 guess mode), you can only miss three times.  A miss is an incorrect guess, or letting time run out during a round.  When playing a timed game, a miss will end the round.

In two player games, both players may guess at any time.  However, a missed guess will disable that players controls until either the round ends, or the other player misses.

CONTROLS:
Use the Tivo remote control to make your choice.  The numbers next to the song display remind you which button to push.  In a two player game, you can both share the remote, or you can use two Tivo remote controls.

GAME OVER:
At the end of the game, you may get your name on the high score list!  All game types except two player point-goal games have a high score list.  If you get a high score, you will be presented with a simple text entry screen.  Move the cursor over the character and press Select.  You can enter up to 15 characters.  To erase a character, press Thumbs Down.  When you are finished, press Thumbs Up.



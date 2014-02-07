/**
 *  Music Monkey, a music quiz game for Tivo.
 *  Copyright (C) 2005 Jeremy Brooks
 *
 *
 *  This file is part of Music Monkey.
 *
 *  Music Monkey is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  Music Monkey is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Music Monkey; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 */


package net.whirljack.tivohme.musicmonkey;

// JAVA AWT
import java.awt.Color;

// JAVA UTIL
import java.util.List;
import java.util.Random;

// TIVO HME
import com.tivo.hme.sdk.View;
import com.tivo.hme.sdk.Resource;
import com.tivo.hme.util.Ticker;

// LOGGING
import org.apache.log4j.Logger;


/** Two player game.
 *
 * @author  jeremyb
 */
public class TwoPlayerView extends View implements Ticker.Client {
		
	/** How many points to start the round with. */
	private static final int ROUND_START_POINTS = 108;
	
	private Logger logger = Logger.getLogger(TwoPlayerView.class);
	
	private MusicMonkey application;
	
	private View errorMessage;
	
	private int livesOne = 1;
	private int livesTwo = 1;
	
	private long scoreOne = 0;
	private long scoreTwo = 0;
	
	/** Flag indicating if game is in play mode. */
	private boolean playing = false;
	
	/** All the songs that are available to choose from. */
	private List songList;
	
	/** Random numbers. */
	private Random rand;
	
	/** The correct song during a round. */
	private int correctSong;
	
	/** The selected songs during a round. */
	private Song[] songsForRound = new Song[4];
	
	
	/** Points available during a round. */
	private int roundPoints;
	
	/** The game type. */
	private long gameType;
	
	/** Number of rounds we have played. */
	private int round;
	
	// These are the names of the songs the player must choose from
	
	TextBoxView playerNameOne;
	TextBoxView playerNameTwo;
	View lifeViewOne;
	View lifeViewTwo;
	TextBoxView scoreViewOne;
	TextBoxView scoreViewTwo;
	TextBoxView[] controlViewOne;
	TextBoxView[] controlViewTwo;
	TextBoxView[] trackView;
	TextBoxView[] artistView;
	TextBoxView pointsView;
	TextBoxView winnersPointsView;
	TextBoxView countdownView;
	TextBoxView messageView;
	
	/** Creates a new instance of TwoPlayerView */
	public TwoPlayerView(View parent, MusicMonkey application, long gameType) {
		super(parent, 30, 25, parent.getWidth() - 60, parent.getHeight() - 50);
		
		this.application = application;
		
		this.gameType = gameType;
		
		this.songList = application.getSongList();
		
		this.rand = new Random(System.currentTimeMillis());
		
		
		// initialize the views
		this.playerNameOne = new TextBoxView(this, 0, 10, 100, 20, " ");
		this.playerNameOne.setMessage("Player One", RSRC_VALIGN_BOTTOM);
		this.playerNameTwo = new TextBoxView(this, this.getWidth()/2, 10, 100, 20, " ");
		this.playerNameTwo.setMessage("Player Two", RSRC_VALIGN_BOTTOM);
		
		this.lifeViewOne = new View(this, 100, 0, 50, 50);
		this.lifeViewOne.setResource(getResource("images/onemonkey.png"));
		this.lifeViewTwo = new View(this, this.getWidth()/2+100, 0, 50, 50);
		this.lifeViewTwo.setResource(getResource("images/onemonkey.png"));
		
		this.scoreViewOne = new TextBoxView(this, 0, 33, 250, 20, "0", "system-20.font");
		this.scoreViewTwo = new TextBoxView(this, this.getWidth()/2, 33, 250, 20, "0", "system-20.font");

		
		this.controlViewOne = new TextBoxView[4];
		this.controlViewTwo = new TextBoxView[4];
		View v = new View(this, 5, 100, 70, 21);
		v.setResource(getResource("images/control_background.png"));
		this.controlViewOne[0] = new TextBoxView(v, 0, 0, 70, 21, "1", Color.BLUE);
		this.controlViewOne[0].fadeOut();
		
		v = new View(this, this.getWidth()-75, 100, 70, 21);
		v.setResource(getResource("images/control_background.png"));
		this.controlViewTwo[0] = new TextBoxView(v, 0, 0, 70, 21, "3", Color.BLUE);
		this.controlViewTwo[0].fadeOut();
		
		v = new View(this, 5, 160, 70, 21);
		v.setResource(getResource("images/control_background.png"));
		this.controlViewOne[1] = new TextBoxView(v, 0, 0, 70, 21, "4", Color.BLUE);
		this.controlViewOne[1].fadeOut();
		
		v = new View(this, this.getWidth()-75, 160, 70, 21);
		v.setResource(getResource("images/control_background.png"));
		this.controlViewTwo[1] = new TextBoxView(v, 0, 0, 70, 21, "6", Color.BLUE);
		this.controlViewTwo[1].fadeOut();
		
		v = new View(this, 5, 220, 70, 21);
		v.setResource(getResource("images/control_background.png"));
		this.controlViewOne[2] = new TextBoxView(v, 0, 0, 70, 21, "7", Color.BLUE);
		this.controlViewOne[2].fadeOut();
		
		v = new View(this, this.getWidth()-75, 220, 70, 21);
		v.setResource(getResource("images/control_background.png"));
		this.controlViewTwo[2] = new TextBoxView(v, 0, 0, 70, 21, "9", Color.BLUE);
		this.controlViewTwo[2].fadeOut();
		
		v = new View(this, 5, 280, 70,21);
		v.setResource(getResource("images/control_background.png"));
		this.controlViewOne[3] = new TextBoxView(v, 0, 0, 70, 21, "Clear", Color.BLUE);
		this.controlViewOne[3].fadeOut();
		
		v = new View(this, this.getWidth()-75, 280, 70, 21);
		v.setResource(getResource("images/control_background.png"));
		this.controlViewTwo[3] = new TextBoxView(v, 0, 0, 70, 21, "Enter", Color.BLUE);
		this.controlViewTwo[3].fadeOut();
		
		
		this.trackView = new TextBoxView[4];
		this.artistView = new TextBoxView[4];
		v = new View(this, 110, 90, 303, 40);
		v.setResource(getResource("images/song_background.png"));
		trackView[0] = new TextBoxView(v, 0, 0, 303, 20, " ", Color.BLACK);
		trackView[0].fadeOut();
		artistView[0] = new TextBoxView(v, 0, 18, 303, 20, " ", Color.BLACK);
		artistView[0].fadeOut();
		
		v = new View(this, 110, 150, 303, 40);
		v.setResource(getResource("images/song_background.png"));
		trackView[1] = new TextBoxView(v, 0, 0, 303, 20, " ", Color.BLACK);
		trackView[1].fadeOut();
		artistView[1] = new TextBoxView(v, 0, 18, 303, 20, " ", Color.BLACK);
		artistView[1].fadeOut();
		

		v = new View(this, 110, 210, 303, 40);
		v.setResource(getResource("images/song_background.png"));
		trackView[2] = new TextBoxView(v, 0, 0, 303, 20, " ", Color.BLACK);
		trackView[2].fadeOut();
		artistView[2] = new TextBoxView(v, 0, 18, 303, 20, " ", Color.BLACK);
		artistView[2].fadeOut();
		
		v = new View(this, 110, 270, 303, 40);
		v.setResource(getResource("images/song_background.png"));
		trackView[3] = new TextBoxView(v, 0, 0, 303, 20, " ", Color.BLACK);
		trackView[3].fadeOut();
		artistView[3] = new TextBoxView(v, 0, 18, 303, 20, " ", Color.BLACK);
		artistView[3].fadeOut();
		
		
		this.pointsView = new TextBoxView(this, this.getWidth()/2 - 50, 340, 100, 20, " ", "system-16.font");
		this.pointsView.fadeOut();
		
		this.countdownView = new TextBoxView(this, 0, 0, this.getWidth(), this.getHeight(), " ", "system-200.font", Color.BLUE);
		this.countdownView.setMessage(" ", RSRC_HALIGN_CENTER | RSRC_VALIGN_CENTER);
		
		this.messageView = new TextBoxView(this, 0, 350, this.getWidth(), 50, " ", "default-20.font");
		this.messageView.translate(0, 40);
		
		this.winnersPointsView = new TextBoxView(this, this.getWidth()/2-50, 40, 100, 60, " ", "default-40.font");
		this.winnersPointsView.fadeOut();
	}
	
	
	/** Show a message at the bottom of the screen.
	 * The message will scroll up into view.
	 * @param message the message to display. 
	 */
	private void showMessage(String message) {
		this.messageView.setMessage(message);
		this.messageView.translate(0, -40, getResource("*500"));
	}
	
	
	/** Hide the message.
	 * Message will scroll down out of view.
	 */
	private void hideMessage() {
		this.messageView.translate(0, 40, getResource("*500"));
	}
	
	
	/** Handle player input.
	 */
	public boolean handleKeyPress(int code, long rawcode) {
		logger.debug("Got key code " + code);
		
		// don't bother if we are not playing
		if (this.playing) {
			
			switch (code) {
				// NOTE: ignore keypresses if user has already guessed them
				
				case KEY_NUM1:
					logger.debug("Handling KEY_NUM1");
					if (this.controlViewOne[0].getTransparency() == 0.0f) {
						if (this.correctSong == 0) {
							win(1);
						} else {
							miss(0, 1);
						}
					} else {
						logger.debug("Player one key 1 not active.");
					}
					break;

				case KEY_NUM3:
					logger.debug("Handling KEY_NUM3");
					if (this.controlViewTwo[0].getTransparency() == 0.0f) {
						if (this.correctSong == 0) {
							win(2);
						} else {
							miss(0, 2);
						}
					} else {
						logger.debug("Player two key 3 not active.");
					}
					break;
					
				case KEY_NUM4:
					logger.debug("Handling KEY_NUM4");
					if (this.controlViewOne[1].getTransparency() == 0.0f) {
						if (this.correctSong == 1) {
							win(1);
						} else {
							miss(1, 1);
						}
					} else {
						logger.debug("Player one key 4 not active.");
					}
					break;

				case KEY_NUM6:
					logger.debug("Handling KEY_NUM6");
					if (this.controlViewTwo[1].getTransparency() == 0.0f) {
						if (this.correctSong == 1) {
							win(2);
						} else {
							miss(1, 2);
						}
					} else {
						logger.debug("Player two key 6 not active.");
					}
					break;
					
				case KEY_NUM7:
					logger.debug("Handling KEY_NUM7");
					if (this.controlViewOne[2].getTransparency() == 0.0f) {
						if (this.correctSong == 2) {
							win(1);
						} else {
							miss(2, 1);
						}
					} else {
						logger.debug("Player one key 7 not active.");
					}
					break;

				case KEY_NUM9:
					logger.debug("Handling KEY_NUM9");
					if (this.controlViewTwo[2].getTransparency() == 0.0f) {
						if (this.correctSong == 2) {
							win(2);
						} else {
							miss(2, 2);
						}
					} else {
						logger.debug("Player two key 9 not active.");
					}
					break;
					
				case KEY_CLEAR:
					logger.debug("Handling KEY_CLEAR");
					if (this.controlViewOne[3].getTransparency() == 0.0f) {
						if (this.correctSong == 3) {
							win(1);
						} else {
							miss(3, 1);
						}
					} else {
						logger.debug("Player one key Clear not active.");
					}
					break;
					
				case KEY_ENTER:
					logger.debug("Handling KEY_ENTER");
					if (this.controlViewTwo[3].getTransparency() == 0.0f) {
						if (this.correctSong == 3) {
							win(2);
						} else {
							miss(3, 2);
						}
					} else {
						logger.debug("Player two key Enter not active.");
					}
					break;
					
				default:
					logger.debug("Invalid key press");
					break;
					
			}
		} else {
			this.logger.debug("Ignoring keypress (playing == false)");
			
		}
		
		
//		return super.handleKeyPress(code, rawcode);
		return true;
	}
	
	
	/** Called to start the game.
	 * Score is set to 0, lives is set to 3, and startRound is called.
	 */
	public void startGame() {
		this.scoreOne = 0;
		this.scoreTwo = 0;
		this.round = 0;
		
		startRound();
	}
	
	
	/** Called to start the round.
	 */
	private void startRound() {
		this.round++;
		this.livesOne = 1;
		this.livesTwo = 1;
		this.lifeViewOne.setTransparency(0.0f, getResource("*1000"));
		this.lifeViewTwo.setTransparency(0.0f, getResource("*1000"));
		
		// get four songs from the list
		logger.debug("Currently there are " + this.songList.size() + " songs in the list.");
		if (this.songList.size() <= 10) {
			logger.debug("Refreshing the list...you are too good.");
			this.songList = null;
			this.songList = application.getSongList();
			logger.debug("Now there are " + this.songList.size() + " songs in the list.");
		}
		for (int i = 0; i < 4; i++) {
			Song s = (Song)this.songList.remove(this.rand.nextInt(this.songList.size()));
			logger.debug("Selected song " + s.getSongInfo());
			this.songsForRound[i] = s;
		}
		
		// select the one we want to use
		this.correctSong = this.rand.nextInt(4);
		this.logger.debug("Correct song is " + correctSong + "(" + this.songsForRound[correctSong].getSongInfo() + ")");
				
		this.roundPoints = ROUND_START_POINTS;

		// countdown
		countdown();
		
		// start the music
		String partial = this.application.createPartialTrack(this.songsForRound[this.correctSong].getLocation());
		this.application.getMusicTrack().setTrack(partial);
		this.application.getMusicTrack().setLoop(false);
		this.application.getMusicTrack().play();
		
		// display the songs and controls
		for (int i = 0; i < 4; i++) {
			this.controlViewOne[i].fadeIn();
			this.controlViewTwo[i].fadeIn();
			this.trackView[i].setMessage(this.songsForRound[i].getTitle());
			
			// if this is not the correct song, put it back in the list
			if (this.correctSong != i) {
				this.songList.add(this.songsForRound[i]);
			}
		}
		
		// start the timer
		this.pointsView.setMessage(Integer.toString(roundPoints));
		Ticker.master.add(this, System.currentTimeMillis() + 100, null);
		
	}
	
	
	/** Called when the player makes the correct choice.
	 * The timer is stopped, the score is updated, and the next round is started.
	 * @param player the player that won the round.
	 */
	private void win(int player) {
		// no more guesses please
		this.playing = false;
		
		// stop the timer
		Ticker.master.remove(this, null);
		
		// show the points earned
		this.winnersPointsView.setVisible(true);
		this.winnersPointsView.setMessage("+" + this.roundPoints);
		flush();
		sleep(1000);
		
		// add the points to the score
		switch (player) {
			case 1:
				this.winnersPointsView.setLocation(100, 33, getResource("*500"));
				this.winnersPointsView.setScale(0.0f, 0.0f, getResource("*500"));
				flush();
				sleep(450);
				this.scoreOne += this.roundPoints;
				this.scoreViewOne.setMessage(Long.toString(this.scoreOne));
				flush();
				sleep(1000);
				this.winnersPointsView.setVisible(false);
				this.winnersPointsView.setScale(1.0f, 1.0f);
				this.winnersPointsView.setLocation(this.getWidth()/2-50, 40);
				endRound("Player One wins this round!");
				break;
				
			case 2:
				this.winnersPointsView.setLocation(395, 33, getResource("*500"));
				this.winnersPointsView.setScale(0.0f, 0.0f, getResource("*500"));
				flush();
				sleep(450);
				this.scoreTwo += this.roundPoints;
				this.scoreViewTwo.setMessage(Long.toString(this.scoreTwo));
				flush();
				sleep(1000);
				this.winnersPointsView.setVisible(false);
				this.winnersPointsView.setScale(1.0f, 1.0f);
				this.winnersPointsView.setLocation(this.getWidth()/2-50, 40);
				endRound("Player Two wins this round!");
				break;
				
			default:
				logger.warn("Bad value passed to win(" + player + ")");
				break;
		}
		
	}
	
	
	/** Called when the player makes an incorrect choice.
	 * 
	 * A life is removed, and the incorrect selection is removed.
	 * If all lives are gone, the game will end.
	 * @param guess the number guessed.
	 * @param player the player that made the guess.
	 */
	private void miss(int guess, int player) {
		// fade out incorrect track and controls
		this.trackView[guess].fadeOut();
		this.artistView[guess].fadeOut();
		this.controlViewOne[guess].fadeOut();
		this.controlViewTwo[guess].fadeOut();
		
		// disable guesses for the player that guessed incorrectly
		if (player == 1) {
			this.lifeViewOne.setTransparency(1.0F, getResource("*1000"));
			for (int i = 0; i < 4; i++) {
				this.controlViewOne[i].fadeOut();
			}
		} else {
			this.lifeViewTwo.setTransparency(1.0F, getResource("*1000"));
			for (int i = 0; i < 4; i++) {
				this.controlViewTwo[i].fadeOut();
			}
		}
		
		// now enable other player's controls
		if (player == 1) {
			this.lifeViewTwo.setTransparency(0.0F, getResource("*1000"));
			for (int i = 0; i < 4; i++) {
				if (this.trackView[i].getTransparency() == 0.0f) {
					this.controlViewTwo[i].fadeIn();
				}
			}
		} else {
			this.lifeViewOne.setTransparency(0.0F, getResource("*1000"));
			for (int i = 0; i < 4; i++) {
				if (this.trackView[i].getTransparency() == 0.0f) {
					this.controlViewOne[i].fadeIn();
				}
			}
		}
		flush();
		
	}
	
	
	/** Called when the round is finished.
	 */
	private void endRound(String message) {
		Ticker.master.remove(this, null);
		showMessage(message);
		
		// score goes away
		this.pointsView.fadeOut();
		
		// reveal the correct artist
		this.artistView[this.correctSong].setMessage(this.songsForRound[this.correctSong].getArtist());
		
		// and hide the incorrect choices
		for (int i = 0; i < 4; i++) {
			if (this.correctSong != i) {
				this.controlViewOne[i].fadeOut();
				this.controlViewTwo[i].fadeOut();
				this.trackView[i].fadeOut();
				this.artistView[i].fadeOut();
			} else {
				this.controlViewOne[i].fadeIn();
				this.controlViewTwo[i].fadeIn();
			}
		}
		flush();
		
		
		// pause
		sleep(4000);

		hideMessage();
		
		// hide the correct song (it is still visible)
		this.controlViewOne[this.correctSong].fadeOut();
		this.controlViewTwo[this.correctSong].fadeOut();
		this.trackView[this.correctSong].fadeOut();
		this.artistView[this.correctSong].fadeOut();
		flush();
		sleep(1000);
		
		// check for game end
		if (this.gameType == 0 && this.round == 10) {
			endGame();
		} else if (this.gameType == 1 && this.round == 25) {
			endGame();
		} else if (this.gameType == 2 && this.round == 50) {
			endGame();
		} else if (this.gameType == 3 && (this.scoreOne >= 500 || this.scoreTwo >= 500)) {
			endGame();
		} else if (this.gameType == 4 && (this.scoreOne >= 1500 || this.scoreTwo >= 1500)) {
			endGame();
		} else if (this.gameType == 5 && (this.scoreOne >= 3000 || this.scoreTwo >= 3000)) {
			endGame();
		} else {
			startRound();
		}

	}
	
	
	/** Called when the game is over.
	 */
	private void endGame() {
		Ticker.master.remove(this, null);
		this.pointsView.fadeOut();
		long winningScore = 0;
		
		showMessage("Game Over");
		flush();
		
		sleep(2000);
		
		// make sure both players are visible
		this.lifeViewOne.setTransparency(0.0f);
		this.lifeViewTwo.setTransparency(0.0f);
		
		application.getMusicTrack().stop();
		application.startBackgroundMusic();
		hideMessage();
		flush();
		sleep(1000);
		
		
		
		if (scoreOne == scoreTwo) {
			showMessage("It's a tie!  Maybe you should play again...");
			
		} else if (scoreOne > scoreTwo) {
			winningScore = scoreOne;
			showMessage("Player One is the winner with " + scoreOne + " points!");
			// move player one to center...
			int distance = (this.getWidth()/2 - this.lifeViewOne.getX()) - this.lifeViewOne.getWidth()/2;
			this.lifeViewOne.setLocation(this.lifeViewOne.getX()+distance, this.lifeViewOne.getY(), getResource("*1000"));
			this.playerNameOne.setLocation(this.playerNameOne.getX()+distance, this.playerNameOne.getY(), getResource("*1000"));
			this.scoreViewOne.setLocation(this.scoreViewOne.getX()+distance, this.scoreViewOne.getY(), getResource("*1000"));
			
			// fade out player 2
			this.lifeViewTwo.setTransparency(1.0f, getResource("*1000"));
			this.playerNameTwo.fadeOut();
			this.scoreViewTwo.fadeOut();
			
		} else {
			winningScore = scoreTwo;
			showMessage("Player Two is the winner with " + scoreTwo + " points!");
			// move player two to center...
			int distance = (this.lifeViewTwo.getX() - this.getWidth()/2) + this.lifeViewTwo.getWidth()/2;
			this.lifeViewTwo.setLocation(this.lifeViewTwo.getX()-distance, this.lifeViewTwo.getY(), getResource("*1000"));
			this.playerNameTwo.setLocation(this.playerNameTwo.getX()-distance, this.playerNameTwo.getY(), getResource("*1000"));
			this.scoreViewTwo.setLocation(this.scoreViewTwo.getX()-distance, this.scoreViewTwo.getY(), getResource("*1000"));
			
			// fade out player 1
			this.lifeViewOne.setTransparency(1.0f, getResource("*1000"));
			this.playerNameOne.fadeOut();
			this.scoreViewOne.fadeOut();
		}
		
		flush();
		
		sleep(5000);
		hideMessage();
		flush();
		
		this.setTransparency(1.0f, getResource("*1000"));
		
		// check for high scores for games 0-2
		if (gameType == 0 || gameType == 1 || gameType == 2) {
			if (winningScore != 0) {
				if (this.application.checkForHighScore(winningScore, this.application.getCurrentGameType())) {
					this.application.setNewHigh(winningScore);
					this.application.setRound(this.round);
					this.application.switchView(application.VIEW_ENTER_HIGH_SCORE);
				} else {
					this.application.switchView(application.VIEW_HIGH_SCORES);
				}
			} else {
				this.application.switchView(application.VIEW_HIGH_SCORES);
			}
		} else {
			this.application.switchView(application.VIEW_MENU);
		}
		
	}
	
	
	private void countdown() {
		flush();
		
		application.getMusicTrack().stop();
		
		if (this.gameType == 0 || this.gameType == 1 || this.gameType == 2) {
			showMessage("Get ready for Round " + this.round + "!");
		} else {
			showMessage("Get ready!");
		}
		flush();
		sleep(1000);
		
		for (int i = 3; i != 0; i--) {
			this.countdownView.setMessage(Integer.toString(i));
			this.countdownView.setVisible(true);
			flush();
			
			sleep(1000);
			
			flush();
		}
		this.countdownView.setMessage(" ");
		this.countdownView.setVisible(false);
		hideMessage();
		
		flush();
	}
	
	
	/** Called every 100ms during game play.
	 */
	public long tick(long param, Object obj) {
		long next = 0;
		this.roundPoints -= 1;
		
		
		if (this.roundPoints < 0) {
			
			// ran out of time
			// end the round
			// lose a life, and end the round
			
			this.application.getMusicTrack().stop();
			Ticker.master.remove(this, null);
			this.playing = false;
			endRound("You ran out of time!");
			
			
		} else {
			
			// still has time, so update the points display
			this.pointsView.setMessage(Integer.toString(this.roundPoints));
			
			// if too much time has gone by, give a hint
			// don't display hint if control on both sides is transparent
			if (this.roundPoints == 60) {
				for (int i = 0; i < 4; i++) {
					if (this.controlViewOne[i].getTransparency() == 0.0f || this.controlViewTwo[i].getTransparency() == 0.0f) {
						this.artistView[i].setMessage(this.songsForRound[i].getArtist());
					}
				}
			}
			
			// check to see if enough time has gone by to consume stray keypresses
			if (this.roundPoints == 100) {
				this.playing = true;
			}
			flush();
			next = System.currentTimeMillis() + 100;
		}

		return next;
	}
	
	
	private void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (Exception e) {
			logger.warn("Error during sleep.", e);
		}
	}
	
}

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


/** One player game.
 *
 * @author  jeremyb
 */
public class OnePlayerView extends View implements Ticker.Client {
		
	/** How many points to start the round with. */
	private static final int ROUND_START_POINTS = 108;
	
	/** Time remaining. */
	private int timeRemaining = 300;
	private int aCounter = 0;
	
	private Logger logger = Logger.getLogger(OnePlayerView.class);
	
	private MusicMonkey application;
	
	private View errorMessage;
	
	private int lives = 3;
	
	private long score = 0;
	
	private long gameType;
	
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
	
	/** Number of rounds we have played. */
	private int round;
	
	// These are the names of the songs the player must choose from
	
	TextBoxView playerName;
	View[] lifeView;
	TextBoxView scoreView;
	TextBoxView[] controlView;
	TextBoxView[] trackView;
	TextBoxView[] artistView;
	TextBoxView pointsView;
	TextBoxView winnersPointsView;
	TextBoxView countdownView;
	TextBoxView messageView;
	TextBoxView timerView;
	
	/** Creates a new instance of OnePlayerView */
	public OnePlayerView(View parent, MusicMonkey application, long gameType) {
		super(parent, 30, 25, parent.getWidth() - 60, parent.getHeight() - 50);
		
		this.application = application;
		this.gameType = gameType;
		
		this.songList = application.getSongList();
		
		this.rand = new Random(System.currentTimeMillis());
		
		
		// initialize the views
		this.playerName = new TextBoxView(this, 0, 10, 100, 20, "");
		this.playerName.setMessage("Player One", RSRC_VALIGN_BOTTOM);
		
		this.lifeView = new View[3];
		this.lifeView[0] = new View(this, 100, 0, 50, 50);
		this.lifeView[0].setResource(getResource("images/onemonkey.png"));
		this.lifeView[1] = new View(this, 150, 0, 50, 50);
		this.lifeView[1].setResource(getResource("images/onemonkey.png"));
		this.lifeView[2] = new View(this, 200, 0, 50, 50);
		this.lifeView[2].setResource(getResource("images/onemonkey.png"));
		
		this.timerView = new TextBoxView(this, this.getWidth()/2-100, 0, 200, 30, " ", "system-20.font", Color.YELLOW);
		if (this.gameType == 0) {
			this.timerView.setVisible(false);
		} else {
			this.timerView.setMessage(this.formatTime());
			this.lifeView[1].setVisible(false);
			this.lifeView[2].setVisible(false);
		}
		
		this.scoreView = new TextBoxView(this, 0, 33, 250, 20, "0", "system-20.font");
		
		this.controlView = new TextBoxView[4];
		View v = new View(this, 5, 100, 70, 21);
		v.setResource(getResource("images/control_background.png"));
		this.controlView[0] = new TextBoxView(v, 0, 0, 70, 21, "1", Color.BLUE);
		this.controlView[0].fadeOut();
		
		v = new View(this, 5, 160, 70,21);
		v.setResource(getResource("images/control_background.png"));
		this.controlView[1] = new TextBoxView(v, 0, 0, 70, 21, "4", Color.BLUE);
		this.controlView[1].fadeOut();
		
		v = new View(this, 5, 220, 70,21);
		v.setResource(getResource("images/control_background.png"));
		this.controlView[2] = new TextBoxView(v, 0, 0, 70, 21, "7", Color.BLUE);
		this.controlView[2].fadeOut();
		
		v = new View(this, 5, 280, 70,21);
		v.setResource(getResource("images/control_background.png"));
		this.controlView[3] = new TextBoxView(v, 0, 0, 70, 21, "Clear", Color.BLUE);
		this.controlView[3].fadeOut();
		
		
		
		this.trackView = new TextBoxView[4];
		this.artistView = new TextBoxView[4];
		v = new View(this, 110, 90, 303, 40);
		v.setResource(getResource("images/song_background.png"));
		trackView[0] = new TextBoxView(v, 0, 0, 303, 20, "", Color.BLACK);
		trackView[0].fadeOut();
		artistView[0] = new TextBoxView(v, 0, 18, 303, 20, "", Color.BLACK);
		artistView[0].fadeOut();
		
		v = new View(this, 110, 150, 303, 40);
		v.setResource(getResource("images/song_background.png"));
		trackView[1] = new TextBoxView(v, 0, 0, 303, 20, "", Color.BLACK);
		trackView[1].fadeOut();
		artistView[1] = new TextBoxView(v, 0, 18, 303, 20, "", Color.BLACK);
		artistView[1].fadeOut();
		

		v = new View(this, 110, 210, 303, 40);
		v.setResource(getResource("images/song_background.png"));
		trackView[2] = new TextBoxView(v, 0, 0, 303, 20, "", Color.BLACK);
		trackView[2].fadeOut();
		artistView[2] = new TextBoxView(v, 0, 18, 303, 20, "", Color.BLACK);
		artistView[2].fadeOut();
		
		v = new View(this, 110, 270, 303, 40);
		v.setResource(getResource("images/song_background.png"));
		trackView[3] = new TextBoxView(v, 0, 0, 303, 20, "", Color.BLACK);
		trackView[3].fadeOut();
		artistView[3] = new TextBoxView(v, 0, 18, 303, 20, "", Color.BLACK);
		artistView[3].fadeOut();
		
		
		this.pointsView = new TextBoxView(this, this.getWidth()/2 - 50, 340, 100, 20, "", "system-16.font");
		this.pointsView.fadeOut();
		
		this.countdownView = new TextBoxView(this, 0, 0, this.getWidth(), this.getHeight(), "", "system-200.font", Color.BLUE);
		this.countdownView.setMessage("", RSRC_HALIGN_CENTER | RSRC_VALIGN_CENTER);
		
		this.messageView = new TextBoxView(this, 0, 350, this.getWidth(), 50, "", "default-20.font");
		this.messageView.translate(0, 40);
		
		this.winnersPointsView = new TextBoxView(this, this.getWidth()/2-50, 40, 100, 60, "", "default-40.font");
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
					
					if (this.controlView[0].getTransparency() == 0.0f) {
						if (this.correctSong == 0) {
							win();
						} else {
							miss(0);
						}
					} else {
						logger.debug("Key 1 has already been guessed; ignoring.");
					}
					break;
					
				case KEY_NUM4:
					logger.debug("Handling KEY_NUM4");
					if (this.controlView[1].getTransparency() == 0.0f) {
						if (this.correctSong == 1) {
							win();
						} else {
							miss(1);
						}
					} else {
						logger.debug("Key 4 has already been guessed; ignoring.");
					}
					break;
					
				case KEY_NUM7:
					logger.debug("Handling KEY_NUM7");
					if (this.controlView[2].getTransparency() == 0.0f) {
						if (this.correctSong == 2) {
							win();
						} else {
							miss(2);
						}
					} else {
						logger.debug("Key 7 has already been guessed; ignoring.");
					}
					break;
					
				case KEY_CLEAR:
					logger.debug("Handling KEY_CLEAR");
					if (this.controlView[3].getTransparency() == 0.0f) {
						if (this.correctSong == 3) {
							win();
						} else {
							miss(3);
						}
					} else {
						logger.debug("Key Clear has already been guessed; ignoring.");
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
		this.score = 0;
		
		if (this.gameType == 0) {
			this.lives = 3;
		} else {
			this.lives = 1;
		}
		
		startRound();
	}
	
	
	/** Called to start the round.
	 */
	private void startRound() {
		// if this is a timed game, make sure the user has one life
		if (this.gameType == 1 && this.lives == 0) {
			this.lives++;
			this.lifeView[0].setTransparency(0.0f, getResource("*1000"));
		}
		
		this.round++;
		// get four songs from the list
		logger.debug("Currently there are " + this.songList.size() + " songs in the list.");
		if (this.songList.size() <= 10) {
			logger.debug("Refreshing the list...you are too good.");
			this.songList = null;
			this.songList = this.application.getSongList();
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
			this.controlView[i].fadeIn();
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
	 */
	private void win() {
		
		// no more guesses please
		this.playing = false;
		
		// stop the timer
		Ticker.master.remove(this, null);
		
		showPointsEarned(this.roundPoints);
		
		endRound("You are correct!");
	}
	
	
	private void showPointsEarned(int points) {
		// show the points earned
		this.winnersPointsView.setVisible(true);
		this.winnersPointsView.setMessage("+" + points);
		flush();
		sleep(1000);
		this.handleTimedGame(1000);
		
		// add the points to the score
		this.winnersPointsView.setLocation(100, 33, getResource("*500"));
		this.winnersPointsView.setScale(0.0f, 0.0f, getResource("*500"));
		flush();
		sleep(450);
		this.score += points;
		this.scoreView.setMessage(Long.toString(this.score));
		flush();
		sleep(1000);
		this.handleTimedGame(1000);
		
		this.winnersPointsView.setVisible(false);
		this.winnersPointsView.setScale(1.0f, 1.0f);
		this.winnersPointsView.setLocation(this.getWidth()/2-50, 40);
	}
	
	
	/** Called when the player makes an incorrect choice.
	 * A life is removed, and the incorrect selection is removed.
	 * If all lives are gone, the game will end.
	 */
	private void miss(int guess) {
		if (this.gameType == 1) {
			this.playing = false;
		}
		die();
		this.controlView[guess].fadeOut();
		this.trackView[guess].fadeOut();
		this.artistView[guess].fadeOut();
		flush();
		
		// a miss during a timed game results in the end of the round.
		if (this.gameType == 1) {
			showPointsEarned(0);
			endRound("You were wrong!");
			
		// a miss during a guess-limited game ends the round if there are no more lives
		} else if (this.lives == 0) {
			endRound("You ran out of guesses!");
		} 
	}
	
	
	/** Called when the player dies, either by 
	 * making an incorrect choice or running out of time.
	 * A life is subtracted, and a monkey is removed.
	 */
	private void die() {
		this.lives -= 1;
		View v = this.lifeView[this.lives];
		v.setTransparency(1.0F, getResource("*1000"));
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
				this.controlView[i].fadeOut();
				this.trackView[i].fadeOut();
				this.artistView[i].fadeOut();
			}
		}
		flush();
		
		
		// pause
		for (int i = 0; i < 5; i++) {
			sleep(1000);
			handleTimedGame(1000);
			flush();
		}

		hideMessage();
		
		// hide the correct song (it is still visible)
		this.controlView[this.correctSong].fadeOut();
		this.trackView[this.correctSong].fadeOut();
		this.artistView[this.correctSong].fadeOut();
		flush();
		sleep(500);
		
		// start another round, or end the game
		if (this.gameType == 0 && this.lives == 0) {
			endGame();
		} else if (this.gameType == 1 && this.timeRemaining <= 0) {
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
		
		showMessage("Game Over");
		flush();
		
		sleep(5000);
		
		application.getMusicTrack().stop();
		application.startBackgroundMusic();
		hideMessage();
		
		setTransparency(1.0f, getResource("*1000"));
		
		if (this.application.checkForHighScore(this.score, this.application.getCurrentGameType())) {
			this.application.setNewHigh(this.score);
			this.application.setRound(this.round);
			this.application.switchView(application.VIEW_ENTER_HIGH_SCORE);
		} else {
			this.application.switchView(application.VIEW_HIGH_SCORES);
		}
	}
	
	
	private void countdown() {
		flush();
		
		application.getMusicTrack().stop();
		
		showMessage("Get ready for Round " + this.round);
		
		for (int i = 3; i != 0; i--) {
			this.countdownView.setMessage(Integer.toString(i));
			this.countdownView.setVisible(true);
			flush();
			
			sleep(1000);
			handleTimedGame(1000);
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
		
		// handle timed games
		if (this.gameType == 1) {
			handleTimedGame(100);
		}
		
		if (this.roundPoints < 0) {
			
			// ran out of time
			// lose a life, and end the round
			
			this.application.getMusicTrack().stop();
			Ticker.master.remove(this, null);
			this.playing = false;
			die();
			endRound("You ran out of time!");
			
			
		} else {
			
			// still has time, so update the points display
			this.pointsView.setMessage(Integer.toString(this.roundPoints));
			
			// if too much time has gone by, give a hint
			// don't display hint if control is transparent
			if (this.roundPoints == 60) {
				for (int i = 0; i < 4; i++) {
					if (this.controlView[i].getTransparency() == 0.0f) {
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
	
	
	/** Return the remaining time as minutes:seconds
	 */
	private String formatTime() {
		StringBuffer buf = new StringBuffer();
		int minutes = this.timeRemaining / 60;
		int seconds = this.timeRemaining - minutes*60;
		buf.append(minutes).append(":");
		if (seconds < 10) {
			buf.append("0");
		}
		buf.append(seconds);
		
		return buf.toString();
	}
	
	private void handleTimedGame(int time) {
		this.aCounter += time;
		if (this.aCounter >= 1000) {
			this.aCounter = 0;
			this.timeRemaining--;
			if (this.timeRemaining >= 0) {
				this.timerView.setMessage(this.formatTime());
			}
		}
	}
}

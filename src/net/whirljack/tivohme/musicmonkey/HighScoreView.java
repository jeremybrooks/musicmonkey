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

// JAVA IO
import java.io.Serializable;

// JAVA UTIL
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

// TIVO HME
import com.tivo.hme.sdk.View;
import com.tivo.hme.util.Ticker;
import com.tivo.hme.sdk.Resource;

// LOGGING
import org.apache.log4j.Logger;


/** Display the high scores.
 *
 * @author  jeremyb
 */
public class HighScoreView extends View implements Ticker.Client {
	
	/** Logging. */
	private Logger logger = Logger.getLogger(HighScoreView.class);
	
	/** Count how many times the timer has triggered. */
	private int count = 0;
		
	/** Reference to the main application. */
	private MusicMonkey application;

	/** Array of high scores. */
	private HighScore[] highScores;
	
	/** Which high score list are we working on? */
	private int highScoreIndex;
	
	/** Array of high score views. */
	private View[] highScoreViews;
	
	/** Display a helpful message. */
	private TextBoxView helpBox;
	
	/** An icon. */
	private View icon;
	
	/** Another icon. */
	private View icon2;
	
	/** What scores we are showing. */
	private TextBoxView scoreTypeView;
	
	/** Creates a new instance of HighScoreView.
	 * @param parent the parent View.
	 * @param application reference to the main application.
	 * @param highScores array of HighScore objects to display.
	 */
//	public HighScoreView(View parent, MusicMonkey application, HighScore[] highScores) {
	public HighScoreView(View parent, MusicMonkey application) {
		super(parent, 0, 0, parent.getWidth(), parent.getHeight());
		
		this.application = application;
		helpBox = new TextBoxView(this, 0, this.getHeight()-50, this.getWidth(), 20, "Press any key on the remote to return to the main menu.");
		
		this.scoreTypeView = new TextBoxView(this, 0, 75, this.getWidth(), 30, "", "system-20.font");
		this.scoreTypeView.fadeOut();
		
		this.icon = new View(this, 50, 60, 50, 50);
		this.icon.setResource(getResource("images/highmonkey.png"));
		
		this.icon2 = new View(this, this.getWidth()-100, 60, 50, 50);
		this.icon2.setTransparency(1.0f);
		this.icon2.setResource(getResource("images/highmonkey.png"));
		
		// set index based on game type
		if (application.getCurrentGameType().equals(application.GAME_TYPE_SINGLE_TIMED)) {
			this.highScoreIndex = 1;
		} else if (application.getCurrentGameType().equals(application.GAME_TYPE_10_ROUNDS)) {
			this.highScoreIndex = 2;
		} else if (application.getCurrentGameType().equals(application.GAME_TYPE_25_ROUNDS)) {
			this.highScoreIndex = 3;
		} else if (application.getCurrentGameType().equals(application.GAME_TYPE_50_ROUNDS)) {
			this.highScoreIndex = 4;
		} else {
			this.highScoreIndex = 0;
		}
		
		// fade in the title
		setTransparency(1f);
		setResource("images/hightitle.png", RSRC_VALIGN_TOP);
		setTransparency(0f, getResource("*1000"));
		
		Ticker.master.add(this, System.currentTimeMillis() + 1000, null);
		
	}
	
	
	/** Handle user input.
	 * When the user presses a key, the main menu is shown.
	 */
	public boolean handleKeyPress(int code, long rawcode) {
		play("select.snd");
		exit();
		
		return super.handleKeyPress(code, rawcode);
	}
	
	
	private void exit() {
		Ticker.master.remove(this, null);
		
		// fade out
		setTransparency(1f, getResource("*1000"));
		
		application.switchView(application.VIEW_MENU);
	}
	
	
	/** Show a new high score periodically.
	 */
	public long tick(long param, Object obj) {
		long delay = 2000;
		
		// exit if all types of high scores have been diplayed
		if (this.highScoreIndex < 5) {
		
			// get scores if needed
			if (this.highScores == null) {
				getHighScores();
				// sort the scores
				Arrays.sort(this.highScores, new HighScoreComparator());

				this.highScoreViews = new View[this.highScores.length];
			}

			// first, display the high scores one at a time
			if (this.count < this.highScores.length) {
				String score = (this.highScores[count].toString());
				logger.debug("Showing score " + count + ": " + score);
				this.highScoreViews[count] = new View(this, 0, 85 + (count* 40), this.getWidth(), this.getHeight());
				View scoreView = new View(this.highScoreViews[count], 0, this.highScoreViews[count].getHeight(), this.highScoreViews[count].getWidth(), 50);
				scoreView.setResource(createText("system-14.font", Color.white, score), RSRC_VALIGN_TOP);

				this.highScoreViews[count].setTranslation(0, -400, getResource("*1000"));
				flush();
				count++;

			// all high scores have been displayed, so increment the index and set array to null
			} else if (this.count == this.highScores.length) {
				this.highScoreIndex += 1;
				this.highScores = null;
				count = 0;
				delay = 4000;
			}
		
		} else {
			exit();
		}
		
		return System.currentTimeMillis() + delay;
	}

	
	
	private void getHighScores() {
		
		if (this.highScoreViews != null) {
			this.scoreTypeView.fadeOut();
			for (int i = 0; i < this.highScoreViews.length; i++) {
				this.highScoreViews[i].setTransparency(1.0f, getResource("*1000"));
			}
		}
		
		switch (this.highScoreIndex) {
			case 0:
				this.scoreTypeView.setMessage("Single Player 3 Guess High Scores");
				this.highScores = this.application.getHighScores(this.application.GAME_TYPE_SINGLE);
				break;
				
			case 1:
				this.scoreTypeView.setMessage("Single Player 5 Minute High Scores");
				this.highScores = this.application.getHighScores(this.application.GAME_TYPE_SINGLE_TIMED);
				break;
				
			case 2:
				this.scoreTypeView.setMessage("Two Player 10-Round Scores");
				this.highScores = this.application.getHighScores(this.application.GAME_TYPE_10_ROUNDS);
				this.icon2.setTransparency(0.0f, getResource("*1000"));
				break;
				
			case 3:
				this.scoreTypeView.setMessage("Two Player 25-Round Scores");
				this.highScores = this.application.getHighScores(this.application.GAME_TYPE_25_ROUNDS);
				this.icon2.setTransparency(0.0f, getResource("*1000"));
				break;
				
			case 4:
				this.scoreTypeView.setMessage("Two Player 50-Round Scores");
				this.highScores = this.application.getHighScores(this.application.GAME_TYPE_50_ROUNDS);
				this.icon2.setTransparency(0.0f, getResource("*1000"));
				break;
				
			default:
				break;
				
		}
	}
}

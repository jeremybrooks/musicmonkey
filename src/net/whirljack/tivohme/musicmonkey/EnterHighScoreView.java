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

// TIVO HME
import com.tivo.hme.sdk.View;
import com.tivo.hme.sdk.Resource;


/** Allow the user to enter their name, then pass the info to 
 * MusicMonkey for addition to the high score list.
 *
 * @author  jeremyb
 */
public class EnterHighScoreView extends View {
		
	/** Number of rows in the display. */
	private final static int ROWS = 7;
	
	/** Number of columns in the display. */
	private final static int COLS = 6;
	
	/** Maximum number of characters user is allowed to enter. */
	private final static int MAX_NAME = 15;
	
	/** Prompt text. */
	private final static String PROMPT = "Enter your name: ";
	/** X coordinate of the menu. */
	private int menuX;
	
	/** Y coordinate of the menu. */
	private int menuY;
	
	/** Size of each menu item. */
	private int menuSize = 30;
	
	/** Reference to the application. */
	private MusicMonkey application;
	
	/** The icon. */
	private View icon;
	
	/** The cursor. */
	private View cursor;
	
	/** The entry menu. */
	private View menu[][];
	
	/** Characters in the entry menu. */
	private String chars[] = {
		"A", "B", "C", "D", "E", "F",
		"G", "H", "I", "J", "K", "L",
		"M", "N", "O", "P", "Q", "R",
		"S", "T", "U", "V", "W", "X",
		"Y", "Z", "0", "1", "2", "3",
		"4", "5", "6", "7", "8", "9",
		".", "-", "*", ":", ")", " "
	};
	
	/** Display a helpful message. */
	private TextBoxView helpView;
	
	/** Display a prompt. */
	private TextBoxView promptView;
	
	/** Display the players high score. */
	private TextBoxView scoreView;
	
	/** Holds player input. */
	private String name;
	
	/** Cursor X position. */
	private int cursorX = 0;
	
	/** Cursor Y position. */
	private int cursorY = 0;
	
	
	/** Creates a new instance of EnterHighScoreView.
	 * @param parent the parent View.
	 * @param application reference to the main application.
	 */
	public EnterHighScoreView(View parent, MusicMonkey application) {
		super(parent, 0, 0, parent.getWidth(), parent.getHeight());
		
		this.menuX = (this.getWidth() - (menuSize * ROWS)) / 2;
		this.menuY = (this.getHeight() - (menuSize * COLS)) / 2;
		
		this.application = application;
		this.icon = new View(this, 50, 60, 50, 50);
		this.icon.setResource(getResource("images/highmonkey.png"));
		
		this.cursor = new View(this, menuX, menuY, menuSize, menuSize);
		this.cursor.setResource(getResource("images/cursor.png"));
		
		this.helpView = new TextBoxView(this, 0, this.getHeight() - 50, this.getWidth(), 30, "Thumbs Up when finished, Thumbs Down to erase a character.");
		
		this.name = "";

		this.scoreView = new TextBoxView(this, 0, 80, this.getWidth(), 30, application.getNewHigh() + " is a high score!", "system-20.font");
		
		this.promptView = new TextBoxView(this, 50, this.getHeight()- 90, this.getWidth() , 30, "", "system-20.font");
		this.promptView.setMessage(PROMPT + name, RSRC_HALIGN_LEFT);
		
		buildMenu();
		
		// fade in the title
		setTransparency(1f);
		setResource("images/hightitle.png", RSRC_VALIGN_TOP);
		setTransparency(0f, getResource("*1000"));
		
	}
	
	
	/** Handle input.
	 * Arrows move the cursor, select adds the character to the name, 
	 * if name is not already too long.  Thumbs Down will erase a 
	 * character, Thumbs Up accepts entry.
	 */
	public boolean handleKeyPress(int code, long rawcode) {
		
		switch (code) {
			case KEY_RIGHT:
				cursorX += 1;
				if (cursorX > COLS - 1) {
					cursorX = 0;
				}
				moveCursor();
				break;
				
			case KEY_LEFT:
				cursorX -= 1;
				if (cursorX < 0) {
					cursorX = COLS - 1;
				}
				moveCursor();
				break;
				
			case KEY_UP:
				cursorY -= 1;
				if (cursorY < 0) {
					cursorY = ROWS - 1;
				}
				moveCursor();
				break;
				
			case KEY_DOWN:
				cursorY += 1;
				if (cursorY > ROWS - 1) {
					cursorY = 0;
				}
				moveCursor();
				break;
				
			case KEY_SELECT:
				if (name.length() != MAX_NAME) {
					play("select.snd");
					name += getCharAt(cursorX, cursorY);
					this.promptView.setMessage(PROMPT + name, RSRC_HALIGN_LEFT);
				} else {
					play("error.snd");
				}
				break;
				
			case KEY_THUMBSDOWN:
				if (name.length() > 0) {
					play("left.snd");
					name = name.substring(0, name.length() - 1);
					this.promptView.setMessage(PROMPT + name, RSRC_HALIGN_LEFT);
				} else {
					play("error.snd");
				}
				break;
			
			case KEY_THUMBSUP:
				play("thumbsup.snd");
				if (this.name.length() == 0) {
					this.name = "???";
				}
				HighScore hs = new HighScore();
				hs.setDate(new java.util.Date());
				hs.setScore(this.application.getNewHigh());
				hs.setPlayer(name);
				hs.setRounds(this.application.getRound());
				
				this.application.addHighScore(hs, this.application.getCurrentGameType());
				this.setTransparency(1.0f, getResource("*1000"));
				
				this.application.switchView(this.application.VIEW_HIGH_SCORES);
				break;
				
			default:
				play("error.snd");
				break;
		}
		
		return super.handleKeyPress(code, rawcode);
	}
	
	
	/** Create the views that make up the menu.
	 * The views are created based on the ROWS and COLS, and assigned 
	 * the character that belongs in that location.
	 */
	private void buildMenu() {
		this.menu = new View[COLS][ROWS];
		
		for (int x = 0; x < COLS; x++) {
			for (int y = 0; y < ROWS; y++) {
				this.menu[x][y] = new TextBoxView(this, menuX + x*30, menuY + y*30, 30, 30, getCharAt(x, y), "system-24.font");
			}
		}
	}
	
	
	/** Determine what character is at the specified coordinates.
	 * @param x the cursor x position.
	 * @param y the cursor y position.
	 * @return character at the specified location.
	 */
	private String getCharAt(int x, int y) {
		return this.chars[x + y*COLS];
		
	}
	
	
	/** Move the cursor on the screen.
	 * The cursor is moved to the new coordinates.
	 */
	private void moveCursor() {
		play("updown.snd");
		this.cursor.setLocation(menuX + cursorX * menuSize, menuY + cursorY * menuSize);
	}
}

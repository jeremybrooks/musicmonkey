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
import com.tivo.hme.util.Ticker;
import com.tivo.hme.sdk.Resource;

// LOGGING
import org.apache.log4j.Logger;

/**
 *
 * @author  jeremyb
 */
public class MenuView extends View implements Ticker.Client {
	
	/** Logging. */
	private Logger logger = Logger.getLogger(MenuView.class);
	
	/** Reference to the main application. */
	private MusicMonkey application;
	
	/** How many times the ticker has fired. */
	private long count = 0;
	
	/** Which help message should be displayed. */
	private int helpIndex = 0;
	
	/** The list of help messages.  These messages get displayed periodically
	 * at the bottom of the screen. 
	 */
	private String[] helpMessages;
	
	
	/** Menu item for one player game. */
	private MenuItemView onePlayer;
	
	/** Menu item for two player game. */
	private MenuItemView twoPlayer;
	
	/** Menu item for high score view. */
	private MenuItemView highMonkey;
	
	/** Menu item for help message. */
	private MenuItemView helpMonkey;
	
	/** Display a message in the middle of the menu. */
	private TextBoxView messageBox;
	
	/** Display help messages. */
	private TextBoxView helpBox;
	
	/** Which menu item is selected. */
	private int selected = -1;
	
	/** Creates a new instance of MenuView.
	 * @param parent the parent View.
	 * @param application reference to the main application.
	 */
	public MenuView(View parent, MusicMonkey application) {
		super(parent, 0, 0, parent.getWidth(), parent.getHeight());
		this.application = application;
		
		int centerX = parent.getWidth()/2;
		int centerY = parent.getHeight()/2;
		
		// fade in the title
		setTransparency(1f);
		setResource("images/menutitle.png", RSRC_VALIGN_TOP);
		setTransparency(0f, getResource("*1000"));
		
		// these menu items are offset depending on where they get displayed
		onePlayer = new MenuItemView(this, centerX-25-150, centerY-25, 50, 50, getResource("images/onemonkey.png"));	
		
		twoPlayer = new MenuItemView(this, centerX-25+150, centerY-25, 50, 50, getResource("images/twomonkeys.png"));
		
		highMonkey = new MenuItemView(this, centerX-25, centerY-25-80, 50, 50, getResource("images/highmonkey.png"));
		
		helpMonkey = new MenuItemView(this,centerX-25, centerY-25+80, 50, 50, getResource("images/helpmonkey.png"));
		
		messageBox = new TextBoxView(this, centerX-100, centerY-10, 200, 20, " ");
		
		helpBox = new TextBoxView(this, 0, this.getHeight()-50, this.getWidth(), 20, "");
		helpBox.fadeOut();
		
		Ticker.master.add(this, System.currentTimeMillis() + 5000, null);
		
		// help messages
		this.helpMessages = new String[] {
			"Use the direction keys to select an icon",
			"Press Select to make a choice",
			"Press Clear to exit Music Monkey",
			"Music Monkey " + application.getProperty("musicmonkey.version") + " by Jeremy Brooks",
			"Now serving " + application.getSongList().size() + " songs."
		};
	
		// show the menu items
		onePlayer.fadeIn();
		twoPlayer.fadeIn();
		highMonkey.fadeIn();
		helpMonkey.fadeIn();
	}
	
	
	/** Handle key presses.
	 * This view handles the direction buttons and the select key.
	 * Direction buttons cause the selected menu item to 
	 * and fade in, while the other menu items fade out slightly.
	 * When the select button is pressed, the currently selected menu item
	 * stays selected, and the other menu items go away.  The next view is
	 * then displayed.
	 *
	 */
	public boolean handleKeyPress(int code, long rawcode) {
		
		switch (code) {
			
			case KEY_CLEAR: 
				application.switchView(application.VIEW_EXIT);
				break;
				
			case KEY_LEFT:
				play("updown.snd");
				logger.debug("Handling KEY_LEFT");
				selected = code;
				onePlayer.select();
				twoPlayer.deselect();
				highMonkey.deselect();
				helpMonkey.deselect();
				messageBox.setMessage("one monkey player");
				break;
				
			case KEY_RIGHT:
				play("updown.snd");
				logger.debug("Handling KEY_RIGHT");
				selected = code;
				onePlayer.deselect();
				twoPlayer.select();
				highMonkey.deselect();
				helpMonkey.deselect();
				messageBox.setMessage("two monkey players");
				break;
				
			case KEY_UP:
				play("updown.snd");
				logger.debug("Handling KEY_UP");
				selected = code;
				onePlayer.deselect();
				twoPlayer.deselect();
				highMonkey.select();
				helpMonkey.deselect();
				messageBox.setMessage("view high monkey scores");
				break;
				
			case KEY_DOWN:
				play("updown.snd");
				logger.debug("Handling KEY_DOWN");
				selected = code;
				onePlayer.deselect();
				twoPlayer.deselect();
				highMonkey.deselect();
				helpMonkey.select();
				messageBox.setMessage("get some monkey help");
				break;
				
			case KEY_SELECT:
				play("select.snd");
				logger.debug("Handling KEY_SELECT");
				switch(selected) {
					case KEY_LEFT:
						setTransparency(1.0f, getResource("*1000"));
						twoPlayer.fadeOut();
						highMonkey.fadeOut();
						helpMonkey.fadeOut();
						messageBox.fadeOut();
						helpBox.fadeOut();
						Ticker.master.remove(this, null);
						application.switchView(application.VIEW_ONE_PLAYER_GAME_TYPE);
						break;
					case KEY_RIGHT:
						setTransparency(1.0f, getResource("*1000"));
						onePlayer.fadeOut();
						highMonkey.fadeOut();
						helpMonkey.fadeOut();
						messageBox.fadeOut();
						helpBox.fadeOut();
						Ticker.master.remove(this, null);
						application.switchView(application.VIEW_TWO_PLAYER_GAME_TYPE);
						break;
					case KEY_UP:
						setTransparency(1f, getResource("*1000"));
						onePlayer.fadeOut();
						twoPlayer.fadeOut();
						helpMonkey.fadeOut();
						messageBox.fadeOut();
						helpBox.fadeOut();
						Ticker.master.remove(this, null);
						application.switchView(application.VIEW_HIGH_SCORES);
						break;
					case KEY_DOWN:
						setTransparency(1f, getResource("*1000"));
						onePlayer.fadeOut();
						twoPlayer.fadeOut();
						highMonkey.fadeOut();
						messageBox.fadeOut();
						helpBox.fadeOut();
						Ticker.master.remove(this, null);
						application.switchView(application.VIEW_HELP);
						break;
					default:
						// no valid selection has been made yet
						break;
				}
				
			
				
			default:
				// we don't handle this keypress
				break;
		}
		
		
		return super.handleKeyPress(code, rawcode);
	}
	
	
	/** Display messages at the bottom of the screen periodically.
	 */
	public long tick(long param, Object obj) {

		count++;
		
		// switch to high score view after 60 seconds
		if (count == 12) {
			count = 0;
			Ticker.master.remove(this, null);
			this.application.switchView(this.application.VIEW_HIGH_SCORES);
			
		// display a help message on odd counts
		} else if (count % 2 != 0) {
			this.helpBox.setMessage(helpMessages[helpIndex]);
			helpIndex++;
			if (helpIndex == helpMessages.length) {
				helpIndex = 0;
			}
			
		// hide the help message on even counts
		} else {
			this.helpBox.fadeOut();
		}
		
		return System.currentTimeMillis() + 5000;
	}
	
}

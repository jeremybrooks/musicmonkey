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

/** Allow the player to select the game type for a one player game.
 * Game types are:
 * <ul><li> Unlimited Time
 *     <li> 5 Minute Limit
 * </ul>
 *
 * @author  jeremyb
 */
public class OnePlayerGameTypeView extends View {
		
	/** Reference to the main application. */
	private MusicMonkey application;
	
	/** Game type views. */
	private TextBoxView[] gameTypeViews;
	
	/** Text. */
	private final static String[] gameTypes = {
		"3 Guesses",
		"5 Minutes"
	};
	
	/** Cursor position. */
	private int cursor = 0;
	
	/** Icon. */
	private View onePlayer;
	
	/** Help message. */
	private TextBoxView helpBox;
	
	/** Prompt. */
	private TextBoxView prompt;
	
	
	/** Creates a new instance of OnePlayerGameTypeView.
	 * @param parent the parent View.
	 * @param application reference to the main application.
	 */
	public OnePlayerGameTypeView(View parent, MusicMonkey application) {
		super(parent, 0, 0, parent.getWidth(), parent.getHeight());
		
		this.application = application;
		
		// fade in the title
		setTransparency(1f);
		setResource("images/menutitle.png", RSRC_VALIGN_TOP);
		setTransparency(0f, getResource("*1000"));
		
		// show the icon
		onePlayer = new View(this, this.getWidth()/2-25-150, this.getHeight()/2-25, 50, 50);
		onePlayer.setResource(getResource("images/onemonkey.png"));
	
		// show the selections
		this.gameTypeViews = new TextBoxView[2];
		for (int i = 0; i < this.gameTypeViews.length; i++) {
			this.gameTypeViews[i] = new TextBoxView(this, 0, 130 + i * 30, this.getWidth(), 30, this.gameTypes[i], "default-20.font");
			this.gameTypeViews[i].fadeHalf();
		}
	
		this.gameTypeViews[0].fadeIn();
		
		prompt = new TextBoxView(this, 0, 50, this.getWidth(), 70, "Select Game Type", "system-16.font");
		
		helpBox = new TextBoxView(this, 0, this.getHeight()-50, this.getWidth(), 20, "Up/Down to select game type.  Select to start the game.");
	}
	
	
	/** Handle user input.
	 * Switch to main menu when the user presses a key.
	 */
	public boolean handleKeyPress(int code, long rawcode) {
		
		switch (code) {
			case KEY_UP:
				play("updown.snd");
				this.gameTypeViews[cursor].fadeHalf(getResource("*300"));
				this.cursor -= 1;
				if (cursor < 0) {
					cursor = this.gameTypeViews.length - 1;
				}
				this.gameTypeViews[cursor].fadeIn(getResource("*300"));
				break;
				
			case KEY_DOWN:
				play("updown.snd");
				this.gameTypeViews[cursor].fadeHalf(getResource("*300"));
				this.cursor += 1;
				if (cursor == this.gameTypeViews.length) {
					cursor = 0;
				}
				this.gameTypeViews[cursor].fadeIn(getResource("*300"));
				break;
				
			case KEY_SELECT:
				play("select.snd");
				application.setOnePlayerGameType(cursor);
				for (int i = 0; i < this.gameTypeViews.length; i++) {
					if (i != this.cursor) {
						this.gameTypeViews[i].fadeOut();
					}
				}
				
				this.setTransparency(1.0f, getResource("*1000"));
				
				application.switchView(application.VIEW_ONE_PLAYER);
				break;
				
			default:
				break;
		}
		
		
		return super.handleKeyPress(code, rawcode);
	}
		
	
}

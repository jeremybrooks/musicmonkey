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

/** Display help for the application.
 * The help is actually a .png with all the text in it.
 *
 * @author  jeremyb
 */
public class HelpView extends View {
		
	/** Reference to the main application. */
	private MusicMonkey application;
	
	/** Creates a new instance of HelpView.
	 * @param parent the parent View.
	 * @param application reference to the main application.
	 */
	public HelpView(View parent, MusicMonkey application) {
		super(parent, 0, 0, parent.getWidth(), parent.getHeight());
		
		this.application = application;
		
		// fade in the title
		setTransparency(1f);
		setResource("images/helptitle.png", RSRC_VALIGN_TOP);
		setTransparency(0f, getResource("*1000"));
	}
	
	
	/** Handle user input.
	 * Switch to main menu when the user presses a key.
	 */
	public boolean handleKeyPress(int code, long rawcode) {
		play("select.snd");
		
		// fade out
		setTransparency(1f, getResource("*1000"));
		
		application.switchView(application.VIEW_MENU);
		
		return super.handleKeyPress(code, rawcode);
	}
		
	
}

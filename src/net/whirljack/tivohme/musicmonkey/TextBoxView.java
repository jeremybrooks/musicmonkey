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

// TIVO HME
import com.tivo.hme.sdk.View;
import com.tivo.hme.sdk.Resource;


/** A text box, with helper methods to manipulate the text displayed.
 *
 * @author  jeremyb
 */
public class TextBoxView extends View {
	
	/** Value to make the View visible. */
	private static final float OPAQUE = 0f;
	
	/** Value to make the View transparent. */
	private static final float TRANSPARENT = 1f;
	
	/** Value to make the View half-visible. */
	private static final float HALF = 0.4f;
	
	/** Default font. */
	private static final String DEFAULT_FONT = "system-12.font";
	
	/** Font to use. */
	private String font;
	
	/** Color to use. */
	private Color color;
	
	/** Default color. */
	private static final Color DEFAULT_COLOR = Color.WHITE;
	
	/** If true, message is showing. */
	private boolean opaque = true;
	
	/** Animation resource. */
	private Resource anim = getResource("*1000");
	
	
	
	/** Create a text box view using the default font.
	 * @param parent the parent view.
	 * @param x the X position of the text box.
	 * @param y the Y position of the text box.
	 * @param width the width of the text box.
	 * @param height the height of the text box.
	 * @param message the message to display.
	 */
	public TextBoxView(View parent, int x, int y, int width, int height, String message) {
		this(parent, x, y, width, height, message, null, null);
	}
	
	/** Create a text box view using the default font.
	 * @param parent the parent view.
	 * @param x the X position of the text box.
	 * @param y the Y position of the text box.
	 * @param width the width of the text box.
	 * @param height the height of the text box.
	 * @param message the message to display.
	 * @param font the font to use.
	 */
	public TextBoxView(View parent, int x, int y, int width, int height, String message, String font) {
		this(parent, x, y, width, height, message, font, null);
	}
	
	
	/** Create a text box view using the default font.
	 * @param parent the parent view.
	 * @param x the X position of the text box.
	 * @param y the Y position of the text box.
	 * @param width the width of the text box.
	 * @param height the height of the text box.
	 * @param message the message to display.
	 * @param color the color to use.
	 */
	public TextBoxView(View parent, int x, int y, int width, int height, String message, Color color) {
		this(parent, x, y, width, height, message, null, color);
	}
	
	/** Create a text box view with a specific font. 
	 * @param parent the parent view.
	 * @param x the X position of the text box.
	 * @param y the Y position of the text box.
	 * @param width the width of the text box.
	 * @param height the height of the text box.
	 * @param message the message to display.
	 * @param font the font to use.  If null, the default font is used.
	 * @param color the color to use.  If null, the default color is used.
	 */
	public TextBoxView(View parent, int x, int y, int width, int height, String message, String font, Color color) {
		super(parent, x, y, width, height);
		
		if (font == null) {
			this.font = DEFAULT_FONT;
		} else {
			this.font = font;
		}
		
		if (color == null) {
			this.color = DEFAULT_COLOR;
		} else {
			this.color = color;
		}
		
		this.setResource(createText(this.font, this.color, message));
	}
	
	
	
	
	/** Display a message using default resource flags.
	 * The current message (if any) will be faded out, and the new
	 * message faded in.
	 * @param message the message to be displayed.
	 */
	public void setMessage(String message) {
		this.setMessage(message, -999);
	}
	
	
	/** Display a message using specific resource flags.
	 * The current message (if any) will be faded out, and the new
	 * message faded in.
	 * @param message the message to be displayed.
	 * @param resourceFlags the resource flags to use.
	 */
	public void setMessage(String message, int resourceFlags) {
		this.setTransparency(TRANSPARENT, anim);
		if (resourceFlags == -999) {
			this.setResource(createText(this.font, color, message));
		} else {
			this.setResource(createText(this.font, color, message), resourceFlags);
		}
		this.setTransparency(OPAQUE, anim);
		this.opaque = true;
	}
	
	
	/** Make the current message invisible. */
	public void fadeOut() {
		this.setTransparency(TRANSPARENT, anim);
		this.opaque = false;
	}
	
	
	/** Make the current message visible. */
	public void fadeIn() {
		this.setTransparency(OPAQUE, anim);
		this.opaque = true;
	}
	
	
	/** Make the current message visible, using a specific animation. 
	 * @param animation the resource to use.
	 */
	public void fadeIn(Resource animation) {
		this.setTransparency(OPAQUE, animation);
		this.opaque = true;
	}
	
	
	/** Make the current message half-visible. */
	public void fadeHalf() {
		this.setTransparency(HALF, anim);
	}
	
	
	/** Make the current message half-visible, using a specific animation.
	 * @param animation the resource to use.
	 */
	public void fadeHalf(Resource animation) {
		this.setTransparency(HALF, animation);
	}
	
	/** Set the color to use.
	 * @param color color to use.
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	
	
	/** Determine if the message is currently visible.
	 * @return true if the message is visible, false otherwise.
	 */
	public boolean isOpaque() {
		return this.opaque;
	}
}

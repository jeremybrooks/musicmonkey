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


/** Encapsulate a menu item.
 *
 * @author  jeremyb
 */
public class MenuItemView extends View {
	
	/** Initial transparency. */
	private static final float TRANS_INITIAL = 1f;
	
	/** Transparency when selected. */
	private static final float TRANS_SELECT = 0f;
	
	/** Transparency when not selected. */
	private static final float TRANS_DESELECT = .6f;
	
	
	/** Create a new MenuItemView.
	 * @param parent the parent View.
	 * @param x the X coordinate for this view.
	 * @param y the Y coordinate for this view.
	 * @param width the width of this view.
	 * @param height the height of this view.
	 * @param resource the resource to display.
	 */
	public MenuItemView(View parent, int x, int y, int width, int height, Resource resource) {
		super(parent, x, y, width, height);
		this.setResource(resource);

		// start out transparent
		this.setTransparency(TRANS_INITIAL);
	}
	
	
	/** Gradualy make the view visible.
	 */
	public void fadeIn() {
		this.setTransparency(TRANS_DESELECT, getResource("*1000"));
	}
	
	
	/** Gradually make the view invisible.
	 */
	public void fadeOut() {
		this.setTransparency(TRANS_INITIAL, getResource("*1000"));
	}
	
	
	/** Show the view selected quickly.
	 */
	public void select() {
		this.setTransparency(TRANS_SELECT, getResource("*300"));
	}
	
	
	/** Show the view deslected quickly.
	 */
	public void deselect() {
		this.setTransparency(TRANS_DESELECT, getResource("*300"));
	}
}

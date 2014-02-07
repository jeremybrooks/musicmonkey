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

// JAVA IO
import java.io.Serializable;

// JAVA UTIL
import java.util.Comparator;
	

/** Comparator to sort high scores in reverse order. */
public class HighScoreComparator implements Comparator, Serializable {

	
	/** Compare high scores, in reverse order.
	 * Returns a negative integer, zero, or a positive integer 
	 * as the first argument is greater than, equal to, or less than the second.
	 */
	public int compare(Object o1, Object o2) {
		long score1 = ((HighScore)o1).getScore();
		long score2 = ((HighScore)o2).getScore();

		int ret = 0;
		if (score1 < score2) {
			ret = 1;

		} else if (score1 > score2) {
			ret = -1;
		}

		return ret;
	}

}
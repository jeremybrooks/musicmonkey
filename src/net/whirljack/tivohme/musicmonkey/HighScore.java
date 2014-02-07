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
import java.util.Date;
import java.text.SimpleDateFormat;

/** Encapsulates high score data.
 *
 * @author  jeremyb
 */
public class HighScore implements Serializable {
	
	/** The score. */
	private long score;
	
	/** Player name. */
	private String player;

	/** The date of this score. */
	private Date date;
	
	/** Number of rounds. */
	private int rounds;
	
	/** Make the date look nice. */
	private static final SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
	
	/** Creates a new instance of HighScore */
	public HighScore() {
	}
	
	/**
	 * Getter for property date.
	 * @return Value of property date.
	 */
	public java.util.Date getDate() {
		return date;
	}
	
	/**
	 * Setter for property date.
	 * @param date New value of property date.
	 */
	public void setDate(java.util.Date date) {
		this.date = date;
	}
	
	/**
	 * Getter for property player.
	 * @return Value of property player.
	 */
	public java.lang.String getPlayer() {
		return player;
	}
	
	/**
	 * Setter for property player.
	 * @param player New value of property player.
	 */
	public void setPlayer(java.lang.String player) {
		this.player = player;
	}
	
	/**
	 * Getter for property score.
	 * @return Value of property score.
	 */
	public long getScore() {
		return score;
	}
	
	/**
	 * Setter for property score.
	 * @param score New value of property score.
	 */
	public void setScore(long score) {
		this.score = score;
	}
	
	
	/** Format the data nicely.
	 *
	 * @return the score, player, and date.
	 */
	public String toString() {
		StringBuffer buf = new StringBuffer();
		
		buf.append(this.getScore()).append("  ");
		buf.append(this.getPlayer()).append("  ");
		buf.append(formatter.format(this.getDate())).append(" ");
		buf.append(this.getRounds()).append(" rounds");
		
		return buf.toString();
	}
	
	/**
	 * Getter for property rounds.
	 * @return Value of property rounds.
	 */
	public int getRounds() {
		return rounds;
	}
	
	/**
	 * Setter for property rounds.
	 * @param rounds New value of property rounds.
	 */
	public void setRounds(int rounds) {
		this.rounds = rounds;
	}
	
}

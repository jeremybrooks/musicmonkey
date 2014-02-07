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

/** Encapsulates song data.
 *
 * @author  jeremyb
 */
public class Song {
	
	/** The name of the artist. */
	private String artist;
	
	/** The name of the album. */
	private String album;
	
	/** The Title of the track. */
	private String title;
	
	/** The location of the song. */
	private String location;
	
	
	
	/** Create a new instance of Song. */
	public Song() {
	}
	
	
	/**
	 * Getter for property album.
	 * @return Value of property album.
	 */
	public java.lang.String getAlbum() {
		return album;
	}	
	
	
	/**
	 * Setter for property album.
	 * @param album New value of property album.
	 */
	public void setAlbum(java.lang.String album) {
		this.album = album;
	}
	
	
	/**
	 * Getter for property artist.
	 * @return Value of property artist.
	 */
	public java.lang.String getArtist() {
		return artist;
	}
	
	
	/**
	 * Setter for property artist.
	 * @param artist New value of property artist.
	 */
	public void setArtist(java.lang.String artist) {
		this.artist = artist;
	}
	
	/**
	 * Getter for property location.
	 * @return Value of property location.
	 */
	public java.lang.String getLocation() {
		return location;
	}
	
	
	/**
	 * Setter for property location.
	 * @param location New value of property location.
	 */
	public void setLocation(java.lang.String location) {
		this.location = location;
	}
	
	
	/**
	 * Getter for property title.
	 * @return Value of property title.
	 */
	public java.lang.String getTitle() {
		return title;
	}
	
	/**
	 * Setter for property title.
	 * @param title New value of property title.
	 */
	public void setTitle(java.lang.String title) {
		this.title = title;
	}
	
	
	/** Get the song data as a string.
	 */
	public String getSongInfo() {
		StringBuffer buf = new StringBuffer();
		buf.append(getTitle()).append(" by ");
		buf.append(getArtist()).append(" from ");
		buf.append(getAlbum()).append("; location ");
		buf.append(getLocation());

		return buf.toString();
	}
}

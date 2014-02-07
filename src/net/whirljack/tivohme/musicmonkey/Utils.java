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
import java.io.File;

// JAVA UTIL
import java.util.ArrayList;
import java.util.List;

// MP3 LIBRARY
import org.blinkenlights.jid3.MediaFile;
import org.blinkenlights.jid3.MP3File;
import org.blinkenlights.jid3.v1.ID3V1Tag;
import org.blinkenlights.jid3.v2.ID3V2Tag;

// LOGGING
import org.apache.log4j.Logger;


/**
 *
 * @author  jeremyb
 */
public class Utils {
	
	private static Logger logger = Logger.getLogger(Utils.class);
	
	private static int bad1;
	private static int bad2;
	
	/** Creates a new instance of Utils */
	private Utils() {
	}
	
	
	
	public static void index(String dir, List list) {
		File[] files = new File(dir).listFiles();
		logger.debug("Indexing " + dir);
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				File f = files[i];
				if (f.isDirectory()) {
					index(dir + System.getProperty("file.separator") + f.getName(), list);
				} else {
					getTag(dir, f, list);
				}
			}
		}
	}
	
	private static void getTag(String directory, File f, List list) {
		try {
			if (f.getName().endsWith(".mp3")) {
				logger.debug("Found mp3 file " + f.getName() + " in " + directory);
				Song s = new Song();
				MediaFile mediaFile = new MP3File(f);
				ID3V2Tag tag2 = mediaFile.getID3V2Tag();
				if (tag2 != null) {
					logger.debug("Got a V2 tag.");
					try {
						s.setTitle(tag2.getTitle());
						s.setArtist(tag2.getArtist());
						s.setAlbum(tag2.getAlbum());
						s.setLocation(directory + System.getProperty("file.separator") + f.getName());
						list.add(s);
						logger.debug("Got Version 2 tags:" + s.getSongInfo());
						
					} catch (Exception e) {
						logger.debug("Could not get V2 tags, trying V1.", e);
						ID3V1Tag tag1 = mediaFile.getID3V1Tag();
						if (tag1 == null) {
							throw new Exception("COULD NOT GET V1 TAG.");
						}
						s.setTitle(tag1.getTitle());
						s.setArtist(tag1.getArtist());
						s.setAlbum(tag1.getAlbum());
						s.setLocation(directory + System.getProperty("file.separator") + f.getName());
						list.add(s);
						logger.debug("Got Version 1 tags: " + s.getSongInfo());
					}
				}
			}
		} catch (Exception e) {
			bad1++;
			System.out.println("bad1="+bad1);
			logger.error("Error getting tags for file " + f.getName() + " in directory " + directory, e);
		}
	}
	
}

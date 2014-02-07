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

// JAVA NET
import java.net.URLEncoder;

// TIVO HME
import com.tivo.hme.sdk.View;
import com.tivo.hme.sdk.Resource;
import com.tivo.hme.sdk.StreamResource;
import com.tivo.hme.sdk.HmeEvent;

// LOGGING
import org.apache.log4j.Logger;


/** Encapsulates the logic needed to play music tracks.
 * To play a track, call the setTrack method, and then the play method.
 * To stop the track, call stop.  If the loop flag is true, the track will
 * repeat when it has completed playing.
 *
 * @author  jeremyb
 */
public class Track extends View {
		
	/** Logging. */
	private Logger logger = Logger.getLogger(Track.class);
	
	/** Loop flag, false by default. */
	private boolean loop = false;
	
	/** URL of the track to play. */
	private String url;
	
	/** Reference to the main application. */
	private MusicMonkey application;
	
	/** Creates a new instance of Track */
	public Track(View parent, MusicMonkey application) {
		super(parent, 0, 0, parent.getWidth(), parent.getHeight());
		this.application = application;
	}
	
	
	/** Play the current track.
	 * If no track has been set, this method does nothing.
	 * If there is a current resource, it is removed.
	 */
	public void play() {
		if (getResource() != null) {
			getResource().remove();
		}
		
		if (this.url != null) {
			setResource(createStream(this.url, null, true));
		}
	}
	
	
	/** Set the track to play.
	 * The current track (if any) is stopped.  A URL is then created for the
	 * track based on the application base.  To start the new track, call
	 * the play method.
	 * This normally is the location of the song on disk, as retrieved from
	 * Song.getLocation().
	 *
	 * @param track the track to play.
	 */
	public void setTrack(String track) {
		// stop the current track
		stop();
		
		// create a URL for the track
		this.url = getApp().getContext().getBase().toString();
		try {
			this.url += URLEncoder.encode(track, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/** Stop the current track.
	 * The resource stream is paused, and the resource is removed.
	 */
	public void stop() {
		if (getResource() != null) {
			((StreamResource)getResource()).pause();
			getResource().remove();
//			getResource() = null;
		}
	}
	
	
	/** Set the loop flag.
	 * If loop is true, the track will start playing again as soon as
	 * it is complete.
	 * 
	 * @param loop true if the track should loop.
	 */
	public void setLoop(boolean loop) {
		this.loop = loop;
	}
	
	
	/** Get the current loop flag setting.
	 * 
	 * @return true if the track will loop, false otherwise.
	 */
	public boolean isLoop() {
		return this.loop;
	}
	
	
	/** Watch for the RSRC_STATUS_COMPLETE event, and start the track over
	 * if the loop flag is true.
	 *
	 * @event the event.
	 * @return true if the event is handled.
	 */
	public boolean handleEvent(HmeEvent event) {
		if (((StreamResource)getResource()).getStatus() == RSRC_STATUS_COMPLETE) {
			logger.debug("Track " + this.url + " has finished playing.");
			if (this.loop) {
				logger.debug("Loop is true, restarting background music.");
				getResource().remove();
//				resource = null;
				this.application.startBackgroundMusic();
			}
		}
		
		return super.handleEvent(event);
	}
}

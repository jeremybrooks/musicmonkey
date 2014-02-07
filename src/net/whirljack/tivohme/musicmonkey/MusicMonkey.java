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
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

// JAVA NET
import java.net.URLEncoder;

// JAVA UTIL
import java.util.Arrays;
import java.util.Properties;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.HashMap;

// TIVO HME
import com.tivo.hme.sdk.Application;
import com.tivo.hme.sdk.View;
import com.tivo.hme.sdk.Factory;
import com.tivo.hme.util.ArgumentList;

// LOGGING
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;



/** 
 *
 * @author  jeremyb
 */
public class MusicMonkey extends Application {
	
	/** Logging. */
	private Logger logger = Logger.getLogger(MusicMonkey.class);
	
	/** Properties */
	private Properties myProps;
	
	/** Title of the application. */
	public final static String TITLE = "Music Monkey";
	
	private long twoPlayerGameType = 0;
	private long onePlayerGameType = 0;
	
	private View content;
	
	/** Views used to show different game screens. */
	private MenuView menuView;
	private HelpView helpView;
	private HighScoreView highView;
	private OnePlayerView onePlayerView;
	private EnterHighScoreView enterHighScoreView;
	private TwoPlayerView twoPlayerView;
	private TwoPlayerGameTypeView twoPlayerGameTypeView;
	private OnePlayerGameTypeView onePlayerGameTypeView;
	
	private Track musicTrack;
	private long newHigh = 0;
	private int round = 0;
	private int numPlayers = 0;
	
	private List themeSongs;
	
	/** Current game type. */
	private String currentGameType;
	
	// CONSTANTS FOR THE DIFFERENT GAME VIEWS
	public static final int VIEW_MENU = 0;
	public static final int VIEW_HIGH_SCORES = 1;
	public static final int VIEW_HELP = 2;
	public static final int VIEW_ONE_PLAYER = 3;
	public static final int VIEW_ENTER_HIGH_SCORE = 4;
	public static final int VIEW_TWO_PLAYER = 5;
	public static final int VIEW_TWO_PLAYER_GAME_TYPE = 6;
	public static final int VIEW_ONE_PLAYER_GAME_TYPE = 7;
	public static final int VIEW_EXIT = 99;
	
	// constants for game types -- used to get high score maps
	public static final String GAME_TYPE_SINGLE = "musicmonkey.scores.single";
	public static final String GAME_TYPE_SINGLE_TIMED = "musicmonkey.scores.timed";
	public static final String GAME_TYPE_10_ROUNDS = "musicmonkey.scores.10rounds";
	public static final String GAME_TYPE_25_ROUNDS = "musicmonkey.scores.25rounds";
	public static final String GAME_TYPE_50_ROUNDS = "musicmonkey.scores.50rounds";
	public static final String GAME_TYPE_POINTS = "pointsonly";
	
	/** Creates a new instance of MusicMonkey */
	public MusicMonkey() {
	}
	
	
	/** Start the application. 
	 * @param context application context.
	 */
	protected void init(Context context) {
		
		// create the temp track dir
		File tempdir = new File(getProperty("musicmonkey.temptrack.dir"));
		if (! tempdir.exists()) {
			tempdir.mkdirs();
		}
		
		
		// default game type to single
		this.currentGameType = GAME_TYPE_SINGLE;
		
		// set image on the root view
		getRoot().setResource(getResource("images/spiral.png"));
		
		// create a view in the display safe area
		content = new View(getRoot(), SAFE_ACTION_H, SAFE_ACTION_V,
                           getRoot().getWidth() - (SAFE_ACTION_H * 2),
                           getRoot().getHeight() - (SAFE_ACTION_V * 2));
				
		this.themeSongs = ((MusicMonkeyFactory)context.getFactory()).getThemeSongList();
		
		this.musicTrack = new Track(content, this);
		this.startBackgroundMusic();
		
		// show the menu
		switchView(VIEW_MENU);
	}
	
	
	void switchView(int view) {
		logger.debug("Switching to view " + view);
		
		// allow any previous commands to finish
		flush();
		
		try {
			Thread.sleep(1500);
		} catch (Exception e) {
			logger.warn("Error in sleep.", e);
		}
		
		this.removeViews();
		
		switch (view) {
			case VIEW_MENU:
				menuView = new MenuView(content, this);
				menuView.setFocus();
				break;
				
			case VIEW_HIGH_SCORES:
//				HighScore[] highScores = ((MusicMonkeyFactory)context.factory).getHighScores();
				highView = new HighScoreView(content, this);
				highView.setFocus();
				break;
				
			case VIEW_HELP:
				helpView = new HelpView(content, this);
				helpView.setFocus();
				break;
				
			case VIEW_ONE_PLAYER:
				// cleanup temp files before game
				cleanTempDir();
				// don't forget to stop the background music when starting a game!
				this.musicTrack.stop();
				onePlayerView = new OnePlayerView(content, this, this.onePlayerGameType);
				onePlayerView.setFocus();
				onePlayerView.startGame();
				break;

			case VIEW_ENTER_HIGH_SCORE:
				this.enterHighScoreView = new EnterHighScoreView(content, this);
				this.enterHighScoreView.setFocus();
				break;
				
			case VIEW_TWO_PLAYER:
				// cleanup temp files before game
				cleanTempDir();
				// stop background music
				this.musicTrack.stop();
				twoPlayerView = new TwoPlayerView(content, this, this.twoPlayerGameType);
				twoPlayerView.setFocus();
				twoPlayerView.startGame();
				break;
				
			case VIEW_TWO_PLAYER_GAME_TYPE:
				twoPlayerGameTypeView = new TwoPlayerGameTypeView(content, this);
				twoPlayerGameTypeView.setFocus();
				break;
				
			case VIEW_ONE_PLAYER_GAME_TYPE:
				onePlayerGameTypeView = new OnePlayerGameTypeView(content, this);
				onePlayerGameTypeView.setFocus();
				break;
				
			case VIEW_EXIT:
				cleanTempDir();
				setActive(false);
				break;
				
			default:
				break;
		}
	}
	
	
	public boolean handleKeyPress(int code, long rawcode) {
		
		return super.handleKeyPress(code, rawcode);
	}
	
	public int getNumPlayers() {
		return this.numPlayers;
	}
	
	public Track getMusicTrack() {
		return this.musicTrack;
	}
	
	public void setNewHigh(long newHigh) {
		this.newHigh = newHigh;
	}
	public long getNewHigh() {
		return this.newHigh;
	}

	public void setRound(int round) {
		this.round = round;
	}
	public int getRound() {
		return this.round;
	}
	
	public List getSongList() {
		return ((MusicMonkeyFactory)getContext().getFactory()).getSongList();
	}
	
	public String getProperty(String key) {
		return ((MusicMonkeyFactory)getContext().getFactory()).getProperty(key);
	}
	
	
	public HighScore[] getHighScores(String gameType) {
		return ((MusicMonkeyFactory)getContext().getFactory()).getHighScores(gameType);
	}
	
	
	public void setCurrentGameType(String currentGameType) {
		this.currentGameType = currentGameType;
	}
	
	public String getCurrentGameType() {
		return this.currentGameType;
	}
	
	/** This method checks all the views used, 
	 * and removes them if they are not null.
	 */
	private void removeViews() {
		if (this.menuView != null) {
			this.menuView.remove();
			this.menuView = null;
		}
		if (this.helpView != null) {
			this.helpView.remove();
			this.helpView = null;
		}
		if (this.highView != null) {
			this.highView.remove();
			this.highView = null;
		}
		if (this.onePlayerView != null) {
			this.onePlayerView.remove();
			this.onePlayerView = null;
		}
		if (this.enterHighScoreView != null) {
			this.enterHighScoreView.remove();
			this.enterHighScoreView = null;
		}
		if (this.twoPlayerView != null) {
			this.twoPlayerView.remove();
			this.twoPlayerView = null;
		}
		if (this.twoPlayerGameTypeView != null) {
			this.twoPlayerGameTypeView.remove();
			this.twoPlayerGameTypeView = null;
		}
		if (this.onePlayerGameTypeView != null) {
			this.onePlayerGameTypeView.remove();
			this.onePlayerGameTypeView = null;
		}
	}
	
	
	/** Set the two-player game type.
	 */
	public void setTwoPlayerGameType(int gameType) {
		this.twoPlayerGameType = gameType;
		
		switch (gameType) {
			case 0:
				this.setCurrentGameType(GAME_TYPE_10_ROUNDS);
				break;
			case 1:
				this.setCurrentGameType(GAME_TYPE_25_ROUNDS);
				break;
			case 2:
				this.setCurrentGameType(GAME_TYPE_50_ROUNDS);
				break;
			default:
				this.setCurrentGameType(GAME_TYPE_POINTS);
				break;
		}
	}
	
	
	/** Start some background music.
	 */
	public void startBackgroundMusic() {
		// stop any playing track first
		
		
		// get song from list, refresh list if needed
		if (this.themeSongs.size() == 0) {
			logger.info("Out of background music, refreshing list.");
			this.themeSongs = ((MusicMonkeyFactory)getContext().getFactory()).getThemeSongList();
		}
		int x = (new Random(System.currentTimeMillis()).nextInt(this.themeSongs.size()));
		this.logger.info("Playing background track " + x + " of " + this.themeSongs.size());
		
		Song s = (Song)this.themeSongs.remove(x);
		this.musicTrack.setTrack(s.getLocation());
		this.musicTrack.setLoop(true);
		this.musicTrack.play();
	}
	
	
	/** Work around the missing seek functionality.
	 * This method will create a temporary mp3 file that is 
	 * part of the specified song.  The starting position is chosen
	 * randomly based on the size of the file.  The resulting 
	 * track will consist of 50-100% of the original.
	 *
	 * @track the song to trim.
	 * @return the path to the temp track, to pass to the player class.
	 */
	public String createPartialTrack(String track) {
		File mp3File = new File(track);
		
		FileInputStream fis = null;
		FileOutputStream fos = null;
		String newFilename = getProperty("musicmonkey.temptrack.dir") + System.getProperty("file.separator") + System.currentTimeMillis() + ".mp3";
				
		// random starting position
		Random r = new Random(System.currentTimeMillis());
		
		int size = (int)mp3File.length();
		int start = r.nextInt(size/2);
		int len = size - start;
		
		logger.debug("size = " + size + ", start = " + start + ", len = " + len);
		
		// now write the new track
		byte[] bytes = new byte[size];
		try {
			fis = new FileInputStream(mp3File);
			fis.read(bytes);
			
			// write the new track
			fos = new FileOutputStream(newFilename);
			fos.write(bytes, start, len);
			fos.flush();
			
		} catch (Exception e) {
			logger.error("Error while cutting track " + mp3File.getName(), e);
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (Exception e) {
				logger.warn("Error closing file output stream.", e);
			} finally {
				try {
					if (fis != null) {
						fis.close();
					}
				} catch (Exception e) {
					logger.warn("Error closing file input stream.", e);
				}
			}
		}
		
		logger.debug("Created partial track " + newFilename);
		
		return newFilename;
	}
	
	
	/** Determine if the score is a high score.
	 * @param score the score to check.
	 * @return true if this is a high score.
	 */
	public boolean checkForHighScore(long score, String gameType) {
		HighScore[] scores = ((MusicMonkeyFactory)getContext().getFactory()).getHighScores(gameType);
		boolean highScore = false;
		
		for (int i = 0; i < scores.length; i++) {
			if (score > scores[i].getScore()) {
				highScore = true;
			}
		}
		
		return highScore;
	}
	
	
	/** Add a high score to the list.
	 * The high score to add should be an instance of HighScore with the 
	 * fields set properly.
	 * This is actually done in the Factory, but the rest of the application 
	 * calls this method to shield other classes from Factory changes.
	 *
	 * @param highScore the high score to add.
	 */
	public void addHighScore(HighScore highScore, String gameType) {
		((MusicMonkeyFactory)getContext().getFactory()).addHighScore(highScore, gameType);
	}
	
	
	/** Clean temp tracks.
	 * The temp track directory may get large, and should be cleaned 
	 * regularly.
	 */
	private void cleanTempDir() {
		File tempdir = new File(getProperty("musicmonkey.temptrack.dir"));
		if (tempdir.exists()) {
			File[] files = tempdir.listFiles();
			if (files != null) {
				for (int i = 0; i < files.length; i++) {
					try {
						files[i].delete();
					} catch (Exception e) {
						logger.warn("Error deleting file " + files[i].getName(), e);
					}
				}
			}
		}
	}
	
	/**
	 * Getter for property onePlayerGameType.
	 * @return Value of property onePlayerGameType.
	 */
	public long getOnePlayerGameType() {
		return onePlayerGameType;
	}	
	
	/**
	 * Setter for property onePlayerGameType.
	 * @param onePlayerGameType New value of property onePlayerGameType.
	 */
	public void setOnePlayerGameType(int gameType) {
		this.onePlayerGameType = gameType;
		
		switch (gameType) {
			case 0:
				this.setCurrentGameType(GAME_TYPE_SINGLE);
				break;
			case 1:
				this.setCurrentGameType(GAME_TYPE_SINGLE_TIMED);
				break;
			default:
				this.setCurrentGameType(GAME_TYPE_POINTS);
				break;
		}
	}	
	
	
	
	////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////
	
	
	
	
	
	/** Custom factory class.
	 * This class handles shared data for all instances of MusicMonkey.
	 * Logging is configured here, high score load/save is handled here,
	 * and the music database is built here.
	 */
	public static class MusicMonkeyFactory extends Factory {
		
		/** Application properties. */
		private Properties props;
		
		/** High scores. */
		private HashMap highScoreMap;
		
		/** Songs available. */
		private List songList;
		
		/** Theme songs. */
		private List themeList;
		
		/** Logging. */
		private Logger logger = Logger.getLogger(MusicMonkeyFactory.class);
		
		
		/** Initialize the Factory.
		 */
		protected void init(ArgumentList args) {
			this.props = new Properties();
			this.songList = new ArrayList();
			this.themeList = new ArrayList();
			
			// read properties
			// The properties file must be in the classpath.  Application will
			// not start if the properties file is not found.
			try {
				this.props.load(MusicMonkeyFactory.class.getClassLoader().getResourceAsStream("musicmonkey.properties"));
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("UNABLE TO LOAD PROPERTIES FILE 'musicmonkey.properties' -- CHECK THE CLASSPATH.");
				System.err.println("CANNOT RUN WITHOUT PROPERTIES FILE.  EXITING.");
				System.exit(1);
			}
			
			// initialize logging
			PropertyConfigurator.configure(this.props);
			
			// get the high scores
			this.highScoreMap = new HashMap();
			this.highScoreMap.put(GAME_TYPE_SINGLE, readScoresFromFile(GAME_TYPE_SINGLE));
			this.highScoreMap.put(GAME_TYPE_SINGLE_TIMED, readScoresFromFile(GAME_TYPE_SINGLE_TIMED));
			this.highScoreMap.put(GAME_TYPE_10_ROUNDS, readScoresFromFile(GAME_TYPE_10_ROUNDS));
			this.highScoreMap.put(GAME_TYPE_25_ROUNDS, readScoresFromFile(GAME_TYPE_25_ROUNDS));
			this.highScoreMap.put(GAME_TYPE_50_ROUNDS, readScoresFromFile(GAME_TYPE_50_ROUNDS));
			
			
			// create an index of songs
			indexSongs();
			
			indexThemes();
		}
		
		
		
		/** Index songs.
		 * All the songs in the music directory will be read, and the Artist,
		 * Title, and Album tags extracted.  These tags will be used to 
		 * create Song objects, which are put in the song list.
		 * Files that have bad tags will NOT be added to the list.  The errors
		 * will show up in the log.
		 * If there are no songs in the music directory, the application will
		 * exit.
		 */
		private void indexSongs() {
			String songDir = this.props.getProperty("musicmonkey.music.dir");
			logger.info("Music directory is " + songDir);

			this.songList = new ArrayList();
			Utils.index(songDir, songList);
			
			// if there were no usable songs, exit
			if (this.songList.size() == 0) {
				logger.fatal("NO USABLE FILES IN THE MUSIC DIRECTORY!  CHECK YOUR CONFIGURATION!");
				System.err.println("NO USABLE FILES IN THE MUSIC DIRECTORY!  CHECK YOUR CONFIGURATION!");
				System.exit(1);
			}
		}
		
		
		/** Index songs.
		 * All the songs in the music directory will be read, and the Artist,
		 * Title, and Album tags extracted.  These tags will be used to 
		 * create Song objects, which are put in the song list.
		 * Files that have bad tags will NOT be added to the list.  The errors
		 * will show up in the log.
		 * If there are no songs in the music directory, the application will
		 * exit.
		 */
		private void indexThemes() {
			String songDir = this.props.getProperty("musicmonkey.background_music.dir");
			logger.info("Theme music directory is " + songDir);
			this.themeList = new ArrayList();
			Utils.index(songDir, themeList);
			
			// if there were no usable songs, exit
			if (this.themeList.size() == 0) {
				logger.fatal("NO USABLE FILES IN THE THEME MUSIC DIRECTORY!  CHECK YOUR CONFIGURATION!");
				System.err.println("NO USABLE FILES IN THE THEME MUSIC DIRECTORY!  CHECK YOUR CONFIGURATION!");
				System.exit(1);
			}
		}
		
		
		/** Get a property.
		 * @param key the key to look up.
		 * @return value of the key, or null if the key does not exist.
		 */
		public String getProperty(String key) {
			return this.props.getProperty(key);
		}
		
				
		/** Get the high scores for a specific game type.
		 * @return high score array.
		 */
		public HighScore[] getHighScores(String gameType) {
			return (HighScore[]) this.highScoreMap.get(gameType);
		}
		
		
		/** Add a high score to the array.
		 * A copy of the current array is made, and the new score is added
		 * to it.  Then the array is sorted, and the lowest score is removed.
		 * After this, the remaining scores are copied to the current
		 * high score array, and it is saved to disk.
		 * @param hs the high score to add.
		 */
		public void addHighScore(HighScore hs, String gameType) {
			HighScore[] currentScores = getHighScores(gameType);
			
			HighScore[] newScores = new HighScore[currentScores.length + 1];
			for (int i = 0; i < currentScores.length; i++) {
				newScores[i] = currentScores[i];
			}
			newScores[currentScores.length] = hs;
			
			Arrays.sort(newScores, new HighScoreComparator());
			
			for (int i = 0; i < currentScores.length; i++) {
				currentScores[i] = newScores[i];
			}
			
			this.writeHighScores(currentScores, gameType);
			currentScores = null;
			newScores = null;
		}
		
		
		/** Get a copy of the song list.
		 * @return list of available songs.
		 */
		public List getSongList() {
			return new ArrayList(this.songList);
		}
		
		
		/** Get the theme song list.
		 * @return shuffled copy of theme song list.
		 */
		public List getThemeSongList() {
			return new ArrayList(this.themeList);
		}
		
		
		/** Read high score list from file.
		 * High scores are stored as serialized Array of HighScore objects.
		 */
		private HighScore[] readScoresFromFile(String gameType) {
			String filename = getProperty(gameType);
			logger.debug("Reading scores from file " + filename);
			ObjectInputStream in = null;
			HighScore[] retArray = null;
			
			File f = new File(filename);
			
			try {
				if (f.exists()) {
					in = new ObjectInputStream(new FileInputStream(f));
					retArray = (HighScore[])in.readObject();
					logger.debug("Read high scores from disk.");
				} else {
					
					// high score list isn't there, so make some up
					logger.debug("Creating new high scores.");
					retArray = new HighScore[5];
					for (int i = 0; i < 5; i++) {
						HighScore hs = new HighScore();
						hs.setScore(0);
						hs.setPlayer("MONKEY");
						hs.setDate(new java.util.Date());
						hs.setRounds(0);
						retArray[i] = hs;
						hs = null;
					}
					writeHighScores(retArray, gameType);
				}
			} catch (Exception e) {
				logger.error("ERROR READING HIGH SCORES FROM FILE " + f.getAbsolutePath(), e);
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (Exception e) {
						logger.error("ERROR CLOSING INPUT STREAM.", e);
					} finally {
						in = null;
					}
				}
			}
			
			return retArray;
		}
		
		
		/** Write high score list to file.
		 */
		private void writeHighScores(HighScore[] scores, String gameType) {
			ObjectOutputStream out = null;
			
			File f = new File(this.props.getProperty(gameType));
			logger.debug("Writing scores for game type " + gameType + " to file " + f.getName());
			
			try {
				out = new ObjectOutputStream(new FileOutputStream(f));
				out.writeObject(scores);
				out.flush();
				logger.debug("High score list saved.");
			} catch (Exception e) {
				logger.error("ERROR WRITING HIGH SCORE FILE " + f.getAbsolutePath(), e);
			} finally {
				if (out != null) {
					try {
						out.close();
					} catch (Exception e) {
						logger.error("ERROR CLOSING OUTPUT STREAM.", e);
					} finally {
						out = null;
					}
				}
			}
			
		}
	}
}

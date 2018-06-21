package deezer;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Track {
	private String title;
	private String preview;
	private JsonObject artist;
	private transient int id;
	private transient String title_short;
	private transient String title_version;
	private transient String link;
	private transient int duration;
	private transient int rank;
	private transient boolean explicit_lyrics;
	private transient int position;
	private transient Object album;
	private transient String type;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPreview() {
		return preview;
	}
	public void setPreview(String preview) {
		this.preview = preview;
	}
	public JsonObject getArtist() {
		return artist;
	}
	public void setArtist(JsonObject artist) {
		this.artist = artist;
	}
	
	public String fetchArtistName() {
		String name = artist.get("name").toString();
	    return name.substring(1, name.length()-1);
	}
}

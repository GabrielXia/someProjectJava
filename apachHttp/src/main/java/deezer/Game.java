package deezer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import okHttp.Get;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Game {
	OkHttpClient client = new OkHttpClient();
	
	 public String run(String url) throws IOException {
	    Request request = new Request.Builder()
	        .url(url)
	        .build();

	    try (Response response = client.newCall(request).execute()) {
	      return response.body().string();
	    }
	  }
	  
	public static void main(String[] args) throws IOException, LineUnavailableException, UnsupportedAudioFileException, InterruptedException {
		Get example = new Get();
	    String response = example.run("https://api.deezer.com/chart");
	    
	    JsonParser parser = new JsonParser();
	    JsonObject obj = parser.parse(response).getAsJsonObject();
	    JsonElement t = obj.get("tracks");
	    String dataString = t.getAsJsonObject().get("data").toString();
	    
	    Gson gson = new Gson();
	    List<Track> tracks = gson.fromJson(dataString, new TypeToken<List<Track>>(){}.getType());
	    Scanner in = new Scanner(System.in);
	    
	    while(true) {
	    	int index = new Random().nextInt(tracks.size());
	    	Track track = tracks.get(index);
	    	String title = track.getTitle();
	    	String artist = track.fetchArtistName();
		    System.out.println("Listen to the music: " + "( " + artist + " " + title + " )");
		    
		    final URL url = new URL(track.getPreview());
			Runnable newRun = new Runnable() {
		    	public void run() {
		    		testPlay(url); 
		    	} 
		    };
		    Thread newThread = new Thread(newRun);
		    newThread.start();
		    System.out.println("Guess the title");
		    String guessTitle = in.nextLine();
		    if (!title.toLowerCase().contains(guessTitle.toLowerCase())) {
		    	System.out.println("Wrong Answer, guess next music");
		    	newThread.stop();
		    	continue;
		    }
		    System.out.println("Guess the artist");
		    String guessArtisit = in.nextLine();
	        if (!artist.toLowerCase().contains(guessArtisit.toLowerCase())) {
	        	System.out.println("Wrong Answer, guess next music");
	        	newThread.stop();
	        	continue;
	        }
	        try{
	        	newThread.stop();
	        } catch(Exception e){}
	        System.out.println("You answer is rigut! Go to next music");
	    }
	}
	
	public static void testPlay(URL url)
	{
	  try {
	    AudioInputStream in= AudioSystem.getAudioInputStream(url);
	    AudioInputStream din = null;
	    AudioFormat baseFormat = in.getFormat();
	    AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 
	                                                                                  baseFormat.getSampleRate(),
	                                                                                  16,
	                                                                                  baseFormat.getChannels(),
	                                                                                  baseFormat.getChannels() * 2,
	                                                                                  baseFormat.getSampleRate(),
	                                                                                  false);
	    din = AudioSystem.getAudioInputStream(decodedFormat, in);
	    // Play now. 
	    rawplay(decodedFormat, din);
	    in.close();
	  } catch (Exception e)
	    {
	        e.printStackTrace();
	    }
	} 

	private static void rawplay(AudioFormat targetFormat, AudioInputStream din) throws IOException,                                                                                                LineUnavailableException
	{
	  byte[] data = new byte[4096];
	  SourceDataLine line = getLine(targetFormat); 
	  if (line != null)
	  {
	    // Start
	    line.start();
	    int nBytesRead = 0, nBytesWritten = 0;
	    while (nBytesRead != -1)
	    {
	        nBytesRead = din.read(data, 0, data.length);
	        if (nBytesRead != -1) nBytesWritten = line.write(data, 0, nBytesRead);
	    }
	    // Stop
	    line.drain();
	    line.stop();
	    line.close();
	    din.close();
	  } 
	}

	private static SourceDataLine getLine(AudioFormat audioFormat) throws LineUnavailableException
	{
	  SourceDataLine res = null;
	  DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
	  res = (SourceDataLine) AudioSystem.getLine(info);
	  res.open(audioFormat);
	  return res;
	} 
}

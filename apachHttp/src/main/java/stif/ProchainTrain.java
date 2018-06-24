package stif;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import deezer.Track;
import okHttp.Get;
import okhttp3.Authenticator;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class ProchainTrain {
	
	private static String apiKey = "4e43b394-60a6-4d03-89b4-8513e02abce5:";
	
	private static String url = "https://api.sncf.com/v1/coverage/sncf/stop_areas/stop_area:OCE:SA:87758649/departures?datetime";
	public static void main(String[] args) {
		Get example = new Get();
	    try {
	    	Builder client = new OkHttpClient.Builder();
			
	    	client.	authenticator(new Authenticator() {
				@Override
				public Request authenticate(Route arg0, Response arg1)
						throws IOException {
					String credential = Credentials.basic(apiKey, "");
					return arg1.request().newBuilder().header("Authorization", credential).build();
				}
	    		});
	    	OkHttpClient okclient = client.build();
	    	Request request = new Request.Builder().url(url).build();
	    	Response res1 = okclient.newCall(request).execute();

	    	String res = res1.body().string();
			JsonParser parser = new JsonParser();
		    JsonObject obj = parser.parse(res).getAsJsonObject();
		    
		    JsonElement t = obj.get("departures");
		    
		    Gson gson = new Gson();
		    List<Departures> departures = gson.fromJson(t.toString(), new TypeToken<List<Departures>>(){}.getType());
		    
		    int count = 0;
		    for(Departures d : departures) {
		    	String time = d.fetchTime();
		    	String y = time.substring(0, 4);
		    	String m = time.substring(4, 6);
		    	String day = time.substring(6, 8);
		    	String h = time.substring(9, 11);
		    	String min = time.substring(11, 13);
		    	String sec = time.substring(13, 15);
		    	System.out.print("Next RER B " + (++count) + " ");
		    	System.out.print("Direction: " + d.fetchDirection() + " ");
		    	System.out.print("Time: " + day + "/" + m + "/" + y + " " + h + ":" + min + ":" + sec);
		    	System.out.println();
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

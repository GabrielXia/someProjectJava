package apachHttp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class ServerEcho {
	public static void main(String[] args) throws IOException {
		
	   HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
	   server.createContext("/", new MyHandler());
	   server.createContext("/get", new MyHandler2());
	   server.setExecutor(null); // creates a default executor
	   server.start();
	}	
}

class MyHandler2 implements HttpHandler {
   public void handle(HttpExchange t) {
	   try{
       InputStream is = t.getRequestBody();
       System.out.println(t.toString());
       String url = "http://localhost:8000"+t.getRequestURI().toString();
       Map<String, String> m = splitQuery(new URL(url));
       Gson gson = new GsonBuilder().create();
       String json = gson.toJson(m);
       System.out.println(json);
       
       String response = json;
       t.sendResponseHeaders(200, response.length());
       OutputStream os = t.getResponseBody();
       os.write(response.getBytes());
       os.close();
	   }catch(Exception e){
		   e.printStackTrace();
	   }
   }
   
	public static Map<String, String> splitQuery(URL url) throws UnsupportedEncodingException {
	    Map<String, String> query_pairs = new LinkedHashMap<String, String>();
	    String query = url.getQuery();
	    String[] pairs = query.split("&");
	    for (String pair : pairs) {
	        int idx = pair.indexOf("=");
	        query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
	    }
	    return query_pairs;
	}
}

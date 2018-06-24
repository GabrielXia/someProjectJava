package apachHttp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.*;


public class Server {
	public static void main(String[] args) throws IOException {
		HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
		   server.createContext("/info", new MyHandler());
		   server.setExecutor(null); // creates a default executor
		   server.start();
	}
}

class MyHandler implements HttpHandler {
   public void handle(HttpExchange t) throws IOException {
       InputStream is = t.getRequestBody();
       System.out.println(is);
       JsonObject innerObject = new JsonObject();
       innerObject.addProperty("name", "HttpServer v0.1");
       String response = innerObject.toString();
       t.sendResponseHeaders(200, response.length());
       OutputStream os = t.getResponseBody();
       os.write(response.getBytes());
       os.close();
   }
}


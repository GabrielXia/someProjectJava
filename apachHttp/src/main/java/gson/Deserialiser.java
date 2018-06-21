package gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;

public class Deserialiser {
	public static void main(String[] args) throws Exception {
        String httpsURL = "https://httpbin.org/ip";
        URL myUrl = new URL(httpsURL);
        HttpsURLConnection conn = (HttpsURLConnection)myUrl.openConnection();
        conn.setRequestMethod("GET");
        InputStream is = conn.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        String inputLine;
        String input = "";
        while ((inputLine = br.readLine()) != null) {
            input += inputLine;
        }

        br.close();
        
        IP ip = new Gson().fromJson(input, IP.class); 
        System.out.println(ip);
    }
}

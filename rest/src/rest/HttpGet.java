package rest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpGet {
	public static void main(String[] args) throws Exception {
        String httpsURL = "https://httpbin.org/get?message=bonjour";
        URL myUrl = new URL(httpsURL);
        HttpsURLConnection conn = (HttpsURLConnection)myUrl.openConnection();
        conn.setRequestMethod("GET");
        //conn.setRequestProperty("message", "Bonjour");
        InputStream is = conn.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        String inputLine;

        while ((inputLine = br.readLine()) != null) {
            System.out.println(inputLine);
        }

        br.close();
    }
}

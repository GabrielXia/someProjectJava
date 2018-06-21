package rest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

public class Post {
	public static void main(String[] args) throws Exception {
        String key1 = "message1";
        String value1 = "bonjour";
        String key2 = "message2";
        String value2 = "au revoir";
        String key3 = "mess age 3";
        String value3 = "special characters : \" ' / \\ % ~ ! @ # $ % ^ & * ( ) etc...";
        String key4 = "m e s s a g e 4";
        String value4 = "C'est l'été à Paris!";

        String httpsURL = "https://httpbin.org/post";
        URL myUrl = new URL(httpsURL);
        HttpsURLConnection conn = (HttpsURLConnection)myUrl.openConnection();
        conn.setRequestMethod("POST");
        
        String urlParam = key1 + "=" + value1 + "&" + key2 + "=" + URLEncoder.encode(value2, "UTF-8") + "&" 
        		+ URLEncoder.encode(key3, "UTF-8") + "=" + URLEncoder.encode(value3, "UTF-8") + "&" 
        		+ URLEncoder.encode(key4, "UTF-8") + "=" + URLEncoder.encode(value4, "UTF-8");
        
        // Send post request
     		conn.setDoOutput(true);
     		DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
     		wr.writeBytes(urlParam);
     		wr.flush();
     		wr.close();
        
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

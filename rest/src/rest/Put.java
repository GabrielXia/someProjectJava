package rest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Put {
	public static void main(String[] args) throws Exception {
        String httpsURL = "https://httpbin.org/put";
        URL myUrl = new URL(httpsURL);
        HttpsURLConnection conn = (HttpsURLConnection)myUrl.openConnection();
        conn.setRequestMethod("PUT");
        conn.setDoOutput(true);
        OutputStreamWriter out = new OutputStreamWriter(
        	    conn.getOutputStream());
       	out.write("42");
        out.close();
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

package apachHttp;

import java.io.IOException;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class GetMoreKeys {
	public final static void main(String[] args) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
        	String key1 = "message1";
            String value1 = "bonjour";
            String key2 = "message2";
            String value2 = "au revoir";
            String key3 = "mess age 3";
            String value3 = "special characters : \" ' / \\ % ~ ! @ # $ % ^ & * ( ) etc...";
            String key4 = "m e s s a g e 4";
            String value4 = "C'est l'été à Paris!";

            String httpsURL = "https://httpbin.org/get?" + key1 + "=" + value1 + "&" + key2 + "=" + URLEncoder.encode(value2, "UTF-8") +
            		"&" + URLEncoder.encode(key3, "UTF-8") + "=" + URLEncoder.encode(value3, "UTF-8") + 
            		"&" + URLEncoder.encode(key4, "UTF-8") + "=" + URLEncoder.encode(value4, "UTF-8");
            HttpGet httpget = new HttpGet(httpsURL);

            System.out.println("Executing request " + httpget.getRequestLine());

            // Create a custom response handler
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
                
                public String handleResponse(
                        final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }

            };
            String responseBody = httpclient.execute(httpget, responseHandler);
            System.out.println("----------------------------------------");
            System.out.println(responseBody);
        } finally {
            httpclient.close();
        }
    }
}

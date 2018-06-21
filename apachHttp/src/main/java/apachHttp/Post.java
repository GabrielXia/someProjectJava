package apachHttp;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class Post {
	public final static void main(String[] args) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
    	String key1 = "message1";
        String value1 = "bonjour";
        String key2 = "message2";
        String value2 = "au revoir";
        String key3 = "mess age 3";
        String value3 = "special characters : \" ' / \\ % ~ ! @ # $ % ^ & * ( ) etc...";
        String key4 = "m e s s a g e 4";
        String value4 = "C'est l'été à Paris!";

        HttpPost httpPost = new HttpPost("https://httpbin.org/post");
        List <NameValuePair> nvps = new ArrayList <NameValuePair>();
        nvps.add(new BasicNameValuePair(key1, value1));
        nvps.add(new BasicNameValuePair(key2, URLEncoder.encode(value2, "UTF-8")));
        nvps.add(new BasicNameValuePair(URLEncoder.encode(key3, "UTF-8"), URLEncoder.encode(value3, "UTF-8")));
        nvps.add(new BasicNameValuePair(URLEncoder.encode(key4, "UTF-8"), URLEncoder.encode(value4, "UTF-8")));
        
        httpPost.setEntity(new UrlEncodedFormEntity(nvps));
        CloseableHttpResponse response2 = httpclient.execute(httpPost);

        try {
            System.out.println(response2.getStatusLine());
            HttpEntity entity2 = response2.getEntity();
            String responseString = EntityUtils.toString(entity2, "UTF-8");
            System.out.println(responseString);
            EntityUtils.consume(entity2);
        } finally {
            response2.close();
        }
    }
}

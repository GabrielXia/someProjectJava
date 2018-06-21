package rest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.io.OutputStreamWriter;

import javax.net.ssl.HttpsURLConnection;

public class PostFormData {
	public static void main(String[] args) throws Exception {
		String param = "value";
		File textFile = new File("file.txt");
		File binaryFile = new File("file.bin");
		String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
		String CRLF = "\r\n"; // Line separator required by multipart/form-data.
		
		String httpsURL = "https://httpbin.org/post";
		URLConnection connection = new URL(httpsURL).openConnection();
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
		String charset = "utf-8";
		try (
		    OutputStream output = connection.getOutputStream();
		    PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);
		) {
		    // Send normal param.
		    writer.append("--" + boundary).append(CRLF);
		    writer.append("Content-Disposition: form-data; name=\"param\"").append(CRLF);
		    writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
		    writer.append(CRLF).append(param).append(CRLF).flush();

		    // Send text file.
		    writer.append("--" + boundary).append(CRLF);
		    writer.append("Content-Disposition: form-data; name=\"textFile\"; filename=\"" + textFile.getName() + "\"").append(CRLF);
		    writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF); // Text file itself must be saved in this charset!
		    writer.append(CRLF).flush();
		    Files.copy(textFile.toPath(), output);
		    output.flush(); // Important before continuing with writer!
		    writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.

		    // Send binary file.
		    writer.append("--" + boundary).append(CRLF);
		    writer.append("Content-Disposition: form-data; name=\"binaryFile\"; filename=\"" + binaryFile.getName() + "\"").append(CRLF);
		    writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(binaryFile.getName())).append(CRLF);
		    writer.append("Content-Transfer-Encoding: binary").append(CRLF);
		    writer.append(CRLF).flush();
		    Files.copy(binaryFile.toPath(), output);
		    output.flush(); // Important before continuing with writer!
		    writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.

		    // End of multipart/form-data.
		    writer.append("--" + boundary + "--").append(CRLF).flush();
		    
		    InputStream is = connection.getInputStream();
	        InputStreamReader isr = new InputStreamReader(is);
	        BufferedReader br = new BufferedReader(isr);

	        String inputLine;

	        while ((inputLine = br.readLine()) != null) {
	            System.out.println(inputLine);
	        }

	        br.close();
		}
    }
}

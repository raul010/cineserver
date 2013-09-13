import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;

public class TesteGeral {

	@Test
	public void test() throws ClientProtocolException, IOException {
		 HttpClient httpclient = new DefaultHttpClient();
	        try {
	            HttpGet httpget = new HttpGet("http://www.adorocinema.com/programacao/em-torno-298363/?page=3");

	            System.out.println("executing request " + httpget.getURI());

	            // Create a response handler
	            ResponseHandler<String> responseHandler = new BasicResponseHandler();
	            String responseBody = httpclient.execute(httpget, responseHandler);
	            System.out.println("----------------------------------------");
	            System.out.println(responseBody);
	            System.out.println("----------------------------------------");

	        } finally {
	            // When HttpClient instance is no longer needed,
	            // shut down the connection manager to ensure
	            // immediate deallocation of all system resources
	            httpclient.getConnectionManager().shutdown();
	        }
		
	}

}

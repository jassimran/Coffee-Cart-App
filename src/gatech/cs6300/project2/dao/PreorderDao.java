/**
 * 
 */
package gatech.cs6300.project2.dao;

import gatech.cs6300.project2.exceptions.ServerCommunicationException;
import gatech.cs6300.project2.model.PreorderItem;

import java.io.IOException;
import java.text.ParseException;
import java.util.NoSuchElementException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

public class PreorderDao extends AbstractDao{
	
	public static final PreorderDao INSTANCE = new PreorderDao("http://gatechteam4proj2.appspot.com");
	
	private final String rootUrl;
	private final HttpClient http;
	
	private PreorderDao(final String rootUrl) {
		this.rootUrl = rootUrl + "/getinventory.json";
		this.http = new DefaultHttpClient();
	}
	
	private HttpResponse send(final HttpRequestBase request){
		try {
			return http.execute(request);
		} catch (ClientProtocolException e) {
			throw new IllegalArgumentException(e);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	public HttpResponse sendGet() {
		return send(new HttpGet(rootUrl));
	}
	
	public PreorderItem parseGet(final HttpResponse response) throws ServerCommunicationException {
		try {
			final int statusCode = response.getStatusLine().getStatusCode();
			switch(statusCode) {
				case 200:
					final JSONObject json = parsePayload(response);
					return PreorderItem.fromJson(json);
				case 404:
					throw new NoSuchElementException("No Preorder Items Avialable");
				default:
					throw new ServerCommunicationException("Unexpected response from server: " + statusCode + ", " + payloadToString(response));
			}
		} catch (final IOException e) {
			throw new ServerCommunicationException(e);
		} catch (final JSONException e) {
			throw new ServerCommunicationException(e);
		} catch (final ParseException e) {
			throw new ServerCommunicationException(e);
		}
	}

}

package gatech.cs6300.project2.dao;

import gatech.cs6300.project2.exceptions.ServerCommunicationException;
import gatech.cs6300.project2.model.Inventory;
import gatech.cs6300.project2.util.Strings;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

public class InventoryDao extends AbstractDao 
{
	public static final InventoryDao INSTANCE = new InventoryDao(Strings.INVENTORY_DAO_URL);
	
	private final String rootUrl;
	private final HttpClient http;
	
	private InventoryDao(final String rootUrl) {
		this.rootUrl = rootUrl;
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
	
	public Inventory parseGet(final HttpResponse response) throws ServerCommunicationException {
		try {
			final int statusCode = response.getStatusLine().getStatusCode();
			switch(statusCode) {
				case 200:
					final JSONObject json = parsePayload(response);
					return Inventory.fromJson(json);
				case 404:
					throw new NoSuchElementException();
				default:
					throw new ServerCommunicationException("Unexpected response from server: " + statusCode + ", " + payloadToString(response));
			}
		} catch (final IOException e) {
			throw new ServerCommunicationException(e);
		} catch (final JSONException e) {
			throw new ServerCommunicationException(e);
		}
	}
}

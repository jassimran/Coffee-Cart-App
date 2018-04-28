package gatech.cs6300.project2.dao;

import gatech.cs6300.project2.exceptions.BadRequestException;
import gatech.cs6300.project2.exceptions.ServerCommunicationException;
import gatech.cs6300.project2.model.Purchase;
import gatech.cs6300.project2.util.JsonErrors;
import gatech.cs6300.project2.util.Strings;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

public class PurchaseDao extends AbstractDao {

	public static final PurchaseDao INSTANCE = new PurchaseDao(Strings.PURCHASE_DAO_URL);
	
	private final String rootUrl;
	private final HttpClient http;
	
	private PurchaseDao(final String rootUrl) {
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
	
	public HttpResponse sendPost(final Purchase purchase){
		final HttpPost post = new HttpPost(rootUrl);
		try {
			post.setEntity(new StringEntity(purchase.toJson().toString()));
			System.out.println(purchase.toJson().toString());
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		} catch (JSONException e) {
			throw new IllegalArgumentException(e);
		}
		return send(post);
	}
	
	public void parsePost(final HttpResponse response) throws ServerCommunicationException, BadRequestException {
		try {
			final int statusCode = response.getStatusLine().getStatusCode();
			switch(statusCode) {
				case 200:
				case 201:
					//In case of success, no need to parse the response.
					return;
				case 400:
					final JsonErrors errors = new JsonErrors(parsePayload(response));
					throw new BadRequestException(errors.toString());
				default:
					throw new ServerCommunicationException("Unexpected response from server: " + statusCode + ", " + payloadToString(response));
			}
		} catch (final IOException e) {
			throw new ServerCommunicationException(e);
		} catch (JSONException e) {
			throw new ServerCommunicationException(e);
		}
	}

}

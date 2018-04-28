package gatech.cs6300.project2.dao;

import gatech.cs6300.project2.exceptions.BadRequestException;
import gatech.cs6300.project2.exceptions.DuplicateCustomerException;
import gatech.cs6300.project2.exceptions.ServerCommunicationException;
import gatech.cs6300.project2.model.PreorderCart;
import gatech.cs6300.project2.util.JsonErrors;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

public class PreorderCartDao extends AbstractDao{
	
	public static final PreorderCartDao INSTANCE = new PreorderCartDao("http://gatechteam4proj2.appspot.com");
	
	private final String rootUrl;
	private final HttpClient http;
	
	private PreorderCartDao(final String rootUrl) {
		this.rootUrl = rootUrl + "/preorder/";
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
	
	public HttpResponse sendPost(final PreorderCart items){
		final HttpPost post = new HttpPost(rootUrl);
		try {
			post.setEntity(new StringEntity(items.toJson().toString()));
			System.out.println(items.toJson().toString());
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		} catch (JSONException e) {
			throw new IllegalArgumentException(e);
		}
		return send(post);
	}
	
	public String parsePost(final HttpResponse response) throws DuplicateCustomerException, ServerCommunicationException, BadRequestException {
		try {
			final int statusCode = response.getStatusLine().getStatusCode();
			switch(statusCode) {
				case 200:
				case 201:
					final JSONObject json = parsePayload(response);
					return PreorderCart.fromJson(json);
				case 400:
					final JsonErrors errors = new JsonErrors(parsePayload(response));
					if(errors.isDuplicate()) {
						throw new DuplicateCustomerException(errors.toString());
					} else {
						throw new BadRequestException(errors.toString());
					}
				default:
					throw new ServerCommunicationException("Unexpected response from server: " + statusCode + ", " + payloadToString(response));
			}
		} catch (final IOException e) {
			throw new ServerCommunicationException(e);
		} catch (JSONException e) {
			throw new ServerCommunicationException(e);
		} catch (ParseException e) {
			throw new ServerCommunicationException(e);
		}
	}
	
	

}

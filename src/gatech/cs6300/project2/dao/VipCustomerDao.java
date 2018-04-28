package gatech.cs6300.project2.dao;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.NoSuchElementException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import gatech.cs6300.project2.exceptions.BadRequestException;
import gatech.cs6300.project2.exceptions.DuplicateCustomerException;
import gatech.cs6300.project2.exceptions.ServerCommunicationException;
import gatech.cs6300.project2.model.VipCustomer;
import gatech.cs6300.project2.util.JsonErrors;

public class VipCustomerDao extends AbstractDao {
	public static final VipCustomerDao INSTANCE = new VipCustomerDao("http://gatechteam4proj2.appspot.com");
	
	private final String rootUrl;
	private final HttpClient http;
	
	private VipCustomerDao(final String rootUrl) {
		this.rootUrl = rootUrl + "/vip/";
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
	
	public HttpResponse sendGet(final int id) {
		return send(new HttpGet(rootUrl  + id));
	}
	
	public HttpResponse sendPost(final VipCustomer customer){
		final HttpPost post = new HttpPost(rootUrl);
		try {
			post.setEntity(new StringEntity(customer.toJson().toString()));
			System.out.println(customer.toJson().toString());
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		} catch (JSONException e) {
			throw new IllegalArgumentException(e);
		}
		return send(post);
	}
	
	public HttpResponse sendPut(final VipCustomer customer) {
		if(customer.getId() == null) {
			throw new NullPointerException();
		}
		final HttpPut put = new HttpPut(rootUrl + customer.getId());
		try {
			put.setEntity(new StringEntity(customer.toJson().toString()));
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		} catch (JSONException e) {
			throw new IllegalArgumentException(e);
		}
		return send(put);
	}
	
	public HttpResponse sendDelete(final int id) {
		return send(new HttpDelete(rootUrl + id));
	}
	
	public VipCustomer parseGet(final HttpResponse response) throws ServerCommunicationException {
		try {
			final int statusCode = response.getStatusLine().getStatusCode();
			switch(statusCode) {
				case 200:
					final JSONObject json = parsePayload(response);
					return VipCustomer.fromJson(json);
				case 404:
					throw new NoSuchElementException("No VIP customer with that ID");
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
	
	public VipCustomer parsePost(final HttpResponse response) throws DuplicateCustomerException, ServerCommunicationException, BadRequestException {
		try {
			final int statusCode = response.getStatusLine().getStatusCode();
			switch(statusCode) {
				case 200:
				case 201:
					final JSONObject json = parsePayload(response);
					return VipCustomer.fromJson(json);
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
	
	public void parsePut(final HttpResponse response) throws DuplicateCustomerException, ServerCommunicationException, BadRequestException {
		try {
			final int statusCode = response.getStatusLine().getStatusCode();
			switch(statusCode) {
				case 200:
					return;
				case 400:
					final JsonErrors errors = new JsonErrors(parsePayload(response));
					if(errors.isDuplicate()) {
						throw new DuplicateCustomerException(errors.toString());
					} else {
						throw new BadRequestException(errors.toString());
					}
				case 404:
					throw new NoSuchElementException("No VIP customer with that ID");
				default:
					throw new ServerCommunicationException("Unexpected response from server: " + statusCode + ", " + payloadToString(response));
			}
		} catch (final IOException e) {
			throw new ServerCommunicationException(e);
		} catch (JSONException e) {
			throw new ServerCommunicationException(e);
		}
	}
	
	public void parseDelete(final HttpResponse response) throws ServerCommunicationException {
		final int statusCode = response.getStatusLine().getStatusCode();
		switch(statusCode) {
			case 200:
			case 204:
				return;
			case 404:
				throw new NoSuchElementException("No VIP customer with that ID");
			default:
			try {
				throw new ServerCommunicationException("Unexpected response from server: " + statusCode + ", " + payloadToString(response));
			} catch (IOException e) {
				throw new ServerCommunicationException(e);
			}
		}
	}
}

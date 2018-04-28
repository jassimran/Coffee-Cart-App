package gatech.cs6300.project2.dao;

import gatech.cs6300.project2.exceptions.ServerCommunicationException;
import gatech.cs6300.project2.model.Inventory;
import gatech.cs6300.project2.model.PreorderReportEntry;
import gatech.cs6300.project2.model.PurchaseReportEntry;
import gatech.cs6300.project2.util.UtilFunctions;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

public class DailyReportDao extends AbstractDao {
	public static final DailyReportDao INSTANCE = new DailyReportDao("http://gatechteam4proj2.appspot.com");
	
	private final String preordersUrl, purchasesUrl;
	private final HttpClient http;
	
	private DailyReportDao(final String rootUrl) {
		this.preordersUrl = rootUrl + "/dailypreorder.json";
		this.purchasesUrl = rootUrl + "/dailypurchase.json";
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
	
	private static PurchaseReportEntry jsonToPurchase(final JSONObject obj) throws JSONException, ParseException {
		return new PurchaseReportEntry(obj.getInt("cart_number"), obj.getInt("customer_ID"), obj.getDouble("total"), UtilFunctions.parseBirthDate(obj.getString("date")));
	}
	
	private static PreorderReportEntry jsonToPreorder(final JSONObject obj, final Inventory inventory) throws JSONException, ParseException {
		return new PreorderReportEntry(obj.getInt("cart_number"), obj.getInt("customer_ID"), inventory.getById(obj.getInt("item_ID")).getPrice());
	}

	public List<PurchaseReportEntry> getPurchases() throws ServerCommunicationException {
		final HttpResponse response = send(new HttpGet(purchasesUrl));
		try {
			final int statusCode = response.getStatusLine().getStatusCode();
			switch(statusCode) {
				case 200:
					final List<JSONObject> objects = parsePayloadAsArray(response, "purchases");
					final List<PurchaseReportEntry> entries = new ArrayList<PurchaseReportEntry>(objects.size() + 1);
					if(!objects.isEmpty()) {
						entries.add(null); //title
					}
					for(final JSONObject obj : objects) {
						entries.add(jsonToPurchase(obj));
					}
					return entries;
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
	
	public List<PreorderReportEntry> getPreorders() throws ServerCommunicationException {
		final HttpResponse response = send(new HttpGet(preordersUrl));
		try {
			final int statusCode = response.getStatusLine().getStatusCode();
			switch(statusCode) {
				case 200:
					final Inventory inventory = InventoryDao.INSTANCE.parseGet(InventoryDao.INSTANCE.sendGet());
					final List<JSONObject> objects = parsePayloadAsArray(response, "preorders");
					final List<PreorderReportEntry> entries = new ArrayList<PreorderReportEntry>(objects.size() + 1);
					if(!objects.isEmpty()) {
						entries.add(null); //title
					}
					for(final JSONObject obj : objects) {
						entries.add(jsonToPreorder(obj, inventory));
					}
					return entries;
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

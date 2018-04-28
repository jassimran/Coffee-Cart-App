package gatech.cs6300.project2.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class AbstractDao {
	public JSONObject parsePayload(final HttpResponse response) throws IOException {
		try {
			return new JSONObject(payloadToString(response));
		} catch (JSONException e) {
			throw new IOException(e);
		}
	}
	
	public List<JSONObject> parsePayloadAsArray(final HttpResponse response, final String key) throws IOException {
		try {
			final JSONObject obj = parsePayload(response);
			final JSONArray array = obj.getJSONArray(key);
			final List<JSONObject> json = new LinkedList<JSONObject>();
			for(int i = 0; i < array.length(); i++) {
				json.add((JSONObject) array.get(i));
			}
			return json;
		} catch (JSONException e) {
			throw new IOException(e);
		}
	}
	
	public String payloadToString(final HttpResponse response) throws IOException {
		if(response.getEntity() == null) {
			return "{}";
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		try {
			final StringBuilder payload = new StringBuilder();
			String line;
			while((line = reader.readLine()) != null) {
				payload.append(line);
				payload.append('\n');
			}
			return payload.toString();
		} finally {
			reader.close();
		}
	}
}

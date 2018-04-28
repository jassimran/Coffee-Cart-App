package gatech.cs6300.project2.util;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonErrors {
	private final List<String> errors;
	
	public JsonErrors(final JSONObject json) throws JSONException {
		final List<String> errors = new LinkedList<String>();
		final Iterator<?> keys = json.keys();
		while(keys.hasNext()) {
			final String key = keys.next().toString();
			if(key.startsWith("error")) {
				errors.add(json.getString(key));
			}
		}
		this.errors = Collections.unmodifiableList(errors);
	}
	
	public boolean isDuplicate() {
		return errors.size() == 1 && errors.get(0).contains("duplicate");
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for(final String error : errors) {
			if(!first) {
				sb.append('\n');
			}
			sb.append(error);
			first = false;
		}
		return sb.toString();
	}
}

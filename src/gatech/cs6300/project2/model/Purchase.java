package gatech.cs6300.project2.model;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Purchase {
	
	private final Integer cardNumber, cartNumber;
	private final List<PurchaseItem> items;

	public Purchase(Integer cardNumber, List<PurchaseItem> items, Integer cartNumber)
	{
		this.cardNumber = cardNumber;		
		this.items = items;
		this.cartNumber = cartNumber;
	}
	
	public JSONObject toJson() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("card_number", cardNumber);	
		JSONArray itemList = new JSONArray();
		for(PurchaseItem item : items)
			itemList.put( item.getId() );
		json.put("item_list", itemList);
		json.put("cart_number", cartNumber);
		return json;
	}

}

package gatech.cs6300.project2.model;

import gatech.cs6300.project2.util.UtilFunctions;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PreorderCart {
	private final int cartId, vipId;
	private final Date preorderDate;
	private final List<PreorderItem> preorderCartItems;

	public PreorderCart(final int cartId, final int vipId, final Date preorderDate, 
			final List<PreorderItem> preorderCartItems ) {
		this.cartId = cartId;
		this.vipId = vipId;
		this.preorderDate = preorderDate;
		this.preorderCartItems = preorderCartItems;
	}
	
	public int getCartId() {
		return cartId;
	}

	public int getVipId() {
		return vipId;
	}

	public Date getPreorderDate() {
		return preorderDate;
	}
	
	public List<PreorderItem> getPreorderCartItems() {
		return preorderCartItems;
	}
	


	public static String fromJson(final JSONObject json) throws JSONException, ParseException {
		String successMessage = null;
		try{
			successMessage = json.getString("complete");
		} catch(final JSONException ex){
			
		}
		/*Integer id;
		try {
			id = json.getInt("card_number");
		} catch(final JSONException ex) {
			id = null;
		}
		final Integer cartNumber = json.getInt("cart_number");
		JSONArray a = json.getJSONArray("item_list");
		
		final List<PreorderItem> pItemList = new ArrayList<PreorderItem>();
		for (int i = 0; i< a.length() ; i++){
			pItemList.add((PreorderItem)a.get(i));
		}
		
		final Date dateOrder = UtilFunctions.parseDeliveryDate(json.getString("preorder_date"));
		return new PreorderCart(cartNumber, id, dateOrder, pItemList );
		
		*/
		return successMessage;
	}
	
	
	public JSONObject toJson() throws JSONException {
		JSONObject json = new JSONObject();

		json.put("card_number", vipId);
		json.put("preorder_date", UtilFunctions.formatBirthDate(preorderDate));
		JSONArray preorderItemList = new JSONArray();
		int quantity;
		for(PreorderItem item : preorderCartItems){
			quantity = (item.getQuantity());
			for (int i = 1; i <= quantity; i++){
				preorderItemList.put( Integer.parseInt(item.getId()));
			}
		}
		
		json.put("item_list", preorderItemList);
		json.put("cart_number", cartId);
		return json;
	}


	
	 

}

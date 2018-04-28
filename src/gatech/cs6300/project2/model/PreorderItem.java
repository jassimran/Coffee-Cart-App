/**
 * 
 */
package gatech.cs6300.project2.model;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class PreorderItem {	
	
	private List<PreorderItem> inventory = new ArrayList<PreorderItem>();
	
	public PreorderItem(String cakeName, int cakeQuantity, String deliveryDate, double price, String id) {
        super();
        this.cakeName = cakeName;
        this.cakeQuantity = cakeQuantity;
        this.deliveryDate = deliveryDate;
        this.price = price;
        this.id = id;
	}
	public PreorderItem(String cakeName, double price) {
        this.cakeName = cakeName;
        this.price = price;
	}
	 
	private PreorderItem() {

	}

	private String cakeName;
	private String id;
	private int cakeQuantity;
	private String deliveryDate;
	private double price;
	
	public String getName() {
        return cakeName;
	}
	public void setName(String nameText) {
		cakeName = nameText;
	}
	public int getQuantity() {
        return cakeQuantity;
	}
	public void setQuantity(int cakeQuantity) {
        this.cakeQuantity = cakeQuantity;
	}
	public String getdeliveryDate() {
        return deliveryDate;
	}
	public void setdeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
	}
	
	public double getprice() {
        return price;
	}
	public void setPrice(Double itemPrice) {
		price = itemPrice;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public static PreorderItem fromJson(final JSONObject json) throws JSONException, ParseException {
		PreorderItem inventory = new PreorderItem();
		List<PreorderItem> items = inventory.getPreorderItems();
		Iterator<String> keys = json.keys();
		while(keys.hasNext()){
			String key = keys.next();
			PreorderItem item = new PreorderItem();
			item.setId(key);
		    try {
			JSONObject jsonItem = json.getJSONObject(key);
			item.setName(jsonItem.getString("itemType"));
			item.setPrice(jsonItem.getDouble("price"));
				if(item.getName().equals("Coffee") || item.getName().equals("Coffee - Refill")){
				
				} else {
					items.add(item);
				}
		    } catch (JSONException ex){
		    	ex.printStackTrace();
		    }			
		}
			return inventory;
	}
		
	public List<PreorderItem> getPreorderItems()
	{
		return inventory;
	}

}

package gatech.cs6300.project2.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.json.JSONException;
import org.json.JSONObject;

public class Inventory 
{
	private final List<PurchaseItem> inventory = new ArrayList<PurchaseItem>();
	
	private Inventory()
	{
		
	}
	
	public static Inventory fromJson(JSONObject json) throws JSONException
	{	
		Inventory inventory = new Inventory();
		List<PurchaseItem> items = inventory.getPurchaseItems();
		Iterator<String> keys = json.keys();
		while( keys.hasNext() )
		{
			String key = keys.next();
			PurchaseItem item = new PurchaseItem();
			item.setId(Integer.parseInt(key));
			JSONObject jsonItem = json.getJSONObject(key);
			item.setName( jsonItem.getString("itemType") );
			item.setPrice( jsonItem.getDouble("price") );
			items.add(item);
		}
		return inventory;
	}
	
	public void discountItems(boolean currentCustomerIsGold)
	{
		for( PurchaseItem item : inventory )
		{
			if( DiscountedItemNames.INSTANCE.get().contains(item.getName()) )
			{
				if( currentCustomerIsGold )
					item.setPrice(0.00);
				else
					item.setPrice( item.getPrice() / 2);
			}
		}
	}
	
	public List<PurchaseItem> getPurchaseItems()
	{
		return inventory;
	}
	
	public PurchaseItem getById(final int id) {
		for(final PurchaseItem item : inventory) {
			if(Integer.valueOf(id).equals(item.getId())) {
				return item;
			}
		}
		throw new NoSuchElementException("No PurchaseItem " + id);
	}
}

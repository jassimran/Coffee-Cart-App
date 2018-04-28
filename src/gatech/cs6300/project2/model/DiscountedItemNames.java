package gatech.cs6300.project2.model;

import gatech.cs6300.project2.util.Strings;

import java.util.ArrayList;
import java.util.List;

public enum DiscountedItemNames 
{
	INSTANCE;
	
	private final List<String> names = createList();
	
	private List<String> createList()
	{
		List<String> result = new ArrayList<String>();
		result.add(Strings.COFFEE_REFILL);
		return result;
	}
	
	public List<String> get()
	{
		return names;
	}	
}

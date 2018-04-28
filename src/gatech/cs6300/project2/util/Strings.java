package gatech.cs6300.project2.util;

public class Strings {
	//The root URL to the server.
	public static final String ROOT_URL = "http://gatechteam4proj2.appspot.com";
	
	//Strings used by the DAOs to access particular REST services.
	public static final String PURCHASE_DAO_URL = ROOT_URL + "/purchase/";
	public static final String INVENTORY_DAO_URL = ROOT_URL + "/getinventory.json";
	
	//Strings used to pass extra information 
	public static final String VIP_CUSTOMER_ID = "vipId";
	public static final String GOLD_STATUS = "goldStatus";
	
	//Strings for the names of discounted items.
	public static final String COFFEE_REFILL = "Coffee - Refill";
	
	//Error strings.
	public static final String SERVER_COMMUNICATION_ERROR = "Server Communication Error";
	public static final String VALIDATION_ERROR = "Validation Error";
	public static final String NOT_FOUND_ERROR = "Not Found";
}

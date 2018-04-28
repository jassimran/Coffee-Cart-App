package gatech.cs6300.project2.util;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import android.content.Context;
import android.widget.EditText;

public final class UtilFunctions {
	private static final String BIRTH_DATE_FORMAT = "yyyy-MM-dd", UTC = "UTC",
			PREFS = "CoffeeCart", CART_ID = "cartId";
	
	private UtilFunctions() {}
	
	public static Date parseBirthDate(final String birthDate) throws ParseException {
		final SimpleDateFormat sdf = new SimpleDateFormat(BIRTH_DATE_FORMAT, Locale.US);
		sdf.setLenient(false);
		sdf.setTimeZone(TimeZone.getTimeZone(UTC));
		return sdf.parse(birthDate);
	}
	
	public static String formatBirthDate(final Date birthDate) {
		final SimpleDateFormat sdf = new SimpleDateFormat(BIRTH_DATE_FORMAT, Locale.US);
		sdf.setLenient(false);
		sdf.setTimeZone(TimeZone.getTimeZone(UTC));
		return sdf.format(birthDate);
	}
	
	public static Date parseDeliveryDate(final String deliveryDate) throws ParseException {
		final SimpleDateFormat sdf = new SimpleDateFormat(BIRTH_DATE_FORMAT, Locale.US);
		sdf.setLenient(false);
		sdf.setTimeZone(TimeZone.getTimeZone("EST"));
		return sdf.parse(deliveryDate);
	}
	
	public static Boolean parseDeliveryDategtCurrDate(final String deliveryDate) throws ParseException {
		final SimpleDateFormat sdf = new SimpleDateFormat(BIRTH_DATE_FORMAT, Locale.US);
		sdf.setLenient(false);
		sdf.setTimeZone(TimeZone.getTimeZone("EST"));
		return new Date().after(sdf.parse(deliveryDate));
	}
	
	public static Boolean parseDeliveryDateltOneMonthFromCurrDate(final String deliveryDate) throws ParseException {
	    
        SimpleDateFormat sdf = new SimpleDateFormat(BIRTH_DATE_FORMAT, Locale.US);
        sdf.setLenient(false);
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(new Date());
	    calendar.add(Calendar.MONTH, 1);
	    sdf.setCalendar(calendar);
        String dateFormatted = sdf.format(calendar.getTime());
       
       return sdf.parse(deliveryDate).after(sdf.parse(dateFormatted));
        
	}
	
	public static String formatPrice(final double price) {
		return NumberFormat.getCurrencyInstance().format(price);
	}
	
	public static boolean isCartIdSet(final Context context) {
		return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).contains(CART_ID);
	}
	
	public static int getCartId(final Context context) {
		return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getInt(CART_ID, -1);
	}
	
	public static boolean setCartId(final Context context, final int cartId) {
		return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putInt(CART_ID, cartId).commit();
	}
	
	public static String validateMandatoryFields(final Map<String, EditText> textFields) {
		final List<String> missingFields = new LinkedList<String>();
		for(final Map.Entry<String, EditText> entry : textFields.entrySet()) {
			if(entry.getValue().getText().toString().isEmpty()) {
				missingFields.add(entry.getKey());
			}
		}
		if(missingFields.isEmpty()) {
			return null;
		} else {
			return "The following fields are mandatory: " + missingFields;
		}
	}
}

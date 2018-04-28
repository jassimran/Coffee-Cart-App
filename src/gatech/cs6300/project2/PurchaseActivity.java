package gatech.cs6300.project2;

import gatech.cs6300.project2.dao.InventoryDao;
import gatech.cs6300.project2.dao.PurchaseDao;
import gatech.cs6300.project2.exceptions.BadRequestException;
import gatech.cs6300.project2.model.Inventory;
import gatech.cs6300.project2.model.Purchase;
import gatech.cs6300.project2.model.PurchaseItem;
import gatech.cs6300.project2.util.Strings;
import gatech.cs6300.project2.util.UtilFunctions;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.http.HttpResponse;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

/*
 * This activity is used to record purchases made by VIP customers.
 */
public class PurchaseActivity extends Activity {
	
	ArrayList<PurchaseItem> purchaseItems = new ArrayList<PurchaseItem>();
	ArrayAdapter<PurchaseItem> adapter;
	private double total = 0.0;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_purchase);
		ListView purchaseItemListView = (ListView) findViewById(R.id.purchaseItemListView);
		adapter = new ArrayAdapter<PurchaseItem>(this, android.R.layout.simple_list_item_1, purchaseItems);
		purchaseItemListView.setAdapter(adapter);
        
        final AsyncTask<String, Void, Inventory> getInventoryTask = new AsyncTask<String, Void, Inventory>() {
			private String errorMessage, errorTitle;
			
			@Override
			protected Inventory doInBackground(final String... params) {
				try {
					final HttpResponse response = InventoryDao.INSTANCE.sendGet();
					return InventoryDao.INSTANCE.parseGet(response);
				} catch(NoSuchElementException ex) {
					errorMessage = "Could not find inventory";
					errorTitle = Strings.NOT_FOUND_ERROR;
				} catch (Exception e) {
					errorMessage = e.getMessage();
					errorTitle = Strings.SERVER_COMMUNICATION_ERROR;
				}
				return null;
			}
			
			@Override
			protected void onPostExecute(final Inventory inventory) {
				if(errorMessage == null) {
					//Discount items in the inventory as appropriate based on gold status.
					boolean currentCustomerIsGold = PurchaseActivity.this.getIntent().getBooleanExtra(Strings.GOLD_STATUS, false);
					inventory.discountItems(currentCustomerIsGold);
					
					Spinner spinner = (Spinner) findViewById(R.id.spinner1);
					List<PurchaseItem> inventoryList = inventory.getPurchaseItems();
					ArrayAdapter<PurchaseItem> adapter =  new ArrayAdapter<PurchaseItem>(PurchaseActivity.this, android.R.layout.simple_list_item_1, inventoryList);
					
					
					// Specify the layout to use when the list of choices appears
					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					// Apply the adapter to the spinner
					spinner.setAdapter(adapter);
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(PurchaseActivity.this);
			    	builder.setMessage(errorMessage).setTitle(errorTitle).setIcon(R.drawable.ic_alert).setOnDismissListener(new OnDismissListener() {
						@Override
						public void onDismiss(DialogInterface dialog) {
							PurchaseActivity.this.finish();
						}
			    	});
			    	AlertDialog dialog = builder.create();
			    	dialog.show();
				}
			}
		};
		getInventoryTask.execute();
	}
	
	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this).setTitle("Cancel this transaction?")
		.setMessage("Are you sure?")
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(final DialogInterface dialog, final int which) {
				finish();
			}
		})
		.setNegativeButton("No", null).create()
		.show();
	}
	
	public void addItem(View view)
	{
		Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		PurchaseItem selectedPurchaseItem = (PurchaseItem)spinner.getSelectedItem();
		adapter.add(selectedPurchaseItem);
		total += selectedPurchaseItem.getPrice();
		updateTotalInGui();
	}
	
	public void clearItems(View view)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		String alertTitle = "Clear Purchase";
		String alertMessage = "Are you sure you want to remove all of the the purchase items?";
		builder.setMessage(alertMessage).setTitle(alertTitle);
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	   adapter.clear();
	       		   total = 0.00;
	       		   updateTotalInGui();
	           }
	       });
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	   dialog.cancel();
	           }
	       });
		builder.show();
		
	}
	
	public void recordPurchase(View view)
	{
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		String moneyString = formatter.format(total);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		String alertTitle = "Proceed With Purchase?";
		String alertMessage = "You are about to record a purchase costing " + moneyString + ". Continue?";
		builder.setMessage(alertMessage).setTitle(alertTitle);
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	   doPurchase();
	           }
	       });
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	   dialog.cancel();
	           }
	       });
		builder.show();
		
	}
	
	private void doPurchase()
	{
		Integer cardNumber = getIntent().getIntExtra(Strings.VIP_CUSTOMER_ID, -1);
		Integer cartNumber = UtilFunctions.getCartId(this);
		final Purchase purchase = new Purchase(cardNumber, purchaseItems, cartNumber);
		final AsyncTask<String, Void, Purchase> purchaseTask = new AsyncTask<String, Void, Purchase>() {
			private String errorMessage, errorTitle;
			
			@Override
			protected Purchase doInBackground(final String... params) {
				try {
					final HttpResponse response = PurchaseDao.INSTANCE.sendPost(purchase);
					PurchaseDao.INSTANCE.parsePost(response);
					return null;
				} catch (BadRequestException e) {
					errorMessage = e.getMessage();
					errorTitle = Strings.VALIDATION_ERROR;
				} catch (Exception e) {
					errorMessage = e.getMessage();
					errorTitle = Strings.SERVER_COMMUNICATION_ERROR;
				} 
				return null;
			}
			
			@Override
			protected void onPostExecute(final Purchase reported) {
				if(errorMessage == null) {
					AlertDialog.Builder builder = new AlertDialog.Builder(PurchaseActivity.this);
			    	builder.setMessage("Purchase successfully recorded!").setTitle("Done").setOnDismissListener(new OnDismissListener() {
						@Override
						public void onDismiss(DialogInterface dialog) {
							PurchaseActivity.this.setResult(RESULT_OK, null);
							PurchaseActivity.this.finish();
						}
			    	});
			    	AlertDialog dialog = builder.create();
			    	dialog.show();
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(PurchaseActivity.this);
			    	builder.setMessage(errorMessage).setTitle(errorTitle).setIcon(R.drawable.ic_alert);
			    	AlertDialog dialog = builder.create();
			    	dialog.show();
				}
			}
		};
		purchaseTask.execute();
	}
	
	private void updateTotalInGui()
	{
		TextView totalTextView = (TextView) findViewById(R.id.total);
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		String moneyString = formatter.format(total);
		totalTextView.setText(moneyString);
	}
}

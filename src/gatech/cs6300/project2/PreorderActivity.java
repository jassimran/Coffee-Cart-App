package gatech.cs6300.project2;

import gatech.cs6300.project2.dao.PreorderCartDao;
import gatech.cs6300.project2.exceptions.BadRequestException;
import gatech.cs6300.project2.exceptions.DuplicateCustomerException;
import gatech.cs6300.project2.model.PreorderCart;
import gatech.cs6300.project2.model.PreorderItem;
import gatech.cs6300.project2.util.UtilFunctions;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.apache.http.HttpResponse;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/**
 * This class allows the user to preorder desserts.
 *
 */
public class PreorderActivity extends Activity {
	
	protected static final String VIP_CUSTOMER_ID = "vipId";
	protected static String PREORDER_DELIVERY_DATE;
	
	protected static ArrayList<PreorderItem> list = new ArrayList<PreorderItem>();
	protected static Boolean FLAG = false;	
	private static CustomArrayAdapter cartadapter;
	protected static PreorderActivity act;
	private static Integer vipId;
	protected static PreorderItem selectedItem;
	protected static int quantity;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preorder);
		
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
	act = this;	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.preorder, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/** This method is called on click of Add Item button. It validates that the 
	 * delivery date is in correct format i.e yyyy-mm-dd and it is greater than
	 * today's date. After validation, AddItemActivity is called to add items to cart.
	 */
	public void allitems(View view) {
		if (!FLAG){
			vipId = getIntent().getExtras().getInt(VIP_CUSTOMER_ID);
		}
		
		EditText deliveryDate = (EditText)findViewById(R.id.preorderdate);
		PREORDER_DELIVERY_DATE = deliveryDate.getText().toString();
		
		try {
			UtilFunctions.parseDeliveryDate(PREORDER_DELIVERY_DATE);
		} catch (final ParseException e) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    	builder.setMessage("Could not parse \"" + PREORDER_DELIVERY_DATE + "\" as a date. It must be in yyyy-mm-dd format (e.g. 2013-12-25)").setTitle("Invalid Delivery Date").setIcon(R.drawable.ic_alert);
	    	AlertDialog dialog = builder.create();
	    	dialog.show();
	    	return;
		}
		
		try {
			if (UtilFunctions.parseDeliveryDategtCurrDate(PREORDER_DELIVERY_DATE)){	
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage("Could not place order for \"" + PREORDER_DELIVERY_DATE + "\" date. It must be greater than today's date").setTitle("Invalid Delivery Date").setIcon(R.drawable.ic_alert);
				AlertDialog dialog = builder.create();
				dialog.show();
				return;
			}
			
			if (UtilFunctions.parseDeliveryDateltOneMonthFromCurrDate(PREORDER_DELIVERY_DATE)){	
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage("Could not place order for \"" + PREORDER_DELIVERY_DATE + "\" date. It must be less than one month from today").setTitle("Invalid Delivery Date").setIcon(R.drawable.ic_alert);
				AlertDialog dialog = builder.create();
				dialog.show();
				return;
			}
			
		} catch (ParseException e) {
				
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    	builder.setMessage("Could not parse \"" + PREORDER_DELIVERY_DATE + "\" as a date. It must be in yyyy-mm-dd format (e.g. 2013-12-25)").setTitle("Invalid Delivery Date").setIcon(R.drawable.ic_alert);
		    	AlertDialog dialog = builder.create();
		    	dialog.show();
		    	return;
		}
		
		//After necessary validations, start AddItemActivity
       Intent intent =	new Intent(this, AddItemActivity.class);
       finish();
       startActivity(intent);
    }
	
	//On click of Clear All button, all items from Cart are removed
	public void clearCart(View view){
		list.clear();
		cartadapter.clear();
		findViewById(R.id.preordertotaltextView).setVisibility(View.INVISIBLE);
		findViewById(R.id.preordertotalvaluetextView).setVisibility(View.INVISIBLE);
		TextView cartEmpty = (TextView)findViewById(R.id.cartheadertextView);
		cartEmpty.setText("Preorder Cart is empty!");
		findViewById(R.id.submitpreorderbutton).setVisibility(View.INVISIBLE);
		findViewById(R.id.cartheadertextView).setVisibility(View.VISIBLE);
		findViewById(R.id.clearallbutton).setVisibility(View.INVISIBLE);
		findViewById(R.id.cartHeaderItemtextView).setVisibility(View.INVISIBLE);
		findViewById(R.id.cartHeaderPricetextView).setVisibility(View.INVISIBLE);
		findViewById(R.id.cartHeaderQuantitytextView).setVisibility(View.INVISIBLE);
		findViewById(R.id.cartIHeaderSnotextView).setVisibility(View.INVISIBLE);
		
		
	}
	
	//Back button to show cancel confirmation dialog
	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this).setTitle("Cancel this preorder?")
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
	
	/** Below method is called on Submit button click. It takes in object of type PreorderCart.
	 * Validation of cart number, VIP card number, delivery date takes place on server side.
	 * Once the preorder is submitted, a success message is displayed to the user. 
	 */
	public void submitPreOrder(View view){
		
		ArrayList<PreorderItem> getPreorderCartItemsIdList = new ArrayList<PreorderItem>();
		Iterator<PreorderItem> it = list.iterator();
		while(it.hasNext())
		{
			PreorderItem item = it.next();
			getPreorderCartItemsIdList.add(item);
		}
		
		try{
			final Date deliveryDate = UtilFunctions.parseDeliveryDate(PREORDER_DELIVERY_DATE);
			final PreorderCart toAdd = new PreorderCart(UtilFunctions.getCartId(view.getContext()), 
				vipId, deliveryDate ,getPreorderCartItemsIdList);
			final AsyncTask<String,Void,String> submitPreorderTask = new AsyncTask<String,Void,String>(){
				private String errorMessage, errorTitle;
		
				@Override
				protected String doInBackground(final String... params) {
					try {
						final HttpResponse response = PreorderCartDao.INSTANCE.sendPost(toAdd);
						return PreorderCartDao.INSTANCE.parsePost(response);
					} catch (DuplicateCustomerException e) {
						errorMessage = e.getMessage();
						errorTitle = "Duplicate Customer";
					} catch (BadRequestException e) {
						errorMessage = e.getMessage();
						errorTitle = "Validation Error";
					} catch (Exception e) {
						errorMessage = e.getMessage();
						errorTitle = "Server Communication Error";
					} 
					return null;
				}
		
				@Override
				protected void onPostExecute(final String successMessage) {
					if(errorMessage == null) {
						
						//Success message displayed if preorder is submitted successfully
						AlertDialog.Builder builder = new AlertDialog.Builder(PreorderActivity.this);
						builder.setMessage("" + successMessage).setTitle("Preorder Submitted!").setOnDismissListener(new OnDismissListener() {
							@Override
							public void onDismiss(DialogInterface dialog) {
								PreorderActivity.this.finish();
								PreorderActivity.this.clearCart(getCurrentFocus());
								findViewById(R.id.submitpreorderbutton).setVisibility(View.INVISIBLE);
								findViewById(R.id.clearallbutton).setVisibility(View.INVISIBLE);
								TextView cartHeader = (TextView)findViewById(R.id.cartheadertextView);
								
								cartHeader.setText("Preorder completed!");
								
							}
						});
						AlertDialog dialog = builder.create();
						dialog.show();
					} else {
						String retval[] = errorMessage.split("\"");
						//Error message is displayed if submission fails
						AlertDialog.Builder builder = new AlertDialog.Builder(PreorderActivity.this);
						builder.setMessage(retval.length >= 4 ? retval[3] : errorMessage).setTitle(errorTitle).setIcon(R.drawable.ic_alert);
						PreorderActivity.this.clearCart(getCurrentFocus());
						AlertDialog dialog = builder.create();
						dialog.show();
					}
				}
			};
			
			//Execute the Async Task
			submitPreorderTask.execute();
			
		} catch (ParseException ex){
			AlertDialog.Builder builder = new AlertDialog.Builder(PreorderActivity.this);
			builder.setMessage("Preorder Delivery Date could not be parsed").setTitle("Parse Exception").setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					PreorderActivity.this.finish();
				}
			});
			AlertDialog dialog = builder.create();
			dialog.show();
		}
	}
	
	/** populatePreorderCart populates the items selected for pre-ordering into
	 * a ListView. Adapter sets the data to be populated in the list. Selected items
	 * are of type PreorderItem and stored in ArrayList named list 
	 */
	public static void populatePreorderCart(){			
		   ListView preorderCart = (ListView)act.findViewById(R.id.preorderCart);		   
		   cartadapter = new CustomArrayAdapter(act.getApplicationContext(),R.layout.preordercartlist,
	       list);		   
	        
	     // Add new item to list
	        list.add(new PreorderItem(selectedItem.getName(), quantity,
	        		PREORDER_DELIVERY_DATE, selectedItem.getprice(), 
	        		selectedItem.getId()));
	        
	     //Calculating total price of items in cart   
	        Double total = 0.0;
			   Iterator<PreorderItem> listitr=list.iterator();
		       while(listitr.hasNext()){
		    	   PreorderItem item = listitr.next();
		    	   Double price = item.getprice();
		    	   Integer quantity = item.getQuantity();
		    	   total += (price * quantity);
		        }
	        
	        preorderCart.setAdapter(cartadapter);
	        act.findViewById(R.id.cartHeaderItemtextView).setVisibility(View.VISIBLE);
			act.findViewById(R.id.cartHeaderPricetextView).setVisibility(View.VISIBLE);
			act.findViewById(R.id.cartHeaderQuantitytextView).setVisibility(View.VISIBLE);
			act.findViewById(R.id.cartIHeaderSnotextView).setVisibility(View.VISIBLE);
	        act.findViewById(R.id.cartheadertextView).setVisibility(View.VISIBLE);
			act.findViewById(R.id.preordertotaltextView).setVisibility(View.VISIBLE);
			act.findViewById(R.id.preordertotalvaluetextView).setVisibility(View.VISIBLE);
			act.findViewById(R.id.submitpreorderbutton).setVisibility(View.VISIBLE);
			act.findViewById(R.id.clearallbutton).setVisibility(View.VISIBLE);
			
			TextView totalValue = (TextView)act.findViewById(R.id.preordertotalvaluetextView);
			totalValue.setText(String.valueOf(total));				
	}
	
	//To save the state of delivery Date, on Pause is used
	@Override
	public void onPause(){
		 EditText deliveryDate = (EditText)findViewById(R.id.preorderdate);			
		 PREORDER_DELIVERY_DATE = deliveryDate.getText().toString() ;
	    super.onPause();
	}
	
	//to update the Cart everytime after Adding an item to the Cart
	@Override
	public void onResume(){
	    super.onResume();
	    
	    if (FLAG)
		{
	  	   EditText deliveryDate = (EditText)findViewById(R.id.preorderdate);
	  	   deliveryDate.setText(PREORDER_DELIVERY_DATE);
	  	   deliveryDate.setEnabled(false);
	  	   populatePreorderCart();
		}
	}
	

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_preorder,
					container, false);
			
			/*
			 * Initially preorder activity should not have any submit buttons/clear all 
			 * buttons, as no items have been added to cart. Once cart has atleast one item
			 * i.e. Flag will be set to true and all the below listed fields will become 
			 * visible and functional to support further processing.
			 */
			rootView.findViewById(R.id.cartheadertextView).setVisibility(View.INVISIBLE);
			rootView.findViewById(R.id.preordertotaltextView).setVisibility(View.INVISIBLE);
			rootView.findViewById(R.id.preordertotalvaluetextView).setVisibility(View.INVISIBLE);
			rootView.findViewById(R.id.submitpreorderbutton).setVisibility(View.INVISIBLE);
			rootView.findViewById(R.id.clearallbutton).setVisibility(View.INVISIBLE);
			rootView.findViewById(R.id.cartHeaderItemtextView).setVisibility(View.INVISIBLE);
			rootView.findViewById(R.id.cartHeaderPricetextView).setVisibility(View.INVISIBLE);
			rootView.findViewById(R.id.cartHeaderQuantitytextView).setVisibility(View.INVISIBLE);
			rootView.findViewById(R.id.cartIHeaderSnotextView).setVisibility(View.INVISIBLE);
			
			
			//Initially when the activity is created, flag is false as the cart is empty,
			if(FLAG){
			 EditText deliveryDate = (EditText)rootView.findViewById(R.id.preorderdate);
			 deliveryDate.setText(PREORDER_DELIVERY_DATE);
			}
			return rootView;
		}
	}

}

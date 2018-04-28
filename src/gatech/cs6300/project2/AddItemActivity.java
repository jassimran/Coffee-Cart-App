package gatech.cs6300.project2;

import gatech.cs6300.project2.dao.PreorderDao;
import gatech.cs6300.project2.model.PreorderItem;
import java.util.List;
import java.util.NoSuchElementException;
import org.apache.http.HttpResponse;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class AddItemActivity extends Activity {
	
	
	protected static AddItemActivity act;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_item);
		
		if (savedInstanceState == null) {
			final PlaceholderFragment frag = new PlaceholderFragment();
			frag.setArguments(getIntent().getExtras());
			getFragmentManager().beginTransaction()
					.add(R.id.container, frag).commit();
		}
		act = this;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_item, menu);
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
	

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {


		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_add_item,
					container, false);	
	        
	        final AsyncTask<String, Void, PreorderItem> getInventoryTask = new AsyncTask<String, Void, PreorderItem>() {
				private String errorMessage, errorTitle;
				
				/**
				 * Asynchronously get the preorder items that the customer can choose from.
				 */
				@Override
				protected PreorderItem doInBackground(final String... params) {
					try {
						final HttpResponse response = PreorderDao.INSTANCE.sendGet();
						return PreorderDao.INSTANCE.parseGet(response);
					} catch(NoSuchElementException ex) {
						errorMessage = "Could not find inventory";
						errorTitle = "Not Found";
					} catch (Exception e) {
						errorMessage = e.getMessage();
						errorTitle = "Server Communication Error";
					}
					return null;
				}
				
				/**
				 * Put the preorder items that were retrieved in the appropriate list.
				 */
				@Override
				protected void onPostExecute(final PreorderItem inventory) {
					if(errorMessage == null) {
						final ListView preorderItemListView = (ListView) act.findViewById(R.id.allItemslistView);
						List<PreorderItem> inventoryList = inventory.getPreorderItems();
						
						//Custom adapter to inventory data in the form of list
						AddItemArrayAdapter addItemadapter = new AddItemArrayAdapter(act,
								android.R.layout.simple_list_item_1, inventoryList );
						
						 // Apply the AddItemArrayAdapter to the list
						preorderItemListView.setAdapter(addItemadapter);
						preorderItemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
							 
				            @Override
				            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				            	
				            	//Item is selected on item click and saved in selectedItem
				            	PreorderActivity.selectedItem = (PreorderItem) preorderItemListView.getItemAtPosition(position);	
				            	
				            	//To get quantity of the selected item, below alert is set up
				                AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());

				                alert.setTitle("Add Quantity");
				                final String[] quanitylist = { "1", "2", "3", "4", "5"};
				    		    	
				                alert.setItems(quanitylist, new DialogInterface.OnClickListener() {
				                	public void onClick(DialogInterface dialog, int which) {
				                		// The 'which' argument contains the index position
				                		// of the selected item
				                		PreorderActivity.quantity = Integer.parseInt(quanitylist[which]);
				                		act.finish();
				                		PreorderActivity.FLAG = true;
				                		Toast.makeText(Dialog.class.cast(dialog).getContext(),
				                				"Please wait while we add "+ PreorderActivity.quantity +" "+ 
				                				PreorderActivity.selectedItem.getName() 
				                				+ " to the Cart!", Toast.LENGTH_SHORT).show();
				                		try {
				                			startActivity(new Intent(Dialog.class.cast(dialog).getContext(), PreorderActivity.class));
				                		} catch (ActivityNotFoundException e) {
				                			AlertDialog.Builder builder = new AlertDialog.Builder(Dialog.class.cast(dialog).getContext());
				     		    	   		builder.setMessage(e.getMessage()).setTitle("Activity not found!").setIcon(R.drawable.ic_alert);
				     		    	   		AlertDialog error = builder.create();
				     		    	   		error.show();
				     		    	   		return;
				                		}
				                		
				                	}
				                });
				    		    	
				    		    alert.setOnDismissListener(new OnDismissListener(){
						    	
				    		    	@Override
				    		    	public void onDismiss(DialogInterface dialog) {
				    		    		getActivity().finish();
				    				}
				    		    });

				    		    alert.show();
				            }
				        }); 
				       
					} else {
						AlertDialog.Builder builder = new AlertDialog.Builder(act);
				    	builder.setMessage(errorMessage).setTitle(errorTitle).setIcon(R.drawable.ic_alert).setOnDismissListener(new OnDismissListener() {
							@Override
							public void onDismiss(DialogInterface dialog) {
								act.finish();
							}
				    	});
				    	AlertDialog dialog = builder.create();
				    	dialog.show();
					}
				}
			};
			
			//to execute the Async task to get items from inventory
			getInventoryTask.execute();
			
			return rootView;
		}
	}

}

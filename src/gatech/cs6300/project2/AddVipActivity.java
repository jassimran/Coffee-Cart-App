package gatech.cs6300.project2;

import gatech.cs6300.project2.dao.VipCustomerDao;
import gatech.cs6300.project2.exceptions.BadRequestException;
import gatech.cs6300.project2.exceptions.DuplicateCustomerException;
import gatech.cs6300.project2.model.VipCustomer;
import gatech.cs6300.project2.util.UtilFunctions;
import gatech.cs6300.project2.R;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class AddVipActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_vip);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_vip, menu);
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
	
	public void addVip(final View view) {
		final EditText nameInput = (EditText) findViewById(R.id.form_name_field);
		final String name = nameInput.getText().toString();
		final EditText phoneInput = (EditText) findViewById(R.id.form_phone_field);
		final String phone = phoneInput.getText().toString();
		final EditText dobInput = (EditText) findViewById(R.id.form_dob_field);
		final Date dob;
		
		final Map<String, EditText> toValidate = new HashMap<String, EditText>();
		toValidate.put(getString(R.string.form_name), nameInput);
		toValidate.put(getString(R.string.form_phone), phoneInput);
		toValidate.put(getString(R.string.form_dob), dobInput);
		final String validation = UtilFunctions.validateMandatoryFields(toValidate);
		if(validation != null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    	builder.setMessage(validation).setTitle("Validation Error").setIcon(R.drawable.ic_alert);
	    	AlertDialog dialog = builder.create();
	    	dialog.show();
	    	return;
		}
		
		try {
			dob = UtilFunctions.parseBirthDate(dobInput.getText().toString());
		} catch (final ParseException e) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    	builder.setMessage("Could not parse \"" + dobInput.getText().toString() + "\" as a date. It must be in yyyy-mm-dd format (e.g. 2013-12-25)").setTitle("Invalid Date of Birth").setIcon(R.drawable.ic_alert);
	    	AlertDialog dialog = builder.create();
	    	dialog.show();
	    	return;
		}
		
		final VipCustomer toAdd = new VipCustomer(null, 0, name, phone, dob, true, false);
		final AsyncTask<String, Void, VipCustomer> addTask = new AsyncTask<String, Void, VipCustomer>() {
			private String errorMessage, errorTitle;
			
			/**
			 * This method asychronously adds a customer to the database on the server.
			 */
			@Override
			protected VipCustomer doInBackground(final String... params) {
				try {
					final HttpResponse response = VipCustomerDao.INSTANCE.sendPost(toAdd);
					return VipCustomerDao.INSTANCE.parsePost(response);
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
			
			/**
			 * After the customer is added, this method shows a confirmation message.
			 */
			@Override
			protected void onPostExecute(final VipCustomer added) {
				if(errorMessage == null) {
					AlertDialog.Builder builder = new AlertDialog.Builder(AddVipActivity.this);
			    	builder.setMessage("VIP customer added with ID " + added.getId()).setTitle("Customer Added").setOnDismissListener(new OnDismissListener() {
						@Override
						public void onDismiss(DialogInterface dialog) {
							AddVipActivity.this.finish();
						}
			    	});
			    	AlertDialog dialog = builder.create();
			    	dialog.show();
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(AddVipActivity.this);
			    	builder.setMessage(errorMessage).setTitle(errorTitle).setIcon(R.drawable.ic_alert);
			    	AlertDialog dialog = builder.create();
			    	dialog.show();
				}
			}
		};
		addTask.execute();
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
			View rootView = inflater.inflate(R.layout.fragment_add_vip,
					container, false);
			return rootView;
		}
	}
}

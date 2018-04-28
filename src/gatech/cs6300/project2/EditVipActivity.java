package gatech.cs6300.project2;

import gatech.cs6300.project2.dao.VipCustomerDao;
import gatech.cs6300.project2.exceptions.BadRequestException;
import gatech.cs6300.project2.exceptions.DuplicateCustomerException;
import gatech.cs6300.project2.model.VipCustomer;
import gatech.cs6300.project2.util.Strings;
import gatech.cs6300.project2.util.UtilFunctions;
import gatech.cs6300.project2.R;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
/**
 * This activity is used to edit the information of an existing VIP customer.
 */
public class EditVipActivity extends Activity {
	protected static final String VIP_CUSTOMER_ID = "vipId", CURRENT_ACTIVITY = "currentActivity";
	protected String activityType;
	private static boolean currentCustomerIsGold = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_vip);

		if (savedInstanceState == null) {
			final EditVipFragment frag = new EditVipFragment();
			frag.setArguments(getIntent().getExtras());
			getFragmentManager().beginTransaction().add(R.id.container, frag)
					.commit();
			activityType = getIntent().getStringExtra(CURRENT_ACTIVITY);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_vip, menu);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//Need to refresh the data after a successful purchase, in case the points changed.
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			recreate();
		}
	}
	
	@Override
	public void onBackPressed() {
		if("Edit".equals(activityType)) {
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
		} else {
			finish();
		}
	}

	public void editVip(final View view) {
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
			builder.setMessage(
					"Could not parse \""
							+ dobInput.getText().toString()
							+ "\" as a date. It must be in yyyy-mm-dd format (e.g. 2013-12-25)")
					.setTitle("Invalid Date of Birth")
					.setIcon(R.drawable.ic_alert);
			AlertDialog dialog = builder.create();
			dialog.show();
			return;
		}

		final VipCustomer toEdit = new VipCustomer(getIntent().getIntExtra(
				Strings.VIP_CUSTOMER_ID, -1), 0, name, phone, dob, true, currentCustomerIsGold);
		final AsyncTask<String, Void, Void> editTask = new AsyncTask<String, Void, Void>() {
			private String errorMessage, errorTitle;

			@Override
			protected Void doInBackground(final String... params) {
				try {
					final HttpResponse response = VipCustomerDao.INSTANCE
							.sendPut(toEdit);
					VipCustomerDao.INSTANCE.parsePut(response);
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
			protected void onPostExecute(Void result) {
				if (errorMessage == null) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							EditVipActivity.this);
					builder.setMessage(
							"VIP customer edited with ID " + toEdit.getId())
							.setTitle("Customer Added")
							.setOnDismissListener(new OnDismissListener() {
								@Override
								public void onDismiss(DialogInterface dialog) {
									EditVipActivity.this.recreate();
								}
							});
					AlertDialog dialog = builder.create();
					dialog.show();
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							EditVipActivity.this);
					builder.setMessage(errorMessage).setTitle(errorTitle)
							.setIcon(R.drawable.ic_alert);
					AlertDialog dialog = builder.create();
					dialog.show();
				}
			}
		};
		editTask.execute();
	}

	// preOrder method is called on click of Preorder button and
	// PreorderActivity is initiated.
	public void preOrder(final View view) {

		final Intent intent = new Intent(this, PreorderActivity.class);
		intent.putExtra(Strings.VIP_CUSTOMER_ID,
				getIntent().getIntExtra(Strings.VIP_CUSTOMER_ID, -1));
		PreorderActivity.FLAG = false;
		PreorderActivity.list.clear();

		startActivity(intent);

	}

	// purchase method is called on click of Purchase button and
	// PurchaseActivity is initiated.
	public void purchase(final View view) {
		final Intent intent = new Intent(this, PurchaseActivity.class);
		intent.putExtra(Strings.VIP_CUSTOMER_ID,
				getIntent().getIntExtra(Strings.VIP_CUSTOMER_ID, -1));
		intent.putExtra(Strings.GOLD_STATUS, currentCustomerIsGold);
		startActivityForResult(intent, 1);
	}
	
	public void askBeforeDelete(final View view) {
		new AlertDialog.Builder(this).setTitle("Delete This Customer?")
			.setMessage("Are you sure?")
			.setPositiveButton(R.string.delete_confirm, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(final DialogInterface dialog, final int which) {
					delete();
				}
			})
			.setNegativeButton(R.string.delete_cancel, null).create()
			.show();
	}
	
	private void delete() {
		final int id = getIntent().getIntExtra(Strings.VIP_CUSTOMER_ID, -1);
		final AsyncTask<String, Void, Boolean> deleteTask = new AsyncTask<String, Void, Boolean>() {
			private String errorMessage, errorTitle;
			
			@Override
			protected Boolean doInBackground(String... params) {
				try {
					final HttpResponse response = VipCustomerDao.INSTANCE.sendDelete(id);
					VipCustomerDao.INSTANCE.parseDelete(response);
				} catch (Exception e) {
					errorMessage = e.getMessage();
					errorTitle = "Server Communication Error";
					return false;
				}
				return true;
			}
			
			@Override
			protected void onPostExecute(final Boolean result) {
				if(result) {
					new AlertDialog.Builder(EditVipActivity.this).setTitle("Success")
						.setMessage("VIP customer deleted")
						.setOnDismissListener(new OnDismissListener() {
							@Override
							public void onDismiss(DialogInterface dialog) {
								EditVipActivity.this.finish();
							}
						}).create().show();
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(EditVipActivity.this);
					builder.setMessage(errorMessage).setTitle(errorTitle)
							.setIcon(R.drawable.ic_alert);
					AlertDialog dialog = builder.create();
					dialog.show();
				}
			}
		};
		deleteTask.execute();
	}

	public static class EditVipFragment extends Fragment {

		public EditVipFragment() {
		}
		
		public void setButtonVisibility(View rootView, int edit, int delete, int purchase, int preorder)
		{
		    Button submitButton = (Button) rootView.findViewById(R.id.submitEditVip);
            Button deleteButton = (Button) rootView.findViewById(R.id.delete_button);
            Button purchaseButton = (Button) rootView.findViewById(R.id.purchase_button);
            Button preorderButton = (Button) rootView.findViewById(R.id.preorder_button);
            
            submitButton.setVisibility(edit);
            deleteButton.setVisibility(delete);
            purchaseButton.setVisibility(purchase);
            preorderButton.setVisibility(preorder);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			final View rootView = inflater.inflate(R.layout.fragment_edit_vip,
					container, false);			
			String activityType = getArguments().getString(CURRENT_ACTIVITY);
	        
			setButtonVisibility(rootView,
			                    activityType.compareTo("Edit") == 0 ? View.VISIBLE : View.GONE,
			                    activityType.compareTo("Delete") == 0 ? View.VISIBLE : View.GONE,
			                    activityType.compareTo("Purchase") == 0 ? View.VISIBLE : View.GONE,
			                    activityType.compareTo("Preorder") == 0 ? View.VISIBLE : View.GONE);
			
			final EditText nameInput = (EditText) rootView.findViewById(R.id.form_name_field);
			final EditText phoneInput = (EditText) rootView.findViewById(R.id.form_phone_field);
			final EditText dobInput = (EditText) rootView.findViewById(R.id.form_dob_field);
			for(final EditText text : new EditText[] { nameInput, phoneInput, dobInput }) {
				text.setEnabled("Edit".equals(activityType));
			}
			
			final int id = getArguments().getInt(Strings.VIP_CUSTOMER_ID, -1);
			final AsyncTask<String, Void, VipCustomer> editTask = new AsyncTask<String, Void, VipCustomer>() {
				private String errorMessage, errorTitle;

				@Override
				protected VipCustomer doInBackground(final String... params) {
					try {
						final HttpResponse response = VipCustomerDao.INSTANCE
								.sendGet(id);
						return VipCustomerDao.INSTANCE.parseGet(response);
					} catch (NoSuchElementException ex) {
						errorMessage = "Could not find a VIP customer with ID "
								+ id + ".";
						errorTitle = "Not Found";
					} catch (Exception e) {
						errorMessage = e.getMessage();
						errorTitle = "Server Communication Error";
					}
					return null;
				}

				@Override
				protected void onPostExecute(final VipCustomer customer) {
					if (errorMessage == null) {
						final TextView idField = (TextView) rootView
								.findViewById(R.id.form_id_field);
						final EditText nameInput = (EditText) rootView
								.findViewById(R.id.form_name_field);
						final EditText phoneInput = (EditText) rootView
								.findViewById(R.id.form_phone_field);
						final EditText dobInput = (EditText) rootView
								.findViewById(R.id.form_dob_field);
						final TextView pointsField = (TextView) rootView
								.findViewById(R.id.vip_point_total);
						final TextView goldStatus = (TextView) rootView
								.findViewById(R.id.vip_gold_status);

						idField.setText(Integer.toString(id));
						nameInput.setText(customer.getName());
						phoneInput.setText(customer.getPhoneNumber());
						dobInput.setText(UtilFunctions.formatBirthDate(customer
								.getBirthDate()));
						pointsField.setText(Integer.toString(customer
								.getPoints()));
						goldStatus
								.setVisibility(customer.isGold() ? View.VISIBLE
										: View.INVISIBLE);
						currentCustomerIsGold = customer.isGold();
					} else {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								getActivity());
						builder.setMessage(errorMessage).setTitle(errorTitle)
								.setIcon(R.drawable.ic_alert)
								.setOnDismissListener(new OnDismissListener() {
									@Override
									public void onDismiss(DialogInterface dialog) {
										getActivity().finish();
									}
								});
						AlertDialog dialog = builder.create();
						dialog.show();
					}
				}
			};
			editTask.execute();
			return rootView;
		}
	}
}

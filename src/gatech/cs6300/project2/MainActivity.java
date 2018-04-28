package gatech.cs6300.project2;

import gatech.cs6300.project2.R;
import gatech.cs6300.project2.util.UtilFunctions;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * This is the activity that is displayed when the user opens the app. It allows the user
 * to navigate to all of the other activities.
 *
 */
public class MainActivity extends Activity {
	protected static final String PREFS = "CoffeeCart", CART_ID = "cartId", CURRENT_ACTIVITY = "currentActivity";
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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

	public void addVip(final View view) {
		startActivity(new Intent(this, AddVipActivity.class));
	}

	public void editVip(final View view) {
	    Intent intent = new Intent(this, FindVipActivity.class);
        intent.putExtra(CURRENT_ACTIVITY, "Edit");
        startActivity(intent);
	}
	
	public void deleteVip(final View view) {
	    Intent intent = new Intent(this, FindVipActivity.class);
        intent.putExtra(CURRENT_ACTIVITY, "Delete");
        startActivity(intent);
    }
	
	public void purchase(final View view) {
	    Intent intent = new Intent(this, FindVipActivity.class);
        intent.putExtra(CURRENT_ACTIVITY, "Purchase");
        startActivity(intent);
    }
	
	public void preorder(final View view) {
	    Intent intent = new Intent(this, FindVipActivity.class);
        intent.putExtra(CURRENT_ACTIVITY, "Preorder");
        startActivity(intent);
    }

	public void dailyReport(final View view) {
		startActivity(new Intent(this, DailyReportActivity.class));
	}
	
	public void editCartId(final View view) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Cart ID");
		alert.setMessage("Please enter the cart ID");
		alert.setCancelable(false);

		final EditText input = new EditText(this);
		input.setInputType(InputType.TYPE_CLASS_NUMBER);
		input.setText(Integer.toString(UtilFunctions.getCartId(this)));
		input.setSelectAllOnFocus(true);
		alert.setView(input);

		alert.setPositiveButton("OK", null);
		alert.setNegativeButton("Cancel", null);
		final AlertDialog dialog = alert.create();
		dialog.show();
		dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				final String value = input.getText().toString();
				try {
					if(UtilFunctions.setCartId(MainActivity.this, Integer.parseInt(value))) {
						final TextView cartIdField = (TextView) MainActivity.this.findViewById(R.id.cartId);
						cartIdField.setText(" " + value);
						dialog.dismiss();
					}
				} catch(final NumberFormatException ex) {
					//Leave dialog open
				}
			}
		});
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
			final View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			final TextView cartIdField = (TextView) rootView.findViewById(R.id.cartId);
			if(UtilFunctions.isCartIdSet(getActivity())) {
				cartIdField.setText(" " + Integer.toString(UtilFunctions.getCartId(getActivity())));
			} else {
				AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

				alert.setTitle("Cart ID");
				alert.setMessage("Please enter the cart ID");
				alert.setCancelable(false);

				final EditText input = new EditText(getActivity());
				input.setInputType(InputType.TYPE_CLASS_NUMBER);
				alert.setView(input);

				alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int whichButton) {
							//Do nothing; see below, cf. https://stackoverflow.com/questions/2620444/how-to-prevent-a-dialog-from-closing-when-a-button-is-clicked
						}
					});
				final AlertDialog dialog = alert.create();
				dialog.show();
				dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(final View v) {
						final String value = input.getText().toString();
						try {
							if(UtilFunctions.setCartId(getActivity(), Integer.parseInt(value))) {
								final TextView cartIdField = (TextView) rootView.findViewById(R.id.cartId);
								cartIdField.setText(" " + value);
								dialog.dismiss();
							}
						} catch(final NumberFormatException ex) {
							//Leave dialog open
						}
					}
				});
			}
			
			return rootView;
		}
	}
}

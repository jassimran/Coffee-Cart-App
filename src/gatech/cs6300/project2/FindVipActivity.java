package gatech.cs6300.project2;

import gatech.cs6300.project2.R;
import gatech.cs6300.project2.util.Strings;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * One of the most common activities performed before other activities. Find
 * the particular VIP by card number that is involved in a particular transaction.
 */
public class FindVipActivity extends Activity {
	protected static final String CURRENT_ACTIVITY = "currentActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_vip);

		if (savedInstanceState == null) {
			final FindVipFragment frag = new FindVipFragment();
			frag.setArguments(getIntent().getExtras());
			getFragmentManager().beginTransaction()
					.add(R.id.container, frag).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.find_vip, menu);
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
	
	public void findVip(final View view) {
		final EditText idInput = (EditText) findViewById(R.id.form_vip_id_field);
		final String idString = idInput.getText().toString();
		try {
			final int id = Integer.parseInt(idString);
			final Intent intent = new Intent(this, EditVipActivity.class); 
			intent.putExtra(Strings.VIP_CUSTOMER_ID, id);
			String activityType = getIntent().getExtras().getString(CURRENT_ACTIVITY);
			intent.putExtra(EditVipActivity.CURRENT_ACTIVITY, activityType);
			startActivity(intent);
			finish();
		} catch(NumberFormatException ex) {
			showIdNotFoundDialog(idString);
	    	return;
		}
	}
	
	private void showIdNotFoundDialog(final String id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage("Could not find a VIP customer with ID " + id + ".").setTitle("Not Found").setIcon(R.drawable.ic_alert);
    	AlertDialog dialog = builder.create();
    	dialog.show();
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class FindVipFragment extends Fragment {

		public FindVipFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.fragment_find_vip, container, false);
			final String vipId = getArguments() == null ? null : getArguments().getString(Strings.VIP_CUSTOMER_ID);
			if(vipId != null) {
				final EditText idField = (EditText) view.findViewById(R.id.form_vip_id_field);
				idField.setText(vipId);
			}
			return view;
		}
	}

}

package gatech.cs6300.project2;

import gatech.cs6300.project2.dao.DailyReportDao;
import gatech.cs6300.project2.model.HasTotal;
import gatech.cs6300.project2.model.PreorderReportEntry;
import gatech.cs6300.project2.model.PurchaseReportEntry;
import gatech.cs6300.project2.util.UtilFunctions;

import java.util.Collection;
import java.util.List;

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
import android.widget.ListView;
import android.widget.TextView;
/**
 *	This class allows the user to display a report of purchases and preorders made that same day.
 */
public class DailyReportActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_daily_report);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.daily_report, menu);
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
			final View rootView = inflater.inflate(R.layout.fragment_daily_report,
					container, false);
			
			final AsyncTask<String, Void, List<PreorderReportEntry>> preorderTask = new AsyncTask<String, Void, List<PreorderReportEntry>>() {
				private String errorMessage, errorTitle;
				
				@Override
				protected List<PreorderReportEntry> doInBackground(final String... params) {
					try {
						return DailyReportDao.INSTANCE.getPreorders();
					} catch (final Exception e) {
						errorMessage = e.getMessage();
						errorTitle = "Server Communication Error";
					}
					return null;
				}
				
				@Override
				protected void onPostExecute(final List<PreorderReportEntry> entries) {
					if(errorMessage == null) {
						populatePreorderReport(rootView, entries);
					} else {
						AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				    	builder.setMessage(errorMessage).setTitle(errorTitle).setIcon(R.drawable.ic_alert).setOnDismissListener(new OnDismissListener() {
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
			
			final AsyncTask<String, Void, List<PurchaseReportEntry>> purchaseTask = new AsyncTask<String, Void, List<PurchaseReportEntry>>() {
				private String errorMessage, errorTitle;
				
				@Override
				protected List<PurchaseReportEntry> doInBackground(final String... params) {
					try {
						return DailyReportDao.INSTANCE.getPurchases();
					} catch (final Exception e) {
						errorMessage = e.getMessage();
						errorTitle = "Server Communication Error";
					}
					return null;
				}
				
				@Override
				protected void onPostExecute(final List<PurchaseReportEntry> entries) {
					if(errorMessage == null) {
						populatePurchaseReport(rootView, entries);
					} else {
						AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				    	builder.setMessage(errorMessage).setTitle(errorTitle).setIcon(R.drawable.ic_alert).setOnDismissListener(new OnDismissListener() {
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
			
			purchaseTask.execute();
			preorderTask.execute();
			return rootView;
		}
		
		private double total(Collection<? extends HasTotal> c) {
			double total = 0d;
			for(final HasTotal item : c) {
				if(item != null) {
					total += item.getTotal();
				}
			}
			return total;
		}

		private void populatePreorderReport(final View view, final List<PreorderReportEntry> preorders) {
			final ListView report = (ListView) view.findViewById(R.id.preorder_report);
			report.setEmptyView(view.findViewById(R.id.preorder_report_empty));
			if(preorders.isEmpty()) {
				view.findViewById(R.id.preorder_total_label).setVisibility(View.INVISIBLE);
				view.findViewById(R.id.preorder_total).setVisibility(View.INVISIBLE);
			}
			PreorderReportAdapter adapter = new PreorderReportAdapter(view.getContext(), preorders);
			report.setAdapter(adapter);
			final TextView preorderTotal = (TextView) view.findViewById(R.id.preorder_total);
			preorderTotal.setText(UtilFunctions.formatPrice(total(preorders)));
		}

		private void populatePurchaseReport(final View view, final List<PurchaseReportEntry> purchases) {
			final ListView report = (ListView) view.findViewById(R.id.purchase_report);
			report.setEmptyView(view.findViewById(R.id.purchase_report_empty));
			if(purchases.isEmpty()) {
				view.findViewById(R.id.purchase_total_label).setVisibility(View.INVISIBLE);
				view.findViewById(R.id.purchase_total).setVisibility(View.INVISIBLE);
			}
			final PurchaseReportAdapter adapter = new PurchaseReportAdapter(view.getContext(), purchases);
			report.setAdapter(adapter);
			final TextView purchaseTotal = (TextView) view.findViewById(R.id.purchase_total);
			purchaseTotal.setText(UtilFunctions.formatPrice(total(purchases)));
		}
	}
}

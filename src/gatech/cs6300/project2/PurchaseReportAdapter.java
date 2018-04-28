package gatech.cs6300.project2;

import java.util.List;

import gatech.cs6300.project2.model.PurchaseReportEntry;
import gatech.cs6300.project2.util.UtilFunctions;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * The daily purchase report needs a custom view for purchase items. That class implements that view.
 */
public class PurchaseReportAdapter extends ArrayAdapter<PurchaseReportEntry> {
	public PurchaseReportAdapter(final Context context, final List<PurchaseReportEntry> entries) {
		super(context, R.layout.purchase_report_entry, entries);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		PurchaseReportEntry item = getItem(position);

		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.purchase_report_entry, parent, false);
		}
		TextView time = (TextView) convertView.findViewById(R.id.purchase_report_time);
		TextView vip = (TextView) convertView.findViewById(R.id.purchase_report_vip);
		TextView total = (TextView) convertView.findViewById(R.id.purchase_report_total);

		if(item == null) {
			time.setText("Time");
			vip.setText("VIP");
			total.setText("Total");
		} else {
			time.setText(UtilFunctions.formatBirthDate(item.getTime()));
			vip.setText(Integer.toString(item.getVipId()));
			total.setText(UtilFunctions.formatPrice(item.getTotal()));
		}

		return convertView;
	}
}

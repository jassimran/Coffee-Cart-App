package gatech.cs6300.project2;

import java.util.List;

import gatech.cs6300.project2.model.PreorderReportEntry;
import gatech.cs6300.project2.util.UtilFunctions;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * The preorder report needs a custom view. That is provided by this class.
 */
public class PreorderReportAdapter extends ArrayAdapter<PreorderReportEntry> {
	public PreorderReportAdapter(final Context context, final List<PreorderReportEntry> entries) {
		super(context, R.layout.preorder_report_entry, entries);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		PreorderReportEntry item = getItem(position);

		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.preorder_report_entry, parent, false);
		}
		TextView vip = (TextView) convertView.findViewById(R.id.preorder_report_vip);
		TextView total = (TextView) convertView.findViewById(R.id.preorder_report_total);

		if(item == null) {
			vip.setText("VIP");
			total.setText("Total");
		} else {
			vip.setText(Integer.toString(item.getVipId()));
			total.setText(UtilFunctions.formatPrice(item.getTotal()));
		}

		return convertView;
	}
}

package gatech.cs6300.project2;

import gatech.cs6300.project2.model.PreorderItem;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
/**
 * The purpose of this class it to provide a custom view for preorders.
 */
public class CustomArrayAdapter extends ArrayAdapter<PreorderItem>{
	
     
     public CustomArrayAdapter(Context context,int preordercartlist, List<PreorderItem> objects){
    	 super(context, R.layout.preordercartlist,objects);
     }

	
	 @Override
     public View getView ( int position, View convertView, ViewGroup parent ) {
		 
		 if (convertView == null) {
	          convertView = LayoutInflater.from(getContext()).inflate(R.layout.preordercartlist, parent, false);
	       }
		 
		 PreorderItem item = getItem(position);
		 
		 TextView itemName = (TextView)convertView.findViewById(R.id.ItemNametextView);
		 TextView itemQuantity = (TextView)convertView.findViewById(R.id.quantitytextView);
		 TextView itemDeliveryDate = (TextView)convertView.findViewById(R.id.deliverydatetextview);
		 TextView itemprice = (TextView)convertView.findViewById(R.id.pricetextView);
		 TextView itemserialno = (TextView)convertView.findViewById(R.id.serialnotextView);
		 
		 itemName.setText(" "+item.getName());
		 itemQuantity.setText(String.valueOf(item.getQuantity()));
		 itemDeliveryDate.setText(" "+item.getdeliveryDate());
		 itemprice.setText("$"+String.valueOf(item.getprice()));
		 itemserialno.setText(String.valueOf(position+1) + ". ");
		 
		 return convertView;
	 }

}
 
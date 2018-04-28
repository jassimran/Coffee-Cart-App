/**
 * 
 */
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
 * @author Jassimran
 * The purpose of this class it to manage the preorders that are displayed.
 */
public class AddItemArrayAdapter extends ArrayAdapter<PreorderItem>{
	
    
    public AddItemArrayAdapter(Context context,int preordercartlist, List<PreorderItem> objects){
   	 super(context, R.layout.preorderitemlist,objects);
    }
	
	 @Override
    public View getView ( int position, View convertView, ViewGroup parent ) {
		 
		 if (convertView == null) {
	          convertView = LayoutInflater.from(getContext()).inflate(R.layout.preorderitemlist, parent, false);
	       }
		 
		 PreorderItem item = getItem(position);
		 
		 TextView itemName = (TextView)convertView.findViewById(R.id.itemNametextViewAdd);
		 TextView itemprice = (TextView)convertView.findViewById(R.id.pricetextViewAdd);
		 TextView itemserialno = (TextView)convertView.findViewById(R.id.serialnotextViewAdd);
		 
		 itemName.setText(item.getName());
		 itemprice.setText(String.valueOf(item.getprice()));
		 itemserialno.setText(item.getId());
		 
		 return convertView;
	 }


}

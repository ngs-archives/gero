package org.ngsdev.android.sample20;

import org.ngsdev.android.util.Log20;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Sample20 extends ListActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setListAdapter(ArrayAdapter.createFromResource(this,
				R.array.top_items, android.R.layout.simple_list_item_1));
		
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		try {
			String item = (String) this.getListAdapter().getItem(position);
			Log20.i("Starting activity for path " + item);
			Intent i = new Intent(Intent.ACTION_VIEW,
					Uri.parse("s20://sample20.app" + item));
			this.startActivity(i);
		} catch (Exception e) {
			Log20.e(e);
		}
	}
}

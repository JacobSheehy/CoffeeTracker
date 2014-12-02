package com.jacobsheehy.coffeetracker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class HomeFragment extends ListFragment {

	public class CoffeeAdapter extends SimpleCursorAdapter {

		private int mSelectedPosition;
		Cursor items;
		private Context ctx;
		private int layout;

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {

		    Cursor c = getCursor();


		    
		    final LayoutInflater inflater = LayoutInflater.from(context);
		    View v = inflater.inflate(layout, parent, false);

		    if(c.getCount()>0) {
		    
			    int nameCol = c.getColumnIndex(CoffeeDb.KEY_SIZE_NAME);
			    String name = c.getString(nameCol);
	
			    TextView name_text = (TextView) v.findViewById(R.id.textCoffeeSize);
			    if (name_text != null) {
			        name_text.setText(name);
			    }
		    }
		    return v;
		}


		public CoffeeAdapter(Context coffeeContext, int layout, Cursor c, String[] from, int[] to) {
		    super(coffeeContext, layout, c, from, to);
		    this.ctx = coffeeContext;
		    this.layout = layout;
		}


		@Override
		public void bindView(View v, Context context, Cursor c) {

		    int nameCol = c.getColumnIndex(CoffeeDb.KEY_SIZE_NAME);
		    String name = c.getString(nameCol);


		    TextView name_text = (TextView) v.findViewById(R.id.textCoffeeSize);
		    if (name_text != null) {
		        name_text.setText(name);
		    }

		}


		public void setSelectedPosition(int position) {
		    mSelectedPosition = position;
		    notifyDataSetChanged();

		}
	}
	
	CoffeeDb db;
	Context context;
	
	ListAdapter adapter = null;
	Cursor cursor = null;
	
	@Override
	public void onResume() {
		super.onResume();
		if(cursor!=null ) {
			cursor.requery();
		}
		updateView();
	}

	private void updateView() {
		Cursor c = db.fetchTodaysCoffees();
		

		getActivity().startManagingCursor(c);

		adapter = new CoffeeAdapter(context,
				R.layout.coffee_list_item, c,
				new String[] { CoffeeDb.KEY_SIZE_NAME, CoffeeDb.KEY_TIME },
				new int[] { R.id.textCoffeeSize, R.id.textCoffeeTime });
		
		
		setListAdapter(adapter);

		//db.close();

	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_home, null);
		context = v.getContext();

		db = new CoffeeDb(context);
		db.open();
		
		// updateView();
		return v;
	}

}

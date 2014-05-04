package com.jacobsheehy.coffeetracker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HomeFragment extends Fragment {

	TextView summary;
	CoffeeDb db;
	Context context;

	@Override
	public void onResume() {
		updateView();
		super.onResume();
	}

	private void updateView() {
		db = new CoffeeDb(context);
		db.open();
		Cursor c = db.fetchTodaysCoffees();

		ArrayList<Coffee> coffeeList = new ArrayList<Coffee>();
		String displayList = "";
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

		while (c.moveToNext()) {
			Coffee coffee = new Coffee();
			coffee.setConsumptionTime(c.getLong(1));
			coffee.setSizeName(c.getString(2));
			coffeeList.add(coffee);
			displayList += sdf.format(new Date(coffee.getConsumptionTime()))
					+ ": " + coffee.getSizeName() + "\n";
		}
		db.close();

		summary.setText(displayList);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_home, null);
		context = v.getContext();
		summary = (TextView) v.findViewById(R.id.home_summary);

		updateView();
		return v;
	}

}

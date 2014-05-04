package com.jacobsheehy.coffeetracker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AddFragment extends Fragment implements OnClickListener {

	Button buttonLarge;
	Button buttonMedium;
	Button buttonSmall;
	
	CoffeeDb db;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_add, null);
		db = new CoffeeDb(v.getContext());
		buttonLarge = (Button) v.findViewById(R.id.buttonLarge);
		buttonLarge.setOnClickListener(this);
		buttonMedium = (Button) v.findViewById(R.id.buttonMedium);
		buttonMedium.setOnClickListener(this);
		buttonSmall = (Button) v.findViewById(R.id.buttonSmall);
		buttonSmall.setOnClickListener(this);
		return v;
	}
	
	
	private void updateView() {
		db = new CoffeeDb(getActivity());
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
		TextView summary = (TextView) getActivity().findViewById(R.id.home_summary);
		summary.setText(displayList);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.buttonLarge:
			db.open();
			db.addCoffee("large");
			db.close();
			Toast.makeText(getActivity(), "Added Large", Toast.LENGTH_SHORT).show();
			updateView();
			break;
		case R.id.buttonMedium:
			db.open();
			db.addCoffee("medium");
			db.close();
			Toast.makeText(getActivity(), "Added Medium", Toast.LENGTH_SHORT).show();
			updateView();
			break;
		case R.id.buttonSmall:
			db.open();
			db.addCoffee("small");
			db.close();
			Toast.makeText(getActivity(), "Added Small", Toast.LENGTH_SHORT).show();
			updateView();
			break;
		default:
			// 
		}
	}
}

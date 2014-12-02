package com.jacobsheehy.coffeetracker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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
	ViewPager pager;
	
	private static final String LARGE = "large";
	private static final String MEDIUM = "medium";
	private static final String SMALL = "small";
	
	public void moveNext() {
	    //it doesn't matter if you're already in the last item
	    pager.setCurrentItem(pager.getCurrentItem() + 1);
	}

	public void movePrevious() {
	    //it doesn't matter if you're already in the first item
	    pager.setCurrentItem(pager.getCurrentItem() - 1);
	}
	
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
				
		pager = (ViewPager) getActivity().findViewById(R.id.pager);
		movePrevious();
	}
	
	private void sendNotification(String size) {
		log("add fragment sending notification for " + size);
		ProductivityNotificationSender sender = new ProductivityNotificationSender(getActivity().getApplicationContext());
		sender.send(0, size);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.buttonLarge:
			db.open();
			db.addCoffee(LARGE);
			db.close();
			Toast.makeText(getActivity(), "Added Large", Toast.LENGTH_SHORT).show();
			updateView();
			sendNotification(LARGE);
			break;
		case R.id.buttonMedium:
			db.open();
			db.addCoffee(MEDIUM);
			db.close();
			Toast.makeText(getActivity(), "Added Medium", Toast.LENGTH_SHORT).show();
			updateView();
			sendNotification(MEDIUM);
			break;
		case R.id.buttonSmall:
			db.open();
			db.addCoffee(SMALL);
			db.close();
			Toast.makeText(getActivity(), "Added Small", Toast.LENGTH_SHORT).show();
			updateView();
			sendNotification(SMALL);
			break;
		default:
			// 
		}
	}
	
	private void log(String message) {
		System.out.println(message);
	}
}

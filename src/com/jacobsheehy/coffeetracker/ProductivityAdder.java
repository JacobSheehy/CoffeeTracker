package com.jacobsheehy.coffeetracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ProductivityAdder extends BroadcastReceiver {

	public static final String ADD_HIGH = "com.jacobsheehy.coffeetracker.ADD_HIGH_PROD";
	public static final String ADD_MEDIUM = "com.jacobsheehy.coffeetracker.ADD_MEDIUM_PROD";
	public static final String ADD_LOW = "com.jacobsheehy.coffeetracker.ADD_LOW_PROD";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		log("productivity adder onreceive");
		if(intent!=null) {
			if(intent.getAction() != null) {
				if(intent.getAction().equals(ADD_HIGH)) {
					log("productivity adder: high");
				} else if(intent.getAction().equals(ADD_MEDIUM)) {
					log("productivity adder: medium");					
				} else if(intent.getAction().equals(ADD_LOW)) {
					log("productivity adder: low");
				} else {
					log("productivity adder: none");
				}
			} else {
				log("productivity adder action null");
			}
		} else {
			log("productivity adder intent null");
		}
		
	}
	
	private void log(String message) {
		System.out.println(message);
	}

}

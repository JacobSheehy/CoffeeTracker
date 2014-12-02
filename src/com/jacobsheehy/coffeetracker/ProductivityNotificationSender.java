package com.jacobsheehy.coffeetracker;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

public class ProductivityNotificationSender  {

	private Context mContext;
	private Handler mHandler;
	
	public ProductivityNotificationSender(Context context) {
		mContext = context;
	}
	
	public void send(long coffeeID, String size) {
		DelayedNotificationSender sendNotification = new DelayedNotificationSender(mContext, coffeeID, size);
		mHandler = new Handler();
		mHandler.postDelayed(sendNotification, 1000);
	}

	public class DelayedNotificationSender implements Runnable {

		Context delayedContext;
		long id;
		String size;
		
		public DelayedNotificationSender (Context context, long notID, String coffeeSize) {
			delayedContext = context;
			id = notID;
			size = coffeeSize;
		}
		
		@Override
		public void run() {
			 if (delayedContext!=null) {
				 log("delayednotificationsender is running");
				 android.support.v4.app.TaskStackBuilder stackBuilder = android.support.v4.app.TaskStackBuilder
							.create(mContext);
				Intent lowIntent = new Intent(mContext, ProductivityAdder.class);
				
				lowIntent.setAction(ProductivityAdder.ADD_LOW);
				stackBuilder.addNextIntent(lowIntent);
				PendingIntent pendingLow = stackBuilder.getPendingIntent(0,
						PendingIntent.FLAG_UPDATE_CURRENT);
				
				Intent mediumIntent = new Intent(mContext, ProductivityAdder.class);
				mediumIntent.setAction(ProductivityAdder.ADD_MEDIUM);
				stackBuilder.addNextIntent(mediumIntent);
				PendingIntent pendingMedium = stackBuilder.getPendingIntent(0,
						PendingIntent.FLAG_UPDATE_CURRENT);
				
				Intent highIntent = new Intent(mContext, ProductivityAdder.class);
				highIntent.setAction(ProductivityAdder.ADD_HIGH);
				stackBuilder.addNextIntent(highIntent);
				PendingIntent pendingHigh = stackBuilder.getPendingIntent(0,
						PendingIntent.FLAG_UPDATE_CURRENT);
				
				String message = "Were you productive after that " + size + " coffee?";
				
				Notification.Builder mBuilder = new Notification.Builder(
						mContext).setSmallIcon(R.drawable.ic_launcher).setContentTitle("Productive?").setContentText(message)
						.addAction(0, "Low", pendingLow)
						.addAction(0, "Medium", pendingMedium)
						.addAction(0, "High", pendingHigh);
				
				
				mBuilder.setContentIntent(pendingLow);
				NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
				// mId allows you to update the
				// notification later on.
				mNotificationManager.notify((int)id, mBuilder.build());
			 } else {
				 log("delayed context is null");
			 }
		}
	}
	
	private void log(String message) {
		System.out.println(message);
	}
}

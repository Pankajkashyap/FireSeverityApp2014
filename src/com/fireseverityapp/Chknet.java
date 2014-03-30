package com.fireseverityapp;

import java.util.ArrayList;
import java.util.Calendar;

import com.fireseverityapp.mail.MailAccount;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

/*
 * Author Kavy 15/09/2013
 */
@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class Chknet  extends IntentService {
	
	private Handler handler= new Handler();
		boolean position = false;
		int position_int = 0;
		boolean network = false;
		Calendar current_time;
		IBinder mBinder;
		
		Thread thread;
		ArrayList<Integer> p_id = new ArrayList<Integer>();
		ArrayList<String> latitude = new ArrayList<String>();
		ArrayList<String> longitude = new ArrayList<String>();
		ArrayList<String> priority = new ArrayList<String>();
		ArrayList<String> path = new ArrayList<String>();
		ArrayList<String> email = new ArrayList<String>();
		ArrayList<String> name_array = new ArrayList<String>();
		ArrayList<String> organistion_ar = new ArrayList<String>();
		ArrayList<String> des_arr = new ArrayList<String>();
		
		int Count = 0;
		Boolean Send = false;
		
		// add by Ling An - 17-10-2013
		String emailSenderId = null;
		String emailSenderPWD = null;
		String emailReceiver = null;
		String emailSubject = null;
	 

	  public Chknet() {
	    super("DownloadService");
	  }

	  // Will be called asynchronously be Android
	  @Override
	  protected void onHandleIntent(Intent intent) {

		  // Initial Email Account Info - add by Ling An - 17-10-2013
		  MailAccount mailAccount = new MailAccount();
		  emailSenderId = mailAccount.getAccountID();
		  emailSenderPWD = mailAccount.getPassword();
		  emailReceiver = mailAccount.getReceiverEmail();
		  emailSubject = mailAccount.getSubject();


			if (android.os.Build.VERSION.SDK_INT >= 11) {
				StrictMode.ThreadPolicy thrd = new StrictMode.ThreadPolicy.Builder()
						.build();
				StrictMode.setThreadPolicy(thrd);

			}

			handler.post(new Runnable() {

				@Override
				public void run() {

					Toast.makeText(
							Chknet.this,
							"Service run",
							Toast.LENGTH_LONG)
							.show();

				}

			});
			
			try {
				p_id.clear();
				latitude.clear();
				longitude.clear();
				email.clear();
				
				name_array.clear();
				organistion_ar.clear();
				des_arr.clear();
				
				priority.clear();
				path.clear();

				if (Utills.isNetworkconn(getApplicationContext())) {
					
					network=false;
					p_id.clear();
					latitude.clear();
					longitude.clear();
					email.clear();
					priority.clear();
					path.clear();
					name_array.clear();
					organistion_ar.clear();
					des_arr.clear();
					DataBase Db = new DataBase(getApplicationContext());
					Db.open();
					Cursor cursor = Db.getAll();
					Log.e("cursor", ""+cursor.getCount());
					while (cursor.moveToNext()) {
						p_id.add(cursor.getInt(0));
						latitude.add(cursor.getString(1));
						longitude.add(cursor.getString(2));
						priority.add(cursor.getString(3));
						path.add(cursor.getString(4));
						email.add(cursor.getString(5));
						name_array.add(cursor.getString(6));
						organistion_ar.add(cursor.getString(7));
						des_arr.add(cursor.getString(8));

					}
					Db.close();

					if (path.size() > 0) {
						while (path.size() > Count) {

							if (!Send) {
								Send = true;
								GMailSender mailsender = new GMailSender(
										emailSenderId,
										emailSenderPWD);

								String[] toArr = {emailReceiver};
								mailsender.set_to(toArr);
								mailsender.set_from(emailSenderId);
								mailsender.set_subject(emailSubject + " From " + name_array.get(Count));
								
						mailsender
								.setBody("Data Sent From "
										+ name_array.get(Count)
										+ "\nLatitude = "
										+ latitude.get(Count)
										+ "\nLongitude = "
										+ longitude.get(Count)
										+ "\nFire Level Severity Attribute = "
										+ priority.get(Count)
										+ "\nOrganisation = "
										+ organistion_ar.get(Count)
										+ " \nDesignation = "
										+ des_arr.get(Count)
										+ " \nEmail_id = "
										+ email.get(Count));
							
						try {
							if (!(path.get(Count).equalsIgnoreCase(""))) {
								mailsender.addAttachment(path.get(Count));
							}
							
							
						} catch (Exception e) {
							// TODO: handle exception
						}
								try {
									if (mailsender.send()) {

										
										
										handler.post(new Runnable() {

											@Override
											public void run() {

												Toast.makeText(
														Chknet.this,
														"Email was sent successfully.",
														Toast.LENGTH_LONG)
														.show();

											}

										});

										Send = false;
									} else {
										handler.post(new Runnable() {

											@Override
											public void run() {

												Toast.makeText(
														Chknet.this,
														"Email was not sent.",
														Toast.LENGTH_LONG)
														.show();

											}

										});
										Send = false;
									}
								} catch (Exception e) {
									Send = false;
									// Log.e("MailApp",
									// "Could not send email", e);
								}
								
								Db.open();
								Db.Delete_from(p_id.get(Count));
								Count++;
							}
							if (path.size() == Count) {
								DataBase data_base = new DataBase(
										getApplicationContext());
								Db.open();
								data_base.delete_table();
							}
						}
					}
					
					
				}
				
				
				
				
				//thread.start();
			} catch (Exception e) {
				e.getMessage();
			}
		
		
		
			

		
			try {
				thread.start();
			} catch (Exception e) {
				// TODO: handle exception
			}
				
	  }

	
	} 
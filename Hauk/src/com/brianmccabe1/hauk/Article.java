package com.brianmccabe1.hauk;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.brianmccabe.hauk.R;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class Article extends Activity {

	private String finalUrl="https://17cc8c03-92e5-4ff6-9b0a-858abac5f74a-bluemix.cloudant.com/hauk/_all_docs?include_docs=true";

	ArrayList<HashMap<String, String>> contactList;
	
	String title;
	String location;
	WebView webview;
	ProgressDialog progress;
	AnimationDrawable frameAnimation;
	ProgressBar mProgressBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_article);
		
		this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.maintitlebar);
		
		 mProgressBar = (ProgressBar) findViewById(R.id.my_progress);
	       mProgressBar.setBackgroundResource(R.drawable.ani_icon);

	        frameAnimation = (AnimationDrawable) mProgressBar.getBackground();

	        frameAnimation.start();
	        
		webview = new WebView(this);
		webview.setWebViewClient(new WebViewClient());
		Bundle extras = getIntent().getExtras();
		Log.d("dtest", extras.getString("source"));
		title = extras.getString("source");
		location = extras.getString("loc");
		
		contactList = new ArrayList<HashMap<String, String>>();
		   
		progress = new ProgressDialog(this);
		progress.setTitle("Loading");
		progress.setMessage("Please wait...");
		progress.show();
		
		new createUser().execute();
		  
	        setContentView(webview);

	    
	}

	
	private void Sort() {
		Log.d("location", contactList.get(0).get("location"));
		Log.d("location", contactList.get(0).get("title"));
		for(int i = 0; i < contactList.size(); i++){
			if(contactList.get(i).get("location").equals(location)){
				if(contactList.get(i).get("paper").equals(title))
				{
					webview.loadUrl(contactList.get(i).get("url"));
					
				}
				
			}
			
		}
		
	}
	
	private class createUser extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			

		}

		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();
			
			String jsonStr = sh.makeServiceCall(finalUrl, ServiceHandler.GET);
			
			Log.d("Response: ", "> " + jsonStr);
			
				if (jsonStr != null) {
				
				try {
					JSONObject t = new JSONObject(jsonStr);
					
					JSONArray rows = t.getJSONArray("rows");
					
					//ArrayList<JSONObject> jo = new ArrayList<JSONObject>();
					for(int i = 0; i < rows.length(); i++){
						JSONObject test = new JSONObject(rows.get(i).toString());
						JSONObject test2 = (JSONObject) test.get("doc");
						String title = test2.getString("title");
						String url = test2.getString("url");
						String location = test2.getString("location");
						String paper = test2.getString("paper");
						Log.d("Responset: ", "> " + title);
						Log.d("Responseu: ", "> " + url);
						Log.d("Responsel: ", "> " + location);
						Log.d("Responsep: ", "> " + paper);
						
						HashMap<String, String> contact = new HashMap<String, String>();

						contact.put("title", title);
						contact.put("url", url);
						contact.put("location", location);
						contact.put("paper", paper);
				
						contactList.add(contact);
						
					}
					
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				 Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			Sort();
			Log.d("testpls", " " + contactList.size());
			frameAnimation.stop();
			mProgressBar.setVisibility(View.INVISIBLE);
			progress.dismiss();
		}

		

		
	}
}

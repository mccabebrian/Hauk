package com.brianmccabe1.hauk;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.brianmccabe.hauk.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.AdapterView.OnItemClickListener;

public class SourceList extends Activity {

	
	 ArrayList<String> ArrayofItem = new ArrayList<String>();
	 Context context = this;
 	 ListView listView;
 	 private String finalUrl="https://17cc8c03-92e5-4ff6-9b0a-858abac5f74a-bluemix.cloudant.com/hauk/_all_docs?include_docs=true";
 	 String location;
 	 ArrayList<HashMap<String, String>> contactList;
 	
 	AnimationDrawable frameAnimation;
	ProgressBar mProgressBar;
 	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_source_list);
        this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.maintitlebar);
        
        mProgressBar = (ProgressBar) findViewById(R.id.my_progress);
        mProgressBar.setBackgroundResource(R.drawable.ani_icon);
        
        frameAnimation = (AnimationDrawable) mProgressBar.getBackground();

        frameAnimation.start();
        
        Bundle extras = getIntent().getExtras();
    	location = extras.getString("title");
    	Log.d("loc", location);
    
    	new createUser().execute();
		contactList = new ArrayList<HashMap<String, String>>();
        listView = (ListView) findViewById(R.id.listView1);

        
        
        
        

	}
	
	private void Sort() {
		// TODO Auto-generated method stub
		for(int i = 0; i < contactList.size(); i++){
    		if(contactList.get(i).get("location").equals(location)){
    			
    			ArrayofItem.add(contactList.get(i).get("paper"));
    			
    		}
    	}
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.listview, ArrayofItem);
		listView.setAdapter(adapter);
    	
		listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                int position, long id) {
            	
            	
            	//Toast.makeText(context, "" + listView.getItemIdAtPosition(position), Toast.LENGTH_LONG).show();
            	
            		Log.d("loc2", location);
            		Intent intent = new Intent(context, Article.class);
            		intent.putExtra("source", ArrayofItem.get(position).toString());
            		intent.putExtra("loc", location);
            		Log.d("loc3", location);
            		startActivity(intent);
            		
            	
            	
            }
        });
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
						Log.d("Response: ", "> " + title);
						
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
		}

		

		
	}
}

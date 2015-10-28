package com.brianmccabe1.hauk;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.brianmccabe.hauk.R;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
	
	Context context = this;
	private String finalUrl="https://17cc8c03-92e5-4ff6-9b0a-858abac5f74a-bluemix.cloudant.com/hauk/_all_docs?include_docs=true";
	private HandleXML obj;
	ArrayList<String> ArrayofItem = new ArrayList<String>();
	ListView listView;
	private GoogleMap googleMap;
	ArrayList<String> list = new ArrayList<String>();
	MarkerOptions marker;
	MarkerOptions marker2;
	MarkerOptions marker3;
	MarkerOptions marker4;
	MarkerOptions marker5;
	double longitude;       
    double latitude;
	ArrayList<HashMap<String, String>> contactList;
	ProgressDialog progress;
	String zoomLevel = "2.0";
	AnimationDrawable frameAnimation;
	ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	 
        super.onCreate(savedInstanceState);
        
        this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);  
        setContentView(R.layout.activity_main);
        
        
      
        this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.maintitlebar);  
        
        mProgressBar = (ProgressBar) findViewById(R.id.my_progress);
        mProgressBar.setBackgroundResource(R.drawable.ani_icon);

        frameAnimation = (AnimationDrawable) mProgressBar.getBackground();

        frameAnimation.start();
        
        ArrayofItem.add("Greek bailout talks go ahead as full EU summit is canceled");
		ArrayofItem.add("MEXICO: Top drug lord Jouguin 'EL CHAPO' Guzman escapes");
		ArrayofItem.add("US secretary of state 'Hopeful' Iran nuke deal near");
		ArrayofItem.add("Wary of climate change, Vanuatu villagers seek higher ground");
		ArrayofItem.add("Spain: 1 dead of heat stroke, others struggling in heat wave");
		contactList = new ArrayList<HashMap<String, String>>();
		//progress = new ProgressDialog(this);
		//progress.setTitle("Loading");
		//progress.setMessage("Please wait...");
		//progress.show();
		// To dismiss the di
		
		new createUser().execute();
		
		
		
		listView = (ListView) findViewById(R.id.listView1);

        
       
    }
    
   

    private void initilizeMap() {
    	
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();
 
            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
        

        Geocoder geocoder = new Geocoder(context);  
        List<Address> addresses;
        try {
        	for(int i=0; i<contactList.size(); i++){
			addresses = geocoder.getFromLocationName(contactList.get(i).get("location2"), 1);
			if(addresses.size() > 0) {
	            latitude= addresses.get(0).getLatitude();
	            longitude= addresses.get(0).getLongitude();
	            googleMap.addMarker(new MarkerOptions()
	    		.position(new LatLng(latitude, longitude))
	    		.icon(BitmapDescriptorFactory.fromResource(R.drawable.dot))
	    		.title(contactList.get(i).get("location2")));
			}
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        
        googleMap.setOnMarkerClickListener(new OnMarkerClickListener()
        {
        	
            @Override
            public boolean onMarkerClick(Marker arg0) {
            	Log.d("test", arg0.getTitle());
            	String loc = arg0.getTitle();
            	HighlightArticle(loc);
            	float getzoom = googleMap.getCameraPosition().zoom;
   	           CameraUpdate center;
   	           Log.d("zoomlev", "" + getzoom);
   	           center=CameraUpdateFactory.newLatLng(arg0.getPosition());
   		       CameraUpdate zoom=CameraUpdateFactory.zoomTo(7);
   		    
   		 //v.setBackgroundColor(Color.RED);
   		      googleMap.moveCamera(center);
   		      googleMap.animateCamera(zoom);
   	          
   	        	 
                return true;
            }

			

        }); 
    }
    
    private void HighlightArticle(String loc) {
		// TODO Auto-generated method stub
    	
    	for(int i = 0; i< contactList.size(); i++){
    		
    		if(contactList.get(i).get("location").equals(loc)){
    			
    		}
    		
    		
    	}
    	
		
	}
    
    public void move(View view){
    	 obj = new HandleXML(finalUrl);
         obj.fetchXML();
         
         while(obj.parsingComplete);
         obj.list.get(1);
         Log.d("title", obj.getTitle());
         Log.d("title", obj.list.get(2));
         Log.d("url", obj.urllist.get(2));
    }

   
    
    private void Sort() {
    	 try {
             // Loading map
             initilizeMap();
  
         } catch (Exception e) {
             e.printStackTrace();
         }
    	Log.d("testpls2", " " + contactList.size());
    	
    	ArrayList<String> titles = new ArrayList<String>();
    	ArrayList<String> flags = new ArrayList<String>();
    	ArrayList<Integer> flagint = new ArrayList<Integer>();
    	
    	for(int i = 0; i < contactList.size(); i++){
    		
    		if(!titles.contains(contactList.get(i).get("title"))){
    			titles.add(contactList.get(i).get("title"));
    		}
    		if(!flags.contains(contactList.get(i).get("location"))){
    			flags.add(contactList.get(i).get("location"));
    			flagint.add(getResources().getIdentifier(flags.get(i), "drawable", getPackageName()));
    		}
    		
    	}
   
    	 CustomList adapter = new  CustomList(MainActivity.this, titles, flagint);
        listView.setAdapter(adapter);
        
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                int position, long id) {
            	
            	
            	 Geocoder geocoder = new Geocoder(context);  
                 List<Address> addresses;
                 Log.d("cl", ""+contactList.get(position));
				Log.d("onclick", "" + position);
				//addresses = geocoder.getFromLocationName(contactList.get(position).get("location"), 1);
				
				 		   Intent intent = new Intent(context, SourceList.class);
				        	intent.putExtra("title", contactList.get(position).get("location2"));
				            startActivity(intent);
            	
            }
        });
	}
    
    public static float round(double d, int decimalPlace) {
        return BigDecimal.valueOf(d).setScale(decimalPlace,BigDecimal.ROUND_HALF_UP).floatValue();
   }


    
    private class createUser extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			

		}

		@SuppressLint("DefaultLocale") protected Void doInBackground(Void... arg0) {
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
						
						String location = test2.getString("location");
						
						
						String jsonStr2 = sh.makeServiceCall("http://maps.googleapis.com/maps/api/geocode/json?address=" + location + "&sensor=false", ServiceHandler.GET);
						JSONObject t2 = new JSONObject(jsonStr2);
						Log.d("town: ", jsonStr2);
						
						JSONArray rows2 = t2.getJSONArray("results");
						
						
							JSONObject obj = new JSONObject(rows2.get(0).toString());
							JSONArray rows3 = obj.getJSONArray("address_components");
							JSONObject obj2 = new JSONObject(rows3.get(rows3.length() -1).toString());
							//JSONObject loc = (JSONObject) rows3.get(1);
							String loc2 = obj2.get("long_name").toString();
							
							Log.d("town: ", loc2);
						
						Log.d("Response: ", "> " + title);
						
						HashMap<String, String> contact = new HashMap<String, String>();

						
						contact.put("title", title);
						
						contact.put("location", loc2.toLowerCase().replaceAll("\\s+",""));
						
						contact.put("location2", location);
						
				
						if(contactList.contains(contact))
							Log.d("dublicate", "vola");
						else
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
			//spinner.setVisibility(View.GONE);
			//progress.dismiss();
			frameAnimation.stop();
			mProgressBar.setVisibility(View.INVISIBLE);
		}

		
	}
}

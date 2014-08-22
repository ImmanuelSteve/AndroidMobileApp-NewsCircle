package id.ac.ukdw.newscircle;

/**
 * Created by Steven Renaldo Antony / 71110054 on 18/04/14.
 */


import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;



public class ContentActivity extends ListActivity implements AccelerometerListener {
	
	 ArrayList<String> news = new ArrayList<String>();
	 ArrayList<String> date = new ArrayList<String>();
	 ArrayList<String> publisher = new ArrayList<String>();
	 ArrayList<String> url = new ArrayList<String>();
	 private double latitude;
	 private double longitude;
	 private String queryNews;
	 private int index_now = 0;
	 private boolean onShake = false;
		    
	    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Log.d("Masuk List View","Ready");
        Bundle bundle = getIntent().getExtras();
        news = bundle.getStringArrayList("news");
        date = bundle.getStringArrayList("date");
        publisher = bundle.getStringArrayList("publisher");
        url = bundle.getStringArrayList("url");
        latitude = bundle.getDouble("latitude");
        longitude = bundle.getDouble("longitude");
        queryNews = bundle.getString("queryNews");
        if(bundle.getInt("index_now")!=0){
        	index_now = bundle.getInt("index_now");
        }
        setListAdapter(new MobileArrayAdapter(this, news.toArray(new String[news.size()]), date.toArray(new String[date.size()]), publisher.toArray(new String[publisher.size()])));
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);
 
        return super.onCreateOptionsMenu(menu);
    }
    
    /**
     * On selecting action bar icons
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
        case R.id.action_load_more:
        	//load more news
        	LoadMoreNews();
        	return true;
        case R.id.action_location:
            // show location in maps
        	Toast.makeText(getBaseContext(), "Show My Location", Toast.LENGTH_LONG).show();
        	ShowMyLocation();
            return true;
        case R.id.action_help:
            // help
        	Help();
            return true;
        case R.id.action_about:
            // about author app
        	About();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    private void ShowMyLocation() {
    	Intent intent = new Intent(ContentActivity.this,MapActivity.class);
		Bundle bundle = new Bundle();
		bundle.putDouble("latitude", latitude);
	    bundle.putDouble("longitude", longitude);
	    intent.putExtras(bundle);  
		startActivity(intent);
    }
    
    private void Help() {
    	Intent intent = new Intent(ContentActivity.this,HelpActivity.class);
		startActivity(intent);
    }
    
    public void LoadMoreNews(){
    	index_now += 8;
    	Intent intent = new Intent(ContentActivity.this,LoadMoreActivity.class);
		Bundle bundle = new Bundle();
		bundle.putStringArrayList("news",news);
        bundle.putStringArrayList("date",date);
        bundle.putStringArrayList("publisher",publisher);
        bundle.putStringArrayList("url", url);
        bundle.putDouble("latitude", latitude);
	    bundle.putDouble("longitude", longitude);
		bundle.putString("queryNews", queryNews);
		bundle.putInt("index_now", index_now);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    intent.putExtras(bundle);  
		startActivity(intent);
		finish();
    }
    
    public void About(){
    	 AlertDialog.Builder alertDialog = new AlertDialog.Builder(ContentActivity.this);
         // Setting Dialog Title
         alertDialog.setTitle("About");
         // Setting Dialog Message
         alertDialog.setMessage(Html.fromHtml("<b>News Circle v1.0</b>" +
         		"<br>Copyright @2014 by:" +
         		"<br><br>- Steven Renaldo Antony" +
         		"<br>FB: <b>Immanuel Steve</b>" +
         		"<br><br>- Aditya Okke Sugiarso" +
         		"<br>FB: <b>Aditya Okke Sugiarso</b>" +
         		"<br><br>- Amadea Kristina Budiman" +
         		"<br>FB: <b>Amadea Kristina Budiman</b>" +
         		"<br><br>- Henri Prasetya" +
         		"<br>FB: <b>Rase(Henri Prasetya)</b>" +
         		"<br><br>Please feel free to contact us if you have any comments and questions"));
         //Setting Icon 
         alertDialog.setIcon(R.drawable.ic_action_about);
         //Setting Button
         alertDialog.setPositiveButton("OK",null);
         // Showing Alert Message
         alertDialog.show();
    }
    
    public void onAccelerationChanged(float x, float y, float z) {
		// TODO Auto-generated method stub
		
	}

	public void onShake(float force) {
		if(onShake == false){
			onShake = true;
			// Called when Motion Detected
			Toast.makeText(getBaseContext(), "Refresh", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(ContentActivity.this,MainActivity.class);
			//supaya tidak numpuk diatasnya
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}
	}

	@Override
    public void onResume() {
            super.onResume();
            //Toast.makeText(getBaseContext(), "onResume Accelerometer Started", Toast.LENGTH_LONG).show();
            
            //Check device supported Accelerometer senssor or not
            if (AccelerometerManager.isSupported(this)) {
            	
            	//Start Accelerometer Listening
    			AccelerometerManager.startListening(this);
            }
    }
	
	@Override
    public void onStop() {
            super.onStop();
            
            //Check device supported Accelerometer senssor or not
            if (AccelerometerManager.isListening()) {
            	
            	//Start Accelerometer Listening
    			AccelerometerManager.stopListening();
    			
    			//Toast.makeText(getBaseContext(), "onStop Accelerometer Stoped", Toast.LENGTH_LONG).show();
            }
           
    }
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i("Sensor", "Service  distroy");
		
		//Check device supported Accelerometer senssor or not
		if (AccelerometerManager.isListening()) {
			
			//Start Accelerometer Listening
			AccelerometerManager.stopListening();
			
			//Toast.makeText(getBaseContext(), "onDestroy Accelerometer Stoped", Toast.LENGTH_LONG).show();
        }
			
	}

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	if(news.get(0).equals("Sorry no data found")){
    		//do nothing	
    	}else{
	        Intent intent = new Intent(ContentActivity.this,WebViewActivity.class);
	        Bundle bundle = new Bundle();
	        bundle.putString("url", url.get(position));
	        intent.putExtras(bundle);
	        startActivity(intent);
    	}
    }
}

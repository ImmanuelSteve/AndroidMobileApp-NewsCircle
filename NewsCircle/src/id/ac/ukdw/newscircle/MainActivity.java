package id.ac.ukdw.newscircle;

/**
 * Created by Steven Renaldo Antony / 71110054 on 22/05/14.
 */

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

 
public class MainActivity extends Activity {
     
    Button btnShowLocation;
     
    // GPSTracker class
    GPSTracker gps;
    private String queryNews;
    private StringBuilder stringBuilder;
    private ProgressBar mActivityIndicator;
    private TextView loadingText;
    
    
    private boolean setting=false;
    
    double latitude = 0;
    double longitude = 0;
 
    ArrayList<String> news = new ArrayList<String>();
    ArrayList<String> date = new ArrayList<String>();
    ArrayList<String> publisher = new ArrayList<String>();
    ArrayList<String> url = new ArrayList<String>();
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadActivity();
    }
   
    private void loadActivity(){
    	setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    	mActivityIndicator = (ProgressBar) findViewById(R.id.address_progress);
        loadingText = (TextView) findViewById(R.id.textView1);
    	 // create class object
        gps = new GPSTracker(MainActivity.this);
        mActivityIndicator.setVisibility(View.GONE);
        loadingText.setVisibility(View.GONE);
        // check internet connection enabled
        //sementara ini diasumsikan gps ON
        //gps.canGetLocation = true;
        connectToWebService();
    }
    
    
    
	@Override
	public void onResume() {
	    super.onResume();  // Always call the superclass method first

	    // Get the Camera instance as the activity achieves full user focus
	    if (setting) {
	    	setting=false;
	        loadActivity();       
	    }
	}
    
    public void connectToWebService()
    {
    	gps.getLocation();
    	if(gps.canGetLocation() && checkConnection(this)){
		    latitude = gps.getLatitude();
		    longitude = gps.getLongitude();
		    new GetNews().execute();
	    }else{
	        checkRequirement();
	    }
    }
    
    
    protected void intentActivity(){
    	Intent intent = new Intent(MainActivity.this,ContentActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("news",news);
        bundle.putStringArrayList("date",date);
        bundle.putStringArrayList("publisher",publisher);
        bundle.putStringArrayList("url", url);
        bundle.putString("queryNews", queryNews);
        bundle.putDouble("latitude", latitude);
        bundle.putDouble("longitude", longitude);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
    
    protected void checkRequirement(){
    	if(checkConnection(this)==false){
    		setting=true;
        	//Toast.makeText(getApplicationContext(),"No Internet Connection", Toast.LENGTH_LONG).show();
        	showSettingsAlertDialog(MainActivity.this, "Connection settings",
                    "Internet connection is not enabled.  Do you want to go to settings menu?", false);
        }
    	if(gps.canGetLocation()==false){
    		setting=true;
		    // can't get location
		    // GPS or Network is not enabled
		    // Ask user to enable GPS/network in settings
		    gps.showSettingsAlert();
    	}
    	
    	
    }
    
    protected boolean checkConnection(Context c) 
    {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) c
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager telephonyManager = (TelephonyManager) c
                .getSystemService(Context.TELEPHONY_SERVICE);

        if (mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .isConnected() || telephonyManager.getDataState() == TelephonyManager.DATA_CONNECTED)
            return true;
        else
            return false;

    }
    
	public void showSettingsAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
 
        // Setting Dialog Title
        alertDialog.setTitle(title);
 
        // Setting Dialog Message
        alertDialog.setMessage(message);
         
        // Setting alert dialog icon
        //alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);
 
     // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                MainActivity.this.startActivity(intent);
            }
        });
  
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            System.exit(0);
            }
        });
  
 
        // Showing Alert Message
        alertDialog.show();
    }
	
    
    protected String getJSON(String url){
        stringBuilder = new StringBuilder();
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);

        try{
            HttpResponse httpResponse = httpClient.execute(httpGet);
            //utk dpt nilai 400,200,404
            StatusLine statusLine = httpResponse.getStatusLine();
            if(statusLine.getStatusCode() == 200){
                //berhasil
                HttpEntity httpEntity = httpResponse.getEntity();
                InputStream inputStream = httpEntity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while((line = reader.readLine()) != null){
                    stringBuilder.append(line);
                }
                inputStream.close();
            }else{
                //gagal
            }
        }catch (Exception e){

        }
        return stringBuilder.toString();
    }

    //input,sdng berjalan,output
    public class GetNews extends AsyncTask<Void, Void,String >{

        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void...voids) {
        	// Turn the indefinite activity indicator on
            mActivityIndicator.setVisibility(View.VISIBLE);
            loadingText.setVisibility(View.VISIBLE);
        	Log.d("url","https://maps.googleapis.com/maps/api/geocode/json?latlng="+Double.toString(latitude)+","+Double.toString(longitude)+"&sensor=true&key=AIzaSyAcYeXHUUoGFhvpO9U6r9Mj2CXZt-VF0ZA");
        	String result = getJSON("https://maps.googleapis.com/maps/api/geocode/json?latlng="+Double.toString(latitude)+","+Double.toString(longitude)+"&sensor=true&key=AIzaSyAcYeXHUUoGFhvpO9U6r9Mj2CXZt-VF0ZA");
            try {
                JSONObject jsonObj = new JSONObject(result);
                Log.d("jajal", result);
                JSONArray hasil = jsonObj.getJSONArray("results");
                JSONObject a = hasil.getJSONObject(0);
                JSONArray b = a.getJSONArray("address_components");
                JSONObject c = b.getJSONObject(1);
                //JSONObject d = b.getJSONObject(2);
                //result = d.getString("long_name")+","+c.getString("long_name");
                result = c.getString("long_name");
                queryNews = result;
            }catch (Exception e){
            	checkRequirement();
            }
            Log.d("IP Address",Utils.getIPAddress(true));
            Log.d("address",result);
            //case jika data tidak ditemukan
            //result = "babarsari";
            Log.d("Alamat lengkap googlenews","https://ajax.googleapis.com/ajax/services/search/news?v=1.0&q="+result+"&rsz=8&userip="+Utils.getIPAddress(true)+"&start=0&hl=id&gl=id&scoring=d");
            result = getJSON("https://ajax.googleapis.com/ajax/services/search/news?v=1.0&q="+result+"&rsz=8&userip="+Utils.getIPAddress(true)+"&start=0&hl=id&gl=id&scoring=d");
            return result;
        }

        protected void onPostExecute(String result){
        	 try {
                 JSONObject jsonObj = new JSONObject(result);
                 Log.d("json news", result);
                 JSONObject responseData = jsonObj.getJSONObject("responseData");
                 JSONArray results  = responseData.getJSONArray("results");
                 for(int i = 0;i < results.length();i++){
                	 JSONObject a = results.getJSONObject(i);
                	 news.add(a.getString("titleNoFormatting").toString());
                	 date.add(a.getString("publishedDate").toString());
                	 publisher.add(a.getString("publisher").toString());
                	 url.add(a.getString("unescapedUrl").toString());
                 }
                 if(news.isEmpty()){
                	 news.add("Sorry no data found");
                	 date.add("");
                	 publisher.add("");
                	 url.add("");
                 }
                 // Turn off the progress bar
                 mActivityIndicator.setVisibility(View.GONE);
                 loadingText.setVisibility(View.GONE);
                 //Log.d("hasile", news.get(0));
                 Toast.makeText(getApplicationContext(),"Load Success", Toast.LENGTH_LONG).show(); 
             }catch (Exception e){
            	 checkRequirement();
             }
        	 intentActivity();
             Log.d("Load Success","OKE");
        }
    } 
}

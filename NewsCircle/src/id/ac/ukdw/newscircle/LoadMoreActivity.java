package id.ac.ukdw.newscircle;

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
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class LoadMoreActivity extends Activity {

	private String queryNews;
	private int index_now = 0;
	private double latitude;
	private double longitude;
	private StringBuilder stringBuilder;
	private ProgressBar mActivityIndicator;
	private TextView loadingText;
	ArrayList<String> news = new ArrayList<String>();
	ArrayList<String> date = new ArrayList<String>();
	ArrayList<String> publisher = new ArrayList<String>();
	ArrayList<String> url = new ArrayList<String>();
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        mActivityIndicator = (ProgressBar) findViewById(R.id.address_progress);
        mActivityIndicator.setVisibility(View.GONE);
        loadingText = (TextView) findViewById(R.id.textView1);
        loadingText.setVisibility(View.GONE);
       
        Bundle bundle = getIntent().getExtras();
        news = bundle.getStringArrayList("news");
        date = bundle.getStringArrayList("date");
        publisher = bundle.getStringArrayList("publisher");
        url = bundle.getStringArrayList("url");
        latitude = bundle.getDouble("latitude");
        longitude = bundle.getDouble("longitude");
        queryNews = bundle.getString("queryNews");
        index_now = bundle.getInt("index_now");
        Log.d("index_now",String.valueOf(index_now));
        new GetNews().execute();
    }
	
	 protected void intentActivity(){
	    	Intent intent = new Intent(LoadMoreActivity.this,ContentActivity.class);
	        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        Bundle bundle = new Bundle();
	        bundle.putStringArrayList("news",news);
	        bundle.putStringArrayList("date",date);
	        bundle.putStringArrayList("publisher",publisher);
	        bundle.putStringArrayList("url", url);
	        bundle.putDouble("latitude", latitude);
		    bundle.putDouble("longitude", longitude);
	        bundle.putString("queryNews", queryNews);
	        bundle.putInt("index_now", index_now);
	        intent.putExtras(bundle);
	        startActivity(intent);
	        finish();
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
            Log.d("IP Address",Utils.getIPAddress(true));
            String results;
            Log.d("Alamat lengkap googlenews","https://ajax.googleapis.com/ajax/services/search/news?v=1.0&q="+queryNews+"&rsz=8&userip="+Utils.getIPAddress(true)+"&start="+index_now+"&hl=id&gl=id&scoring=d");
            results = getJSON("https://ajax.googleapis.com/ajax/services/search/news?v=1.0&q="+queryNews+"&rsz=8&userip="+Utils.getIPAddress(true)+"&start="+index_now+"&hl=id&gl=id&scoring=d");
            return results;
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
            	 //nothing
             }
        	 intentActivity();
             Log.d("Load Success","OKE");
        }
    } 
}

package makemachine.android.examples;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.Button;
import android.widget.EditText;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Activity2 extends Activity{
	private String url1 = " http://107.170.69.155:8080/easy-split-api/login";
	private HandleJSON obj;
	    /** Called when the activity is first created. */
		Context ctx;
		Intent i1;
		@Override
		public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test2);
		ctx = Activity2.this;
        Button l = (Button)findViewById(R.id.button1);
        l.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				// TODO Auto-generated method stub
				i1 = new Intent(ctx, Profile.class);
			    startActivity(i1);
			}
		});
        Button p = (Button)findViewById(R.id.button2);
        p.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				// TODO Auto-generated method stub
				i1 = new Intent(ctx, Login.class);
			    startActivity(i1);
			}
		});
	}     
        public void open(View view) throws MalformedURLException, IOException{
        	EditText username = (EditText)findViewById(R.id.editText1);
            String url3 = username.getText().toString();
            fetchJSON(url3);
           }
        public void fetchJSON(final String name){
  	      Thread thread = new Thread(new Runnable(){
  	         @Override
  	         public void run() {
  	         try {
  	            URL url = new URL(url1);
  	     		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
  	    		conn.setDoOutput(true);
  	    		conn.setRequestMethod("POST");
  	    		conn.setRequestProperty("Content-Type", "application/json");
  	     
  	    		String input = "{\"userName\":\"}";
  	    		
  	    		System.out.println("url:"+url);
  	    		
  	    		System.out.println("input:"+input);
  	     
  	    		OutputStream os = conn.getOutputStream();
  	    		os.write(input.getBytes());
  	    		os.flush();
  	    		
  	     
  	    		BufferedReader br = new BufferedReader(new InputStreamReader(
  	    				(conn.getInputStream())));
  	     
  	    		String output;
  	    		while ((output = br.readLine()) != null) {
  	    			System.out.println(output);
  	    		}
  	    		
  	    		

  	   	      readAndParseJSON(br);
  	     
  	    		conn.disconnect();
  	     
  	    	  } catch (MalformedURLException e) {
  	     
  	    		e.printStackTrace();
  	     
  	    	  } catch (IOException e) {
  	     
  	    		  e.printStackTrace();
  	     
  	    	   }
  	     
  	    	}

			private void readAndParseJSON(BufferedReader br) {
				// TODO Auto-generated method stub
				
			}
  	     
  	         
  	      });

  	       thread.start(); 		
  	   }
  	  
  
 
		
}
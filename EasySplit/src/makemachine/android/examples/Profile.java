package makemachine.android.examples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Profile extends Activity {
	private String url1 = "http://107.170.69.155:8080/easy-split-api/login";
	private HandleJSON obj;
	Context ctx;
	ListView listView;
	String val1,val2;
	JSONObject val3;
	Intent i1;
	String[] s = new String[100];
	protected ArrayList<String> arr = new ArrayList<String>();
	protected ArrayList<String> arrId = new ArrayList<String>();
	ArrayList<HashMap<String, String> > lst = null;
	ArrayList<JSONObject> stat = new ArrayList<JSONObject>();
	String memName = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ctx = Profile.this;
		Bundle bndl = getIntent().getExtras();
		memName = bndl.getString("name");
		setContentView(R.layout.profile);
		Button l = (Button)findViewById(R.id.button132);
	    l.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				i1 = new Intent(ctx, AddGroup.class);
//			    startActivity(i1);
			}
		});
		try {
			open();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		createMyView();
	}
	
	public void createMyView(){
		listView = (ListView) findViewById(R.id.list);

		// Defined Array values to show in ListView
		String[] values = new String[arr.size()];
		final Integer[] val1 = new Integer[arrId.size()];
		for(int i=0;i<arr.size();i++){
			values[i] = arr.get(i).toString();
			val1[i] = Integer.valueOf(arrId.get(i));
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1, values){
			@Override
	        public View getView(int position, View convertView, ViewGroup parent) {
	            View view =super.getView(position, convertView, parent);

	            TextView textView=(TextView) view.findViewById(android.R.id.text1);

	            /*YOUR CHOICE OF COLOR*/
	            textView.setTextColor(Color.BLACK);
	            

	            return view;
	        }
			
		};

		// Assign adapter to ListView
		listView.setAdapter(adapter);

		// ListView Item Click Listener
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				System.out.println("lst size is "+lst.size()+" position "+position);
				Intent intent = new Intent(ctx,GroupDetails.class);
				intent.putExtra("groupId",arrId.get(position));
				intent.putExtra("groupName",arr.get(position));
				intent.putExtra("owner",lst.get(position).get("owner").toString());
				intent.putExtra("memName", memName);
				intent.putExtra("stats",stat.get(position).toString());
				startActivity(intent);
			}
		});		
	}

	public void open() throws RuntimeException, MalformedURLException, IOException {
		Bundle bndl = getIntent().getExtras();
		String name = bndl.getString("name");
		fetchJSON(name);
	}
	
	public void fetchJSON(final String name){
	         try {
	        	String resp = "";
	            URL url = new URL(url1);
	            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	            conn.setDoOutput(true);
	    		conn.setRequestMethod("POST");
	    		conn.setRequestProperty("Content-Type", "application/json");
	     
	    		String input = "{\"userName\":\""+name+"\"}";
	    		
	    		System.out.println("url:"+url);
	    		
	    		System.out.println("input:"+input);
	    		
	    		OutputStream os = conn.getOutputStream();
	    		os.write(input.getBytes());
	    		os.flush();
	    		
	     
	    		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
	     
	    		String output;
	    		while ((output = br.readLine()) != null) {
	    			resp += output;
	    		}  		
	    		
	    		int cnt = 0;
	   	        lst = readAndParseJSON(resp);
	   	       
		   	        
	    		conn.disconnect();
	     
	    	  }catch (NullPointerException e) {
	    		     
		    		e.printStackTrace();
		     
	    	  }catch (MalformedURLException e) {
	     
	    		e.printStackTrace();
	     
	    	  } catch (IOException e) {
	     
	    		  e.printStackTrace();
	     
	    	   }
	     
	  }
	     
	

	public ArrayList< HashMap<String, String > > readAndParseJSON(String response)
			throws IOException {
		int cnt = 0;
		ArrayList<HashMap<String, String>> lst = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> hm = new HashMap<String, String>();
		JSONObject jarr = null;
		JSONObject tempobj = null;
		JSONObject object = null;
		try {
			JSONObject result = new JSONObject(response);
			JSONArray memberArray = result.getJSONArray("memberOf");
			boolean status = result.getBoolean("success");
			for (int i = 0; i < memberArray.length(); i++) {
				hm.clear();
				hm.put("status", status + "");
				jarr = memberArray.getJSONObject(i);
				System.out.println("group ids "+jarr.getInt("groupId"));
				hm.put("groupId", ""+jarr.getInt("groupId"));
				System.out.println("group ids in hm "+hm.get("groupId"));
				hm.put("groupName", jarr.getString("groupName"));
				hm.put("owner", jarr.getString("owner"));
				tempobj = jarr.getJSONObject("stats");
				val1 = Integer.toString(jarr.getInt("groupId"));
				val2 = jarr.getString("groupName");
				val3 = jarr.getJSONObject("stats");
				stat.add(val3);
				Iterator<String> keys = tempobj.keys();
				while (keys.hasNext()) {
					String key = keys.next();
					hm.put(key, Double.toString(tempobj.getDouble(key)));
				}
				s[cnt] = hm.get("groupName").toString();
				arr.add(hm.get("groupName").toString());
				arrId.add(hm.get("groupId").toString());
				cnt++;
				lst.add(hm);
					
			}
			System.out.println("try size is "+lst.get(0).size());

		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		return lst;
	}
}